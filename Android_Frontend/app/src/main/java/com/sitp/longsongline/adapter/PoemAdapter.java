package com.sitp.longsongline.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.widget.QMUIVerticalTextView;
import com.sitp.longsongline.R;
import com.sitp.longsongline.entity.Poem;

import java.util.ArrayList;

public class PoemAdapter extends RecyclerView.Adapter<PoemAdapter.ViewHolder>{

    private ArrayList<Poem> localDataSet;
    private OnItemClickLitener myOnItemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout itemLayout;
        public ViewHolder(View view){
            super(view);
        }
        public void SetPoem(Poem poem){
            TextView title_textView = itemView.findViewById(R.id.title_textview);
            title_textView.setText(poem.title);
            QMUIVerticalTextView author_textView = itemView.findViewById(R.id.author_textview);
            author_textView.setText(poem.author);
            LinearLayout content_layout=itemView.findViewById(R.id.content_layout);
            for(int i=0;i<poem.contents.length;i++){
                String line=poem.contents[i];
                QMUIVerticalTextView line_textView = (QMUIVerticalTextView)content_layout.getChildAt(i);
                line_textView.setText(line);
            }

            itemLayout = (RelativeLayout)itemView.findViewById(R.id.item_layout);
        }
    }

    public interface OnItemClickLitener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickListener){
        this.myOnItemClickListener = mOnItemClickListener;
    }

    public PoemAdapter(ArrayList<Poem> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_poem_card, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.SetPoem(localDataSet.get(position));

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
