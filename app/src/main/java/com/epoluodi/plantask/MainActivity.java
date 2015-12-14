package com.epoluodi.plantask;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {


    ListView list;
    TextView title;
    SimpleAdapter simpleAdapter;
    List<Map<String,String>> mapList;
    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView)findViewById(R.id.list);
        title=(TextView)findViewById(R.id.title);
        mapList = new ArrayList<Map<String, String>>();

        title.setText("");



        thread = new Thread(runnable);
        thread.start();
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {

        }
    };


    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case 1:
                    simpleAdapter = new SimpleAdapter(MainActivity.this,
                            mapList,android.R.layout.simple_list_item_2,
                            new String[]{"ts","pxid"},
                            new int[]{android.R.id.text1,android.R.id.text2});

                    list.setAdapter(simpleAdapter);
                    break;
            }
        }
    };
}





