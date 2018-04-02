package com.zju.biomedit.copdmanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.Appointment;
import com.zju.biomedit.copdmanager.model.LoginUserInfo;
import com.zju.biomedit.copdmanager.model.MedicineTask;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.popupwindow.TimePopupWidow;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangzheyu on 2017/10/27.
 */

public class AppHistoryAdapter extends RecyclerView.Adapter<AppHistoryAdapter.AppViewHolder>{
    private List<Appointment> mAppSet;
    private Context context;
    private int code;

    //构造器，接受数据集
    public AppHistoryAdapter(List<Appointment> data){
        this.mAppSet = data;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局文件
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_history,parent,false);
        return new AppViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AppViewHolder holder, final int position) {
        //将数据填充到具体的view中
        String userName = mAppSet.get(position).getUserName();
        String doctorName = mAppSet.get(position).getDoctorName();
        String date = mAppSet.get(position).getRegDate();
        String time = mAppSet.get(position).getClinicTime();
        String id = mAppSet.get(position).getId();
        Boolean status = mAppSet.get(position).getStatus();
        holder.tvName.setText("预约人："+userName);
        holder.tvDocName.setText("医生："+doctorName);
        holder.tvDate.setText("预约日期："+date);
        holder.tvTime.setText("最晚取号时间："+time);
        if(status){
            holder.tvStatus.setText("预约成功");
            holder.tvStatus.setTextColor(Color.parseColor("#14E715"));
            holder.tvCancel.setVisibility(View.VISIBLE);
        }else{
            holder.tvStatus.setText("已取消预约");
            holder.tvStatus.setTextColor(Color.parseColor("#F44336"));
            holder.tvCancel.setVisibility(View.GONE);
        }
        holder.tvCancel.setOnClickListener((View v) ->{
            MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                    .title(R.string.tv_make_sure)
                    .content(R.string.tv_appointment_cancel_text)
                    .positiveText(R.string.tv_sure)
                    .negativeText(R.string.tv_cancel);
            MaterialDialog dialog = builder.build();
            dialog.show();
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            // TODO
                            dialog.dismiss();
                            cancelApp(id,holder.tvStatus,holder.tvCancel,position);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            // TODO
                            dialog.dismiss();
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return mAppSet.size();
    }

    class AppViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_app_status)
        TextView tvStatus;
        @BindView(R.id.tv_app_cancel)
        TextView tvCancel;
        @BindView(R.id.tv_user_name)
        TextView tvName;
        @BindView(R.id.tv_doctor_name)
        TextView tvDocName;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public AppViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void cancelApp(String id,TextView tvStatus,TextView tvCancel,int position){
        LoginUserInfo loginUserInfo = PatientUserManager.getInstance().getLoginUserInfo();
        String input = "<Request>" +
                "<orderid>"+id+"</orderid>" +
                "<mobile>"+loginUserInfo.getPhoneNumber()+"</mobile>" +
                "<cancelreason>cancel</cancelreason>" +
                "<UserId>CMCC</UserId>" +
                "<AdmID></AdmID>" +
                "</Request>";
        Log.i("appcancel",input);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .content(Utils.MESSAGE_CANCEL_APPOINTMENT)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();
        OkGo.<String>get(Utils.APPOINTMENT_URL)
                .tag(this)
                .params("soap_method",Utils.CANCEL_APP)
                .params("Input",input)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dialog.dismiss();
                        Log.i("appcancel",response.body());
                        xmlPrase(response.body());
                        if(code == 0){
                            Toast.makeText(context,"取消成功！",Toast.LENGTH_SHORT).show();
                            tvStatus.setText("已取消预约");
                            tvStatus.setTextColor(Color.parseColor("#F44336"));
                            tvCancel.setVisibility(View.GONE);
                            mAppSet.get(position).setStatus(false);
                        }else{
                            Toast.makeText(context,"取消失败，请重试",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(Response<String> response) {
                        dialog.dismiss();
                        Log.i("appcancel",response.toString());
                    }
                });

    }

    /**
     * 解析xml响应
     */
    private void xmlPrase(String response){
        try{
            //得到XML解析器
            XmlPullParser pullParser = Xml.newPullParser();
            InputStream is = new ByteArrayInputStream(response.getBytes("UTF-8"));
            pullParser.setInput(is, "utf-8");
            //得到事件类型
            int eventType = pullParser.getEventType();
            //遍历内部的内容
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType) {
                    case XmlPullParser.START_DOCUMENT :
                        //TODO 一般进行数据的初始化操作
                        break;
                    case XmlPullParser.START_TAG :
                        //TODO 解析
                        String tag = pullParser.getName();//获取当前节点的标记
                        //pullParser.getAttributeValue(0);//获取当前节点的第一个属性值
                        if(tag.equals("CancelOrderResult")){
                            String text = pullParser.nextText();//获得当前里的文本内容
                            XmlPullParser parser = Xml.newPullParser();
                            InputStream isDetail = new ByteArrayInputStream(text.getBytes("UTF-8"));
                            parser.setInput(isDetail,"utf-8");
                            int type = parser.getEventType();
                            while (type != XmlPullParser.END_DOCUMENT){
                                switch (type){
                                    case XmlPullParser.START_DOCUMENT :
                                        //TODO 一般进行数据的初始化操作
                                        break;
                                    case XmlPullParser.START_TAG:
                                        String name = parser.getName();
                                        if(name.equals("resultcode")){
                                            code = Integer.parseInt(parser.nextText());
                                        }
                                        break;
                                    case XmlPullParser.END_TAG:
                                        //TODO 将数据进行保存操作
                                        break;
                                }
                                type = parser.next();
                            }

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //TODO 将数据进行保存操作
                        break;
                    default:
                        break;
                }
                eventType = pullParser.next();//读取下一个标签
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
