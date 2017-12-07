package com.woch.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by wangdake on 2017/12/1.
 */

public class SuperVideoView extends ViewGroup{

    private List<String> ImageLs;

    private boolean isInit = true;

    public SuperVideoView(Context context) {
        this(context, null);
    }

    public SuperVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //this.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
        initUi();
    }

    public SuperVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private String videoPath;

    public void setVideoPath(String videoPath){
        this.videoPath = videoPath;
    }

    public void setImageLs(List ImageLs){
        this.ImageLs = ImageLs;
        initUi();
    }

    private int minValue, maxValue;

    public void setRangeValues(int minValue, int maxValue){
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SuperVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs);
    }

    private View touchView;

    private void initUi() {

        removeAllViews();
        if (ImageLs == null)
            return;
        if (mWidth == 0){
            addView(new View(getContext()));
            return;
        }
        LayoutParams layoutParams = initParams();

        initImage(layoutParams);
        touchView = new View(getContext());
        touchView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.roudrect));
        addView(touchView);
        //getchild

    }

    private void initImage(LayoutParams layoutParams){

        for (int i=0;i<ImageLs.size();i++){
            ImageView imageView = new ImageView(getContext());
            try {
                Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(ImageLs.get(i)), imageWidth, height, true);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getContext().getResources(), bitmap);
                imageView.setImageDrawable(bitmapDrawable);
            }catch (Exception e){
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), Integer.parseInt(ImageLs.get(i))));
            }
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setLayoutParams(layoutParams);
            if (layoutParams != null);{
                Log.e("LayoutParams","=====add============");
                this.addView(imageView,layoutParams);
            }
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private LayoutParams layoutParams;
    private int height;

    private int imageWidth;

    private LayoutParams initParams() {
        if (layoutParams == null){
            height = Math.min((mWidth-40)/ImageLs.size(), mHeight);
            imageWidth = (mWidth-40)/ImageLs.size();
            layoutParams = new LayoutParams(imageWidth, height);
        }

        return layoutParams;

    }

    private int MAX_WIDTH;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureHelp(widthMeasureSpec, true);
        measureHelp(heightMeasureSpec, false);
