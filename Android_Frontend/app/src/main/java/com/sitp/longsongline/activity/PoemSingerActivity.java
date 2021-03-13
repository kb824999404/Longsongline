package com.sitp.longsongline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qmuiteam.qmui.layout.QMUIButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.sitp.longsongline.R;

import java.util.ArrayList;
import java.util.List;

public class PoemSingerActivity extends AppCompatActivity {

    private EditText poemTitle;
    private ArrayList<EditText> poemContents;

    private static String TAG="PoemSingerActivity";
    private static final int TEXT_ERROR = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem_singer);
        //隐藏默认标题栏
        getSupportActionBar().hide();
        Init();
    }

    private void Init(){
        //设置标题栏标题
        QMUITopBar topbar=(QMUITopBar)findViewById(R.id.topbar);
        topbar.setTitle("诗词配乐");

        poemTitle = (EditText)findViewById(R.id.poemTitle);
        LinearLayout poemContentLayout = (LinearLayout)findViewById(R.id.poemContent);
        poemContents = new ArrayList<EditText>();
        for(int i=0;i<poemContentLayout.getChildCount();i++){
            LinearLayout contentLayout = (LinearLayout)poemContentLayout.getChildAt(i);
            EditText poemContent = (EditText)contentLayout.getChildAt(0);
            Log.d(TAG,poemContent.toString());
            poemContents.add(poemContent);
        }

        //配乐按钮
        QMUIRoundButton singerButton=(QMUIRoundButton)findViewById(R.id.singerButton);
        singerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String poem="";
                Boolean error=false;
                for(EditText editText : poemContents){
                    String text = editText.getText().toString();
                    if(text.length()<=7){
                        poem +=text+"|";
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"每句诗不超过七个字！",Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"字数错误！");
                        error = true;
                        break;
                    }
                }
                if(!error){
                    Intent intent=new Intent(PoemSingerActivity.this,SongSynthesisActivity.class);
                    intent.putExtra("poem",poem);
                    Log.d(TAG,poem);
                    intent.putExtra("title",poemTitle.getText().toString());
                    startActivity(intent);
                }
            }
        });

        QMUIRoundButton myMusicButton=(QMUIRoundButton)findViewById(R.id.myMusicButton);
        myMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMusicList = new Intent(getApplicationContext(),MusicListActivity.class);
                startActivity(toMusicList);

            }
        });

    }
}