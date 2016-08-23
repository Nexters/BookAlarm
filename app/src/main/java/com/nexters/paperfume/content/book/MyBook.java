package com.nexters.paperfume.content.book;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nexters.paperfume.App;
import com.nexters.paperfume.enums.Feeling;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sangyeonK on 2016-08-16.
 */
public class MyBook {
    private static MyBook ourInstance = new MyBook();

    public static MyBook getInstance() {
        return ourInstance;
    }

    private MyBook() {
        mDatabase = new MyBookDBHeler(App.getInstance().getApplicationContext()).getWritableDatabase();

        readUsedBookHistory();
    }

    public static final int MY_BOOK_COUNT_BY_FEELING = 3;
    public static final int USED_BOOK_HISTORY_COUNT = 12;

    private SQLiteDatabase mDatabase;
    private HashMap<Feeling, BookInfo[]> mMyBooks = new HashMap<Feeling, BookInfo[]>();
    private ArrayList<Integer> mUsedBookHistory = new ArrayList<Integer>(USED_BOOK_HISTORY_COUNT);
    private long mRecentBookHistoryTime = 0L;


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mDatabase.close();
    }

    public void resetMyBooks() {
        mMyBooks.clear();

        mDatabase.delete(MyBookDBSchema.MyBookTable.NAME, null, null);
    }

    /**
     * DB에 저장된 책 리스트 를 가져온다. - 이미 read된 데이터가 있을경우엔 read된 데이터를 반환
     * 책 ID만 가져오고 그외 데이터는 필요시 추후 json파일 의 내용을 불러오는 식으로 처리한다.
     * @param feeling 읽어올 책에 어울리는 기분상태
     * @return 읽어들인 책 리스트
     */
    public BookInfo[] readMyBookInfos(Feeling feeling) {
        BookInfo[] myBooks = mMyBooks.get(feeling);
        if (myBooks != null)
            return myBooks;

        myBooks = new BookInfo[MY_BOOK_COUNT_BY_FEELING];
        for(int i = 0 ; i < MY_BOOK_COUNT_BY_FEELING; i++) {
            myBooks[i] = new BookInfo();
        }

        mMyBooks.put(feeling,myBooks);



        Cursor cursor = mDatabase.query(
                MyBookDBSchema.MyBookTable.NAME,
                new String[] {MyBookDBSchema.MyBookTable.Cols.BOOK_ID},
                MyBookDBSchema.MyBookTable.Cols.FEELING + " = ?",
                new String[] {feeling.name()},
                null,
                null,
                "date asc"
        );

        ArrayList<Integer> myBooks_onDB = new ArrayList<Integer>(MY_BOOK_COUNT_BY_FEELING);
        if(cursor.moveToFirst()) {
            do {
                int bookID = cursor.getInt(0);
                myBooks_onDB.add(bookID);
            } while(cursor.moveToNext());
        }

        cursor.close();

        //DB에 저장되어 있는 내 책이 지정한 개수 이하라면 추천도서로 갱신한다.
        if(myBooks_onDB.size() < MY_BOOK_COUNT_BY_FEELING) {
            myBooks_onDB.clear();
            //추천도서에서 이미 본 책은 제거
            ArrayList<Integer> featuredBooks = new ArrayList<Integer>(FeaturedBook.getInstance().getFeaturedBooks(feeling));
            featuredBooks.removeAll(mUsedBookHistory);

            //추천리스트에 있는 책에서 이미 본 책을 제외했을때, 지정한 개수 이하라면 가장 마지막에 봤던 책을 다시 추천해준다
            if(featuredBooks.size() < MY_BOOK_COUNT_BY_FEELING) {
                int index = mUsedBookHistory.size();
                while (featuredBooks.size() < MY_BOOK_COUNT_BY_FEELING && 0 < index) {
                    index--;
                    featuredBooks.add(mUsedBookHistory.get(index) );
                }
            }

            for(int i = 0 ; i < featuredBooks.size(); i++ ) {
                if( MY_BOOK_COUNT_BY_FEELING <= i)
                    break;
                myBooks_onDB.add(featuredBooks.get(i));
            }

            //DB에 내 책 기록
            mDatabase.beginTransaction();
            try {
                mDatabase.delete(MyBookDBSchema.MyBookTable.NAME,
                        MyBookDBSchema.MyBookTable.Cols.FEELING + " = ?",
                        new String[] {feeling.name()});
                for( int i = 0 ; i < myBooks_onDB.size() ; i++ ) {
                    ContentValues cv = new ContentValues();
                    //과거시간으로 변경되었을 경우에 대한 처리
                    long currentTime = System.currentTimeMillis() + i;
                    if(currentTime < mRecentBookHistoryTime)
                        currentTime = mRecentBookHistoryTime + i;

                    cv.put(MyBookDBSchema.MyBookTable.Cols.BOOK_ID, myBooks_onDB.get(i) );
                    cv.put(MyBookDBSchema.MyBookTable.Cols.FEELING, feeling.name());
                    cv.put(MyBookDBSchema.MyBookTable.Cols.DATE, currentTime);
                    mDatabase.insert(MyBookDBSchema.MyBookTable.NAME,null,cv);

                    cv = new ContentValues();
                    cv.put(MyBookDBSchema.MyBookHistoryTable.Cols.BOOK_ID, myBooks_onDB.get(i));
                    cv.put(MyBookDBSchema.MyBookHistoryTable.Cols.DATE, currentTime);
                    mDatabase.insertWithOnConflict(MyBookDBSchema.MyBookHistoryTable.NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                }
                readUsedBookHistory();

                mDatabase.setTransactionSuccessful();

            } catch  (SQLException e){

            } finally {
                mDatabase.endTransaction();
            }

        }

        for( int i = 0 ; i < myBooks_onDB.size() ; i++ ) {
            if( MY_BOOK_COUNT_BY_FEELING <= i)
                break;
            myBooks[i].mBookID = myBooks_onDB.get(i);
        }


        return myBooks;
    }

    private void readUsedBookHistory() {


        mUsedBookHistory.clear();

        Cursor cursor = mDatabase.query(
                MyBookDBSchema.MyBookHistoryTable.NAME,
                new String[] {MyBookDBSchema.MyBookHistoryTable.Cols.BOOK_ID, MyBookDBSchema.MyBookHistoryTable.Cols.DATE},
                null,
                null,
                null,
                null,
                "date desc",
                Integer.toString(USED_BOOK_HISTORY_COUNT)
        );

        if(cursor.moveToFirst()) {
            do {
                mUsedBookHistory.add(cursor.getInt(0));
                long time = cursor.getLong(1);
                if(mRecentBookHistoryTime < time)
                    mRecentBookHistoryTime = time;

            } while(cursor.moveToNext());
        }
        cursor.close();
    }

    /**
     * Sqlite DB 스키마 정의
     */
    class MyBookDBSchema {
        class MyBookTable {
            public static final String NAME = "mybook";

            class Cols {
                public static final String BOOK_ID = "book_id";
                public static final String FEELING = "feeling";
                public static final String DATE = "date";
            }
        }
        class MyBookHistoryTable {
            public static final String NAME = "mybook_history";

            class Cols {
                public static final String BOOK_ID = "book_id";
                public static final String DATE = "date";
            }
        }
    }

    class MyBookDBHeler extends SQLiteOpenHelper {
        private static final int VERSION = 1;
        private static final String DATABASE_NAME = "mybook.db";

        public MyBookDBHeler(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table " + MyBookDBSchema.MyBookTable.NAME + "(" +
                    MyBookDBSchema.MyBookTable.Cols.BOOK_ID + " integer primary key," +
                    MyBookDBSchema.MyBookTable.Cols.FEELING + "," +
                    MyBookDBSchema.MyBookTable.Cols.DATE +
                    ")"
            );

            sqLiteDatabase.execSQL("create table " + MyBookDBSchema.MyBookHistoryTable.NAME + "(" +
                    MyBookDBSchema.MyBookHistoryTable.Cols.BOOK_ID + " integer primary key," +
                    MyBookDBSchema.MyBookHistoryTable.Cols.DATE +
                    ")"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
