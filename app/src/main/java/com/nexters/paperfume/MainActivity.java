package com.nexters.paperfume;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nexters.paperfume.Data.DataStorage;
import com.nexters.paperfume.Resource.BackPressCloseHandler;

import com.nexters.paperfume.firebase.Firebase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.Vector;


/*
* 인터파크 api key -
* 01753BB80F582D3BF1E9D78C52EFD191B53F9AB6E2AC301A88342C2187083EFB
* */
public class MainActivity extends AppCompatActivity {

    private final String[] CATEGORY_KEYS = {"101", "102", "103", "104", "105", "107", "108", "109",
            "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "122", "123", "124", "125"
            , "126", "128", "129"};
    DataStorage dataStorage = new DataStorage();
    ListView listView;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MainActivity.this, Splash.class);
        startActivity(intent);
        setContentView(R.layout.activity_main);
        Spinner category_Spinner = (Spinner) findViewById(R.id.category_Spinner);
        listView = (ListView) findViewById(R.id.Book_list);
        if (isNetworkAvailable(getApplicationContext())) {
            new GetData().execute();
        } else {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Error")
                    .setMessage("네트워크에 연결되지 않았습니다. 네트워크를 확인해주세요.")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }


        backPressCloseHandler = new BackPressCloseHandler(this);
        category_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataStorage.setInterPark_URL("http://book.interpark.com/api/newBook.api?key=01753BB80F582D3BF1E9D78C52EFD191B53F9AB6E2AC301A88342C2187083EFB&categoryId=" + CATEGORY_KEYS[position]);
                new GetData().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Firebase 로그인
        Firebase.getInstance().login();
    }

    public boolean isNetworkAvailable(Context context) {
        boolean value = false;

        ConnectivityManager connec = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            value = true;
        }

        // Log.d ("1", Boolean.toString(value) );
        return value;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    public class GetData extends AsyncTask<Void, Void, Void> {
        DataStorage dataStorage = new DataStorage();
        // 뉴스의 타이틀을 저장해 주는 객체 생성
        Vector<String> titlevec = new Vector<>();
        // 뉴스의 기사내용을 저장해 주는 객체 생성
        Vector<String> descvec = new Vector<>();
        // 웹사이트에 접속할 주소
        String uri;
        // 웹사이트에 접속을 도와주는 클래스
        URL url;
        // XML문서의 내용을 임시로 저장할 변수
        String tagname = "", title = "", desc = "";
        // 데이터의 내용을 모두 읽어드렸는지에 대한 정보를 저장
        Boolean flag = null;

        // 네트워크(url)에 접속해서 xml문서를 가져오는 메소드
        @Override
        protected Void doInBackground(Void... params) {
            try {
                //xml문서를 읽고 해석해줄 수 있는 객체를 선언
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                //네임스페이스 사용여부
                factory.setNamespaceAware(true);
                //실제 xml문서를 읽어 드리면서 데이터를 추출해주는 객체 선언
                XmlPullParser xpp = factory.newPullParser();

                // 웹사이트에 접속

                url = new URL(uri);
                // 사이트 접속후에 xml 문서를 읽어서 가져옴
                InputStream in = url.openStream();
                // 웹사이트로부터 받아온 xml문서를 읽어드리면서 데이터를 추출해 주는 XmlPullParser객체로 넘겨줌
                xpp.setInput(in, "utf-8");

                // 이벤트 내용을 사용하기 위해서 변수 선언
                int eventType = xpp.getEventType();
                // 반복문을 사용하여 문서의 끝까지 읽어 들이면서 데이터를 추출하여 각각의 벡터에 저장
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        // 태그의 이름을 알아야 텍스트를 저장하기에 태그이름을 읽어서 변수에 저장
                        tagname = xpp.getName();

                    } else if (eventType == XmlPullParser.TEXT) {
                        // 태그 이름이 title과 같다면 변수에 title 저장
                        if (tagname.equals("title")) {
                            if (xpp.getText().equals("새로나온책")) {

                            } else {
                                title += xpp.getText();
                            }
                        }
                        // 태그 이름이 description과 같다면 desc변수에 저장
                        else if (tagname.equals("coverSmallUrl"))
                            desc += xpp.getText();
                    } else if (eventType == XmlPullParser.END_TAG) {
                        // end tag 이름을 얻어옴
                        tagname = xpp.getName();
                        // end tag 이름이 item이라면 저장한 변수 title과 desc를 벡터에 저장
                        if (tagname.equals("item")) {
                            titlevec.add(title);
                            descvec.add(desc);
                            // 변수 초기화
                            title = "";
                            desc = "";
                        }
                    }
                    // 다음 이벤트를 넘김
                    eventType = xpp.next();
                    if (isCancelled()) {
                        break;
                    }
                }
                // 모든 xml문서를 읽어드렸다면
                flag = true;


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dataStorage.getInterPark_URL() == null) {
                uri = "http://book.interpark.com/api/newBook.api?key=01753BB80F582D3BF1E9D78C52EFD191B53F9AB6E2AC301A88342C2187083EFB&categoryId=100";
            } else {
                uri = dataStorage.getInterPark_URL();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CustomList adapter = new CustomList(titlevec, descvec);
            listView = (ListView) findViewById(R.id.Book_list);
            listView.setAdapter(adapter);
        }
    }

    public class CustomList extends BaseAdapter {
        Vector<String> titlevec;
        Vector<String> Imagevec;

        //클래스의 객체가 생성됬을때 정보를 arrayAdapter에 보내면 여기 안에있는 생성자의 정보를 받아와서 다시 this.context = context를 통해 안에 넣어준다
        CustomList(Vector<String> titlevec, Vector<String> Imagevec) {
            this.titlevec = titlevec;
            this.Imagevec = Imagevec;
        }

        @Override
        public int getCount() {
            return titlevec.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_item, null, true);
            TextView title = (TextView) rowView.findViewById(R.id.list_Item);
            ImageView book_image = (ImageView) rowView.findViewById(R.id.Book_Image);

            try {
                title.setText(titlevec.get(position));
                Glide.with(MainActivity.this).load(Imagevec.get(position).trim()).into(book_image);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return rowView;
        }
    }
}
