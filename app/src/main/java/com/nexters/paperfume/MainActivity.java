package com.nexters.paperfume;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nexters.paperfume.firebase.Firebase;
import com.nexters.paperfume.models.RecommendBooks;
import com.nexters.paperfume.tmp.Book;
import com.nexters.paperfume.util.SecretTextView;

import java.util.ArrayList;

import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.LinkagePagerContainer;

public class MainActivity extends AppCompatActivity {

    SecretTextView text;
    LinkagePager cover;
    PagerAdapter coverAdapter;
    int endPage; // 마지막 선택
    int otherPage[];
    boolean first;
    ArrayList<String> BookTitle;
    ArrayList<String> BookAuthor;
    ArrayList<String> imageURL;
    ArrayList<String> info;
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
        otherPage = new int[2];
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


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            setOtherPage(endPage);

            cover.setCurrentItem(endPage,true);
            text.show();

            if(first) {
                text.setText("오후 3시 30분의\n우울한 당신에겐\n이 세권의 책을 추천할게요.");
                text.setDuration(2000);
            }else{
                cover.getChildAt(otherPage[0]).setVisibility(View.VISIBLE);
                cover.getChildAt(otherPage[1]).setVisibility(View.VISIBLE);
                cover.getChildAt(endPage).setVisibility(View.VISIBLE);
            }


            cover.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //animation 부분 fadeOut
                    if(cover.getCurrentItem()==0) {
                        setOtherPage(cover.getCurrentItem());
                        cover.getChildAt(otherPage[0]).setVisibility(View.GONE);
                        cover.getChildAt(otherPage[1]).setVisibility(View.GONE);
                        text.hide();

                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("index", "0");
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
                        //animation 부분 fadeOut
                        setOtherPage(cover.getCurrentItem());
                        cover.getChildAt(otherPage[0]).setVisibility(View.GONE);
                        cover.getChildAt(otherPage[1]).setVisibility(View.GONE);
                        text.hide();

                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("index", "1");
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
                        setOtherPage(cover.getCurrentItem());
                        cover.getChildAt(otherPage[0]).setVisibility(View.GONE);
                        cover.getChildAt(otherPage[1]).setVisibility(View.GONE);
                        text.hide();

                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("index", "2");
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
            Drawable drawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getResources().getDrawable(Book.image[position], getTheme());
            } else {
                drawable = getResources().getDrawable(Book.image[position]);
            }
            Glide.with(MainActivity.this).load(imageURL.get(position).trim()).into(image);
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
    private void setOtherPage(int currentPage){
        if(currentPage==0){
            otherPage[0]=1;
            otherPage[1]=2;
        }else if(currentPage==1){
            otherPage[0]=0;
            otherPage[1]=2;
        }else{
            otherPage[0]=0;
            otherPage[1]=1;
        }
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
