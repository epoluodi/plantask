package com.epoluodi.plantask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import WebService.Webservice;

public class VerifyAppActivity extends Activity {

    private EditText code;
    private Button btnok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_app);
        code = (EditText)findViewById(R.id.code);
        btnok = (Button)findViewById(R.id.btnsubmit);


        btnok.setOnClickListener(onClickListener);

        Toast.makeText(this,"请验证手机",Toast.LENGTH_SHORT).show();
    }


    //验证码确认
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (code.getText().toString().equals("") || code.getText().toString().length() != 4)
            {
                Toast.makeText(VerifyAppActivity.this,"请输入4位验证码",Toast.LENGTH_SHORT).show();
                return;
            }


            Common.ShowPopWindow(btnok,getLayoutInflater(),"验证...");
            new Thread(runnable).start();

        }
    };


    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Common.CLosePopwindow();
            switch (msg.what)
            {
                case 0:
                    Toast.makeText(VerifyAppActivity.this,"验证失败，请重新验证",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    String json = (String) msg.obj;
                    Log.i("结果",json);
                    if (json.equals("null"))
                    {
                        Toast.makeText(VerifyAppActivity.this,"验证失败，请重新验证",Toast.LENGTH_SHORT).show();

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

//
                        finish();
                    }
                    break;
            }
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try
            {
                Webservice webservice = new Webservice(Common.ServerWCF,15000);
                PropertyInfo[] propertyInfos=new PropertyInfo[2];
                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("code");
                propertyInfo.setValue(code.getText().toString());
                propertyInfos[0] = propertyInfo;
                propertyInfo = new PropertyInfo();
                propertyInfo.setName("deviceid");
                propertyInfo.setValue(Common.deviceId);
                propertyInfos[1] = propertyInfo;

                String r = webservice.PDA_GetInterFaceForStringNew(propertyInfos,"A_CheckDeviceInfo");
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
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == 4)
        {
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }
}
