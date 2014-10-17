package com.leigo.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.leigo.android.mimi.R;

/**
 * 底图缩放，浮层不变
 *
 * @author yanglonghui
 */
public class CropImageView extends View {

    //状态
    private final int STATUS_TOUCH_SINGLE = 1;//单点
    private final int STATUS_TOUCH_MULTI_START = 2;//多点开始
    private final int STATUS_TOUCH_MULTI_TOUCHING = 3;//多点拖拽中

    private int clipAreaColor;

    private boolean isFirst;

    private Bitmap mBitmap;

    private Rect mDrawableDst = new Rect();
    private Rect mDrawableFloat = new Rect();//浮层选择框，就是头像选择框
    private FloatDrawable mFloatDrawable;//浮层

    private int mFloatRectSize;
    private int mStatus = STATUS_TOUCH_SINGLE;

    private int maskAreaColor;

    private final float maxZoomOut = 5.0f;//最大扩大到多少倍
    private final float minZoomIn = 0.3f;//最小缩小到多少倍

    //单点触摸的时候
    private float oldX = 0.0f;
    private float oldY = 0.0f;

    //多点触摸的时候
    private float oldx_0 = 0.0f;
    private float oldy_0 = 0.0f;

    private float oldx_1 = 0.0f;
    private float oldy_1 = 0.0f;

    //默认的裁剪图片宽度与高度
    private int oriHeight;
    private int oriWidth;

    protected float oriRationWH = 0.0f;//原始宽高比率

    public CropImageView(Context context) {
        super(context);
        init(context);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void checkBounds() {
        int newLeft = mDrawableDst.left;
        int newTop = mDrawableDst.top;
        int floatLeft = mDrawableFloat.left;

        int flag = 0;
        if (newLeft > floatLeft) {
            newLeft = mDrawableFloat.left;
            flag = 1;
        }
        if (mDrawableDst.right < mDrawableFloat.right) {
            newLeft = -(mDrawableDst.width() - mDrawableFloat.right);
            flag = 1;
        }
        if (mDrawableDst.top > mDrawableFloat.top) {
            newTop = mDrawableFloat.top;
            flag = 1;
        }
        if (mDrawableDst.bottom < mDrawableFloat.bottom) {
            newTop = -(mDrawableDst.height() - mDrawableFloat.bottom);
            flag = 1;
        }
        if (flag != 0) {
            mDrawableDst.offsetTo(newLeft, newTop);
            invalidate();
        }
    }

    protected void configureBounds() {
        oriWidth = mBitmap.getWidth();
        oriHeight = mBitmap.getHeight();
        oriRationWH = oriWidth / oriHeight;

        int w = 0;
        int h = 0;
        if (oriRationWH > 1.0f) {  //宽>高
            w = mFloatRectSize;
            h = (int) (w * oriRationWH);
        } else {//宽<高
            h = mFloatRectSize;
            w = (int) (h * oriRationWH);
        }

        int left = (getWidth() - w) / 2;
        int top = (getHeight() - h) / 2;
        int right = left + w;
        int bottom = top + h;

        mDrawableDst.set(left, top, right, bottom);

        int floatLeft = getLeft();
        int floatTop = (getHeight() - mFloatRectSize) / 2;
        int floatRight = floatLeft + mFloatRectSize;
        int floatBottom = floatTop + mFloatRectSize;
        mDrawableFloat.set(floatLeft, floatTop, floatRight, floatBottom);
        mFloatDrawable.setBounds(mDrawableFloat);
    }

    private void init(Context context) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        maskAreaColor = getResources().getColor(R.color.crop_image_mask_area);
        clipAreaColor = getResources().getColor(R.color.crop_image_clip_area);
        mFloatDrawable = new FloatDrawable(context);//头像选择框
    }

