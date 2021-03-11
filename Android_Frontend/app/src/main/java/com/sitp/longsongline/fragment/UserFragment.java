package com.sitp.longsongline.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sitp.longsongline.R;
import com.sitp.longsongline.activity.LoginActivity;
import com.sitp.longsongline.data.UserInfo;

public class UserFragment extends BaseFragment  {

    private LinearLayout container_nologin;
    private LinearLayout container;

    @Override
    protected int InitLayout(){
        return R.layout.fragment_user;
    }

    @Override
    protected void InitView(){
        //设置标题栏标题
        QMUITopBar topbar=(QMUITopBar)myView.findViewById(R.id.topbar);
        topbar.setTitle("个人中心");

        container_nologin = (LinearLayout)myView.findViewById(R.id.user_container_nologin);
        container = (LinearLayout)myView.findViewById(R.id.user_container);


        Button loginButton = (Button)myView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    Log.d("UserFragment","Login click!");
                }
        });

        Button logoutButton = (Button)myView.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                UserInfo.isLogin = false;
                UserInfo.uid = 0;
                isLogin();
            }
        });
        UserInfo.isLogin=true;
        isLogin();

    }
    private void isLogin(){
        if(UserInfo.isLogin){
            container_nologin.setVisibility(View.INVISIBLE);
            container.setVisibility(View.VISIBLE);
            TextView username_tv = (TextView)myView.findViewById(R.id.username_tv);
            username_tv.setText(UserInfo.userName);
        }
        else{
            container.setVisibility(View.INVISIBLE);
            container_nologin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isLogin();
    }

}