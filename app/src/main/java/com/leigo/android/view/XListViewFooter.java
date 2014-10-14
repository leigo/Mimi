/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.leigo.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leigo.android.mimi.R;

public class XListViewFooter extends LinearLayout {
    /*1.无动画*/
    public final static int STATE_NORMAL = 0;
    /*2.显示箭头图标*/
    public final static int STATE_READY = 1;
    /*3.显示进度条*/
    public final static int STATE_LOADING = 2;

    private int footerHintLoading;
    private int footerHintNormal;
    private int footerHintReady;

    private int footerOffset;

    private ImageView mArrowImageView;
    private View mContentView;
    private TextView mHintTextView;
    private View mProgressBar;
    private Animation mRotateDownAnim;
    private Animation mRotateUpAnim;

    private int mState = STATE_NORMAL;

    public XListViewFooter(Context context) {
        super(context);
    }

    public XListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XListView);
        footerOffset = a.getInteger(R.styleable.XListView_footerOffset, getResources().getDimensionPixelSize(R.dimen.xlist_view_footer_offset));
        footerHintNormal = a.getResourceId(R.styleable.XListView_footerHintNormal, R.string.xlist_view_footer_hint_normal);
        footerHintReady = a.getResourceId(R.styleable.XListView_footerHintReady, R.string.xlist_view_footer_hint_ready);
        footerHintLoading = a.getResourceId(R.styleable.XListView_footerHintLoading, R.string.xlist_view_footer_hint_loading);
        int hintTextColor = a.getColor(R.styleable.XListView_hintTextColor, getResources().getColor(R.color.xlist_view_hint_text));
        boolean showFooterArrow = a.getBoolean(R.styleable.XListView_showFooterArrow, false);
        a.recycle();

        mContentView = LayoutInflater.from(context).inflate(R.layout.xlist_view_footer, null);
        mContentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, footerOffset));
        addView(mContentView);

        if (showFooterArrow) {
            mArrowImageView = (ImageView) mContentView.findViewById(R.id.xlist_view_footer_arrow);
            mArrowImageView.setVisibility(View.VISIBLE);
        }

        mProgressBar = mContentView.findViewById(R.id.xlist_view_footer_progressbar);
        mHintTextView = (TextView) mContentView.findViewById(R.id.xlist_view_footer_hint_textview);
        mHintTextView.setTextColor(hintTextColor);
        mHintTextView.setText(footerHintNormal);

        mRotateUpAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(180);
        mRotateUpAnim.setFillAfter(true);

        mRotateDownAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(180);
        mRotateDownAnim.setFillAfter(true);
    }

    public int getFooterOffset() {
        return footerOffset;
    }

    public View getContentView() {
        return mContentView;
    }

    public int getVisibleHeight() {
        return mContentView.getLayoutParams().height;
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
        mContentView.setVisibility(View.INVISIBLE);
    }

    /**
     * loading status
     */
    public void loading() {
        mHintTextView.setText(R.string.xlist_view_hint_loading);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * normal status
     */
    public void normal() {
        mHintTextView.setText(R.string.xlist_view_footer_hint_normal);
        mProgressBar.setVisibility(View.GONE);
    }

    public void setArrowImage(int resId) {
        if (mArrowImageView != null) {
            mArrowImageView.setImageResource(resId);
        }
    }

    public void setHintTextColor(int color) {
        mHintTextView.setTextColor(color);
    }

    public void setState(int state) {
        if (mState == state) {
            return;
        }

        if (state == STATE_LOADING) {  //显示进度
            if (mArrowImageView != null) {
                mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(View.INVISIBLE);
            }
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            if (mArrowImageView != null) {
                mArrowImageView.setVisibility(View.VISIBLE);
            }
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
                if(mArrowImageView != null) {
                    if (mState == STATE_READY) {
                        mArrowImageView.startAnimation(mRotateDownAnim);
                    }
                    if (mState == STATE_LOADING) {
                        mArrowImageView.clearAnimation();
                    }
                }
                mHintTextView.setText(footerHintNormal);
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
                    if (mArrowImageView != null) {
                        mArrowImageView.clearAnimation();
                        mArrowImageView.startAnimation(mRotateUpAnim);
                    }
                    mHintTextView.setText(footerHintReady);
                }
                break;
            case STATE_LOADING:
                mHintTextView.setText(footerHintLoading);
                break;
        }

        mState = state;
    }

    public void setVisibleHeight(int height) {
        if (mContentView.getVisibility() != View.VISIBLE) {
            return;
        }
        if (height < footerOffset) {
            height = footerOffset;
        }
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = height;
        mContentView.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = footerOffset;
        mContentView.setLayoutParams(lp);
        mContentView.setVisibility(View.VISIBLE);
    }
}
