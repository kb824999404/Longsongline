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

    private UserInfoFragment userInfoFragment;
    private UserInfoNoLoginFragment userInfoNoLoginFragment;

    @Override
    protected int InitLayout(){
        return R.layout.fragment_user;
    }

    @Override
    protected void InitView(){
        //设置标题栏标题
        QMUITopBar topbar=(QMUITopBar)myView.findViewById(R.id.topbar);
        topbar.setTitle("个人中心");

        userInfoFragment = new UserInfoFragment();
        userInfoNoLoginFragment = new UserInfoNoLoginFragment();

        isLogin();

    }
    public void isLogin(){
        if(UserInfo.isLogin){
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.frame_container,userInfoFragment).commit();
//            TextView username_tv = (TextView)myView.findViewById(R.id.username_tv);
//            username_tv.setText(UserInfo.userName);
        }
        else{
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.frame_container,userInfoNoLoginFragment).commit();
        }
    }

//    @Override
//    public void onStart() {
//        isLogin();
//
//        super.onStart();
//    }


    @Override
    public void onResume() {
        isLogin();
        super.onResume();

    }

}