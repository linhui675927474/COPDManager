package com.zju.biomedit.copdmanager.activity.sixMWD;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.activity.EvaluationActivity;
import com.zju.biomedit.copdmanager.activity.MainActivity;
import com.zju.biomedit.copdmanager.activity.SixMinuteWalkTestActivity;
import com.zju.biomedit.copdmanager.model.CatScale;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.model.ScaleTask;
import com.zju.biomedit.copdmanager.model.SixMWDTask;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.JsonUtil;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import java.util.ArrayList;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import io.realm.Realm;


//数据展示和提交

public class SixMWDActivity2 extends AppCompatActivity  implements OnChartGestureListener, OnChartValueSelectedListener {
   // @BindView(R.id.six_mwd_start_test)
 //   Button btnSubmit;
    public static final String TAG="SixMWDActivity2";
    public Realm realm;
    private SixMWDTask sixMWD;
    private double distance=0;
    private String submitTime;


    private Button btnSubmit;

    private LineChart lineChart;
    private LineData lineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6mwd3);
        lineChart = findViewById(R.id.line_chart);
        btnSubmit=findViewById(R.id.submit_only_6mwd_data);

        lineChart.setOnChartGestureListener(this);
        lineChart.setOnChartValueSelectedListener(this);
        lineChart.setDrawGridBackground(false);

        //设置可拖动或缩放
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        //设置可点击
        lineChart.setTouchEnabled(true);

        //若为false，则x，y方向可分别缩放
        lineChart.setPinchZoom(true);

        //设置上下限
        Typeface tf = Typeface.createFromAsset(getBaseContext().getAssets(), "OpenSans-Regular.ttf");

        LimitLine ll1 = new LimitLine(90f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll1.setTextSize(10f);
        ll1.setTypeface(tf);

        LimitLine ll2 = new LimitLine(80f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        ll2.setTypeface(tf);

        XAxis xAxis = lineChart.getXAxis();//X轴的类
        //设置X轴的文字在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //设置描述文字
        Description description = new Description();
        description.setText("7天走势图");
        lineChart.setDescription(description);

        //去除右边Y轴
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.addLimitLine(ll1);
        yAxisLeft.addLimitLine(ll2);
        init();
      //  submit();
    }

    public void init(){
        String[] xl={"1", "2", "3", "4", "5", "6", "7", "8"};
        String[] yl={"80", "85", "90", "95", "100", "105", "110", "115"};
        lineData=getData(xl,yl);
        lineChart.setData(lineData);
        lineChart.animateX(2000);//动画时间
    }

    public LineData getData(String[] xl, String[] yl){
        ArrayList<Entry> yValues= new ArrayList<>();
        for (int i=0; i<yl.length; i++){
            //要显示的y轴数据
            yValues.add(new Entry(Float.parseFloat(xl[i]), Float.parseFloat(yl[i])));
        }

        //一条曲线对应一个LineDataSet
        LineDataSet set1= new LineDataSet(yValues, "前8次评分");
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);//设置曲线为圆滑线
        set1.setCubicIntensity(0.2f);
        set1.setDrawCircles(true);
        set1.setLineWidth(2f);//设置线宽
        set1.setCircleSize(5f);//设置小圆大小
        set1.setDrawFilled(true);//设置包括的范围区域内填充颜色
        set1.setCircleColor(Color.rgb(244,117,117));
        set1.setColor(Color.rgb(244,117,117));

        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
        dataSets.add(set1);

        LineData lineData=new LineData(dataSets);
        return lineData;
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture){
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture){
        if (lastPerformedGesture!= ChartTouchListener.ChartGesture.SINGLE_TAP){
            lineChart.highlightValues(null);
        }
    }

    @Override
    public void onChartLongPressed(MotionEvent me){}

    @Override
    public void onChartDoubleTapped(MotionEvent me){}

    @Override
    public void onChartSingleTapped(MotionEvent me){}

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY){}

    @Override
    public void onChartScale(MotionEvent me, float x, float y){}

    @Override
    public void onChartTranslate(MotionEvent me, float x, float y){}

    @Override
    public void onValueSelected(Entry e, Highlight h){}

    @Override
    public void onNothingSelected(){}


    @Override
    protected void onStop(){
        Log.e(TAG, "onStop: ");
        super.onStop();
        finish();
    }



    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        UserAgent.OnPause(getApplicationContext(), Utils.P_SCALE_CAT);
        super.onPause();
    }


    @Override
    protected void onDestroy(){
        Log.e(TAG, "onDestroy:");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        UserAgent.OnResume(Utils.P_SCALE_CAT);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SixMWDActivity2.this, SixMinuteWalkTestActivity.class);
        startActivity(intent);
    }

    private void submitDistance() {
    //    btnSubmit.setOnClickListener((View v) -> {
      //      distance=0;
        //    commitDataToCloud();});
    }

    private void initData() {
        submitTime = TimeManager.getStrDateTime();
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        sixMWD = realm.where(SixMWDTask.class).findFirst();
        realm.executeTransaction((Realm realm) -> {
            sixMWD.setDistance(distance);
        });
        }
        //  catScale1 = realm.where(CatScale.class).equalTo("problemId",1).findFirst();//需要定义为全局变量

    private void commitDataToCloud() {
        // 访问网络前先检查网络状态
        if(!GlobalMethod.getNetworkConnection()){
            GlobalMethod.showToast(getApplicationContext(), Utils.NETWORK_DISABLED, Toast.LENGTH_LONG);
            return;
        }


        SixMWDTask sixMWD= new SixMWDTask();
        sixMWD.setDistance(distance);
        sixMWD.setId(0);
        sixMWD.setMeasureTime(submitTime);

        JsonObject json = new JsonObject();
        String data = JsonUtil.toJson(sixMWD);

        Log.d("lh6MWD", "the data is: " + data);
        json.addProperty("data", data);
        json.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
        json.addProperty("recordType", Utils.SixMWD);
        String url = Utils.BASE_URL + Utils.COMMIT_GENERIC_RECORD;   // "http://120.27.141.50:18909/COPDService.svc//CommitGenericRecord"

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_UPLOADING)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();

        Ion.with(SixMWDActivity2.this).load(url).setJsonObjectBody(json).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                dialog.dismiss();

                Log.d("lh6MWD", "the error is: " + e);
                Log.d("lh6MWD", "the result is: " + result);

                if (e != null) {
                    GlobalMethod.showToast(getApplicationContext(), Utils.MESSAGE_UPLOAD_FAIL, Toast.LENGTH_SHORT);
                }else{
                    int flag = result.get("flag").getAsInt();
                    if (flag != Utils.SERVICE_RETURN_SUCCEED) {
                        GlobalMethod.showToast(getApplicationContext(), Utils.MESSAGE_UPLOAD_FAIL, Toast.LENGTH_SHORT);
                    }else{
                        GlobalMethod.showToast(getApplicationContext(), Utils.MESSAGE_UPLOAD_SUCCESS, Toast.LENGTH_SHORT);
                        GlobalMethod.updateLatest6MWDRecord(sixMWD);
                        Intent intent = new Intent(SixMWDActivity2.this,MainActivity.class);
                        intent.putExtra("FLAG",1);
                        startActivity(intent);
                        /**
                        int point = GlobalMethod.calculateEvaluationValue();
                        if(point == 0){
                            Intent intent = new Intent(SixMWDActivity2.this,MainActivity.class);
                            intent.putExtra("FLAG",1);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(SixMWDActivity2.this,EvaluationActivity.class);
                            startActivity(intent);
                        }
                         */

                    }
                }
            }
        });
    }

    private void submit() {
        btnSubmit.setOnClickListener((View v) -> {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                    .title(R.string.tv_notice)
                    .content(R.string.tv_notice_text)
                    .positiveText(R.string.tv_sure)
                    .negativeText(R.string.tv_cancel);
            MaterialDialog dialog = builder.build();
            dialog.show();
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    // TODO
                    dialog.dismiss();
                    commitDataToCloud();

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
}
