package com.zju.biomedit.copdmanager.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.HealthVideo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by wangzheyu on 2017/7/6.
 */

public class HealthVideoAdapter extends RecyclerView.Adapter<HealthVideoAdapter.KnoViewHolder>{
    private List<HealthVideo> mVideoSet;
    private Activity context; //设置全局Application会与现有的冲突

    //构造器，接受数据集
    public HealthVideoAdapter(Activity activity, List<HealthVideo> data){
        context = activity;
        this.mVideoSet = data;
    }

    @Override
    public KnoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局文件
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
        return new KnoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final KnoViewHolder holder, final int position) {
        //将数据填充到具体的view中
        String videoName = mVideoSet.get(position).getVideoName();
        String videoPic = mVideoSet.get(position).getVideoPic();
        String videoUrl = mVideoSet.get(position).getVideoUrl();
        holder.jcVideoPlayer.setUp(videoUrl, JCVideoPlayer.SCREEN_LAYOUT_LIST,"");
        Glide.with(context)
                .load(videoPic)
                .into(holder.jcVideoPlayer.thumbImageView);
        holder.tvVideoName.setText(videoName);
//        holder.videoCard.setOnClickListener((View v) ->{
//            UserAgent.OnEvent(context, Utils.E_VIDEO + " " + videoName);
//        });


    }

    @Override
    public int getItemCount() {
        return mVideoSet.size();
    }

    class KnoViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.video_name)
        TextView tvVideoName;
        @BindView(R.id.video_kno)
        JCVideoPlayerStandard jcVideoPlayer;
        @BindView(R.id.kno_video_card)
        CardView videoCard;

        public KnoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



}
