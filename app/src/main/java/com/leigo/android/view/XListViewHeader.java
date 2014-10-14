/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.leigo.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leigo.android.mimi.R;

public class XListViewHeader extends LinearLayout {

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    private int headerHintLoading;
    private int headerHintNormal;
    private int headerHintReady;
    private int headerOffset;

    private ImageView mArrowImageView;
    private LinearLayout mContainer;
    private TextView mHintTextView;
    private ProgressBar mProgressBar;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private int mState = STATE_NORMAL;

    private TextView mTimeDes;
    private TextView mTimeView;

    private RelativeLayout mViewContent;
    private String runtimeLoadingHint;
    private LinearLayout timeContainer;

    public XListViewHeader(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public XListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XListView);
        headerOffset = a.getInteger(R.styleable.XListView_headerOffset, 0);
        headerHintNormal = a.getResourceId(R.styleable.XListView_headerHintNormal, R.string.xlist_view_header_hint_normal);
        headerHintReady = a.getResourceId(R.styleable.XListView_headerHintReady, R.string.xlist_view_header_hint_ready);
        headerHintLoading = a.getResourceId(R.styleable.XListView_headerHintLoading, R.string.xlist_view_hint_loading);

        int hintTextColor = a.getColor(R.styleable.XListView_hintTextColor, getResources().getColor(R.color.xlist_view_hint_text));
        boolean displayUpdateTime = a.getBoolean(R.styleable.XListView_displayUpdateTime, true);
        a.recycle();

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, headerOffset);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.xlist_view_header, null);
        addView(mContainer, lp);

        setGravity(Gravity.BOTTOM);

        mViewContent = (RelativeLayout) findViewById(R.id.xlist_view_header_content);
        timeContainer = (LinearLayout) findViewById(R.id.time_container);
        mArrowImageView = (ImageView) findViewById(R.id.xlist_view_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.xlist_view_header_hint_textview);
        mTimeView = (TextView) findViewById(R.id.xlist_view_header_time);
        mTimeDes = (TextView) findViewById(R.id.xlist_view_header_time_des);

        mHintTextView.setText(headerHintNormal);
        setHintTextColor(hintTextColor);

        if (!displayUpdateTime) {
            timeContainer.setVisibility(View.GONE);
        }

        mProgressBar = (ProgressBar) findViewById(R.id.xlist_view_header_progressbar);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(180);
        mRotateUpAnim.setFillAfter(true);

        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(180);
        mRotateDownAnim.setFillAfter(true);
    }

    public int getHeaderOffset() {
        return headerOffset;
    }

    public int getState() {
        return mState;
    }

    public TextView getTimeView() {
        return mTimeView;
    }

    public RelativeLayout getViewContent() {
        return mViewContent;
    }

    public int getVisibleHeight() {
        return mContainer.getLayoutParams().height;
    }

    public void setArrowImageView(int resId) {
        mArrowImageView.setImageResource(resId);
    }

    public void setHintTextColor(int color) {
        mHintTextView.setTextColor(color);
        mTimeView.setTextColor(color);
        mTimeDes.setTextColor(color);
    }

    public void setRuntimeLoadingHint(String runtimeLoadingHint) {
        this.runtimeLoadingHint = runtimeLoadingHint;
    }

    public void setState(int state) {
        if (state == mState) return;

        if (state == STATE_REFRESHING) {    // 显示进度
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {    // 显示箭头图片
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }
                mHintTextView.setText(headerHintNormal);
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText(headerHintReady);
                }
                break;
            case STATE_REFRESHING:
                if (runtimeLoadingHint != null) {
                    mHintTextView.setText(runtimeLoadingHint);
                } else {
                    mHintTextView.setText(headerHintLoading);
                }

                break;
            default:
        }

        mState = state;
    }

    public void setVisibleHeight(int height) {
        if (height < headerOffset)
            height = headerOffset;
        LayoutParams lp = (LayoutParams) mContainer
                .getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

}
