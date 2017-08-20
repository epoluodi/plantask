package com.epoluodi.plantask;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import WebService.Webservice;

public class MenuActivity extends Activity {

    private RelativeLayout menu1,menu2,menu3,menu4,menu5,menu6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menu1 = (RelativeLayout)findViewById(R.id.menu1);
        menu2 = (RelativeLayout)findViewById(R.id.menu2);

        menu3 = (RelativeLayout)findViewById(R.id.menu3);
        menu4 = (RelativeLayout)findViewById(R.id.menu4);
        menu5 = (RelativeLayout)findViewById(R.id.menu5);
        menu6 = (RelativeLayout)findViewById(R.id.menu6);

        menu1.setOnClickListener(onClickListener);
        menu2.setOnClickListener(onClickListener);
        menu3.setOnClickListener(onClickListener);

        menu4.setOnClickListener(onClickListener);
        menu5.setOnClickListener(onClickListener);
        menu6.setOnClickListener(onClickListener);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Thread.sleep(500);
                    handler.sendEmptyMessage(-1);
                    Thread.sleep(1000);

                    Webservice webservice = new Webservice(Common.ServerWCF,15000);
                    PropertyInfo[] propertyInfos=new PropertyInfo[1];
                    PropertyInfo propertyInfo = new PropertyInfo();
                    propertyInfo.setName("deviceid");
                    propertyInfo.setValue(Common.deviceId);
                    propertyInfos[0] = propertyInfo;


                    String r = webservice.PDA_GetInterFaceForStringNew(propertyInfos,"A_getDeviceInfo");
                    Log.i("结果",r);
                    if (r.equals("0"))
                    {
                        handler.sendEmptyMessage(0);
                        return;
                    }
                    Message message=handler.obtainMessage();
                    message.obj = r;
                    message.what=1;
                    handler.sendMessage(message);


                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case 0:
                    Common.CLosePopwindow();
                    Toast.makeText(MenuActivity.this,"网络连接失败，请重新打开",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 1:
                    Common.CLosePopwindow();
                    String json = (String) msg.obj;
                    Log.i("结果",json);
                    if (json.equals("null"))
                    {
                        Intent intent=new Intent(MenuActivity.this,VerifyAppActivity.class);
                        startActivity(intent);
                        return;
                    }
                    else
                    {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            Common.workId = jsonObject.getString("id");
                            Common.userName = jsonObject.getString("name");
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(MenuActivity.this,"验证成功",Toast.LENGTH_SHORT).show();

                    break;
                case -1:
                    Common.ShowPopWindow(menu1,getLayoutInflater(),"验证...");
                    break;
            }
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            Uri uri = null;
            switch (view.getId())
            {
                case R.id.menu1:
                    intent = new Intent(MenuActivity.this,MainActivity.class);
                    break;
                case R.id.menu2:
                    intent = new Intent(MenuActivity.this,WQActivity.class);
                    break;
                case R.id.menu3:
//                    intent = new Intent(MenuActivity.this,WebActivity.class);
//                    intent.putExtra("url","http://vc1818.88ip.org:8091/");

                    uri = Uri.parse("http://vc1818.88ip.org:8091/");
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    break;
                case R.id.menu4:
//                    intent = new Intent(MenuActivity.this,WebActivity.class);
//                    intent.putExtra("url","http://vc1818.88ip.org:8091/H5/record");

                    uri = Uri.parse("http://vc1818.88ip.org:8091/H5/record");
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    break;
                case R.id.menu5:
//                    intent = new Intent(MenuActivity.this,WebActivity.class);
//                    intent.putExtra("url","http://vc1818.88ip.org:666/");

                    uri = Uri.parse("http://vc1818.88ip.org:666/");
                    intent = new Intent(Intent.ACTION_VIEW, uri);


                    break;
                case R.id.menu6:
//                    intent = new Intent(MenuActivity.this,WebActivity.class);
//                    intent.putExtra("url","http://vc1818.88ip.org:888/");


                    uri = Uri.parse("http://vc1818.88ip.org:888/");
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    break;
            }
            startActivity(intent);
        }
    };


}
