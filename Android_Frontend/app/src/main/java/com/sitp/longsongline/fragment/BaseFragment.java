package com.sitp.longsongline.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sitp.longsongline.R;

public abstract class BaseFragment extends Fragment{
    protected View myView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        myView = inflater.inflate(InitLayout(),container,false);
        InitView();
        return myView;
    }

    protected abstract int InitLayout();

    protected abstract void InitView();
}
