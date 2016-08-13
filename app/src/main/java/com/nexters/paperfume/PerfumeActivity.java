package com.nexters.paperfume;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by user on 2016-07-27.
 */

public class PerfumeActivity extends AppCompatActivity {
    Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("recommend_books");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    int buffersize = Character.MAX_VALUE;
    char readBuf[] = new char[buffersize];
    int resultSize = 0;
    private List object;
    StorageReference islandRef;
    String title[] = new String[3];
    String author[] = new String[3];
    String imageURL[] = new String[3];
    String info[] = new String[3];
    JSONObject jsonObject;
    private File localFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfume);
        button = (Button) findViewById(R.id.getting_books);
        final Intent intent = getIntent();

        final StorageReference storageRef = storage.getReferenceFromUrl("gs://nexters-paperfume.appspot.com");
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Selected feelings post to server

                myRef.child("by_feeling").child(intent.getStringExtra("feeling")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        object = (List) dataSnapshot.getValue();

                        Log.e("value", object.get(1).toString());
                        int a[] = new int[3];
                        for (int i = 0; i < 3; i++) {
                            while ((i > 0 && a[i - 1] == a[i])) {
                                a[i] = (int) (Math.random() * 10);
                            }
                            a[i] = (int) (Math.random() * 10);
                        }

                        for (int num = 0; num < 3; num++) {
                            Log.e("Random i", String.valueOf(a[num]) + " " + object.get(a[num]).toString());
                            islandRef = storageRef.child("book_data/" + object.get(a[num]).toString() + ".json");
                            try {
                                localFile = null;
                                localFile = File.createTempFile(object.get(a[num]).toString(), "json");
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
                                        Log.e("fail?", "?");
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                title[num] = jsonObject.getString("title");
                                author[num] = jsonObject.getString("author");
                                imageURL[num] = jsonObject.getString("url");
                                info[num] = jsonObject.getString("introduce");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            readBuf = null;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(PerfumeActivity.this, MainActivity.class);
               // intent.putExtra("title", title);
                //intent.putExtra("author", author);
                //intent.putExtra("imageURL", imageURL);
                //intent.putExtra("info", info);
                //intent.putExtra() //books data
                startActivity(intent);
                finish();
            }
        });
    }

}