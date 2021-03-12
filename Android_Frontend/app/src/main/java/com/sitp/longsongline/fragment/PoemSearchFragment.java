package com.sitp.longsongline.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sitp.longsongline.R;
import com.sitp.longsongline.activity.TabLayoutActivity;
import com.sitp.longsongline.data.UserInfo;

public class PoemSearchFragment extends BaseFragment {

        @Override
        protected int InitLayout(){
            return R.layout.fragment_poem_search;
        }

        @Override
        protected void InitView() {
            QMUITopBar topbar=(QMUITopBar)myView.findViewById(R.id.topbar);
            topbar.setTitle("文库");

        }

        @Override
        public void onResume(){
            super.onResume();
        }
}
