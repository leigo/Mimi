package com.leigo.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.util.SimpleArrayMap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.leigo.android.mimi.R;

/**
 * Created by Administrator on 2014/8/22.
 */
public class QuickSelectionBar extends View {

    private SimpleArrayMap<Integer, String> indexer;
    private OnItemSelectedListener onItemSelectedListener;
    private int overlaySize;
    private Paint paint;
    private TextView popupTextView;
    private PopupWindow popupWindow;


    public QuickSelectionBar(Context context) {
        super(context);
    }

    public QuickSelectionBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public QuickSelectionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuickSelectionBar);
        overlaySize = typedArray.getDimensionPixelSize(R.styleable.QuickSelectionBar_overlay_size, getResources().getDimensionPixelOffset(R.dimen.quick_selection_overlay_size));
        typedArray.recycle();
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setTextSize(20.0f);
        paint.setAntiAlias(true);
    }

    private void dismissPopup() {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        }, 100L);
    }

    private void performItemSelected(int position) {
        if (onItemSelectedListener != null) {
            showPopup(position);
            onItemSelectedListener.onItemSelected(position);
        }
    }

    private void showPopup(int position) {
        if (popupWindow == null) {
            popupTextView = new TextView(getContext());
            popupTextView.setTextSize(50.0f);
            popupTextView.setTextColor(Color.WHITE);
            popupTextView.setGravity(Gravity.CENTER);
            popupTextView.setIncludeFontPadding(false);
            popupWindow = new PopupWindow(popupTextView, overlaySize, overlaySize);
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.quick_selection_overlay_bg));
        }
        popupTextView.setText(indexer.valueAt(position));
        if (popupWindow.isShowing()) {
            popupWindow.update();
        } else {
            popupWindow.showAtLocation(getRootView(), Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (indexer == null || indexer.isEmpty()) {
            return true;
        }
        int action = event.getAction();
        int position = (int) (event.getY() / getHeight() * indexer.size());

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (position > 0 && position < indexer.size()) {
                    performItemSelected(position);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                dismissPopup();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (indexer == null || indexer.isEmpty()) {
            return;
        }

        int height = getHeight();
        int width = getWidth();
        int size = indexer.size();
        float indexerHeight = height / size;
        for (int i = 0; i < size; i++) {
            String index = indexer.valueAt(i);
            Rect rect = new Rect();
            paint.getTextBounds(index, 0, 1, rect);
            canvas.drawText(index, (width - paint.measureText(index)) / 2.0f, indexerHeight * i + (indexerHeight / 2.0f + rect.height() / 2.0f), paint);
        }
    }

    public void setIndexer(SimpleArrayMap<Integer, String> indexer) {
        this.indexer = indexer;
        invalidate();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public static abstract interface OnItemSelectedListener {
        public abstract void onItemSelected(int position);
    }
}
