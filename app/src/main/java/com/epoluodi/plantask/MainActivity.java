package com.epoluodi.plantask;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import WebService.Webservice;


public class MainActivity extends Activity {


    ListView list;
    TextView title;
    SimpleAdapter simpleAdapter;
    List<Map<String,String>> mapList;
    Thread thread;
    Boolean Isrun=false;
    Button btnrefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnrefresh = (Button)findViewById(R.id.btnrefresh);
        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Isrun)
                {
                    thread = new Thread(runnable);
                    thread.start();
                }
            }
        });

        list = (ListView)findViewById(R.id.list);
        list.setOnItemClickListener(onItemClickListener);
        title=(TextView)findViewById(R.id.title);
        mapList = new ArrayList<Map<String, String>>();

        title.setText("计划任务表");



        thread = new Thread(runnable);
        thread.start();




    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Map<String,String> map  = mapList.get(i);
            Intent intent= new Intent(MainActivity.this,DetailList.class);
            intent.putExtra("title",map.get("dwmc"));
            intent.putExtra("id",map.get("id"));
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Isrun=true;
            Webservice webservice = new Webservice(Common.ServerWCF,15000);
            PropertyInfo[] propertyInfos=new PropertyInfo[1];
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName("fzr");
            propertyInfo.setValue(Common.userName);
            propertyInfos[0] = propertyInfo;
            String r =webservice.PDA_GetInterFaceForStringNew(propertyInfos,"A_PDA_getplantask");
            Log.d("结果:",r);
            if (r.equals("-1"))
            {
                handler.sendEmptyMessage(-1);
                return;
            }
            Message message = handler.obtainMessage();
            message.what=1;
            message.obj = r;
            handler.sendMessage(message);
            return;
        }
    };


    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Isrun=false;
            switch (msg.what)
            {
                case 1:
                    MakeJsonPlanTask(msg.obj.toString());

                    simpleAdapter = new SimpleAdapter(MainActivity.this,
                            mapList,R.layout.list,
                            new String[]{"dwmc","pxid"},
                            new int[]{android.R.id.text1,android.R.id.text2});


                    list.setAdapter(simpleAdapter);
                    Toast.makeText(MainActivity.this,"成功获取",Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(MainActivity.this,"无法连接服务，获取任务计划",Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };



    void MakeJsonPlanTask(String json)
    {
        Map<String,String> map ;
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("plantask");
            mapList.clear();
            for (int i = 0;i<jsonArray.length();i++)
            {
                map= new Hashtable<String, String>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                map.put("id",jsonObject1.getString("id"));
                map.put("dwmc",jsonObject1.getString("dwmc"));
                map.put("pxid","编号:" + jsonObject1.getString("pxid"));
                mapList.add(map);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}