//        Log.e("LayoutParams", mHeight+"===========onMeasure============="+mWidth/ImageLs.size());
        if (MAX_WIDTH == 0){
            MAX_WIDTH = mWidth;
            rangeRX = mWidth;
            initUi();
        }
        setMeasuredDimension(mWidth, mHeight);

        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private int mWidth, mHeight;
    private int mostWidth, mostHeight;

    private void measureHelp(int mMeasureSpec, boolean isW) {

        int measureMode = MeasureSpec.getMode(mMeasureSpec);
        int measureSize = MeasureSpec.getSize(mMeasureSpec);



        if (isW){

            measureView(measureMode, measureSize, true);

        }else {

            measureView(measureMode, measureSize, false);

        }


    }

    private void measureView(int measureMode, int measureSize, boolean isW){

        if (measureMode == MeasureSpec.EXACTLY){

            if (isW){
                mWidth = measureSize;
            }else {
                mHeight = measureSize;
            }

        }else if (measureMode == MeasureSpec.AT_MOST){


            if (isW){

                if (mostWidth == 0)
                    return;
                mWidth = Math.min(mostWidth, measureSize);

            }else {

                if (mWidth == 0){
                    mHeight = measureSize;
                    return;
                }
                mHeight = Math.min((mWidth-40)/ImageLs.size()+20, measureSize);

            }

        }

    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {



        int childCount = getChildCount();
        int l = 20;
        int t = 10;
        int r = 0;
        int b1 = 0;
        for (int j=0;j<childCount-1;j++){

            View viewI = getChildAt(j);
            l = 20+(mWidth-40)/ImageLs.size()*j;
            r = 20+(mWidth-40)/ImageLs.size()*(j+1);
            b1 = height+10;
            viewI.layout(l, t, r, b1);

        }
        View view = getChildAt(childCount-1);
        view.layout(280, 2, 300, mHeight-2);

    }

    private Paint paint,paint2;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (paint == null) {
            paint = new Paint();
            paint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            paint.setStrokeWidth(20);
            paint.setStyle(Paint.Style.STROKE);
        }
        if (paint2 == null){
            paint2 = new Paint();
            paint2.setColor(ContextCompat.getColor(getContext(), R.color.colorw));
        }
        if (height!=0 && rangeViewRight !=0){
            Rect rect2 = new Rect(0, 10, mWidth, height+10);
            canvas.drawRect(rect2, paint2);
        }
        //Log.e("onTouchEvent","---------dispatchDraw--------"+rangeViewRight);
        Rect rect = new Rect(rangeViewX, 0, rangeViewRight, mHeight);
        canvas.drawRect(rect, paint);

    }
    private float touchWidth = 20;
    private boolean isClickTouchView = false;
    private boolean isClickRangeLeft = false;
    private boolean isClickRangeRight = false;
    private int rangeViewX = 0;
    private int rangeViewRight;
    private int rangeLX = 0;
    private int temp;
    private int rangeRX;
    private float tempY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float downX = 0;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            downX = event.getX();
            tempY = event.getX();
            if ((touchView.getX()-50f)<downX && downX<(touchWidth+50f+touchView.getX()) && !isClickRangeLeft && !isClickRangeRight){
                isClickTouchView = true;
                //Log.e("onTouchEvent","-------ACTION_DOWN---isClickTouchView---true----");
            }
            //Log.e("onTouchEvent",(touchView.getX()-50f)+"-------ACTION_DOWN-------"+(touchWidth+50f+touchView.getX()));
            //Log.e("onTouchEvent","-------ACTION_DOWN-------"+downX);
            //Log.e("onTouchEvent","-------ACTION_DOWN---------"+(mWidth-60f-rangeRX));
            if (downX<60f+rangeLX && !isClickTouchView){
                isClickRangeLeft = true;
                Log.e("onTouchEvent","-------ACTION_DOWN---isClickRange---true----");
            }
            if (downX >rangeRX-60f && !isClickTouchView){
                isClickRangeRight = true;
            }
            //Log.e("onTouchEvent",isClickRangeLeft+"-------ACTION_DOWN----------"+isClickRangeRight);


        }else if (event.getAction() == MotionEvent.ACTION_MOVE){

            //Log.e("onTouchEvent",tempY+"-------ACTION_MOVE--ACTION_MOVE-----"  +(int)event.getX());
            if (Math.abs(event.getX() - tempY) >2){



            if (isClickTouchView && event.getX()>= 20 && event.getX()<=mWidth-20){

                touchView.layout((int)event.getX(), 2, (int)(event.getX()+touchWidth), mHeight-2);
                onMSeekListener.getSeek((int) ((maxValue-minValue)*event.getX()/(mWidth-40)));
                //Log.e("onTouchEvent","-------ACTION_MOVE-------"  +(int)event.getX());

            }
            //Log.e("onTouchEvent",isClickRangeLeft+"-------ACTION_MOVE----------"+isClickRangeRight);
            if (isClickRangeLeft){
                touchView.setVisibility(GONE);
                if (rangeViewRight == 0)
                    rangeViewRight = mWidth;
                temp = rangeViewX;
                rangeViewX = (int) event.getX();

                Log.e("onTouchEvent","-------ACTION_MOVE--isClickRange-----"  +(int)event.getX());
                if (rangeViewRight - rangeViewX > 100){
                    invalidate();
                    rangeLX = rangeViewX;
                }else {
                    rangeViewX = temp;
                }
                onMRangeListener.getRange(rangeViewX*(maxValue-minValue)/(mWidth-40), rangeViewRight*(maxValue-minValue)/(mWidth-40));

            }
            if (isClickRangeRight){
                touchView.setVisibility(GONE);
                temp = rangeViewRight;
                rangeViewRight = (int) event.getX();

                if (rangeViewRight - rangeViewX > 100){
                    invalidate();
                    rangeRX = rangeViewRight;
                }else {
                    rangeViewRight = temp;
                }
                onMRangeListener.getRange(rangeViewX*(maxValue-minValue)/(mWidth-40), rangeViewRight*(maxValue-minValue)/(mWidth-40));
                Log.e("onTouchEvent",rangeRX+"-------ACTION_MOVE--isClickRangeRight-----" + rangeViewRight);

            }


            }

        }else if (event.getAction() == MotionEvent.ACTION_UP){
            isClickTouchView = false;
            isClickRangeLeft = false;
            isClickRangeRight = false;
            tempY = event.getX();
            if(rangeViewRight - rangeViewX > mWidth-10){
                touchView.setVisibility(VISIBLE);
                rangeViewX = 0;
                rangeViewRight = 0;
            }
            Log.e("onTouchEvent",(rangeViewRight - rangeViewX)+"---------ACTION_UP-------"+mWidth);
        }

        return true;


    }

    private OnMSeekListener onMSeekListener;
    private OnMRangeListener onMRangeListener;

    public void setOnMSeekListener(OnMSeekListener onMSeekListener){
        this.onMSeekListener = onMSeekListener;
    }

    public interface OnMSeekListener{
        public void getSeek(int v);
    }

    public interface OnMRangeListener{
        public void getRange(int rangeViewX, int rangeViewRight);
    }

    public void setOnMRangeListener(OnMRangeListener onMRangeListener){
        this.onMRangeListener = onMRangeListener;
    }

}
