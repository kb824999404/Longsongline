package com.sitp.longsongline.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.widget.QMUIVerticalTextView;
import com.sitp.longsongline.R;
import com.sitp.longsongline.entity.Music;
import com.sitp.longsongline.entity.Poem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>{

    private ArrayList<Music> localDataSet;
    private OnItemClickLitener myOnItemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout;
        public ViewHolder(View view){
            super(view);
        }
        public void SetMusic(Music music){
            TextView title_textView = itemView.findViewById(R.id.music_name);
            title_textView.setText(music.getTitle());
            TextView time_textView = itemView.findViewById(R.id.music_time);
            time_textView.setText(music.getTime());
            itemLayout = (LinearLayout)itemView.findViewById(R.id.item_layout);
        }
    }

    public interface OnItemClickLitener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickListener){
        this.myOnItemClickListener = mOnItemClickListener;
    }

    public MusicAdapter(ArrayList<Music> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_music, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.SetMusic(localDataSet.get(position));

        if(myOnItemClickListener!=null){
            viewHolder.itemLayout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    myOnItemClickListener.onItemClick(view,position);
                }
            });
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
