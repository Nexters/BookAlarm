package com.nexters.paperfume;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nexters.paperfume.tmp.Book;

public class DetailActivity extends AppCompatActivity {
    RelativeLayout detailCover;
    TextView detailText;
    LinearLayout root;

    //애니메이션 위한 변수
    DisplayMetrics dm;
    int[] originalPos;
    int xDest;
    int yDest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String index = intent.getStringExtra("index");
        int position= Integer.parseInt(index);
        detailCover = (RelativeLayout) findViewById(R.id.detailCover);
        detailText = (TextView) findViewById(R.id.textView3);
        detailText.setVisibility(View.GONE);
        root = (LinearLayout) findViewById( R.id.rootLayout );

        View view = LayoutInflater.from(DetailActivity.this).inflate(R.layout.cover_item,null);

        ImageView image = (ImageView)view.findViewById(R.id.imageView);
        TextView title = (TextView)view.findViewById(R.id.textView);
        TextView author = (TextView)view.findViewById(R.id.textView2);
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(Book.image[position], getTheme());
        } else {
            drawable = getResources().getDrawable(Book.image[position]);
        }
        image.setImageDrawable(drawable);
        title.setText(Book.title[position]);
        author.setText(Book.author[position]);
        title.setTextColor(Color.WHITE);
        author.setTextColor(Color.WHITE);

        //뷰 연결
        detailCover.addView(view);
        detailText.setText("누군가를 생각하지 않으려고 애를\n 쓰다 보면\n 누군가를 얼마나 많이 생각하고\n 있는지 깨닫게 된다.\n 있다와 없다는 공생한다.\n 부재는 존재를 증명한다.\n 누군가가 머물다 떠난 자리일까\n 혹은 누군가를 기다리는 자리일까\n 당신의 마음속 빈자리는");
        detailText.setTextColor(Color.WHITE);
    }
    public void onWindowFocusChanged(boolean hasFocus){
        if(hasFocus){
            startDetailView(detailCover);
            detailCover.invalidate();
        }
    }
    @Override
    public void onBackPressed() {
        endDetailView(detailCover);
    }
    private void startDetailView(View view)
    {
        dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics( dm );

        originalPos = new int[2];
        view.getLocationOnScreen( originalPos );

        xDest = dm.widthPixels/2;
        xDest -= (view.getMeasuredWidth()/2);
        yDest = dm.heightPixels/2 - (view.getMeasuredHeight()/2);

        TranslateAnimation anim = new TranslateAnimation( 0, xDest - originalPos[0] , 0, (yDest - originalPos[1])*2 );
        anim.setDuration(1000);
        anim.setFillAfter( true );
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                detailText.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).duration(700).playOn(detailText);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(anim);
    }
    private void endDetailView(View view)
    {
        TranslateAnimation anim = new TranslateAnimation( xDest - originalPos[0], 0 , (yDest - originalPos[1])*2, 0 );
        anim.setDuration(1000);
        anim.setFillAfter( true );
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                YoYo.with(Techniques.FadeOut).duration(700).playOn(detailText);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(anim);
    }
}
