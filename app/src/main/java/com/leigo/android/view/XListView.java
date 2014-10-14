/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * 		Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package com.leigo.android.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

import com.leigo.android.mimi.R;
import com.leigo.android.util.Utils;

import java.util.Date;

public class XListView extends ListView implements OnScrollListener {

    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
    // feature.

    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 300; // scroll back duration

    private boolean autoLoadMore = true;

    private boolean computeHeaderLoadingScrollStarted;
    private int lastTotalItemCount;
    private boolean mEnableLoadMore;
    private boolean mEnablePullRefresh = true;

    // -- footer view
    private XListViewFooter mFooterView;

    // -- header view
    private XListViewHeader mHeaderView;
    private int mHeaderViewHeight; // header view's height

    private boolean mIsFooterReady = false;

    private float mLastY = -1; // save event y
    private boolean mLoadingMore;

    private boolean mPullRefreshing = false; // is refreshing.

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;

    private OnScrollListener mScrollListener; // user's scroll listener

    private Scroller mScroller; // used for scroll back

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    // the interface to trigger load more.
    private OnLoadMoreListener onLoadMoreListener;

    // the interface to trigger refresh.
    private OnRefreshListener onRefreshListener;

    private int pullLoadMoreDelta;
    private boolean resetHeaderRequested;

