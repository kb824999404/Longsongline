package com.sitp.longsongline.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.sitp.longsongline.R;
import com.sitp.longsongline.activity.PoemReadActivity;
import com.sitp.longsongline.adapter.PoemAdapter;
import com.sitp.longsongline.api.ApiConfig;
import com.sitp.longsongline.entity.Poem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReadFragment extends BaseFragment {

    private static final String TAG="ReadFragment";
    private RecyclerView recyclerView;
    private ArrayList<Poem> poems;
    private static final int GET_POEMS=0;

    private Handler handler;

    @Override
    protected int InitLayout(){
        return R.layout.fragment_read;
    }

    @Override
    protected void InitView() {
        //设置标题栏标题
        QMUITopBar topbar = (QMUITopBar) myView.findViewById(R.id.topbar);
        topbar.setTitle("诗词阅读");

        //处理线程间消息
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case  GET_POEMS:
                        setPoemView();
                        break;
                }
                return true;
            }
        });

        requestPoemList();

    }
    private void setPoemView(){
        recyclerView = myView.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        PoemAdapter poemAdapter=new PoemAdapter(poems);
        recyclerView.setAdapter(poemAdapter);

        PagerSnapHelper pagerSnapHelper=new PagerSnapHelper();
        recyclerView.setOnFlingListener(null);
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        poemAdapter.setOnItemClickListener(new PoemAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getContext(), PoemReadActivity.class);
                intent.putExtra("Index",position);
                startActivity(intent);
            }
        });
    }

    public void onItemClick(View view){
        int position = recyclerView.getChildAdapterPosition(view);
        Toast.makeText(getActivity(),"点击了"+poems.get(position),Toast.LENGTH_SHORT).show();
    }


    private void requestPoemList(){

        //Post请求生成诗词
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("start", "0")
                .addFormDataPart("number", "100")
                .build();
        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URl+ApiConfig.GET_POEM_LIST)
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
                        //成功获取诗词列表
                        if(status.equals("true")){
                            Log.d(TAG,status);
                            poems = new ArrayList<Poem>();
                            JSONArray poemArray=jsonobject.getJSONArray("poemList");
                            for(int i=0;i<poemArray.length();i++){
                                JSONObject poemJSON=poemArray.getJSONObject(i);
                                String title=poemJSON.getString("title");
                                String author=poemJSON.getString("author");
                                String []contents=poemJSON.getString("content").
                                        split("\\|");
                                Poem poem=new Poem(title,author,contents);
                                poems.add(poem);

                                Message msg=new Message();
                                msg.what=GET_POEMS;
                                handler.sendMessage(msg);
                            }
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
