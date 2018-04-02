package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.Doctor;
import com.zju.biomedit.copdmanager.model.DoctorAppointment;
import com.zju.biomedit.copdmanager.support.Utils;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppointResultActivity extends AppCompatActivity {
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.btn_history)
    Button btnHistory;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    private String response;
    private int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener((View v) -> {
            finish();
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        response = intent.getStringExtra("response");
        initView();
        clickEvent();
    }

    private void initView(){
        xmlPrase(response);
    }

    private void clickEvent(){
        btnFinish.setOnClickListener((View v) ->{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        });
        btnHistory.setOnClickListener((View v) ->{
            Intent intent = new Intent(this,AppointHistoryActivity.class);
            startActivity(intent);
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
                        if(tag.equals("OPAppSubmitResult")){
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
                                        if(name.equals("resultcode")){
                                            code = Integer.parseInt(parser.nextText());
                                            if(code == 0){
                                                tvResult.setText("预约成功");
                                                tvResult.setTextColor(Color.parseColor("#14E715"));
                                            }else{
                                                tvResult.setText("预约失败");
                                                tvResult.setTextColor(Color.parseColor("#F44336"));
                                            }
                                        }else if(name.equals("codedesc")){
                                            if(code != 0){
                                                tvDetail.setText(parser.nextText());
                                            }
                                        }else if(name.equals("msg")){
                                            if(code == 0){
                                                tvDetail.setText(parser.nextText());
                                            }
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
