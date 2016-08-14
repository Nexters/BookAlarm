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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nexters.paperfume.content.fragrance.FragranceInfo;
import com.nexters.paperfume.content.fragrance.FragranceManager;
import com.nexters.paperfume.enums.Feeling;
import com.nexters.paperfume.util.CustomFont;

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
import java.util.List;

/**
 * Created by user on 2016-07-27.
 */

public class PerfumeActivity extends AppCompatActivity {
    Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("recommend_books");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private int buffersize = Character.MAX_VALUE;
    private int localnum = 0;
    private int resultSize = 0;
    private char readBuf[];
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> author = new ArrayList<>();
    private ArrayList<String> imageURL = new ArrayList<>();
    private ArrayList<String> info = new ArrayList<>();
    private JSONObject jsonObject;
    private Feeling intentedFeeling;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        intentedFeeling = (Feeling) intent.getSerializableExtra("feeling");

        setContentView(R.layout.activity_perfume);
        View imageView = findViewById(R.id.image_activity_perfume);
        button = (Button) findViewById(R.id.getting_books);
        TextView fragranceGuide = (TextView) findViewById(R.id.fragrance_guide);

        FragranceInfo fragranceInfo = FragranceManager.getInstance().getFragrance(intentedFeeling);

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("aa K시 mm분");

        String sFragranceGuide = getResources().getString(R.string.foramt_fragrance_guide, dateFormat.format(cal.getTime()), intentedFeeling.toMeans(), fragranceInfo.getAdjective(), fragranceInfo.getName());

        fragranceGuide.setText(sFragranceGuide);


        final StorageReference storageRef = storage.getReferenceFromUrl("gs://nexters-paperfume.appspot.com");
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(PerfumeActivity.this, "","책을 받아오는 중입니다 잠시만 기다려주세요.",true);
                //Selected feelings post to server
                String strFeeling = intentedFeeling.toString();
                myRef.child("by_feeling").child(intentedFeeling.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List object = (List) dataSnapshot.getValue();

                        Log.e("value", object.get(1).toString());
                        int a[] = new int[3];
                        for (int i = 0; i < 3; i++) {
                            a[i] = (int) (Math.random() * 10);
                            for (int j = 0; j < i; j++) {
                                if (a[i] == a[j]) {
                                    i--;
                                }
                            }
                        }

                        for (int num = 0; num < 3; num++) {
                            localnum = num;

                            StorageReference islandRef = storageRef.child("book_data/" + object.get(a[num]).toString().trim() + ".json");
                            try {
                                final File localFile = File.createTempFile("temp", ".json");//이거 전역변수로 선언하고 초기화 시키면 안됨
                                readBuf = new char[buffersize];
                                islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        try {

                                            FileInputStream fs = new FileInputStream(localFile);
                                            BufferedReader bf = new BufferedReader(new InputStreamReader(fs));
                                            StringBuilder sb = new StringBuilder();

                                            while ((resultSize = bf.read(readBuf)) != -1) {
                                                if (resultSize == buffersize) {
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
                                                title.add(jsonObject.getString("title"));
                                                author.add(jsonObject.getString("author"));
                                                imageURL.add(jsonObject.getString("image"));
                                                info.add(jsonObject.getString("introduce"));
                                                Log.e("title", title.toString());
                                                if (info.size() >= 3) {
                                                    dialog.dismiss();
                                                    Intent mainIntent = new Intent(PerfumeActivity.this, MainActivity.class);
                                                    mainIntent.putExtra("title",title);
                                                    mainIntent.putExtra("author", author);
                                                    mainIntent.putExtra("imageURL", imageURL);
                                                    mainIntent.putExtra("info", info);
                                                    startActivity(mainIntent);
                                                    finish();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            Log.e("sb", sb.toString());


                                            readBuf = null;
                                            readBuf = new char[buffersize];

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

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    public void backButtonClick(View view){
        onBackPressed();
    }

}