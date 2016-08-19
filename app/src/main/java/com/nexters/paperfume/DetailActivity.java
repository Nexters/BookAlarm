package com.nexters.paperfume;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.nexters.paperfume.util.CustomFont;

public class DetailActivity extends AppCompatActivity {

    //view
    private ImageView topImage;
    private TextView detailText;
    private LinearLayout rootLayout;
    private TextView dTitle;
    private TextView dAuthor;

    private DisplayMetrics displaymetrics;
    private int height;
    private int width;

    //intent
    private Intent intent;
    private String imageURL;
    private String BookTitle;
    private String BookAuthor;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        intent = getIntent();
        BookTitle = intent.getStringExtra("title");
        BookAuthor = intent.getStringExtra("author");
        info = intent.getStringExtra("info");
        imageURL = intent.getStringExtra("imageURL");

        detailText = (TextView) findViewById(R.id.detailText);
        topImage = (ImageView)findViewById(R.id.topImage);
        rootLayout = (LinearLayout)findViewById(R.id.rootLayout);
        dTitle = (TextView)findViewById(R.id.d_Title);
        dAuthor = (TextView)findViewById(R.id.d_Author);

        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        Glide.with(this).load(imageURL).asBitmap().into(new SimpleTarget<Bitmap>(width, 250) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    topImage.setBackground(drawable);
                }
            }
        });
        dTitle.setText(BookTitle);
        dAuthor.setText(BookAuthor);

        //뷰 연결
        detailText.setText(info);
        detailText.setTextColor(Color.BLACK);

        //폰트 설정
        detailText.setTypeface(CustomFont.getInstance().getTypeface());
    }
}
