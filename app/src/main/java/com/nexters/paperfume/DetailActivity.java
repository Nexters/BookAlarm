package com.nexters.paperfume;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nexters.paperfume.util.BitmapBlur;
import com.nexters.paperfume.util.CustomFont;

import java.net.URL;
import java.util.concurrent.ExecutionException;

public class DetailActivity extends AppCompatActivity {

    String imageURL;
    Bitmap normal;
    Bitmap result;
    Drawable blurD;
    ImageView image;
    ImageView topImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String BookTitle = intent.getStringExtra("title");
        String BookAuthor = intent.getStringExtra("author");
        String info = intent.getStringExtra("info");
        imageURL = intent.getStringExtra("imageURL");

        RelativeLayout detailCover = (RelativeLayout) findViewById(R.id.detailCover);
        TextView detailText = (TextView) findViewById(R.id.detailText);
        RelativeLayout root = (RelativeLayout) findViewById( R.id.rootLayout );

        View view = LayoutInflater.from(DetailActivity.this).inflate(R.layout.cover_item,null);

        image = (ImageView)view.findViewById(R.id.imageView);
        topImage = (ImageView)findViewById(R.id.topImage);
        TextView title = (TextView)view.findViewById(R.id.textView);
        TextView author = (TextView)view.findViewById(R.id.textView2);

        normal = null;
        result = null;

        GlideBitmap gb = new GlideBitmap();
        gb.execute();

        title.setText(BookTitle);
        author.setText(BookAuthor);
        title.setTextColor(Color.BLACK);
        author.setTextColor(Color.GRAY);

        //뷰 연결
        detailCover.addView(view);
        detailText.setText(info);
        detailText.setTextColor(Color.BLACK);
        root.bringChildToFront(detailCover);

        //폰트 설정
        detailText.setTypeface(CustomFont.getInstance().getTypeface());
    }
    private class GlideBitmap extends AsyncTask<URL, Integer, Long> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Long doInBackground(URL... urls) {
            long l = 0;

            try {
                normal = Glide.with(DetailActivity.this).load(imageURL).asBitmap().into(180,250).get();
            } catch (final ExecutionException e) {
                Log.d("error", e.getMessage());
            } catch (final InterruptedException e) {
                Log.d("error", e.getMessage());
            }

            result = BitmapBlur.blur(getApplicationContext(), normal, 15);

            blurD = new BitmapDrawable(getResources(), result);

            return l;
        }
        @Override
        protected void onPostExecute(Long result) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                topImage.setBackground(blurD);
            } else {
                topImage.setBackgroundDrawable(blurD);
            }

            Glide.with(DetailActivity.this).load(imageURL).override(180, 250).into(image);
        }
    }

}
