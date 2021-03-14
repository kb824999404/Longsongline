package com.sitp.longsongline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.sitp.longsongline.R;
import com.sitp.longsongline.api.ApiConfig;
import com.sitp.longsongline.entity.QuestionAnswer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

public class ReadTestActivity extends AppCompatActivity {

    private static final String TAG = "ReadTestActivity";

    private int poemIndex;

    private ArrayList<QuestionAnswer> QAList;

    private Handler handler;

    private static final int GET_QA=0;

    private int answer1_check = 0;
    private int answer2_check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_test);

        //隐藏默认标题栏
        getSupportActionBar().hide();

        Init();
    }

    private void Init() {
        //设置标题栏标题
        QMUITopBar topbar = (QMUITopBar) findViewById(R.id.topbar);
        topbar.setTitle("阅读测试");

        QAList = new ArrayList<QuestionAnswer>();

        Intent intent=getIntent();
        poemIndex = intent.getIntExtra("Index",-1);

        RadioGroup answers1=(RadioGroup)findViewById(R.id.answer1);
        answers1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                answer1_check = checkedId-1;
            }
        });
        RadioGroup answers2=(RadioGroup)findViewById(R.id.answer2);
        answers1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                answer2_check = checkedId-1;
            }
        });

        QMUIRoundButton submitButton = (QMUIRoundButton)findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent submitIntent = new Intent(ReadTestActivity.this,ReportActivity.class);
                submitIntent.putExtra("answer1",answer1_check);
                submitIntent.putExtra("answer2",answer2_check);
                startActivity(submitIntent);
                finish();
            }
        });

        //处理线程间消息
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case  GET_QA:
                        TextView question1=(TextView)findViewById(R.id.question1);
                        question1.setText("1. "+QAList.get(0).question);
                        RadioGroup answers1=(RadioGroup)findViewById(R.id.answer1);
                        ((RadioButton)answers1.getChildAt(0)).setText(QAList.get(0).answer_right);
                        for(int i=1;i<answers1.getChildCount();i++){
                            RadioButton button=(RadioButton)answers1.getChildAt(i);
                            button.setText(QAList.get(0).answers_wrong.get(i-1));
                        }
                        TextView question2=(TextView)findViewById(R.id.question2);
                        question2.setText("2. "+QAList.get(1).question);
                        RadioGroup answers2=(RadioGroup)findViewById(R.id.answer2);
                        ((RadioButton)answers2.getChildAt(0)).setText(QAList.get(1).answer_right);
                        for(int i=1;i<answers2.getChildCount();i++){
                            RadioButton button=(RadioButton)answers2.getChildAt(i);
                            button.setText(QAList.get(1).answers_wrong.get(i-1));
                        }
                        break;
                }
                return true;
            }
        });

        if(poemIndex!=-1){
            requestQA();
        }
    }


    private void requestQA() {

        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("index", poemIndex+"")
                .build();
        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URl+ApiConfig.GET_QUESTION_LIST)
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
                            JSONArray questionList=jsonobject.getJSONArray("questionList");
                            for(int i=0;i<questionList.length();i++){
                                JSONObject question = questionList.getJSONObject(i);
                                JSONObject answer = question.getJSONObject("Answer");
                                QuestionAnswer qs = new QuestionAnswer();
                                qs.question = question.getString("Question");
                                qs.answer_right = answer.getString("right");
                                JSONArray answer_wrong = answer.getJSONArray("wrong");
                                ArrayList<String> as = new ArrayList<>();
                                for(int j=0;j<answer_wrong.length();j++){
                                    as.add(answer_wrong.getString(j));
                                }
                                qs.answers_wrong = as;

                                QAList.add(qs);
                            }
                            Message msg = new Message();
                            msg.what = GET_QA;
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