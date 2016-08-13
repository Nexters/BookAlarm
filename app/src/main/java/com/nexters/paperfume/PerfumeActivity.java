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

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by user on 2016-07-27.
 */

public class PerfumeActivity extends AppCompatActivity {
    Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("recommend_books");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfume);
        button = (Button) findViewById(R.id.getting_books);
        final Intent intent = getIntent();

        final StorageReference storageRef = storage.getReferenceFromUrl("gs://nexters-paperfume.appspot.com");
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Selected feelings post to server

                myRef.child("by_feeling").child(intent.getStringExtra("feeling")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List object = (List) dataSnapshot.getValue();

                        Log.e("value", object.get(1).toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                StorageReference islandRef = storageRef.child("book_data/19418350.json");


                try {
                    File localFile = File.createTempFile("19418350","json");
                    islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.e("?", "?");
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

                Intent intent = new Intent(PerfumeActivity.this, MainActivity.class);
                //intent.putExtra() //books data
                startActivity(intent);
                finish();
            }
        });
    }

}