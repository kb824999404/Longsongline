package com.sitp.longsongline.fragment;

import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.sitp.longsongline.R;
import com.sitp.longsongline.activity.LoginActivity;
import com.sitp.longsongline.activity.PoemGenerateActivity;
import com.sitp.longsongline.activity.PoemSingerActivity;
import com.sitp.longsongline.activity.RegisterActivity;
import com.sitp.longsongline.activity.SearchByPicActivity;
import com.sitp.longsongline.data.UserInfo;

public class HomeFragment extends BaseFragment implements View.OnClickListener{


    @Override
    protected int InitLayout(){
        return R.layout.fragment_home;
    }


    @Override
    protected void InitView(){
        //设置标题栏标题
        QMUITopBar topbar=(QMUITopBar)myView.findViewById(R.id.topbar);
        topbar.setTitle("长歌行");
        InitButtons();
    }

    private void InitButtons(){


        //获取Button
        QMUIRoundButton poemGenerate=(QMUIRoundButton)myView.findViewById(R.id.poemGenerate);
        QMUIRoundButton searchByPic=(QMUIRoundButton)myView.findViewById(R.id.searchByPic);
        QMUIRoundButton poemMusic=(QMUIRoundButton)myView.findViewById(R.id.poemMusic);
        poemGenerate.setOnClickListener(this);
        searchByPic.setOnClickListener(this);
        poemMusic.setOnClickListener(this);


    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.poemGenerate:
                startActivity(new Intent(getContext(), PoemGenerateActivity.class));
                break;
            case R.id.searchByPic:
                startActivity(new Intent(getContext(), SearchByPicActivity.class));
                break;
            case R.id.poemMusic:
                if(UserInfo.isLogin){
                    startActivity(new Intent(getContext(), PoemSingerActivity.class));
                }
                else {
                    showDialog();
                }
                break;
            default:
                showDialog();
                break;
        }
    }

    private void showDialog(){
        new QMUIDialog.MessageDialogBuilder(getContext())
                .setTitle("提示")
                .setMessage("该功能需要登录，请先登录！")
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
