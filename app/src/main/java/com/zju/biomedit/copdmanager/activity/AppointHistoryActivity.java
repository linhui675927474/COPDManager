package com.zju.biomedit.copdmanager.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.adapter.AppHistoryAdapter;
import com.zju.biomedit.copdmanager.adapter.DrugHistoryAdapter;
import com.zju.biomedit.copdmanager.model.Appointment;
import com.zju.biomedit.copdmanager.model.Doctor;
import com.zju.biomedit.copdmanager.model.DoctorAppointment;
import com.zju.biomedit.copdmanager.model.LoginUserInfo;
import com.zju.biomedit.copdmanager.model.MedicineTask;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppointHistoryActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView_app_history)
    RecyclerView mRecyclerView;
    private AppHistoryAdapter appHistoryAdapter;
    private String time;
    private LoginUserInfo loginUserInfo;
    private List<Appointment> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener((View v) -> {
            finish();
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        getAppHistory();
    }

    private void initRecyclerView(List<Appointment> dataList) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AppointHistoryActivity.this));
        mRecyclerView.setHasFixedSize(true);
        appHistoryAdapter = new AppHistoryAdapter(dataList);
        mRecyclerView.setAdapter(appHistoryAdapter);
    }

    private void getAppHistory(){
        time = TimeManager.getLatestFriday();
        loginUserInfo = PatientUserManager.getInstance().getLoginUserInfo();
        String input = "<Request>" +
                "<CardNo></CardNo>" +
                "<PatientName>"+loginUserInfo.getPatientName()+"</PatientName>" +
                "<IDCardNo>"+loginUserInfo.getIdentityCardNumber()+"</IDCardNo>" +
                "<StartDate>"+time+"</StartDate>" +
                "<EndDate>"+time+"</EndDate>"+"" +
                "<AppMethod>CMAPP</AppMethod>" +
                "</Request>";
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_GET_APPOINTMENT_HISTORY)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();
        OkGo.<String>get(Utils.APPOINTMENT_URL)
                .tag(this)
                .params("soap_method",Utils.GET_APP_HISTORY)
                .params("Input",input)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dialog.dismiss();
                        Log.i("apphistory",response.body());
                        xmlPrase(response.body());
                        initRecyclerView(dataList);
                    }
                    @Override
                    public void onError(Response<String> response) {
                        dialog.dismiss();
                        Log.i("apphistory",response.toString());
                    }
                });

    }

    /**
     * 解析xml响应
     */
    private void xmlPrase(String response){
        dataList = new ArrayList<>();
        Appointment appointment = new Appointment();
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
                        if(tag.equals("QueryOrderResult")){
                            String text = pullParser.nextText();//获得当前里的文本内容
                            Log.i("appxml",text);
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
                                        if(name.equals("order")){
                                            appointment = new Appointment();
                                            String id = parser.getAttributeValue(0);
                                            appointment.setId(id);
                                        }else if(name.equals("username")){
                                            String userName = parser.nextText();
                                            appointment.setUserName(userName);
                                        }else if(name.equals("tel")){
                                            String tel = parser.nextText();
                                            appointment.setTel(tel);
                                        }else if(name.equals("hospname")){
                                            String hosName = parser.nextText();
                                            appointment.setHospName(hosName);
                                        }else if(name.equals("deptname")) {
                                            String deptName = parser.nextText();
                                            appointment.setDeptName(deptName);
                                        }else if(name.equals("doctorname")){
                                            String docName = parser.nextText();
                                            appointment.setDoctorName(docName);
                                        }else if(name.equals("regdate")){
                                            String date = parser.nextText();
                                            appointment.setRegDate(date);
                                        }else if(name.equals("status")){
                                            String status = parser.nextText();
                                            if(status.equals("I")){
                                                appointment.setStatus(true);
                                            }else{
                                                appointment.setStatus(false);
                                            }
                                        }else if(name.equals("msg")){
                                            String msg = parser.nextText();
                                            appointment.setMsg(msg);
                                        }else if(name.equals("clinictime")){
                                            String time= parser.nextText();
                                            appointment.setClinicTime(time);
                                        }else if(name.equals("source")){
                                            String source= parser.nextText();
                                            appointment.setSource(source);
                                        }
                                        break;
                                    case XmlPullParser.END_TAG:
                                        //TODO 将数据进行保存操作
                                        if (parser.getName().equals("order")) {
                                            dataList.add(appointment);
                                            Log.i("appxml",appointment.getId());
                                            appointment = null;
                                        }
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
