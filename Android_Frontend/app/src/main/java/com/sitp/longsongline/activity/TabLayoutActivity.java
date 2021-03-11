package com.sitp.longsongline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.sitp.longsongline.R;
import com.sitp.longsongline.custom.CustomTabLayout;
import com.sitp.longsongline.fragment.MyFragmentManager;
import com.sitp.longsongline.util.FontManager;

public class TabLayoutActivity extends AppCompatActivity {

    private CustomTabLayout tabLayout;
    private Fragment [] mFragmensts;
    private static final int defaultFragment=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);

        //隐藏默认标题栏
        getSupportActionBar().hide();

        Init();
    }

    private void Init(){
        final Typeface tf= FontManager.getTypeFace(this,FontManager.FONTAWESOME);

        tabLayout = (CustomTabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.fa_icon_areachart));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.fa_icon_linechart));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.fa_icon_piechart));

        mFragmensts = MyFragmentManager.getFragments("Tab Layout");
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frame_container,mFragmensts[defaultFragment]).commit();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void onTabItemSelected(int position){
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = mFragmensts[0];
                break;
        }
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).commit();
        }
    }
}