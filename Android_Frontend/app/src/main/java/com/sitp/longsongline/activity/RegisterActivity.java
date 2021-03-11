package com.sitp.longsongline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.sitp.longsongline.R;
import com.sitp.longsongline.api.Api;
import com.sitp.longsongline.api.ApiConfig;
import com.sitp.longsongline.api.HttpCallBack;
import com.sitp.longsongline.data.UserInfo;
import com.sitp.longsongline.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";
    private Handler handler;
    private static final int REGISTER_FAIL=0;
    private static final int REGISTER_SUCCESS=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText etAccount = findViewById(R.id.et_account);
        EditText etName = findViewById(R.id.et_name);
        EditText etPwd = findViewById(R.id.et_pwd);
        Button btnRegister = findViewById(R.id.register_button);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                register(account, name , pwd);
            }
        });

        Button toLoginButton=(Button)findViewById(R.id.toLogin_button);
        toLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case REGISTER_FAIL:
                        String s = (String) msg.obj;
                        Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
                        break;
                    case REGISTER_SUCCESS:
                        Toast.makeText(mContext,"注册成功！",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(mContext,LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                }
                return true;
            }
        });
    }


    private void register(String account, String name ,String pwd) {
        if (StringUtils.isEmpty(account)) {
            showToast("请输入账号");
            return;
        }
        if (StringUtils.isEmpty(name)) {
            showToast("请输入昵称");
            return;
        }
        if (StringUtils.isEmpty(pwd)) {
            showToast("请输入密码");
            return;
        }

        QMUIDialog dialog=new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("注册中...")
                .setCancelable(false)
                .show();


        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", account)
                .addFormDataPart("passwd", pwd)
                .build();
        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URl+ApiConfig.REGISTER)
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
                if(response.code()==200){
                    try{
                        String responseBody=response.body().string();
                        JSONObject jsonobject=new JSONObject(responseBody);
                        String status=jsonobject.getString("status");
                        Log.d(TAG,status);
                        dialog.dismiss();
                        //成功获取生成诗词
                        if(status.equals("true")){
                            Message msg=new Message();
                            msg.what=REGISTER_SUCCESS;
                            handler.sendMessage(msg);
                        }
                        else{
                            Message msg=new Message();
                            msg.what=REGISTER_FAIL;
                            msg.obj=jsonobject.getString("message");
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