package com.sitp.longsongline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartActivity extends AppCompatActivity {
    private Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoMain();
            }
        }, 3000);

    }
//    进入首页
    private  void gotoMain(){
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}