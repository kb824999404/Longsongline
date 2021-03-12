package com.sitp.longsongline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    private static final String TAG="PoemReadActivity";
    private int poemIndex;
    private Handler handler;
    private static final int GET_POEM=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem_read);
        //隐藏默认标题栏
        getSupportActionBar().hide();



        Init();
    }
    private void Init(){
        //设置标题栏标题
        QMUITopBar topbar=(QMUITopBar)findViewById(R.id.topbar);
        topbar.setTitle("诗词阅读");

        Intent intent=getIntent();

        poemIndex = intent.getIntExtra("Index",-1);
        if(poemIndex!=-1){
            requestPoemDetail();
        }

        //处理线程间消息
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case  GET_POEM:
                        break;
                }
                return true;
            }
        });
    }

    private void requestPoemDetail(){

        //Post请求生成诗词
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("index", poemIndex+"")
                .build();
        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URl+ApiConfig.GET_POEM_DETAIL)
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse");
                if(response.code()==200){
                    try{
                        String responseBody=response.body().string();
                        JSONObject jsonobject=new JSONObject(responseBody);
                        String status=jsonobject.getString("status");
                        //成功获取生成诗词
                        if(status.equals("true")){
                            Log.d(TAG,status);
                            JSONObject poem=jsonobject.getJSONObject("poem");
                            String title=poem.getString("title");
                            String author=poem.getString("author");
                            String dynasty=poem.getString("dynasty");
                            String[] keywords=poem.getString("keywords").split(" ");
                            String[] contents=poem.getString("content").split("\\|");
                        }
                    }
                    catch (JSONException e){
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                }
            }
        });
    }

}