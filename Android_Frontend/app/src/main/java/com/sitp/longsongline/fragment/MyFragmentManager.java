package com.sitp.longsongline.fragment;

import androidx.fragment.app.Fragment;

public class MyFragmentManager {
    public static Fragment[] getFragments(String from){
        Fragment fragments[] = new Fragment[]{
                new ReadFragment(),
                new PoemSearchFragment(),
                new HomeFragment(),
                new UserFragment()
        };
        return  fragments;
    }
}
