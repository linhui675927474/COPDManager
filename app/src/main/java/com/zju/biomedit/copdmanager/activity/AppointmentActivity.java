package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.Doctor;
import com.zju.biomedit.copdmanager.model.DoctorAppointment;
import com.zju.biomedit.copdmanager.model.LoginUserInfo;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.OptionPicker;

public class AppointmentActivity extends AppCompatActivity {
    @BindView(R.id.rl_time)
    RelativeLayout rlTime;
    @BindView(R.id.rl_appointment)
    RelativeLayout rlAppointment;
    @BindView(R.id.rl_doctor)
    RelativeLayout rlDoctor;
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_appointment)
    TextView tvAppointment;
    @BindView(R.id.tv_doctor)
    TextView tvDoctor;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.btn_appointment)
    Button btnAppointment;

    private ArrayList<String> availableAppointmentList;
    private String time;
    private LoginUserInfo loginUserInfo;
    private String reginfoId;
    private String orderNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener((View v) -> {
            finish();
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        clickEvent();
        setAppointmentinfo();
    }

    @Override
    protected void onResume() {
        getAppoinmentInfo();
        super.onResume();
    }

    private void clickEvent() {
        rlTime.setOnClickListener((View v) ->{
            Toast.makeText(this,"就诊时间为距离当前时间最近的周五下午，不能更改",Toast.LENGTH_SHORT).show();
        });
        rlDoctor.setOnClickListener((View v) ->{
            Toast.makeText(this,"目前只可以选择陈娟医生",Toast.LENGTH_SHORT).show();
        });
        rlName.setOnClickListener((View v) ->{
            Toast.makeText(this,"就诊人为当前用户，不能更改",Toast.LENGTH_SHORT).show();
        });
        rlAppointment.setOnClickListener((View v) ->{
            OptionPicker picker = new OptionPicker(this,availableAppointmentList);
            picker.setTextSize(20);
            picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                @Override
                public void onOptionPicked(int position, String option) {
                    tvAppointment.setText(option);
                    orderNum = getAppNum(option);
                }
            });
            picker.show();
        });
        btnAppointment.setOnClickListener((View v) ->{
            if(orderNum.equals("-1")){
                Toast.makeText(this,"很抱歉，预约已满",Toast.LENGTH_SHORT).show();
            }else{
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                        .title(R.string.tv_make_sure)
                        .content(R.string.tv_appointment_notice_text)
                        .positiveText(R.string.tv_sure)
                        .negativeText(R.string.tv_cancel);
                MaterialDialog dialog = builder.build();
                dialog.show();
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.dismiss();
                        sendAppointment();
                    }
                })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // TODO
                                dialog.dismiss();
                            }
                        });
            }
        });
        tvHistory.setOnClickListener((View v) ->{
            Intent intent = new Intent(this,AppointHistoryActivity.class);
            startActivity(intent);
        });

    }

    private void setAppointmentinfo(){
        loginUserInfo = PatientUserManager.getInstance().getLoginUserInfo();
        String name = loginUserInfo.getPatientName();
        time = TimeManager.getLatestFriday();
        tvDoctor.setText(Utils.DOCTOR_NAME);
        tvName.setText(name);
        tvTime.setText(time);

    }

    private void getAppoinmentInfo(){
        String input = "<Request>" +
                "<HospitalId>01</HospitalId>" +
                "<DeptId>119</DeptId>" +
                "<UserID>CMCC</UserID>" +
                "<DoctorId></DoctorId>" +
                "<StartDate>"+time+"</StartDate>" +
                "<EndDate>"+time+"</EndDate><IDCardNo></IDCardNo>" +
                "</Request>";
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_GET_APPOINTMENT_LIST)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();
        OkGo.<String>get(Utils.APPOINTMENT_URL)
                .tag(this)
                .params("soap_method",Utils.GET_DOC)
                .params("Input",input)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dialog.dismiss();
                        Log.i("copdsuccess",response.body());
                        Doctor doctor = xmlPrase(response.body());
                        reginfoId = doctor.getReginfoid();
                        availableAppointmentList = new ArrayList<>();
                        for(DoctorAppointment doctorAppointment:doctor.getDoctorAppointmentArrayList()){
                            if(doctorAppointment.getAvailable()){
                                String appStr = doctorAppointment.getTime()+" "+doctorAppointment.getNum()+"号";
                                availableAppointmentList.add(appStr);
                            }
                        }
                        if(availableAppointmentList.size()>0){
                            String appStr = availableAppointmentList.get(0);
                            tvAppointment.setText(appStr);
                            orderNum = getAppNum(appStr);
                        }else{
                            tvAppointment.setText("已预约满");
                            orderNum = "-1";
                        }
                        Log.i("copdsuccess",doctor.getId()+" "+doctor.getName()+" "+doctor.getTimeflag()+" "+doctor.getDoctorAppointmentArrayList().size()+" "+doctor.getReginfoid());
                    }
                    @Override
                    public void onError(Response<String> response) {
                        dialog.dismiss();
                        Log.i("copderror",response.toString());
                    }
                });
    }

    /**
     * 解析xml响应
     */
    private Doctor xmlPrase(String response){
        ArrayList<Doctor> doctorArrayList = new ArrayList<>();
        Doctor doctor = new Doctor();
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
                        if(tag.equals("GetOPDocResult")){
                            String text = pullParser.nextText();//获得当前里的文本内容
                            Log.i("copdxml",text);
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
                                        if(name.equals("doctor")){
                                            doctor = new Doctor();
                                            int id = Integer.parseInt(parser.getAttributeValue(0));
                                            doctor.setId(id);
                                        }else if(name.equals("name")){
                                            String doctorName = parser.nextText();
                                            doctor.setName(doctorName);
                                        }else if(name.equals("reginfo")){
                                            String date = parser.getAttributeValue(0);
                                            doctor.setDate(date);
                                        }else if(name.equals("timeflag")){
                                            String timeFlag = parser.nextText();
                                            doctor.setTimeflag(timeFlag);
                                        }else if(name.equals("TimeRange")){
                                            String timeRange = parser.nextText();
                                            ArrayList<DoctorAppointment> doctorAppointmentArrayList = strParse(timeRange);
                                            doctor.setDoctorAppointmentArrayList(doctorAppointmentArrayList);
                                        }
                                        else if(name.equals("reginfoid")){
                                            String reginfoid = parser.nextText();
                                            doctor.setReginfoid(reginfoid);
                                        }
                                        break;
                                    case XmlPullParser.END_TAG:
                                        //TODO 将数据进行保存操作
                                        if (parser.getName().equals("doctor")) {
                                            doctorArrayList.add(doctor);
                                            doctor = null;
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

        Doctor targetDoctor = new Doctor();
        for(Doctor doctors:doctorArrayList){
            if(doctors.getName().equals(Utils.DOCTOR_NAME)){
                targetDoctor = doctors;
            }
        }
        return targetDoctor;
    }

    /**
     * 解析号源分时段信息
     */
    private ArrayList<DoctorAppointment> strParse(String timeRangeStr){
        ArrayList<DoctorAppointment> doctorAppointmentArrayList = new ArrayList<>();
        DoctorAppointment doctorAppointment;
        String[] strArray;
        strArray = timeRangeStr.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        for(String s:strArray){
            Log.i("copdtime",s);
            String[] str;
            str = s.split("\\^");
            doctorAppointment = new DoctorAppointment();
            doctorAppointment.setNum(Integer.parseInt(str[0]));
            doctorAppointment.setTime(str[1]);
            int isAvailable = Integer.parseInt(str[2]);
            if(isAvailable == 0){
                doctorAppointment.setAvailable(true);
            }else{
                doctorAppointment.setAvailable(false);
            }
            doctorAppointmentArrayList.add(doctorAppointment);
            Log.i("copdtime",doctorAppointment.getNum()+" "+doctorAppointment.getTime()+" "+doctorAppointment.getAvailable());
        }

        return doctorAppointmentArrayList;

    }

    private void sendAppointment(){
        String input = "<Request>" +
                "<HospitalId>01</HospitalId>" +
                "<ASRowId>"+reginfoId+"</ASRowId>" +
                "<UserID>CMCC</UserID>" +
                "<PatientId></PatientId>" +
                "<TelPhone>"+loginUserInfo.getPhoneNumber()+"</TelPhone>" +
                "<TelPhone1></TelPhone1>" +
                "<PatientName>"+loginUserInfo.getPatientName()+"</PatientName>" +
                "<IDCardNo>"+loginUserInfo.getIdentityCardNumber()+"</IDCardNo>" +
                "<AppMethod>CMAPP</AppMethod>" +
                "<PayModeCode></PayModeCode>" +
                "<BilledFlag></BilledFlag>" +
                "<AppTradeNo>N</AppTradeNo>" +
                "<QueueNo>"+orderNum+"</QueueNo>" +
                "</Request>";
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_SEND_APPOINTMENT)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();
        OkGo.<String>get(Utils.APPOINTMENT_URL)
                .tag(this)
                .params("soap_method",Utils.APP_SUBMIT)
                .params("Input",input)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dialog.dismiss();
                        Log.i("appsuccess",response.body());
                        Intent intent = new Intent(AppointmentActivity.this,AppointResultActivity.class);
                        intent.putExtra("response",response.body());
                        startActivity(intent);
                    }
                    @Override
                    public void onError(Response<String> response) {
                        dialog.dismiss();
                        Log.i("apperror",response.toString());
                    }
                });
    }

    private String getAppNum(String appStr){
        appStr = appStr.substring(0,appStr.length()-1);
        String[] str;
        str = appStr.split(" ");
        return str[1];
    }

}
