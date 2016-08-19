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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nexters.paperfume.content.Status;
import com.nexters.paperfume.content.book.BookInfo;
import com.nexters.paperfume.content.book.MyBook;
import com.nexters.paperfume.content.fragrance.FragranceInfo;
import com.nexters.paperfume.content.fragrance.FragranceManager;
import com.nexters.paperfume.enums.Feeling;
import com.nexters.paperfume.util.BitmapBlur;
import com.nexters.paperfume.util.SecretTextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.LinkagePagerContainer;

public class MainActivity extends AppCompatActivity {

    private SecretTextView text;
    private LinkagePager cover;
    private PagerAdapter coverAdapter;
    private LinearLayout mainBack;

    private int endPage; // 마지막 선택
    private boolean first;

    private Feeling feeling;
    private FragranceInfo fragranceInfo;
    private BookInfo[] bookInfos;

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

        mainBack = (LinearLayout)findViewById(R.id.mainBack);

        feeling = Status.getInstance().getCurrentFeeling();
        fragranceInfo = FragranceManager.getInstance().getFragrance(feeling);
        bookInfos = MyBook.getInstance().readMyBookInfos(feeling);

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
                        intent.putExtra("title",bookInfos[0].getTitle());
                        intent.putExtra("author",bookInfos[0].getAuthor());
                        intent.putExtra("info",bookInfos[0].getInside());
                        intent.putExtra("imageURL",bookInfos[0].getImage());
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
                        intent.putExtra("title",bookInfos[1].getTitle());
                        intent.putExtra("author",bookInfos[1].getAuthor());
                        intent.putExtra("info",bookInfos[1].getInside());
                        intent.putExtra("imageURL",bookInfos[1].getImage());
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
                        intent.putExtra("title",bookInfos[2].getTitle());
                        intent.putExtra("author",bookInfos[2].getAuthor());
                        intent.putExtra("info",bookInfos[2].getInside());
                        intent.putExtra("imageURL",bookInfos[2].getImage());
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

            if(position < bookInfos.length ) {
                Glide.with(MainActivity.this).load(bookInfos[position].getImage()).override(155, 223).into(image);
            }

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
}
