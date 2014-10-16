package com.leigo.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * 头像图片选择框的浮层
 *
 * @author Administrator
 */
public class FloatDrawable extends Drawable {

    private Paint mLinePaint = new Paint();
    private Rect mRect;

    public FloatDrawable(Context context) {
        mLinePaint.setStrokeWidth(2.0F);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.parseColor("#a0a0a0"));
        mRect = new Rect();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mRect.isEmpty()) {
            int left = getBounds().left;
            int top = getBounds().top;
            int right = getBounds().right;
            int bottom = getBounds().bottom;
            mRect.set(left, top, right, bottom);
        }
        //方框
        canvas.drawRect(mRect, mLinePaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

}
