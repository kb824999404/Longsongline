package com.sitp.longsongline.fragment;

import androidx.fragment.app.Fragment;

public class MyFragmentManager {
    public static Fragment[] getFragments(String from){
        Fragment fragments[] = new Fragment[1];
        fragments[0] = new HomeFragment();
        return  fragments;
    }
}
