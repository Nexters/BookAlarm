package com.nexters.paperfume.content.book;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by sangyeonK on 2016-08-16.
 */
public class MyBook {
    private static MyBook ourInstance = new MyBook();

    public static MyBook getInstance() {
        return ourInstance;
    }

    private MyBook() {

    }

    private SQLiteDatabase mDatabase;
    private ArrayList<BookInfo> mBooks;

    public void init(Context context) {
        mDatabase = new MyBookDBHeler(context).getWritableDatabase();
    }

    public void resetMyBooks() {
        mBooks.clear();
        mDatabase.delete(MyBookDBSchema.MyBookTable.NAME, null, null);
    }

    public void readMyBooks() {
        mBooks.clear();
        Cursor cursor = mDatabase.query(
                MyBookDBSchema.MyBookTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );


        while(cursor.getCount() > 0) {

            cursor.moveToNext();
        }

        cursor.close();
    }

    public void getMyBooks() {

    }

    class MyBookDBSchema {
        class MyBookTable {
            public static final String NAME = "mybook";

            class Cols {
                public static final String BOOK_ID = "book_id";
                public static final String FEELING = "feeling";
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
        private static final String DATABASE_NAME = "mybook_1.db";

        public MyBookDBHeler(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table " + MyBookDBSchema.MyBookTable.NAME + "(" +
                    MyBookDBSchema.MyBookTable.Cols.BOOK_ID + " integer primary key," +
                    MyBookDBSchema.MyBookTable.Cols.FEELING +
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
