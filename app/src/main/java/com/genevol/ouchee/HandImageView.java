package com.genevol.ouchee;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by RussellM on 2/06/2016.
 */
public class HandImageView extends ImageView  implements
         GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener{

    Point latestPoint;
    int latestColor;

    boolean readyToStart;

    public GestureDetectorCompat mDetector;

    public HandImageView( Context context) {
        super(context);
        readyToStart = true;

    }
    /**
     * @param context
     * @param attrs
     */
    public HandImageView(Context context, AttributeSet attrs)



    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        readyToStart = true;
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public HandImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub

        readyToStart = true;
    }

    public void setLatestPoint(Point p,int c) {
        latestPoint = p;
        latestColor = c;

    }



    @Override
    protected void onDraw( Canvas canvas)
    {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        setBackgroundResource(R.drawable.choppingboard4);
        System.out.println("Painting content");
        Paint paint  = new Paint(Paint.LINEAR_TEXT_FLAG);
        paint.setColor(Color.RED);
        paint.setTextSize(30.0F);
        System.out.println("Drawing text");
       // canvas.drawText("Hello World in custom view", 200, 200, paint);
        //canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);
        if (latestPoint != null) {
            paint.setColor(latestColor);
            canvas.drawCircle(latestPoint.x,latestPoint.y, 6, paint);
        }

        if (readyToStart) {
            paint.setColor(Color.BLUE);
            canvas.drawText("Tap bottom left to start", 40, 140, paint);
        }
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d("debug", "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d("debug", "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d("debug", "onSingleTapConfirmed: " + event.toString());
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        Log.d("debug", "onScroll: " + e1.toString()+e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d("debug", "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d("debug", "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d("debug","onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d("debug", "onFling: " + event1.toString()+event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d("debug", "onLongPress: " + event.toString());
    }




}
