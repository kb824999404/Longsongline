package com.sitp.longsongline.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.sitp.longsongline.R;
import com.sitp.longsongline.activity.LoginActivity;
import com.sitp.longsongline.activity.TabLayoutActivity;
import com.sitp.longsongline.data.UserInfo;

import androidx.fragment.app.Fragment;

import java.util.List;

public class UserInfoFragment extends BaseFragment {


    @Override
    protected int InitLayout(){
        return R.layout.fragment_user_container;
    }

    @Override
    protected void InitView() {
        Button logoutButton = (Button)myView.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                UserInfo.isLogin = false;
                UserInfo.uid = 0;
//                ((UserFragment)getParentFragment()).isLogin();
                ((TabLayoutActivity)getActivity()).changeToHome();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        TextView username_tv = (TextView)myView.findViewById(R.id.username_tv);
        username_tv.setText(UserInfo.userName);
    }
}
