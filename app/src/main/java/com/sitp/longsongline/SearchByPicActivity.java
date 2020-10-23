package com.sitp.longsongline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.qmuiteam.qmui.widget.QMUITopBar;

public class SearchByPicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_pic);
        //隐藏默认标题栏
        getSupportActionBar().hide();

        Init();
    }
    private void Init(){
        //设置标题栏标题
        QMUITopBar topbar=(QMUITopBar)findViewById(R.id.topbar);
        topbar.setTitle("图片配诗词");
    }
}