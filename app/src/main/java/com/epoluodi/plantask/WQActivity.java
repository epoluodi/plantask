package com.epoluodi.plantask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;

import org.ksoap2.serialization.PropertyInfo;

import WebService.Webservice;

public class WQActivity extends Activity {


    private GPS_Server gps_server;
    private EditText name, txtgps, memo;
    private Button btnsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_wq);
        gps_server = new GPS_Server(getApplicationContext(), gpsCallBack);

        name = (EditText) findViewById(R.id.name);
        txtgps = (EditText) findViewById(R.id.txtgps);
        memo = (EditText) findViewById(R.id.memo);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        btnsubmit.setEnabled(false);

        btnsubmit.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (name.equals("")) {
                Toast.makeText(WQActivity.this, "请输入操作人姓名", Toast.LENGTH_SHORT).show();
                return;
            }
            if (memo.equals("")) {
                Toast.makeText(WQActivity.this, "请填写事由", Toast.LENGTH_SHORT).show();
                return;
            }

            Common.ShowPopWindow(btnsubmit, getLayoutInflater(), "提交中...");

            new Thread(runnable).start();
            btnsubmit.setEnabled(false);

        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Webservice webservice = new Webservice(Common.ServerWCF,15000);
            PropertyInfo[] propertyInfos=new PropertyInfo[3];
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName("addr");
            propertyInfo.setValue(txtgps.getText().toString());
            propertyInfos[0] = propertyInfo;
            propertyInfo = new PropertyInfo();
            propertyInfo.setName("name");
            propertyInfo.setValue(name.getText().toString());
            propertyInfos[1] = propertyInfo;
            propertyInfo = new PropertyInfo();
            propertyInfo.setName("memo");
            propertyInfo.setValue(memo.getText().toString());
            propertyInfos[2] = propertyInfo;

            String r = webservice.PDA_GetInterFaceForStringNew(propertyInfos,"A_submitWQ");
            Log.i("结果",r);
            if (r.equals("0"))
            {
                handler.sendEmptyMessage(0);
                return;
            }

            handler.sendEmptyMessage(1);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    btnsubmit.setEnabled(true);
                    Common.CLosePopwindow();
                    Toast.makeText(WQActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 1:
                    btnsubmit.setEnabled(true);
                    Common.CLosePopwindow();
                    Toast.makeText(WQActivity.this, "提交失败，请重新提交", Toast.LENGTH_SHORT).show();
                    break;
                case 2:

                    BDLocation location = (BDLocation)msg.obj;
                    txtgps.setText(location.getAddrStr());
                    btnsubmit.setEnabled(true);
                    Toast.makeText(WQActivity.this, "定位完成，可以提交", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        gps_server.StartLocation();

    }

    @Override
    protected void onPause() {
        super.onPause();
        gps_server.StopLocation();
    }

    GPS_Server.GPSCallBack gpsCallBack = new GPS_Server.GPSCallBack() {
        @Override
        public void UpdateGpsLocation(BDLocation location) {
            Log.i("位置", location.getCity());
            gps_server.StopLocation();
//            txtgps.setText(location.getAddrStr());
//            btnsubmit.setEnabled(true);
//            Toast.makeText(WQActivity.this, "定位完成，可以提交", Toast.LENGTH_SHORT).show();
            Message message=handler.obtainMessage();
            message.obj = location;
            message.what=2;
            handler.sendMessage(message);
        }
    };
}
