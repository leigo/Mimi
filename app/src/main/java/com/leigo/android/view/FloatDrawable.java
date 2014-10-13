package com.leigo.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2014/10/13.
 */
public class FloatDrawable extends Drawable {

    private Paint mLinePaint = new Paint();
    private Rect mRect;

    public FloatDrawable(Context paramContext) {
        mLinePaint.setStrokeWidth(2.0f);
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
