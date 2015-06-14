package com.dugan.tileshade;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by Todd on 5/31/2015.
 */
public class MyDrawable extends ShapeDrawable {

    private Paint mFillPaint;
    private Paint mStrokePaint;
    private int mColor;

    public MyDrawable() {
        super();
        //empty constructor
    }

    public MyDrawable(Shape s, int strokeWidth) {
        super(s);
        mFillPaint = this.getPaint();
        mStrokePaint = new Paint(mFillPaint);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(strokeWidth);
        mColor = Color.DKGRAY;
    }

    public void setColors(Paint.Style style, int color){
        mColor = color;
        if(style.equals(Paint.Style.FILL)){
            mFillPaint.setColor(mColor);
        }else if(style.equals(Paint.Style.STROKE)){
            mStrokePaint.setColor(mColor);
        }else{
            mFillPaint.setColor(mColor);
            mStrokePaint.setColor(mColor);
        }
        super.invalidateSelf();
    }

    @Override
    protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
        //shape.drawPaint(mFillPaint, canvas);
        //shape.drawPaint(mStrokePaint, canvas);
        super.onDraw(shape, canvas, paint);
    }
}