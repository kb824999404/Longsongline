package com.sitp.longsongline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.sitp.longsongline.R;
import com.sitp.longsongline.api.ApiConfig;
import com.sitp.longsongline.data.UserInfo;

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

public class SongSynthesisActivity extends AppCompatActivity {

    private static final String TAG = "SongSynthesisActivity";
    private String title="哈哈歌";
    private String poem="哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈";
    private QMUIRoundButton voiceButton;
    private QMUIRoundButton midiButton;
    private QMUIRoundButton bgmButton;

    private static final String[] voiceChoice = {"男中音","女高音","男低音"};
    private static final String[] midiChoice = {"旋律1","旋律2"};
    private static final String[] bgmChoice = {"吉他","钢琴"};

    private int voiceIndex=0;
    private int midiIndex=0;
    private int bgmIndex=0;

    private Handler handler;

    private static final int SYNTHESIS_SONG=0;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_synthesis);
        //隐藏默认标题栏
        getSupportActionBar().hide();
        Init();
    }

    private void Init(){
        //设置标题栏标题
        QMUITopBar topbar=(QMUITopBar)findViewById(R.id.topbar);
        topbar.setTitle("合成乐曲");

        Intent intent=getIntent();
        if(intent.hasExtra("poem")){
            poem=intent.getStringExtra("poem");
            Log.d(TAG,poem);
        }
        if(intent.hasExtra("title")){
            title=intent.getStringExtra("title");
        }
//        Toast.makeText(this,poem,Toast.LENGTH_SHORT).show();

        //获取按钮，绑定事件
        voiceButton = (QMUIRoundButton)findViewById(R.id.select_voice_button);
        midiButton = (QMUIRoundButton)findViewById(R.id.select_midi_button);
        bgmButton = (QMUIRoundButton)findViewById(R.id.select_bgm_button);
        voiceButton.setText(voiceChoice[voiceIndex]);
        midiButton.setText(midiChoice[midiIndex]);
        bgmButton.setText(bgmChoice[bgmIndex]);
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QMUIDialog.CheckableDialogBuilder builder =
                        new QMUIDialog.CheckableDialogBuilder(v.getContext());
                builder.setTitle("请选择人声")
                        .setCheckedIndex(voiceIndex)
                        .addItems(voiceChoice, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder.setCheckedIndex(which);
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                voiceIndex = builder.getCheckedIndex();
                                Toast.makeText(v.getContext(), "你选择了 " + voiceChoice[voiceIndex], Toast.LENGTH_SHORT).show();
                                voiceButton.setText(voiceChoice[voiceIndex]);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        midiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QMUIDialog.CheckableDialogBuilder builder =
                        new QMUIDialog.CheckableDialogBuilder(v.getContext());
                builder.setTitle("请选择旋律")
                        .setCheckedIndex(midiIndex)
                        .addItems(midiChoice, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder.setCheckedIndex(which);
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                midiIndex = builder.getCheckedIndex();
                                Toast.makeText(v.getContext(), "你选择了 " + midiChoice[midiIndex], Toast.LENGTH_SHORT).show();
                                midiButton.setText(midiChoice[midiIndex]);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        bgmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QMUIDialog.CheckableDialogBuilder builder =
                        new QMUIDialog.CheckableDialogBuilder(v.getContext());
                builder.setTitle("请选择伴奏")
                        .setCheckedIndex(bgmIndex)
                        .addItems(bgmChoice, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder.setCheckedIndex(which);
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                bgmIndex = builder.getCheckedIndex();
                                Toast.makeText(v.getContext(), "你选择了 " + bgmChoice[bgmIndex], Toast.LENGTH_SHORT).show();
                                bgmButton.setText(bgmChoice[bgmIndex]);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        //合成乐曲按钮
        QMUIRoundButton singerButton = (QMUIRoundButton)findViewById(R.id.synthesisButton);
        singerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSongSynthesis();
            }
        });

        //处理线程间消息
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case  SYNTHESIS_SONG:
                        Toast.makeText(getApplicationContext(),"合成乐曲成功！",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
                return true;
            }
        });

    }


    private void requestSongSynthesis(){
        QMUIDialog dialog=new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("加载中...")
                .setCancelable(false)
                .show();

        //Post请求生成诗词
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", UserInfo.uid+"")
                .addFormDataPart("title", title)
                .addFormDataPart("content", poem)
                .addFormDataPart("voice", voiceIndex+"")
                .addFormDataPart("midi", midiIndex+"")
                .addFormDataPart("bgm", bgmIndex+"" )
                .build();
        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URl+ApiConfig.SONG_SYNTHESIS)
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
                        //成功合成乐曲
                        if(status.equals("true")){
                            Message msg=new Message();
                            msg.what = SYNTHESIS_SONG;
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