    /**
     * @param context
     */
    public XListView(Context context) {
        super(context);
        initWithContext(context, null);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context, attrs);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context, attrs);
    }

    private int getHeaderActionHeight() {
        return mHeaderView.getHeaderOffset() + mHeaderViewHeight;
    }

    private void initWithContext(Context context, AttributeSet attrs) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

        // init header view
        mHeaderView = new XListViewHeader(context, attrs);
        addHeaderView(mHeaderView);

        // init footer view
        mFooterView = new XListViewFooter(context, attrs);

        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mHeaderViewHeight = mHeaderView.getViewContent().getHeight();
                        getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });
        pullLoadMoreDelta = context.getResources().getDimensionPixelSize(R.dimen.xlist_view_pull_load_more_delta);
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void resetFooterHeight() {
        if (mLoadingMore && mFooterView.getFooterOffset() == 0) {

        }
        int bottomMargin = mFooterView.getVisibleHeight() - pullLoadMoreDelta;
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, mFooterView.getVisibleHeight(), 0, -bottomMargin,
                    SCROLL_DURATION);
            invalidate();
        }
    }

    private void startLoadMore() {
        if (onLoadMoreListener != null && mLoadingMore != true) {
            mLoadingMore = true;
            mFooterView.setState(XListViewFooter.STATE_LOADING);
            onLoadMoreListener.onLoadMore();
        }
    }

    private void updateFooterHeight(float delta) {
        int height = mFooterView.getVisibleHeight() + (int) delta;
        if (mEnableLoadMore && !mLoadingMore) {
            if (height - mFooterView.getFooterOffset() > pullLoadMoreDelta) { // height enough to invoke load
                // more.
                mFooterView.setState(XListViewFooter.STATE_READY);
            } else {
                mFooterView.setState(XListViewFooter.STATE_NORMAL);
            }
        }
        mFooterView.setVisibleHeight(height);
        if (delta < 0) {
            setSelection(mTotalItemCount - 1); // scroll to bottom
        }

    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisibleHeight((int) delta
                + mHeaderView.getVisibleHeight());
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            if ((onRefreshListener == null || mHeaderView.getVisibleHeight() <= getHeaderActionHeight()) || delta < 0) {
                if (delta < 0 && mHeaderView.getVisibleHeight() < getHeaderActionHeight()) {
                    mHeaderView.setState(XListViewHeader.STATE_NORMAL);
                }
            } else {
                mHeaderView.setState(XListViewHeader.STATE_READY);
            }
        }
        if (delta > 0 && mPullRefreshing) {
            setSelection(0); // scroll to top each time
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisibleHeight(mScroller.getCurrY());
            } else {
                mFooterView.setVisibleHeight(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
        computeHeaderLoadingScrollStarted = false;
        if (resetHeaderRequested) {
            resetHeaderHeight();
        }
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void enableLoadMore(boolean enable) {
        if (mEnableLoadMore == enable) {
            return;
        }
        mEnableLoadMore = enable;
        if (!mEnableLoadMore) {
            mFooterView.hide();
            mFooterView.setOnClickListener(null);
        } else {
            mFooterView.show();
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void enablePullRefresh(boolean enable) {
        if (mEnablePullRefresh == enable) {
            return;
        }
        mEnablePullRefresh = enable;// disable, hide the content
        if (!mEnablePullRefresh) {
            mHeaderView.getViewContent().setVisibility(View.INVISIBLE);
        } else {
            this.mHeaderView.getViewContent().setVisibility(View.VISIBLE);
        }
    }

    public void fakePullRefresh() {
        if (onRefreshListener == null) {
        }
        while (!showLoadingState()) {
            return;
        }
        onRefreshListener.onRefresh();
    }

    public boolean isEnablePullRefresh() {
        return mEnablePullRefresh;
    }

    public boolean isPullRefreshing() {
        return mPullRefreshing;
    }

    /**
     * stop refresh, reset footer view.
     */
    public void onLoadMoreComplete() {
        if (mLoadingMore) {
            mLoadingMore = false;
            resetFooterHeight();
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
        }
    }

    /**
     * stop refresh, reset header view.
     */
    public boolean onRefreshComplete(boolean paramBoolean) {
        resetAutoLoadMoreState();
        if (mPullRefreshing) {
            mPullRefreshing = false;
            resetHeaderHeight();
            mHeaderView.setState(XListViewHeader.STATE_NORMAL);
            if (paramBoolean) {
                setRefreshTime(Utils.now());
            }
            return true;
        }
        return false;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
        if (lastTotalItemCount == 0) {
            lastTotalItemCount = getFooterViewsCount() + getHeaderViewsCount();
        }

        if (autoLoadMore && mEnableLoadMore && totalItemCount > lastTotalItemCount && getLastVisiblePosition() == lastTotalItemCount - 1) {
            lastTotalItemCount = totalItemCount;
            startLoadMore();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (mEnablePullRefresh && getFirstVisiblePosition() == 0
                        && (mHeaderView.getVisibleHeight() - mHeaderView.getHeaderOffset() > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    if (mHeaderView.getVisibleHeight() - mHeaderView.getHeaderOffset() > 0 && deltaY < 0) {
                        setSelection(0);
                    }
                    invokeOnScrolling();
                } else if (!autoLoadMore && mEnableLoadMore && getLastVisiblePosition() == mTotalItemCount - 1
                        && (mFooterView.getVisibleHeight() - mFooterView.getFooterOffset() > 0 || deltaY < 0)) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            default:
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
                    // invoke refresh
                    if (mEnablePullRefresh
                            && mHeaderView.getVisibleHeight() > getHeaderActionHeight() && mHeaderView.getState() == XListViewHeader.STATE_READY) {
                        mPullRefreshing = true;
                        mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
                        if (onRefreshListener != null) {
                            onRefreshListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // invoke load more.
                    if (mEnableLoadMore
                            && mFooterView.getVisibleHeight() - mFooterView.getFooterOffset() > pullLoadMoreDelta) {
                        startLoadMore();
                    }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void resetAutoLoadMoreState() {
        lastTotalItemCount = 0;
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        resetHeaderRequested = true;
        int height = mHeaderView.getVisibleHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = mHeaderView.getHeaderOffset(); // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > getHeaderActionHeight()) {
            finalHeight = getHeaderActionHeight();
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height,
                SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        // make sure XListViewFooter is the last footer view, and only add once.
        if (!mIsFooterReady) {
            mIsFooterReady = true;
            addFooterView(mFooterView);
            mFooterView.hide();
        }
        super.setAdapter(adapter);
    }

    public void setAutoLoadMore(boolean autoLoadMore) {
        this.autoLoadMore = autoLoadMore;
    }

    public void setHeaderHintTextColor(int color) {
        this.mHeaderView.setHintTextColor(color);
    }

    public void setHeaderRuntimeLoadingHint(String runtimeLoadingHint) {
        this.mHeaderView.setRuntimeLoadingHint(runtimeLoadingHint);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public void setOnScrollListener(OnScrollListener mScrollListener) {
        this.mScrollListener = mScrollListener;
    }

    /**
     * set last refresh time
     *
     * @param date
     */
    public void setRefreshTime(Date date) {
        if (date == null) {
            return;
        }
        mHeaderView.getTimeView().setText(Utils.getFormattedDate("MM-dd HH:mm", date));
    }

    public boolean showLoadingState() {
        if (mPullRefreshing) {
            return false;
        }
        if (mHeaderViewHeight == 0) {
            mHeaderViewHeight = mHeaderView.getViewContent().getLayoutParams().height;
        }
        setSelection(0);
        mPullRefreshing = true;
        mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
        enablePullRefresh(true);
        if (autoLoadMore) {
            enableLoadMore(false);
        }
        mScrollBack = 0;
        mScroller.startScroll(0, this.mHeaderView.getHeaderOffset(), 0, getHeaderActionHeight() - this.mHeaderView.getHeaderOffset(), 150);
        computeHeaderLoadingScrollStarted = true;
        invalidate();
        return true;
    }


    /**
     * implements this interface to get rload more event.
     */
    public static abstract interface OnLoadMoreListener {
        public abstract void onLoadMore();
    }

    /**
     * implements this interface to get refresh more event.
     */
    public static abstract interface OnRefreshListener {
        public abstract void onRefresh();
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }
}
