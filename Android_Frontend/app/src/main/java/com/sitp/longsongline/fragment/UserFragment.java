package com.sitp.longsongline.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sitp.longsongline.R;

public class UserFragment extends BaseFragment  {

    @Override
    protected int InitLayout(){
        return R.layout.fragment_user;
    }

    @Override
    protected void InitView(){
        //设置标题栏标题
        QMUITopBar topbar=(QMUITopBar)myView.findViewById(R.id.topbar);
        topbar.setTitle("个人中心");
    }

}