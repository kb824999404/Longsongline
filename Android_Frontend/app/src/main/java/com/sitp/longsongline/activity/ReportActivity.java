package com.sitp.longsongline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sitp.longsongline.R;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //隐藏默认标题栏
        getSupportActionBar().hide();

        Init();
    }

    private void Init() {
        //设置标题栏标题
        QMUITopBar topbar = (QMUITopBar) findViewById(R.id.topbar);
        topbar.setTitle("阅读报告");

        Intent intent = getIntent();
        int answer1 = intent.getIntExtra("answer1", 0);
        int answer2 = intent.getIntExtra("answer2", 0);
        int score = 0;
        if (answer1 == 0) {
            score++;
        }
        if (answer2 == 0) {
            score++;
        }

        TextView test_score = (TextView)findViewById(R.id.test_score);
        test_score.setText(score+"/2");
    }
}
