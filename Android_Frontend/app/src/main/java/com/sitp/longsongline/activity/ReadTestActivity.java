package com.sitp.longsongline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sitp.longsongline.R;

public class ReadTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_test);

        //隐藏默认标题栏
        getSupportActionBar().hide();

        Init();
    }

    private void Init() {
        //设置标题栏标题
        QMUITopBar topbar = (QMUITopBar) findViewById(R.id.topbar);
        topbar.setTitle("阅读测试");
    }
}