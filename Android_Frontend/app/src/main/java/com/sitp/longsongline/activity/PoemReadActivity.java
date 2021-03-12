package com.sitp.longsongline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.sitp.longsongline.R;
import com.sitp.longsongline.api.ApiConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PoemReadActivity extends AppCompatActivity {
    private int poemIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem_read);
        //隐藏默认标题栏
        getSupportActionBar().hide();

        Intent intent=getIntent();
        poemIndex = intent.getIntExtra("Index",-1);
        Toast.makeText(this,"点击诗"+poemIndex,Toast.LENGTH_SHORT).show();

        Init();
    }
    private void Init(){
        //设置标题栏标题
        QMUITopBar topbar=(QMUITopBar)findViewById(R.id.topbar);
        topbar.setTitle("诗词阅读");
    }

}