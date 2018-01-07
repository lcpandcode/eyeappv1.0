package com.bysj.eyeapp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.bysj.eyeapp.view.R;

/**
 * Created by lcplcp on 2018/1/6.
 */

/**
 * 圆弧类：该类用来画圆弧，用在用眼数据中界面圆弧的描绘
 */
public class ArcView extends View {

    //屏幕相关信息（主要是分辨率）
    private int width;
    private int height;
    private ArcViewParams params;

    private Paint mPaint;
    Context context;
    public ArcView(Context context,ArcViewParams params) {
        super(context);
        this.context = context;
        this.params = params;
        init();
    }

    /**
     * 初始化方法
     */
    private void init(){
        //初始化屏幕尺寸相关数据
        DisplayMetrics dm = new DisplayMetrics();
        if(!(context instanceof Activity)){
            throw new RuntimeException("context 上下文必须是Activity!");
        }
        Activity activity = (Activity) context;
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        //初始化默认的圆弧矩形区域大小
        params.setLeft(width * (1-params.getDeaultWeightRate())/2 - dpToPx(params.getArcSize()));
        params.setRight(width - width * (1-params.getDeaultWeightRate())/2 + dpToPx(params.getArcSize()));
        params.setTop(dpToPx(params.getArcSize()));
        params.setBottom(params.getTop() + (params.getRight() - params.getLeft()));

    }

    @Override
    public void onDraw(Canvas canvas){
        //采用默认设置创建一个画笔
        Paint paint=new Paint();
        //使用抗锯齿功能
        paint.setAntiAlias(true);
        //设置画笔的颜色为绿色
        paint.setColor(params.getColor()==-1?context.getResources().getColor(R.color.eyedata_bar_arc):params.getColor());
        //设置画笔类型为STROKE类型，即描边而不是图形
        if(!params.isRound()){
            paint.setStyle(Paint.Style.STROKE);//如果描绘的是弧线，设置
        }
        paint.setStrokeWidth(dpToPx(params.getArcSize()));
        RectF rectf=new RectF(params.getLeft(), params.getTop(), params.getRight(), params.getBottom());//确定外切矩形范围
        canvas.drawArc(rectf, params.getStartAngle(), params.getArcAngle(), params.isUseCenter(), paint);//绘制圆弧，不含圆心
    }

    public ArcViewParams getParams() {
        return params;
    }

    /**
     * dp 和 px之间的转换
     */
    private float dpToPx(float dpSize){
        DisplayMetrics dm = getResources().getDisplayMetrics() ;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
    }


    /**
     * 由于自定义View需要自己处理wrap_content情形，所以重写该方法
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //统一尺寸
        float weight = params.getRight() - params.getLeft();
        setMeasuredDimension(width, (int)weight/2 + 1);
    }

    /**
     * 圆弧参数封装类，该类用于设置圆弧相关的参数
     */
    public static class ArcViewParams{
        //描绘圆弧的矩形范围（这些默认值和屏幕尺寸有关，默认值在构造函数中设置）
        private float left ;
        private float right ;
        private float  top ;
        private float bottom ;
        //圆弧参数相关信息
        private float arcSize = 10;//圆弧线条大小
        private int startAngle = 180;//圆弧起始角度
        private int arcAngle = 180;//圆弧总角度
        //圆弧大小占总屏幕宽度大小的比率：默认值0.5
        private  float deaultWeightRate = 0.5F;//宽度默认为屏幕一半
        //圆弧线条的颜色
        private int color = -1;//为-1默认时，自动寻找配置文件color的设定值
        private boolean useCenter = false;//是否设置圆心，默认为否
        private boolean isRound = false;//是否描绘圆型，如果是true，描绘圆型

        public float getLeft() {
            return left;
        }

        public void setLeft(float left) {
            this.left = left;
        }

        public float getRight() {
            return right;
        }

        public void setRight(float right) {
            this.right = right;
        }

        public float getTop() {
            return top;
        }

        public float getDeaultWeightRate() {
            return deaultWeightRate;
        }

        public void setDeaultWeightRate(float deaultWeightRate) {
            this.deaultWeightRate = deaultWeightRate;
        }

        public boolean isUseCenter() {
            return useCenter;
        }

        public void setUseCenter(boolean useCenter) {
            this.useCenter = useCenter;
        }

        public void setTop(float top) {

            this.top = top;
        }

        public float getBottom() {
            return bottom;
        }

        public void setBottom(float bottom) {
            this.bottom = bottom;
        }

        public float getArcSize() {
            return arcSize;
        }

        public void setArcSize(float arcSize) {
            this.arcSize = arcSize;
        }

        public int getStartAngle() {
            return startAngle;
        }

        public void setStartAngle(int startAngle) {
            this.startAngle = startAngle;
        }

        public int getArcAngle() {
            return arcAngle;
        }

        public void setArcAngle(int arcAngle) {
            this.arcAngle = arcAngle;
        }

        public  float getDefaultWeightRate() {
            return deaultWeightRate;
        }

        public int getColor() {
            return color;
        }

        public boolean isRound() {
            return isRound;
        }

        public void setRound(boolean round) {
            isRound = round;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
