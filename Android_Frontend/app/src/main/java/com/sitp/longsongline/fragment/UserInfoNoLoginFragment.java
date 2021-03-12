package com.sitp.longsongline.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sitp.longsongline.R;
import com.sitp.longsongline.activity.LoginActivity;
import com.sitp.longsongline.data.UserInfo;

public class UserInfoNoLoginFragment extends BaseFragment {
    @Override
    protected int InitLayout(){
        return R.layout.fragment_user_container_nologin;
    }

    @Override
    protected void InitView() {
        Button loginButton = (Button)myView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                Log.d("UserFragment","Login click!");
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
    }
}
