package com.nexters.paperfume;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.LinkagePager;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nexters.paperfume.content.fragrance.FragranceInfo;
import com.nexters.paperfume.content.fragrance.FragranceManager;
import com.nexters.paperfume.enums.Feeling;
import com.nexters.paperfume.firebase.Firebase;
import com.nexters.paperfume.models.RecommendBooks;
import com.nexters.paperfume.tmp.Book;
import com.nexters.paperfume.util.BitmapBlur;
import com.nexters.paperfume.util.SecretTextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.LinkagePagerContainer;

public class MainActivity extends AppCompatActivity {

    SecretTextView text;
    LinkagePager cover;
    PagerAdapter coverAdapter;
    LinearLayout mainBack;
    TextView reSetting;

    int endPage; // 마지막 선택
    boolean first;
    ArrayList<String> BookTitle;
    ArrayList<String> BookAuthor;
    ArrayList<String> imageURL;
    ArrayList<String> info;

    Feeling feeling;
    FragranceInfo fragranceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (SecretTextView)findViewById(R.id.text);
        //책 이미지 페이저 등록
        LinkagePagerContainer bContainer = (LinkagePagerContainer)findViewById(R.id.book_container);
        cover = bContainer.getViewPager();
        coverAdapter = new BookAdapter();
        cover.setAdapter(coverAdapter);

        cover.setOffscreenPageLimit(coverAdapter.getCount());

        new CoverFlow.Builder()
                .withLinkage(cover)
                .pagerMargin(0f)
                .scale(0.3f)
                .spaceSize(0f)
                .build()
        ;

        //처음 페이지가 중간페이지로 설정되도록.
        endPage = 1; //최초값 1
        first=true;

        //Firebase 로그인
        //successMethod 에서 로그인 완료처리..여기서 책 데이터 도 로딩..
        Firebase.getInstance().login(
                new Runnable() {
                    @Override
                    public void run() {
                        processLoginSuccess();
                    }
                },
                new Runnable() {
                    @Override
                    public void run() {
                        processLoginFail();
                    }
                } );
        BookTitle = getIntent().getStringArrayListExtra("title");//그냥 배열로 넘기는거 list로 넘기는거 왜 안되는지!
        BookAuthor = getIntent().getStringArrayListExtra("author");
        imageURL = getIntent().getStringArrayListExtra("imageURL");
        Log.e("image",imageURL.toString());
        info = getIntent().getStringArrayListExtra("info");


        mainBack = (LinearLayout)findViewById(R.id.mainBack);

        feeling = (Feeling)getIntent().getSerializableExtra("back");

        fragranceInfo = FragranceManager.getInstance().getFragrance(feeling);

        try {
            Drawable d = Drawable.createFromStream(getAssets().open(fragranceInfo.getImageAsset()), null);
            BitmapDrawable bd = (BitmapDrawable)d;
            Bitmap normal = bd.getBitmap();
            Bitmap result = BitmapBlur.getInstance().blur(getApplicationContext(),normal,20);

            Drawable blurD = new BitmapDrawable(getResources(), result);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mainBack.setBackground(blurD);
            } else {
                mainBack.setBackgroundDrawable(blurD);
            }
        }
        catch (IOException ioe){

        }
        reSetting = (TextView)findViewById(R.id.reSetting);

        reSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent reset = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(reset);
                finish(); // 뒤로 돌아 갈 수 있게 할지 고민..
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){

            cover.setCurrentItem(endPage,true);
            text.show();

            if(first) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("aa K시 mm분");

                String sFragranceMain = getResources().getString(R.string.format_fragrance_main, dateFormat.format(cal.getTime()), feeling.toMeans());

                text.setText(sFragranceMain);
                text.setDuration(2000);
            }

            cover.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //animation 부분 fadeOut
                    if(cover.getCurrentItem()==0) {
                        text.hide();

                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("title",BookTitle.get(0));
                        intent.putExtra("author",BookAuthor.get(0));
                        intent.putExtra("info",info.get(0));
                        intent.putExtra("imageURL",imageURL.get(0));
                        endPage = 0;
                        first=false;
                        startActivity(intent);
                    }
                }
            });
            cover.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cover.getCurrentItem()==1) {
                        text.hide();

                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("title",BookTitle.get(1));
                        intent.putExtra("author",BookAuthor.get(1));
                        intent.putExtra("info",info.get(1));
                        intent.putExtra("imageURL",imageURL.get(1));
                        endPage = 1;
                        first=false;
                        startActivity(intent);
                    }
                }
            });
            cover.getChildAt(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cover.getCurrentItem()==2) {
                        //animation 부분 fadeOut
                        text.hide();

                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("title",BookTitle.get(2));
                        intent.putExtra("author",BookAuthor.get(2));
                        intent.putExtra("info",info.get(2));
                        intent.putExtra("imageURL",imageURL.get(2));
                        endPage = 2;
                        first=false;
                        startActivity(intent);
                    }
                }
            });
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    //책 이미지
    private class BookAdapter extends PagerAdapter{
        @Override
        public Object instantiateItem(final ViewGroup container, int position){
            final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.cover_item,null);

            ImageView image = (ImageView)view.findViewById(R.id.imageView);
            TextView title = (TextView)view.findViewById(R.id.textView);
            TextView author = (TextView)view.findViewById(R.id.textView2);

            Glide.with(MainActivity.this).load(imageURL.get(position).trim()).override(180, 250).into(image);

            title.setText(BookTitle.get(position));
            author.setText(BookAuthor.get(position));
            title.setTextColor(Color.WHITE);
            author.setTextColor(Color.WHITE);

            container.addView(view);

            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position,Object object){
            container.removeView((View)object);
        }

        @Override
        public int getCount() {return 3;}

        @Override
        public boolean isViewFromObject(View view,Object object) {return (view == object);}
    }
    private void processLoginSuccess(){
        //로그인 성공에 대한 처리
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("recommend_books/by_feeling");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        RecommendBooks rbook = dataSnapshot.getValue(RecommendBooks.class);

                        RecommendBooks.getInstance().getHappy().clear();
                        RecommendBooks.getInstance().getHappy().addAll(rbook.getHappy());

                        RecommendBooks.getInstance().getMiss().clear();
                        RecommendBooks.getInstance().getMiss().addAll(rbook.getMiss());

                        RecommendBooks.getInstance().getGroomy().clear();
                        RecommendBooks.getInstance().getGroomy().addAll(rbook.getGroomy());

                        RecommendBooks.getInstance().getStifled().clear();
                        RecommendBooks.getInstance().getStifled().addAll(rbook.getStifled());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //TODO
                    }
                }
        );
    }

    private void processLoginFail(){
        //로그인 실패에 대한 처리 ( 네트워크 연결 실패 )
        Log.d("Paperfume", "processLoginFailed");
    }

}
