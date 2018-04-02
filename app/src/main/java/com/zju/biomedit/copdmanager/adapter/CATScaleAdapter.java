package com.zju.biomedit.copdmanager.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.CatScale;

import java.util.List;

import io.realm.Realm;

/**
 * Created by wangzheyu on 2017/7/6.
 */

public class CATScaleAdapter extends RecyclerView.Adapter<CATScaleAdapter.ViewHolder>{
    private List<CatScale> mCatSet;
    private Context context; //设置全局Application会与现有的冲突

    //构造器，接受数据集
    public CATScaleAdapter(List<CatScale> data){
        mCatSet = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局文件
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cat_scale,parent,false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //将数据填充到具体的view中
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        String catZero = mCatSet.get(position).getZeroPointText();
        String catFive = mCatSet.get(position).getFivePointText();
        holder.catTvZero.setText(catZero);
        holder.catTvFive.setText(catFive);
        holder.valueZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCatSet.get(position).getScore()!=0){
                    holder.valueZero.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale_pressed));
                    holder.valueZero.setTextColor(ContextCompat.getColor(context,R.color.white));
                    holder.valueOne.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueOne.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueTwo.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueTwo.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueThree.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueThree.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueFour.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueFour.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueFive.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueFive.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            CatScale catScale = realm.where(CatScale.class).equalTo("problemId", position+1).findFirst();
                            catScale.setScore(0);
                        }
                    });
                }
            }

        });
        holder.valueOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCatSet.get(position).getScore()!=1){
                    holder.valueOne.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale_pressed));
                    holder.valueOne.setTextColor(ContextCompat.getColor(context,R.color.white));
                    holder.valueZero.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueZero.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueTwo.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueTwo.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueThree.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueThree.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueFour.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueFour.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueFive.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueFive.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            CatScale catScale = realm.where(CatScale.class).equalTo("problemId", position+1).findFirst();
                            catScale.setScore(1);
                        }
                    });
                }

            }

        });
        holder.valueTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCatSet.get(position).getScore()!=2){
                    holder.valueTwo.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale_pressed));
                    holder.valueTwo.setTextColor(ContextCompat.getColor(context,R.color.white));
                    holder.valueOne.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueOne.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueZero.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueZero.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueThree.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueThree.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueFour.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueFour.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueFive.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueFive.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            CatScale catScale = realm.where(CatScale.class).equalTo("problemId", position+1).findFirst();
                            catScale.setScore(2);
                        }
                    });

                }

            }

        });
        holder.valueThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCatSet.get(position).getScore()!=3){
                    holder.valueThree.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale_pressed));
                    holder.valueThree.setTextColor(ContextCompat.getColor(context,R.color.white));
                    holder.valueOne.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueOne.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueZero.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueZero.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueTwo.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueTwo.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueFour.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueFour.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueFive.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueFive.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            CatScale catScale = realm.where(CatScale.class).equalTo("problemId", position+1).findFirst();
                            catScale.setScore(3);
                        }
                    });
                }

            }

        });
        holder.valueFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCatSet.get(position).getScore()!=4){
                    holder.valueFour.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale_pressed));
                    holder.valueFour.setTextColor(ContextCompat.getColor(context,R.color.white));
                    holder.valueOne.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueOne.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueZero.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueZero.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueThree.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueThree.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueTwo.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueTwo.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueFive.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueFive.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            CatScale catScale = realm.where(CatScale.class).equalTo("problemId", position+1).findFirst();
                            catScale.setScore(4);
                        }
                    });
                }

            }

        });
        holder.valueFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCatSet.get(position).getScore()!=5){
                    holder.valueFive.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale_pressed));
                    holder.valueFive.setTextColor(ContextCompat.getColor(context,R.color.white));
                    holder.valueOne.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueOne.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueZero.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueZero.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueThree.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueThree.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueFour.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueFour.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.valueTwo.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cat_scale));
                    holder.valueTwo.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            CatScale catScale = realm.where(CatScale.class).equalTo("problemId", position+1).findFirst();
                            catScale.setScore(5);
                        }
                    });
                }

            }

        });
    }

    @Override
    public int getItemCount() {
        return mCatSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView catTvZero;
        TextView catTvFive;
        TextView valueZero;
        TextView valueOne;
        TextView valueTwo;
        TextView valueThree;
        TextView valueFour;
        TextView valueFive;
        ViewHolder(View itemView) {
            super(itemView);
            //由于itemView是item的布局文件，我们需要的是里面的textView，因此利用itemView.findViewById获
            //取里面的textView实例，后面通过onBindViewHolder方法能直接填充数据到每一个textView了
            catTvZero = (TextView) itemView.findViewById(R.id.cat_text_0);
            catTvFive = (TextView) itemView.findViewById(R.id.cat_text_5);
            valueZero = (TextView) itemView.findViewById(R.id.tv_value_zero);
            valueOne = (TextView) itemView.findViewById(R.id.tv_value_one);
            valueTwo = (TextView) itemView.findViewById(R.id.tv_value_two);
            valueThree = (TextView) itemView.findViewById(R.id.tv_value_three);
            valueFour = (TextView) itemView.findViewById(R.id.tv_value_four);
            valueFive = (TextView) itemView.findViewById(R.id.tv_value_five);
        }
    }

}
