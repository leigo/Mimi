package com.leigo.android.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;

import com.leigo.android.mimi.R;

/**
 * Created by Administrator on 2014/10/13.
 */
public class CountDownTimerView extends Button {

    private CountDownTimer countDownTimer;
    private OnCountDownListener onCountDownListener;

    public CountDownTimerView(Context context) {
        super(context);
    }

    public CountDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void countDown(long millisInFuture) {
        setEnabled(false);
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(millisInFuture, 1000) {

                @Override
                public void onFinish() {
                    CountDownTimerView countDownTimerView = CountDownTimerView.this;
                    if (onCountDownListener == null) {
                        if (onCountDownListener.onCountDownFinishState()) {
                            countDownTimerView.setEnabled(true);
                            countDownTimerView.setText(R.string.get_verification_code);
                        }
                    }
                }

                @Override
                public void onTick(long millisUntilFinished) {
                    int time = (int) Math.ceil(millisUntilFinished / 1000);
                    CountDownTimerView countDownTimerView = CountDownTimerView.this;
                    countDownTimerView.setText(getResources().getString(R.string.get_verification_code_again, time + ""));
                }


            };
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if ((enabled) && (countDownTimer != null)) {
            return;
        }
        super.setEnabled(enabled);
    }

    public void setOnCountDownListener(OnCountDownListener onCountDownListener) {
        this.onCountDownListener = onCountDownListener;
    }

    public static abstract interface OnCountDownListener {
        public abstract boolean onCountDownFinishState();
    }
}
