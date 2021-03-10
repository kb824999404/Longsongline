package com.sitp.longsongline.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.sitp.longsongline.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //隐藏默认标题栏
        getSupportActionBar().hide();

        Init();
    }

    private void Init(){
        //设置标题栏标题
        QMUITopBar topbar=(QMUITopBar)findViewById(R.id.topbar);
        topbar.setTitle("长歌行");

        InitButtons();



    }
    private void InitButtons(){
        //获取Button
        QMUIRoundButton poemGenerate=(QMUIRoundButton)findViewById(R.id.poemGenerate);
        QMUIRoundButton poemRead=(QMUIRoundButton)findViewById(R.id.poemRead);
        QMUIRoundButton searchByPic=(QMUIRoundButton)findViewById(R.id.searchByPic);
        QMUIRoundButton poemMusic=(QMUIRoundButton)findViewById(R.id.poemMusic);
        QMUIRoundButton toLogin=(QMUIRoundButton)findViewById(R.id.toLogin);
        QMUIRoundButton toRegister=(QMUIRoundButton)findViewById(R.id.toRegister);
        poemGenerate.setOnClickListener(this);
        poemRead.setOnClickListener(this);
        searchByPic.setOnClickListener(this);
        poemMusic.setOnClickListener(this);
        toLogin.setOnClickListener(this);
        toRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.poemGenerate:
                startActivity(new Intent(MainActivity.this, PoemGenerateActivity.class));
                break;
            case R.id.searchByPic:
                startActivity(new Intent(MainActivity.this, SearchByPicActivity.class));
                break;
            case R.id.poemMusic:
                startActivity(new Intent(MainActivity.this, PoemSingerActivity.class));
                break;
            case R.id.toLogin:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.toRegister:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            default:
                showDialog();
                break;
        }
    }

    private void showDialog(){
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("该功能尚在开发中，敬请期待！")
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


}