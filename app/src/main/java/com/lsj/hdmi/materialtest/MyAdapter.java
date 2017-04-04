package com.lsj.hdmi.materialtest;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lsj.hdmi.materialtest.bean.HeaderBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdmi on 17-3-25.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<String> dataSet;
    private List<HeaderBean> headerSet;
    private Context context;
    private String TAG="MyAdapter";

    public MyAdapter(List<String> dataSet) {
        this.dataSet = dataSet;
        this.context=context;
        headerSet=new ArrayList<HeaderBean>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       if (headerSet!=null&&getHeaderCount()>0){
           for (HeaderBean headerBean:headerSet){
               if(headerBean.getViewType()==viewType){
                   Log.d(TAG, "onCreateViewHolder: ------------createHeader------------");
                   View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_header_view,parent,false);
                   HeaderHolder headerHolder=new HeaderHolder(v);
                   return headerHolder;
               }
           }
       }
           Log.d(TAG, "onCreateViewHolder: ------------createContent------------");
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_view,parent,false);
            ContentHolder contentHolder =new ContentHolder(v);
            return contentHolder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(isHeader(position)){
            onBindHeaderHolder((HeaderHolder) holder,position);
            Log.d(TAG, "onBindViewHolder: ----------------bindData---------------");
        }else{
            onBindContentHolder((ContentHolder) holder,position-getHeaderCount());
        }


    }

    @Override
    public int getItemViewType(int position) {
        if(isHeader(position)){
            Log.d(TAG, "onCreateViewHolder: ------------createHeaderViewType------------");
            return headerSet.get(position).getViewType();
        }
        Log.d(TAG, "onCreateViewHolder: ------------createContentViewType------------");
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataSet.size()+getHeaderCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if(headerSet!=null&&!headerSet.isEmpty()){
            for (HeaderBean headerBean:headerSet){
                recyclerView.getRecycledViewPool().setMaxRecycledViews(headerBean.getViewType(),headerBean.getCacheSize());
            }
        }
//        recyclerView.getRecycledViewPool().setMaxRecycledViews(0,0);
    }

    private void onBindContentHolder(final ContentHolder holder, int position){
        holder.cardTextview.setText(dataSet.get(position));
        holder.shadowSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    holder.cardView.setElevation(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        holder.cornerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    holder.cardView.setRadius(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

      class ContentHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private SeekBar shadowSeekbar;
        private SeekBar cornerSeekbar;
        private TextView cardTextview;


        public ContentHolder(View itemView) {
            super(itemView);
            init(itemView);
        }


        private void init(View view){
            cardView= (CardView) view.findViewById(R.id.card_view);
            shadowSeekbar= (SeekBar) view.findViewById(R.id.shadow_seekbar);
            cornerSeekbar= (SeekBar) view.findViewById(R.id.corner_seekbar);
            cardTextview= (TextView) view.findViewById(R.id.card_textview);
        }


    }


    class HeaderHolder extends RecyclerView.ViewHolder{
        private TextView headerTextview;
        public HeaderHolder(View headView){
            super(headView);
            init(headView);
        }

        private void init(View headView){
            headerTextview= (TextView) headView.findViewById(R.id.header_textview);
        }
    }

    public void addHeader(HeaderBean headerBean){
        headerSet.add(headerBean);
    }

    private boolean isHeader(int position){
        if(headerSet.size()>position){
            if(headerSet.get(position).getViewType()>0){
                Log.d(TAG, "isHeader:---------------"+position);
                return true;
                
            }
        }
        return false;
    }

    private int getHeaderCount(){
        if(headerSet!=null){
            return headerSet.size();
        }
        return 0;
    }

    private void onBindHeaderHolder(HeaderHolder holder,int position){
            holder.headerTextview.setText(headerSet.get(position).getData().toString());
    }


}
