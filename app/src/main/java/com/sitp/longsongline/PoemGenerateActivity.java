package com.sitp.longsongline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.qmuiteam.qmui.widget.QMUIFontFitTextView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

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


public class PoemGenerateActivity extends AppCompatActivity {

    private EditText editText;
    private QMUIFontFitTextView textView;
    private static String TAG="PoemGenerateActivity";
    private static final int GETPOEM= 0;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem_generate);
        //隐藏默认标题栏
        getSupportActionBar().hide();

        Init();
    }
    private void Init(){
        //设置标题栏标题
        QMUITopBar topbar=(QMUITopBar)findViewById(R.id.topbar);
        topbar.setTitle("AI辅助创作");

        //文本输入框
        editText=(EditText)findViewById(R.id.poemHints);
        //生成的诗词
        textView=(QMUIFontFitTextView)findViewById(R.id.poemText);

        //生成诗词按钮
        QMUIRoundButton generateButton=(QMUIRoundButton)findViewById(R.id.generateButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestGeneratePoem();


            }
        });
        //处理线程间消息
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case  GETPOEM:
                        String poemText=(String) msg.obj;
                        textView.setText(poemText);
                        break;
                }
                return true;
            }
        });

    }

    private void requestGeneratePoem(){
        String hints=editText.getText().toString();
        if(hints.equals(""))   return;
        QMUIDialog dialog=new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("加载中...")
                .setCancelable(false)
                .show();

        //Post请求生成诗词
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("hints", hints)
                .build();
        Request request = new Request.Builder()
                .url("http://120.25.145.41:6543/generatePoem")
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
                            JSONObject data=jsonobject.getJSONObject("data");
                            JSONArray keywords=data.getJSONArray("keywords");
                            JSONArray poemArray=data.getJSONArray("poem");
                            String poemText="";
                            for(int i=0;i<poemArray.length();i++){
                                String line=poemArray.getString(i);
                                if(!line.equals(""))
                                    poemText+=line+"\n";
                            }
                            Log.d(TAG,poemText);
                            //不能在子线程中修改视图
                            //给handler发送消息
                            Message msg = new Message();
                            msg.what = GETPOEM;
                            msg.obj=poemText;
                            handler.sendMessage(msg);
                            dialog.dismiss();
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