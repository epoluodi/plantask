package com.epoluodi.plantask;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epoluodi.plantask.Scan.ScanActivity;

import org.ksoap2.serialization.PropertyInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import WebService.Webservice;

public class JiXiuScanActivity extends Activity {

    private ImageView btnreturn,scanimg;
    private Button btnsubmit;
    private TextView jynum,ddtime,bxr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ji_xiu_scan);
        btnreturn = (ImageView)findViewById(R.id.btnreturn);
        scanimg = (ImageView)findViewById(R.id.scanimg);
        btnsubmit = (Button)findViewById(R.id.btnsubmit);
        jynum = (TextView)findViewById(R.id.jynum);
        ddtime = (TextView)findViewById(R.id.ddtime);
        bxr = (TextView)findViewById(R.id.bxr);
        btnsubmit.setOnClickListener(onClickListener);
        btnsubmit.setEnabled(false);

        btnreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        scanimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JiXiuScanActivity.this, ScanActivity.class);

                startActivityForResult(intent, ScanActivity.SCANRESULTREQUEST);
            }
        });

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Common.ShowPopWindow(btnsubmit,getLayoutInflater(),"提交...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String[] strings = ddtime.getText().toString().split(" ");

                    Webservice webservice = new Webservice(Common.ServerWCF,15000);
                    PropertyInfo[] propertyInfos=new PropertyInfo[4];
                    PropertyInfo propertyInfo = new PropertyInfo();
                    propertyInfo.setName("jynum");
                    propertyInfo.setValue(jynum.getText().toString());
                    propertyInfos[0] = propertyInfo;
                    propertyInfo = new PropertyInfo();
                    propertyInfo.setName("rq");
                    propertyInfo.setValue(strings[0]);
                    propertyInfos[1] = propertyInfo;
                    propertyInfo = new PropertyInfo();
                    propertyInfo.setName("dt");
                    propertyInfo.setValue(strings[1]);
                    propertyInfos[2] = propertyInfo;
                    propertyInfo = new PropertyInfo();
                    propertyInfo.setName("bxr");
                    propertyInfo.setValue(Common.userName);
                    propertyInfos[3] = propertyInfo;
                    String r = webservice.PDA_GetInterFaceForStringNew(propertyInfos,"A_BXSubmit");
                    Log.i("结果",r);
                    if (r.equals("ok"))
                    {
                        handler.sendEmptyMessage(0);
                        return;
                    }
                    handler.sendEmptyMessage(1);
                }
            }).start();
        }
    };


    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Common.CLosePopwindow();
            if (msg.what == 0)
            {
                Toast.makeText(JiXiuScanActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                finish();
                return;
            }else if (msg.what == 1)
            {
                Toast.makeText(JiXiuScanActivity.this,"提交失败,重新尝试提交",Toast.LENGTH_SHORT).show();

                return;
            }

        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==ScanActivity.SCANRESULTREQUEST)
        {
            if (resultCode ==1)
            {

                String code =data.getExtras().getString("code");
                Log.i("onActivityResult 回调 " ,code);
                code = code.substring(code.lastIndexOf("=")==-1?0:code.lastIndexOf("=")+1,code.length());

                jynum.setText(code);
                bxr.setText(Common.userName);

                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                String sdate = sDateFormat.format(new Date());
                ddtime.setText(sdate);
                btnsubmit.setEnabled(true);
            }
            return;
        }



    }


}
