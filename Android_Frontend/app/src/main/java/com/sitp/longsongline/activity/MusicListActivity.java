package com.sitp.longsongline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.sitp.longsongline.R;
import com.sitp.longsongline.adapter.MusicAdapter;
import com.sitp.longsongline.adapter.PoemAdapter;
import com.sitp.longsongline.api.ApiConfig;
import com.sitp.longsongline.data.UserInfo;
import com.sitp.longsongline.entity.Music;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MusicListActivity extends AppCompatActivity {

    private static final String TAG="MusicListAcivity";
    private ArrayList<Music> musicList;
    private RecyclerView recyclerView;

    private static final int GET_MUSICS=0;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        //隐藏默认标题栏
        getSupportActionBar().hide();
        Init();
    }

    private void Init() {
        //设置标题栏标题
        QMUITopBar topbar = (QMUITopBar) findViewById(R.id.topbar);
        topbar.setTitle("我的音乐");

        //处理线程间消息
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case  GET_MUSICS:
                        setMusicList();
                        break;
                }
                return true;
            }
        });


        if(UserInfo.isLogin){
            requestMusicList();
        }
    }

    private void setMusicList(){
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        MusicAdapter musicAdapter=new MusicAdapter(musicList);
        recyclerView.setAdapter(musicAdapter);

        musicAdapter.setOnItemClickListener(new MusicAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getApplicationContext(), MusicActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("music",musicList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private String formatTime(String time){
        String time_format="";
        SimpleDateFormat sdf=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        try{
            Date date = sdf.parse(time);
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time_format = sdf.format(date);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return time_format;
    }

    private void requestMusicList(){
        QMUIDialog dialog=new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("加载中...")
                .setCancelable(false)
                .show();

        //Post请求生成诗词
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", UserInfo.uid+"")
                .build();
        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URl+ApiConfig.GET_ALL_MUSIC)
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
                        if(status.equals("true")){
                            Log.d(TAG,status);
                            JSONArray musics=jsonobject.getJSONArray("musics");
                            musicList = new ArrayList<Music>();
                            Log.d(TAG,musics.length()+"");

                            for(int i=0;i<musics.length();i++){
                                JSONArray music = musics.getJSONArray(i);
                                String time=formatTime(music.getString(4));
                                musicList.add(new Music(music.getInt(0),music.getString(2),
                                        music.getString(3),time,music.getString(5)));
                            }
                            Message msg=new Message();
                            msg.what = GET_MUSICS;
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