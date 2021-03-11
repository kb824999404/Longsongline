package com.sitp.longsongline.fragment;

import androidx.fragment.app.Fragment;

public class MyFragmentManager {
    public static Fragment[] getFragments(String from){
        Fragment fragments[] = new Fragment[3];
        fragments[0] = new ReadFragment();
        fragments[1] = new HomeFragment();
        fragments[2] = new UserFragment();
        return  fragments;
    }
}
