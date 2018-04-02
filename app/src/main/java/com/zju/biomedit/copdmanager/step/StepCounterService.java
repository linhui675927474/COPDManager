package com.zju.biomedit.copdmanager.step;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

public class StepCounterService extends Service {

    public static boolean FLAG = false;// 服务运行标志

    private SensorManager mSensorManager;// 传感器服务
    private StepDetector detector;// 传感器监听对象(加速度）
    private NewStepDetector newDetector;// 传感器监听对象（计步）

    private PowerManager mPowerManager;// 电源管理服务
    private WakeLock mWakeLock;// 屏幕灯（耗电？）

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        FLAG = true;// 标记为服务正在运行

        // 获取传感器的服务，初始化传感器
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        if(hasStepSensor(this)){
            Log.i("step","stepcount");
            newDetector = new NewStepDetector(this);
            mSensorManager.registerListener(newDetector,mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),SensorManager.SENSOR_DELAY_FASTEST);
        }else{
            // 创建监听器类，实例化监听对象
            Log.i("step","acc");
            detector = new StepDetector(this);
            // 注册传感器，注册监听器
            mSensorManager.registerListener(detector,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_FASTEST);
            // 电源管理服务
            mPowerManager = (PowerManager) this
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "S");
            //| PowerManager.ACQUIRE_CAUSES_WAKEUP
            mWakeLock.acquire();
        }

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        FLAG = false;// 服务停止
        if (detector != null) {
            mSensorManager.unregisterListener(detector);
        }

        if (newDetector != null) {
            mSensorManager.unregisterListener(newDetector);
        }

        if (mWakeLock != null) {
            mWakeLock.release();
        }
    }

    public class StepDetector implements SensorEventListener {

        public float sensitivity = 10;//SENSITIVITY灵敏度
        private float mLastValues[] = new float[3 * 2];
        private float mScale[] = new float[2];
        private float mYOffset;
        private long end = 0;
        private long start = 0;

        /**
         * 最后加速度方向
         */
        private float mLastDirections[] = new float[3 * 2];
        private float mLastExtremes[][] = {new float[3 * 2], new float[3 * 2]};
        private float mLastDiff[] = new float[3 * 2];
        private int mLastMatch = -1;

        // 传入的Context
        private Context context;

        /**
         * 传入上下文的构造函数
         *
         * @param context
         */
        public StepDetector(Context context) {
            // TODO Auto-generated constructor stub
            super();

            this.context = context;

            // 读取本地保存的计步信息
            String savedDate = LocalStepHelper.getLocalStepDate(context);
            String nowDate = TimeManager.getStrDate();
            if(savedDate.equals("") || !savedDate.equals(nowDate)){
                //更新为当日的信息
                LocalStepHelper.updateLocalStepCount(context, 0);
                LocalStepHelper.updateLocalStepDate(context, nowDate);
            }

            int h = 480;
            mYOffset = h * 0.5f;
            mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
            mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            Sensor sensor = event.sensor;
            synchronized (this) {
                if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
                } else {
                    int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
                    if (j == 1) {
                        float vSum = 0;
                        for (int i = 0; i < 3; i++) {
                            final float v = mYOffset + event.values[i] * mScale[j];
                            vSum += v;
                        }
                        int k = 0;
                        float v = vSum / 3;

                        float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
                        if (direction == -mLastDirections[k]) {
                            // Direction changed
                            int extType = (direction > 0 ? 0 : 1); // minumum or
                            // maximum?
                            mLastExtremes[extType][k] = mLastValues[k];
                            float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

                            if (diff > sensitivity) {
                                boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
                                boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
                                boolean isNotContra = (mLastMatch != 1 - extType);

                                if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                    end = System.currentTimeMillis();
                                    if (end - start > 500) {// 此时判断为走了一步
                                        int localStep = LocalStepHelper.getLocalStepCount(context);
                                        localStep++;
                                        Log.i("localStep",localStep+"");
                                        LocalStepHelper.updateLocalStepCount(context, localStep);

                                        Intent intent = new Intent(Utils.LOCAL_STEP_CHANGED_ACTION);
                                        intent.putExtra(Utils.INTENT_EXTRA_KEY_LOCAL_STEP, localStep);
                                        sendBroadcast(intent);

                                        mLastMatch = extType;
                                        start = end;

                                        // 更新通知栏
                                        Notification notification = new Notification.Builder(context).
                                                setTicker("COPD管家").
                                                setSmallIcon(R.mipmap.ic_launcher_new).
                                                setContentTitle("COPD管家 - 计步器").
                                                setContentText("今日步数：" + localStep + "步").build();
                                        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

                                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        notificationManager.notify(Utils.NOTIFICATION_ID_STEP_CHANGED, notification);
                                    }
                                } else {
                                    mLastMatch = -1;
                                }
                            }
                            mLastDiff[k] = diff;
                        }
                        mLastDirections[k] = direction;
                        mLastValues[k] = v;
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
    }

    public class NewStepDetector implements SensorEventListener {

        // 传入的Context
        private Context context;

        /**
         * 传入上下文的构造函数
         *
         * @param context
         */
        public NewStepDetector(Context context) {
            // TODO Auto-generated constructor stub
            super();

            this.context = context;

            // 读取本地保存的计步信息
            String savedDate = LocalStepHelper.getLocalStepDate(context);
            String nowDate = TimeManager.getStrDate();
            if(savedDate.equals("") || !savedDate.equals(nowDate)){
                //更新为当日的信息
                LocalStepHelper.updateLocalStepCount(context, 0);
                LocalStepHelper.updateLocalStepDate(context, nowDate);
            }
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            int localStep = LocalStepHelper.getLocalStepCount(context);
            localStep++;
            Log.i("localStep",localStep+"");
            LocalStepHelper.updateLocalStepCount(context, localStep);

            Intent intent = new Intent(Utils.LOCAL_STEP_CHANGED_ACTION);
            intent.putExtra(Utils.INTENT_EXTRA_KEY_LOCAL_STEP, localStep);
            sendBroadcast(intent);

            // 更新通知栏
            Notification notification = new Notification.Builder(context).
                    setTicker("COPD管家").
                    setSmallIcon(R.mipmap.ic_launcher_new).
                    setContentTitle("COPD管家 - 计步器").
                    setContentText("今日步数：" + localStep + "步").build();
            notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(Utils.NOTIFICATION_ID_STEP_CHANGED, notification);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
    }

    /**
     * 检测计步传感器是否可以使用
     *
     * @param context 上下文
     * @return 是否可用计步传感器
     */
    public static boolean hasStepSensor(Context context) {
        if (context == null) {
            return false;
        }

        Context appContext = context.getApplicationContext();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false;
        } else {
            boolean hasSensor = false;
            Sensor sensor = null;
            try {
                hasSensor = appContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR);
                SensorManager sm = (SensorManager) appContext.getSystemService(Context.SENSOR_SERVICE);
                sensor = sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return hasSensor && sensor != null;
        }
    }
}
