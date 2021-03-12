package com.sitp.longsongline.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sitp.longsongline.R;
import com.sitp.longsongline.activity.PoemReadActivity;
import com.sitp.longsongline.adapter.PoemAdapter;
import com.sitp.longsongline.entity.Poem;

import java.util.ArrayList;

public class ReadFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private ArrayList<Poem> poems;

    @Override
    protected int InitLayout(){
        return R.layout.fragment_read;
    }

    @Override
    protected void InitView() {
        //设置标题栏标题
        QMUITopBar topbar = (QMUITopBar) myView.findViewById(R.id.topbar);
        topbar.setTitle("诗词阅读");

        String[] content1={"春眠不觉晓","处处闻啼鸟","夜里风雨声","花落知多少"};
        String[] content2={"杨花落尽子规啼","闻道龙标过无锡","212323","1232321"};
        poems = new ArrayList<Poem>();
        poems.add(new Poem("春晓","李白",content1));
        poems.add(new Poem("杨花落尽子规啼","李白",content2));

        recyclerView = myView.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        PoemAdapter poemAdapter=new PoemAdapter(poems);
        recyclerView.setAdapter(poemAdapter);

        PagerSnapHelper pagerSnapHelper=new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        poemAdapter.setOnItemClickListener(new PoemAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getContext(), PoemReadActivity.class);
                intent.putExtra("Index",position);
                startActivity(intent);
            }
        });

    }

    public void onItemClick(View view){
        int position = recyclerView.getChildAdapterPosition(view);
        Toast.makeText(getActivity(),"点击了"+poems.get(position),Toast.LENGTH_SHORT).show();
    }
}
