package com.sitp.longsongline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.sitp.longsongline.R;
import com.sitp.longsongline.api.ApiConfig;
import com.sitp.longsongline.data.UserInfo;
import com.sitp.longsongline.entity.Poem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private static final int NO_LOGIN=1;

    private String title="";
    private String author="";
    private String dynasty="";
    private String[] keywords;
    private String[] contents;

    private long startTime;


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
                        displayPoem();
                        break;
                    case NO_LOGIN:
                        new QMUIDialog.MessageDialogBuilder(PoemReadActivity.this)
                                .setTitle("提示")
                                .setMessage("该功能需要登录，请先登录！")
                                .addAction("确定", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                        break;
                }
                return true;
            }
        });

        QMUIRoundButton readTestBtn=(QMUIRoundButton)findViewById(R.id.read_test_button);
        readTestBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                long readTime = System.currentTimeMillis() - startTime;
                Intent readTestIntent=new Intent(getApplicationContext(),ReadTestActivity.class);
                readTestIntent.putExtra("Index",poemIndex);
                readTestIntent.putExtra("readTime",readTime);
                startActivity(readTestIntent);
            }
        });

        QMUIRoundButton musicBtn=(QMUIRoundButton)findViewById(R.id.music_button);
        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserInfo.isLogin){
                    Intent musicIntent = new Intent(getApplicationContext(),PoemSingerActivity.class);
                    musicIntent.putExtra("contents",contents);
                    startActivity(musicIntent);
                }
                else {
                    Message msg=new Message();
                    msg.what = NO_LOGIN;
                    handler.sendMessage(msg);
                }
            }
        });

        startTime =  System.currentTimeMillis();
    }
    private void displayPoem(){
        TextView title_textview=(TextView)findViewById(R.id.poem_title);
        title_textview.setText(title);
        TextView author_textview=(TextView)findViewById(R.id.poem_author);
        author_textview.setText(author);
        TextView line1_textview=(TextView)findViewById(R.id.poem_line1);
        TextView line2_textview=(TextView)findViewById(R.id.poem_line2);
        line1_textview.setText(contents[0]+"，"+contents[1]+"。");
        line2_textview.setText(contents[2]+"，"+contents[3]+"。");

        TextView dynasty_textview=(TextView)findViewById(R.id.poem_dynasty);
        dynasty_textview.setText(dynasty);
        TextView keyword_textview=(TextView)findViewById(R.id.poem_keyword);
        String keyword_str="";
        for(String k : keywords){
            keyword_str+=k;
            keyword_str+=" ";
        }
        keyword_textview.setText(keyword_str);
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
                            Log.d(TAG,poem.toString());
                            title=poem.getString("title");
                            author=poem.getString("author");
                            dynasty=poem.getString("dynasty");
                            keywords=poem.getString("keywords").split(" ");
                            contents=poem.getString("content").split("\\|");
                            Message msg=new Message();
                            msg.what=GET_POEM;
                            handler.sendMessage(msg);
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