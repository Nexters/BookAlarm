package com.nexters.paperfume;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nexters.paperfume.tmp.Book;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String index = intent.getStringExtra("index");
        int position= Integer.parseInt(index);

        RelativeLayout detailCover = (RelativeLayout) findViewById(R.id.detailCover);
        TextView detailText = (TextView) findViewById(R.id.detailText);
        RelativeLayout root = (RelativeLayout) findViewById( R.id.rootLayout );

        View view = LayoutInflater.from(DetailActivity.this).inflate(R.layout.cover_item,null);

        ImageView image = (ImageView)view.findViewById(R.id.imageView);
        ImageView topImage = (ImageView)findViewById(R.id.topImage);
        TextView title = (TextView)view.findViewById(R.id.textView);
        TextView author = (TextView)view.findViewById(R.id.textView2);


        Bitmap normal = BitmapFactory.decodeResource(getResources(), Book.image[position]);
        Bitmap result = blur(getApplicationContext(),normal,25);

        Drawable blurD = new BitmapDrawable(getResources(), result);

        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(Book.image[position], getTheme());
            topImage.setBackground(blurD);
            //blur 효과 추가해야 함. 그리고 15버전 일때 처리 따로 해줘야 함..
        } else {
            drawable = getResources().getDrawable(Book.image[position]);
            topImage.setBackgroundDrawable(getResources().getDrawable(Book.image[position]));
        }

        image.setImageDrawable(drawable);
        title.setText(Book.title[position]);
        author.setText(Book.author[position]);
        title.setTextColor(Color.BLACK);
        author.setTextColor(Color.GRAY);

        //뷰 연결
        detailCover.addView(view);
        detailText.setText("“미란다, 아서가 어젯밤에 심장마비로 죽었어요.” 바다 위에 떠 있는 불빛이 흐릿해지더니 빛의 동그라미가 서로 겹쳐지며 한 줄로 늘어섰다. “정말 유감입니다. 이 소식을 뉴스를 통해 알게 하고 싶지 않아서 전화했어요.”" +
                "“얼마 전에 만났는데요.” 그녀가 말했다. “2주 전 토론토에서요.”" +
                "“받아들이기 힘들 겁니다.” 그가 다시 목소리를 가다듬었다. “충격이죠, 정말……. 우린 열여덟 살 때부터 친구였어요. 나도 도무지 믿기지가 않아요.”" +
                "“어쩌다, 어쩌다 그렇게 된 건가요?” 그녀가 말했다." +
                "“실은, 음…… 불쾌하게 듣지 않으셨으면 좋겠는데, 실은 이런 게 아서가 원하던 죽음이 아닌가 싶기도 합니다. 무대에서 죽었거든요. 〈리어 왕〉 4막 중간에, 급성 심장마비로요.”" +
                "“연기하다가 쓰러졌다고요?”" +
                "“네. 관객 중에 의사가 두 명 있었는데, 무슨 일인지 알아차리고 급히 무대로 뛰어 올라가서 아서를 구하려고 애써봤지만 소용이 없었다고 하네요. 병원에 도착하자마자 사망이 선언되었답니다.”" +
                "이렇게 끝이 날 수도 있구나. 통화가 끝나고 그녀는 생각했다. 이렇게 시시한 결말이라니. 그러자 마음이 진정되었다. 한때 함께 늙어갈 거라고 생각했던 남자가 이 세상을 떠났다는 사실을, 머나먼 타국에서 전화 한 통으로 알게 될 수도 있는 거였다." +
                "근처의 어둠 속에서는 스페인어 대화가 이어지고 있었다. 배들은 여전히 수평선 위에서 빛을 발했고, 여전히 바람 한 점 없었다. 뉴욕은 아침이겠지. 그녀는 클라크가 맨해튼에 있는 자기 사무실에서 수화기를 내려놓는 모습을 상상했다. 전화기 버튼 몇 개만 누르면 지구 반대쪽에 있는 사람과 이야기할 수 있었던 시대의 마지막 달에 일어난 일이었다." +
                "---「5장」중에서");
        detailText.setTextColor(Color.BLACK);
        root.bringChildToFront(detailCover);
    }
    public static Bitmap blur(Context context, Bitmap sentBitmap, int radius) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius); //0.0f ~ 25.0f
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }else{
            return sentBitmap;
        }
    }
}
