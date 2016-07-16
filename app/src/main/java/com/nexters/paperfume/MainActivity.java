package com.nexters.paperfume;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    GetData getData = new GetData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData.execute();
        while(true){//안에 ENDTAG넣는거!
            try{
                Thread.sleep(1000);
                if(getData.flag == true){
                    CustomList adapter = new CustomList(getData.titlevec);
                    ListView listView = (ListView) findViewById(R.id.test);
                    listView.setAdapter(adapter);
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(MainActivity.this, Splash.class);
        startActivity(intent);

    }

    public class CustomList extends BaseAdapter {
        Vector<String> titlevec;
        //클래스의 객체가 생성됬을때 정보를 arrayAdapter에 보내면 여기 안에있는 생성자의 정보를 받아와서 다시 this.context = context를 통해 안에 넣어준다
        CustomList(Vector<String> titlevec){
            this.titlevec = titlevec;
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
            TextView title = (TextView) rowView.findViewById(R.id.test_txt);
            title.setText(titlevec.get(position));
            return rowView;
        }
    }
}
