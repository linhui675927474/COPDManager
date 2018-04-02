package com.zju.biomedit.copdmanager.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleTimeProgressView extends View {
    private static final String TAG= "CircleProgressView";

    private int maxProgress =100;
    private int progress=0;

    private final int circleLineStrokeWidthBottom =10;
    private final int circleLineStrokeWidthAbove =20;

    //画圆所在矩形区域
    private final RectF rectFBottom;
    private final RectF rectFAbove;

    private final Paint paint;
    private final Context context;

    private int width;
    private int height;


    public CircleTimeProgressView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context=context;
        rectFAbove=new RectF();
        rectFBottom=new RectF();
        paint=new Paint();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        width=this.getWidth();
        height=this.getHeight();

        if (width!=height){
            int min=Math.min(width,height);
            width=min;
            height=min;
        }

        //设置背景画笔相关属性
        paint.setAntiAlias(true);//抗锯齿
        paint.setColor(Color.rgb(0x3a,0x58,0x5f));
        canvas.drawColor(Color.TRANSPARENT);//设置背景透明
        paint.setStrokeWidth(circleLineStrokeWidthBottom);
        paint.setStyle(Paint.Style.STROKE);
        //位置
        setRectFPosition(rectFBottom, circleLineStrokeWidthBottom);
        setRectFPosition(rectFAbove, circleLineStrokeWidthAbove);
        //
        //绘制圆圈、进度条背景
        canvas.drawArc(rectFBottom, -90, 360, false, paint);//顺时针为正方向，起点为-90，视图上最高点
        //绘制上面可变的进度条
        paint.setColor(Color.rgb(0x2f, 0xc2, 0xe6));
        paint.setStrokeWidth(circleLineStrokeWidthAbove);
        canvas.drawArc(rectFAbove, -90, (float) progress / maxProgress * 360, false, paint);

    }

    private void setRectFPosition(RectF rectF, int circleLineStrokeWidth){
        rectF.left=circleLineStrokeWidth/2;//左上角x
        rectF.top=circleLineStrokeWidth/2;//右上角y
        rectF.right=width-circleLineStrokeWidth/2;//左下角x
        rectF.bottom=height-circleLineStrokeWidth/2;//右下角y
    }

    public int getMaxProgress(){
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress){
        this.maxProgress=maxProgress;
    }

    public void setProgress(int progress){
        this.progress=progress;
        this.invalidate();
    }

    public void setProgressNotInUiThread(int progress){
        this.progress=progress;
        this.postInvalidate();
    }



}