    public Bitmap getCroppedImage() {
        float scale = mDrawableDst.width() / oriWidth;
        int left = (int) Math.abs(mDrawableDst.left / scale);
        int top = (int) ((mDrawableFloat.top - mDrawableDst.top) / scale);
        int offset = (int) (mFloatRectSize / scale);
        Rect src = new Rect(left, top, left + offset, top + offset);
        Bitmap bitmap = Bitmap.createBitmap(offset, offset, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Rect dst = new Rect(0, 0, offset, offset);
        canvas.drawBitmap(mBitmap, src, dst, null);
        canvas.drawColor(clipAreaColor);

        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            return; // nothing to draw (empty bounds)
        }

        if (isFirst) {
            configureBounds();
            isFirst = false;
        }

        canvas.drawBitmap(mBitmap, null, mDrawableDst, null);
        canvas.save();
        canvas.clipRect(mDrawableFloat, Region.Op.DIFFERENCE);
        canvas.drawColor(maskAreaColor);
        canvas.restore();
        canvas.save();
        canvas.clipRect(mDrawableFloat, Region.Op.INTERSECT);
        canvas.drawColor(clipAreaColor);
        canvas.restore();
        mFloatDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getPointerCount() > 1) {
            if (mStatus == STATUS_TOUCH_SINGLE) {
                mStatus = STATUS_TOUCH_MULTI_START;

                oldx_0 = event.getX(0);
                oldy_0 = event.getY(0);

                oldx_1 = event.getX(1);
                oldy_1 = event.getY(1);
            } else if (mStatus == STATUS_TOUCH_MULTI_START) {
                mStatus = STATUS_TOUCH_MULTI_TOUCHING;
            }
        } else {
            if (mStatus == STATUS_TOUCH_MULTI_START || mStatus == STATUS_TOUCH_MULTI_TOUCHING) {
                oldx_0 = 0;
                oldy_0 = 0;

                oldx_1 = 0;
                oldy_1 = 0;

                oldX = event.getX();
                oldY = event.getY();
            }

            mStatus = STATUS_TOUCH_SINGLE;
        }

//Log.v("count currentTouch"+currentTouch, "-------");

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//Log.v("count ACTION_DOWN", "-------");
                oldX = event.getX();
                oldY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
//Log.v("count ACTION_UP", "-------");
                checkBounds();
                break;

            case MotionEvent.ACTION_POINTER_1_DOWN:
//Log.v("count ACTION_POINTER_1_DOWN", "-------");
                break;

            case MotionEvent.ACTION_POINTER_UP:
//Log.v("count ACTION_POINTER_UP", "-------");
                break;

            case MotionEvent.ACTION_MOVE:
//Log.v("count ACTION_MOVE", "-------");
                if (mStatus == STATUS_TOUCH_MULTI_TOUCHING) {
                    float newx_0 = event.getX(0);
                    float newy_0 = event.getY(0);

                    float newx_1 = event.getX(1);
                    float newy_1 = event.getY(1);

                    float oldWidth = Math.abs(oldx_1 - oldx_0);
                    float oldHeight = Math.abs(oldy_1 - oldy_0);

                    float newWidth = Math.abs(newx_1 - newx_0);
                    float newHeight = Math.abs(newy_1 - newy_0);

                    boolean isDependHeight = Math.abs(newHeight - oldHeight) > Math.abs(newWidth - oldWidth);

                    float ration = isDependHeight ? ((float) newHeight / (float) oldHeight) : ((float) newWidth / (float) oldWidth);
                    int centerX = mDrawableDst.centerX();
                    int centerY = mDrawableDst.centerY();
                    int _newWidth = (int) (mDrawableDst.width() * ration);
                    int _newHeight = (int) ((float) _newWidth / oriRationWH);

                    float tmpZoomRation = (float) _newWidth / (float) oriWidth;
                    if (tmpZoomRation >= maxZoomOut) {
                        _newWidth = (int) (maxZoomOut * oriWidth);
                        _newHeight = (int) ((float) _newWidth / oriRationWH);
                    } else if (tmpZoomRation <= minZoomIn) {
                        _newWidth = (int) (minZoomIn * oriWidth);
                        _newHeight = (int) ((float) _newWidth / oriRationWH);
                    }

                    mDrawableDst.set(centerX - _newWidth / 2, centerY - _newHeight / 2, centerX + _newWidth / 2, centerY + _newHeight / 2);
                    invalidate();

                    Log.v("width():" + oriWidth + "height():" + oriHeight, "new width():" + (mDrawableDst.width()) + "new height():" + (mDrawableDst.height()));
                    Log.v("" + (float) oriHeight / (float) oriWidth, "mDrawableDst:" + (float) mDrawableDst.height() / (float) mDrawableDst.width());

                    oldx_0 = newx_0;
                    oldy_0 = newy_0;

                    oldx_1 = newx_1;
                    oldy_1 = newy_1;
                } else if (mStatus == STATUS_TOUCH_SINGLE) {
                    int dx = (int) (event.getX() - oldX);
                    int dy = (int) (event.getY() - oldY);

                    oldX = event.getX();
                    oldY = event.getY();

                    if (!(dx == 0 && dy == 0)) {
                        mDrawableDst.offset(dx, dy);
                        invalidate();
                    }
                }
                break;
        }

//		Log.v("event.getAction()："+event.getAction()+"count："+event.getPointerCount(), "-------getX:"+event.getX()+"--------getY:"+event.getY());
        return true;
    }


    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mFloatRectSize = getResources().getDisplayMetrics().widthPixels;
        isFirst = true;
        invalidate();
    }
}
