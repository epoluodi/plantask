package com.epoluodi.plantask;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MenuActivity extends Activity {

    private RelativeLayout menu1,menu2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menu1 = (RelativeLayout)findViewById(R.id.menu1);
        menu2 = (RelativeLayout)findViewById(R.id.menu2);
        menu1.setOnClickListener(onClickListener);
        menu2.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId())
            {
                case R.id.menu1:
                    intent = new Intent(MenuActivity.this,MainActivity.class);
                    break;
                case R.id.menu2:
                    intent = new Intent(MenuActivity.this,WQActivity.class);
                    break;
            }
            startActivity(intent);
        }
    };
}
