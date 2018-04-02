package com.zju.biomedit.copdmanager.activity.sixMWD;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.AudioTask;
import com.zju.biomedit.copdmanager.step.LocalStepHelper;
import com.zju.biomedit.copdmanager.support.LocationService;
import com.zju.biomedit.copdmanager.view.CircleTimeProgressView;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;

//测试过程
public class SixMWDActivity1 extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    //定位相关
    private double lat1=0.0;
    private double lon1=0.0;
    private double lat2=0.0;
    private double lon2=0.0;
    private static double distance=0;
    public LocationService locationService;
    private int scanSpan=10000;

    //地图相关
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;  //定位信息（地图用）
    private SensorManager sensorManager;  //方向传感器
    private double lastX = 0.0;
    private int direction = 0;
    private LocationMode mode;
    BitmapDescriptor marker;
    private float accuracy=0;
    private MapView mapView=null;
    BaiduMap mBaiduMap;

    //计步相关
    private int stepCount=0;
    private static double distance_step=0;
    private double stepLength=0.6;//单位是米

    //常量
    private static final String TAG="SixMWDActivity";
    private static final double EARTH_RADIUS=6371.0;  //地球半径
    private static final int TIME_INTERVAL=1000;      //进度条转动间隔(ms)

    //语音相关
    private AudioTask audioActivity=new AudioTask();

    //计时进度、播报进度
    private int count=0;  //每分钟加1
    private int time_5M;//将6分钟分成times段
    private int time_1M;
    private int time_3M;
    private int currentProgress=0;
    private CircleTimeProgressView circleProgress;

    //UI
    private TextView currentState;
    private Button end_button;
    private Button submit_button;

    //设置计时进度
    private Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
                currentProgress += 1;
                circleProgress.setProgress(currentProgress);
                handler.postDelayed(runnable, TIME_INTERVAL);
        }
    };

    //打印定位信息
    public void logMsg(String str) {
        final String s = str;
        try {
            if (currentState != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        currentState.post(new Runnable() {
                            @Override
                            public void run() {
                                currentState.setText(s);
                            }
                        });
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: 初始化View和AudioTask" );

        SDKInitializer.initialize(getApplicationContext());//百度SDK初始化，在setContentView之前
        setContentView(R.layout.activity_6mwd2);
        LocalStepHelper.clearOutdatedStepSharedPreferences(getApplicationContext());//过时计步数据清空？
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务

        circleProgress=findViewById(R.id.six_mwd_circle_progress);
        currentState=findViewById(R.id.current_state);
        end_button=findViewById(R.id.end_test);
        submit_button=findViewById(R.id.submit_display_6mwd_data);
        mapView=findViewById(R.id.map_view);

		end_button.setOnClickListener(this);
        submit_button.setOnClickListener(this);

        //获取定时间隔数据
        Intent intent=getIntent();
        int interval=intent.getIntExtra("interval", 10);//单位为s
        scanSpan=interval*1000;//单位为ms
        //计时、播报进度设置
        circleProgress.setMaxProgress(60*6);
        time_5M=(int) (60*5 /interval);//6分钟=6*60*1000ms
        time_1M=(int) (60 /interval);//6分钟=6*60*1000ms
        time_3M=(int) (60*3 /interval);//6分钟=6*60*1000ms
        //语音、定位初始化
        audioActivity.initAudio(this);
        requestLocation();
    }


    //终止按钮动作
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.end_test:
                audioActivity.AudioDestroy();
                locationService.stop();
                handler.removeCallbacks(runnable);
                break;
            case R.id.submit_display_6mwd_data:
                Intent intent=new Intent(SixMWDActivity1.this, SixMWDActivity2.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onStart(){
        super.onStart();
        Log.e(TAG, "onStart:");
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        super.onStop();

        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
        //为系统的方向传感器注册监听器
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
	@Override
	protected void onDestroy(){
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        distance_step=0;
        distance=0;
        mBaiduMap.setMyLocationEnabled(false);         //关闭定位图层
        mapView.onDestroy();                           //停止地图服务
        mapView = null;
        audioActivity.AudioDestroy();                  //停止播报服务
		locationService.unregisterListener(mListener); //注销掉定位监听
		locationService.stop();                        //停止定位服务
        handler.removeCallbacks(runnable);             //取消转圈进度定时器线程
	}

	//定位、地图初始化和启动
    private void requestLocation(){
        Log.e(TAG, "requestLocation: SDKInitial，初始化locationService并start");

        //地图初始化
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);//开启定位图层

        //marker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
        mode = LocationMode.COMPASS;//地图定位模式——普通（NORMAL)，跟随（FOLLOWING），罗盘（COMPASS）
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mode, true, marker));
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.overlook(0);//罗盘模式不需要这句
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));//罗盘模式不需要这句

        //定位初始化
        locationService = new LocationService(getApplicationContext());
        locationService.registerListener(mListener);
        locationService.setScanSpan(scanSpan);
        locationService.setLocationOption(locationService.getOption());
        locationService.start();

        //计时开始
        handler.postDelayed(runnable,TIME_INTERVAL);
	}

        //定位监听
	private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
            StringBuffer currentPosition =new StringBuffer(256);
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                Log.e(TAG, "onReceiveLocation: 现在接收到地址信息");

                //获取定位信息
                lat1 = location.getLatitude();
                lon1 = location.getLongitude();
                LatLng ll = new LatLng(lat1, lon1);
                accuracy = location.getRadius();

                //地图定位
                locData = new MyLocationData.Builder()
                        .accuracy(accuracy)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(direction).latitude(lat1).longitude(lon1).build();
                mBaiduMap.setMyLocationData(locData);
                if (isFirstLoc) {
                    isFirstLoc = false;
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);//地图放大倍数，18
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                    //绘制定位圆和标记
                    OverlayOptions ooCircle = new CircleOptions().fillColor(0x000000FF).center(ll)
                            .stroke(new Stroke(5, 0xAAFF0000)) .radius(500);

                    OverlayOptions ooCircle2 = new CircleOptions().fillColor(0x000000FF).center(ll)
                            .stroke(new Stroke(5, 0xAA00FF00)) .radius(300);

                    TextOptions text1= new TextOptions().position(new LatLng(lat1+0.3/111, lon1)).text("300").fontSize(30);
                    TextOptions text2=new TextOptions().position(new LatLng(lat1+0.5/111, lon1)).text("500").fontSize(30);

                    mBaiduMap.addOverlay(ooCircle);
                    mBaiduMap.addOverlay(ooCircle2);
                    mBaiduMap.addOverlay(text1);
                    mBaiduMap.addOverlay(text2);
                }

                //求距离
                double distance_sec = GetDistance(lon1, lon2, lat1, lat2);
                int stepCount_sec = LocalStepHelper.getLocalStepCount(getApplicationContext());
                int stepCount_last=stepCount_sec-stepCount;
                double distance_step_sec = stepCount_last * stepLength;

                //定位修正——10s
                if (distance_sec < 1000) {
                    if (distance_sec * 1000 > 18) {
                        distance = distance + 0.011;
                    } else {
                        distance = distance + distance_sec;
                    }
                }
                //求距修正
                if (count==1){
                    distance_step=0;
                    distance=0;
                }else {
                    distance_step=distance_step_sec+distance_step;
                }
                //更新定位
                lat2 = lat1;
                lon2 = lon1;
                stepCount=stepCount_sec;

                //检测用户在测试中是否中途停止
                //CheckStop(distance_sec);
                //CheckStop(distance_step_sec);

                //定位信息
                currentPosition.append(location.getTime());
                //currentPosition.append("\nlocType : ");// 定位类型
                //currentPosition.append(location.getLocType());
                currentPosition.append("\nlocType description : ");// *****对应的定位类型说明*****
                currentPosition.append(location.getLocTypeDescription());
                //currentPosition.append("\n latitude : ");// 纬度
                //currentPosition.append(lat1);
                //currentPosition.append("\n longitude : ");// 经度
                //currentPosition.append(lon1);
                currentPosition.append("\n"+scanSpan/1000+"s行走距离为"+distance_sec);
                currentPosition.append("\n总行走距离为(单位米)"+distance*1000);
                currentPosition.append("\n行走步数为"+stepCount);
                currentPosition.append("\n步数*步长为"+distance_step);
                currentPosition.append("\n现在是第"+count+"次播报");
                /**
                 currentPosition.append("\nradius : ");//精度
                 currentPosition.append(location.getRadius());
                 currentPosition.append("\nCountryCode : ");// 国家码
                 currentPosition.append(location.getCountryCode());
                 currentPosition.append("\nCountry : ");// 国家名称
                 currentPosition.append(location.getCountry());
                 currentPosition.append("\ncitycode : ");// 城市编码
                 currentPosition.append(location.getCityCode());
                 currentPosition.append("\ncity : ");// 城市
                 currentPosition.append(location.getCity());
                 currentPosition.append("\nDistrict : ");// 区
                 currentPosition.append(location.getDistrict());
                 currentPosition.append("\nStreet : ");// 街道
                 currentPosition.append(location.getStreet());
                 currentPosition.append("\naddr : ");// 地址信息
                 currentPosition.append(location.getAddrStr());
                currentPosition.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                currentPosition.append(location.getUserIndoorState());
                 currentPosition.append("\nDirection(not all devices have value): ");
                 currentPosition.append(location.getDirection());// 方向
                 currentPosition.append("\nlocationdescribe: ");
                 currentPosition.append(location.getLocationDescribe());// 位置语义化信息
                 currentPosition.append("\nPoi: ");// POI信息
                 if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                 for (int i = 0; i < location.getPoiList().size(); i++) {
                 Poi poi = (Poi) location.getPoiList().get(i);
                 currentPosition.append(poi.getName() + ";");
                 }
                 }
                 */
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    /**
                    GPS定位结果
                    currentPosition.append("\nspeed : ");
                    currentPosition.append(location.getSpeed());// 速度 单位：km/h
                    currentPosition.append("\nsatellite : ");
                    currentPosition.append(location.getSatelliteNumber());// 卫星数目
                    currentPosition.append("\nheight : ");
                    currentPosition.append(location.getAltitude());// 海拔高度 单位：米
                    currentPosition.append("\ngps status : ");
                    currentPosition.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    */
                    currentPosition.append("\ndescribe : ");
                    currentPosition.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    /**
                     网络定位结果
                     运营商信息
                      if (location.hasAltitude()) {// *****如果有海拔高度*****
                          currentPosition.append("\n height : ");
                          currentPosition.append(location.getAltitude());// 单位：米
                      }
                      currentPosition.append("\n operationers : ");// 运营商信息
                      currentPosition.append(location.getOperators());
                    */
                    currentPosition.append("\ndescribe : ");
                    currentPosition.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    currentPosition.append("\ndescribe : ");
                    currentPosition.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    currentPosition.append("\ndescribe : ");
                    currentPosition.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    currentPosition.append("\ndescribe : ");
                    currentPosition.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    currentPosition.append("\ndescribe : ");
                    currentPosition.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
            }

                //每分钟播报语音并更改界面定位信息
                if (count < 100) {
                logMsg(currentPosition.toString());
                    if (count==0){
                            audioActivity.speak("即将开始，", count);//播报
                    } else if (count==(time_5M+1)){
                        audioActivity.speak("已过去5分钟啦，加油加油！",count);
                    } else if (count==(time_1M+1)){
                       audioActivity.speak("已过去1分钟啦，加油！", count);
                    } else if (count==(time_3M+1)){
                        audioActivity.speak("已过去3分钟啦，加油！",count);
                    } else {
                            audioActivity.speak("走呀走呀，", count);
                        }
                } else {
                    audioActivity.AudioDestroy();                  //停止播报
                    locationService.unregisterListener(mListener); //注销掉监听
                    locationService.stop();                        //停止定位服务
                    mapView.onDestroy();                           //停止地图服务
                    handler.removeCallbacks(runnable);             //取消转圈进度定时器线程
                }
            count++;
        }
    };


    /**
     * 求角度的弧度制
     * 根据两点经纬度求距离——Harversine公式
     */
    public static double rad(double degrees)
    {
        return degrees * Math.PI / 180;
    }
    public double GetDistance(double long1, double long2, double lat1, double lat2) {
        Log.e(TAG, "GetDistance: 正在计算距离");
        double a, b, d, sa2, sb2;
        lat1 = rad(lat1);
        lat2 = rad(lat2);
        a = lat1 - lat2;
        b = rad(long1 - long2);

        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2   * EARTH_RADIUS
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
                * Math.cos(lat2) * sb2 * sb2));
        return d;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            direction = (int) x;
            locData = new MyLocationData.Builder().accuracy(accuracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(direction).latitude(lat1).longitude(lon1).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    //检测用户是否中途停止
    public void CheckStop(double distance){
        if (Math.abs(distance*1000 - 0) < 3 ){
           // audioActivity.speak("停止了",count);
           // audioActivity.initAudio(SixMWDActivity1.this, count+99);
        }
    }
}


