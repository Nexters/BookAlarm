package com.nexters.paperfume;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import com.google.firebase.storage.StorageReference;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by user on 2016-07-27.
 */

public class PerfumeActivity extends AppCompatActivity {
    Button button;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private JSONObject jsonObject;

    private ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfume);
        View imageView = findViewById(R.id.image_activity_perfume);
        button = (Button) findViewById(R.id.getting_books);
        TextView fragranceGuide = (TextView) findViewById(R.id.fragrance_guide);

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


        final StorageReference storageRef = storage.getReferenceFromUrl("gs://nexters-paperfume.appspot.com");
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingDialog = ProgressDialog.show(PerfumeActivity.this, null,"책을 받아오는 중입니다 잠시만 기다려주세요.",true);

                int loadedBookCount = 0;
                for(BookInfo bookInfo : bookInfos) {
                    if(bookInfo.isLoadedBookData())
                        loadedBookCount++;
                }
                if(loadedBookCount == bookInfos.length){
                    startMainActivity();
                }
                else {
                    //Selected feelings post to server
                    for(final BookInfo bookInfo : bookInfos) {
                        StorageReference islandRef = storageRef.child("book_data/" + String.valueOf(bookInfo.getBookID()) + ".json");
                        try {
                            final File localFile = File.createTempFile("temp", ".json");//이거 전역변수로 선언하고 초기화 시키면 안됨

                            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    try {
                                        int resultSize = 0;
                                        int totalByteCount = (int)taskSnapshot.getTotalByteCount();
                                        char readBuf[] = new char[totalByteCount];
                                        FileInputStream fs = new FileInputStream(localFile);
                                        BufferedReader bf = new BufferedReader(new InputStreamReader(fs));
                                        StringBuilder sb = new StringBuilder();

                                        while ((resultSize = bf.read(readBuf)) != -1) {
                                            if (resultSize == totalByteCount) {
                                                sb.append(readBuf);
                                            } else {
                                                for (int i = 0; i < resultSize; i++) {
                                                    sb.append(readBuf[i]);
                                                }
                                            }
                                        }

                                        jsonObject = null;
                                        String jString = sb.toString();

                                        jsonObject = new JSONObject(jString);

                                        try {
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

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("sb", sb.toString());

                                        int loadedBookCount = 0;
                                        for (BookInfo bookInfo : bookInfos) {
                                            if (bookInfo.isLoadedBookData())
                                                loadedBookCount++;
                                        }
                                        if (loadedBookCount == bookInfos.length) {
                                            startMainActivity();
                                        }
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {

                                        e.printStackTrace();
                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Log.e("fail?", "?" + e.getMessage());
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });
    }

    public void backButtonClick(View view){
        onBackPressed();
    }

    public void startMainActivity(){
        mLoadingDialog.dismiss();
        Intent mainIntent = new Intent(PerfumeActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }


}