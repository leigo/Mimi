package com.leigo.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.leigo.android.mimi.R;

/**
 * Created by Administrator on 2014/10/27.
 */
public class ChatBottomPan extends FrameLayout {

    protected LayoutInflater inflater;
    private View mainPan;
    protected int width;

    public ChatBottomPan(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        width = getResources().getDisplayMetrics().widthPixels;
    }

    protected int getKeyboardHeight() {
        return 1;
    }

    public void hide() {
        removeAllViews();
    }

    public boolean isShowingPan() {
        return mainPan != null && mainPan.isShown();
    }

    public void showMainPan(int height) {
        removeAllViews();
        if (mainPan == null) {
            mainPan = inflater.inflate(R.layout.chat_bottom_pan_main, this, false);
            if (getKeyboardHeight() != 0) {
                mainPan.setLayoutParams(new LayoutParams(width, getKeyboardHeight()));
            }
        }

        if (height != 0 && mainPan.getLayoutParams().height != height) {
            mainPan.setLayoutParams(new LayoutParams(width, height));
        }
        addView(mainPan);
    }
}
