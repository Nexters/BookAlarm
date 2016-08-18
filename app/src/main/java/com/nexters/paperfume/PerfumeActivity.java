package com.nexters.paperfume;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.nexters.paperfume.content.Status;
import com.nexters.paperfume.content.book.BookInfo;
import com.nexters.paperfume.content.book.MyBook;
import com.nexters.paperfume.content.fragrance.FragranceInfo;
import com.nexters.paperfume.content.fragrance.FragranceManager;
import com.nexters.paperfume.enums.Feeling;
import com.nexters.paperfume.util.CustomFont;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by user on 2016-07-27.
 */

public class PerfumeActivity extends AppCompatActivity {

    private static boolean active = false;

    Button button;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private LinkedList<StorageTask<FileDownloadTask.TaskSnapshot>> mStorageTasks = new LinkedList<StorageTask<FileDownloadTask.TaskSnapshot>>();
    private ProgressDialog mLoadingDialog;
    private AlertDialog mFailedDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfume);
        View imageView = findViewById(R.id.image_activity_perfume);
        button = (Button) findViewById(R.id.getting_books);
        TextView fragranceGuide = (TextView) findViewById(R.id.fragrance_guide);

        mFailedDialog = new AlertDialog.Builder(PerfumeActivity.this).create();
        mFailedDialog.setTitle(R.string.error);
        mFailedDialog.setMessage(getResources().getString(R.string.failed_download_book));
        mFailedDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getText(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        //향기 갱신시간이 되면 내 향과 내 책 리스트 초기화
        if(FragranceManager.getInstance().checkResetFragrance()) {
            FragranceManager.getInstance().resetFragrance();
            MyBook.getInstance().resetMyBooks();
        }

        Feeling feeling = Status.getInstance().getCurrentFeeling();
        FragranceInfo fragranceInfo = FragranceManager.getInstance().getFragrance(feeling);
        final BookInfo[] bookInfos = MyBook.getInstance().readMyBookInfos(feeling);

        //폰트 설정
        button.setTypeface(CustomFont.getInstance().getTypeface());
        fragranceGuide.setTypeface(CustomFont.getInstance().getTypeface());

        //배경화면 설정
        try {
            Drawable d = Drawable.createFromStream(getAssets().open(fragranceInfo.getImageAsset()), null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setBackground(d);
            } else {
                imageView.setBackgroundDrawable(d);
            }
        } catch (IOException ioe) {

        }

        //문구 설정
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat;
        if(cal.get(Calendar.HOUR_OF_DAY) < 12 )
            dateFormat = new SimpleDateFormat("오전 K시 mm분");
        else
            dateFormat = new SimpleDateFormat("오후 K시 mm분");

        String sFragranceGuide = getResources().getString(R.string.foramt_fragrance_guide, dateFormat.format(cal.getTime()), feeling.toMeans(), fragranceInfo.getAdjective(), fragranceInfo.getName());

        fragranceGuide.setText(sFragranceGuide);

        storage.setMaxDownloadRetryTimeMillis(2000);
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://nexters-paperfume.appspot.com");


        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStorageTasks.clear();
                mLoadingDialog = ProgressDialog.show(PerfumeActivity.this, null, getResources().getString(R.string.download_book) , true);
                //변수에 책 데이터가 아직 남아있는지 확인
                //모든 책의 로딩이 완료되었다면 다음 액티비티로 이동
                if(checkBookDataLoadComplete(bookInfos)) {
                    startMainActivity();
                } else {
                    //파일로부터 책 데이터 read
                    for (final BookInfo bookInfo : bookInfos) {

                        if(true == bookInfo.isLoadedBookData())
                            continue;

                        final File cacheFile = new File(getApplicationContext().getCacheDir(), "book_data_" + bookInfo.getBookID() + ".json");
                        //책 파일이 북데이터 디렉토리에 남아있는지 확인
                        if (true == cacheFile.exists() && true == cacheFile.canRead()) {
                            if(false == readCachedBookData(cacheFile, bookInfo) ) {
                                cancelReadBookData();
                                break;
                            }
                            //모든 책의 로딩이 완료되었다면 다음 액티비티로 이동
                            if (checkBookDataLoadComplete(bookInfos))
                                startMainActivity();
                        } else {
                            //책 파일이 없으면 Firebase Storage 로부터 다운로드
                            try {
                                cacheFile.createNewFile();
                                StorageReference ref = storageRef.child("book_data/" + String.valueOf(bookInfo.getBookID()) + ".json");
                                mStorageTasks.add(ref.getFile(cacheFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                        Log.d("PAPERFUME","sucess book loading");
                                        if(false == readCachedBookData(cacheFile, bookInfo) ) {
                                            cancelReadBookData();
                                        }
                                        //모든 책의 로딩이 완료되었다면 다음 액티비티로 이동
                                        else if (checkBookDataLoadComplete(bookInfos))
                                            startMainActivity();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Log.e("fail?", "?" + e.getMessage());
                                        cancelReadBookData();
                                    }
                                }));
                            } catch( IOException e ) {
                                e.printStackTrace();
                                cancelReadBookData();
                            }
                        }

                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void backButtonClick(View view){
        onBackPressed();
    }

    public boolean checkBookDataLoadComplete(BookInfo[] bookInfos) {
        int loadedBookCount = 0;

        for (BookInfo bookInfo : bookInfos) {
            if (bookInfo.isLoadedBookData())
                loadedBookCount++;
        }

        if (loadedBookCount == bookInfos.length)
            return true;
        else
            return false;
    }

    public boolean readCachedBookData(File bookFile, BookInfo bookInfo) {
        boolean complete;
        try {
            FileInputStream fs = new FileInputStream(bookFile);
            BufferedReader bf = new BufferedReader(new InputStreamReader(fs));
            StringBuilder sb = new StringBuilder();
            String s = new String();

            while((s = bf.readLine()) != null)  {
                sb.append(s).append("\n");
            }

            bf.close();

            String jString = sb.toString();
            JSONObject jsonObject = new JSONObject(jString);

            bookInfo.setTitle(jsonObject.getString("title"));
            bookInfo.setAuthor(jsonObject.getString("author"));
            bookInfo.setImage(jsonObject.getString("image"));
            JSONArray ja = jsonObject.getJSONArray("inside");
            String insideBook = new String();
            for (int i = 0; i < ja.length(); i++) {
                insideBook += ja.getString(i) + "\n\n";
            }
            bookInfo.setInside(insideBook);
            bookInfo.setLoadedBookData(true);

            Log.d("sb", sb.toString());

            //책 정보 읽어오기 완료
            complete = true;

        } catch ( FileNotFoundException e ) {
            //
            complete = false;
        } catch (JSONException e ) {
            //
            complete = false;
        } catch (IOException e ) {
            //
            complete = false;
        }

        if(complete == false)
            bookFile.delete();

        return complete;
    }

    public void cancelReadBookData() {
        for(StorageTask<FileDownloadTask.TaskSnapshot> task : mStorageTasks) {
            if( false == task.isComplete() ){
                task.cancel();
            }
        }
        mLoadingDialog.dismiss();
        mFailedDialog.show();
    }

    public void startMainActivity(){
        mLoadingDialog.dismiss();
        if(active) {
            Intent mainIntent = new Intent(PerfumeActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }


}