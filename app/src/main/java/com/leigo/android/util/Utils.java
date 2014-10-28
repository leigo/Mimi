package com.leigo.android.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.leigo.android.mimi.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2014/8/21.
 */
public class Utils {
    private static final String DEFAULT_TEMPLATE_URL_SUFFIX = "/resources/background/1.jpg";
    private static final String DRAWABLE_URI_SCHEME = "drawable://";
    private static final String FILE_URI_SCHEME = "file://";

    private static final long ONE_DAY = 24 * 60 * 60 * 1000;
    private static final long ONE_HOUR = 60 * 60 * 1000;
    private static final long ONE_MINUTE = 60 * 1000;
    private static final long ONE_MONTH = 30 * 24 * 60 * 60 * 1000;
    private static final long ONE_SECOND = 1000;
    private static final long ONE_YEAR = 12 * 30 * 24 * 60 * 60 * 1000;

    private static final float SECRET_CONTENT_PADDING_RATIO = 0.1125F;

    private static final Logger logger = new Logger(Utils.class);

    private static NumberFormat numberFormat;
    private static StringBuilder reusableBuilder;

    public static boolean beforeYesterday(long milliseconds) {
        return System.currentTimeMillis() - milliseconds > ONE_DAY;
    }

    public static String calcDisplayTime(Date date) {
        if (date == null) {
            reusableBuilder = new StringBuilder();
        }
        long time = System.currentTimeMillis() - date.getTime();
        if (time < 0) {
            time = 0;
        }

        if (time < ONE_MINUTE) {
            reusableBuilder.append(time / ONE_SECOND);
            reusableBuilder.append("秒");
        } else if (time < ONE_HOUR) { // 360000
            reusableBuilder.append(time / ONE_MINUTE);
            reusableBuilder.append("分钟");
        } else if (time < ONE_DAY) {  //86400000
            reusableBuilder.append(time / ONE_HOUR);
            reusableBuilder.append("小时");
        } else if (time < ONE_MONTH) { //2592000000
            reusableBuilder.append(time / ONE_DAY);
            reusableBuilder.append("天");
        } else if (time < ONE_YEAR) { //31104000000
            reusableBuilder.append(time / ONE_MONTH);
            reusableBuilder.append("月");
        } else {
            reusableBuilder.append(time / ONE_YEAR);
            reusableBuilder.append("年");
        }
        reusableBuilder.append("前");
        String str = reusableBuilder.toString();
        reusableBuilder.delete(0, reusableBuilder.length());
        return str;
    }


    public static boolean isDefaultSecretTemplate(String template) {
        return template.endsWith(DEFAULT_TEMPLATE_URL_SUFFIX);
    }

    public static void hideSoftKeyboard(InputMethodManager inputMethodManager, IBinder token) {
        inputMethodManager.hideSoftInputFromInputMethod(token, 0);
    }

    public static boolean isAppRunning(Context context) {
        ActivityManager.RunningTaskInfo runningTaskInfo = getRunningTaskInfo(context);
        if (runningTaskInfo == null) {
            return false;
        }
        return context.getPackageName().equals(runningTaskInfo.topActivity.getPackageName());
    }

    public static ActivityManager.RunningTaskInfo getRunningTaskInfo(Context context) {
        List<ActivityManager.RunningTaskInfo> runningTasks = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
        if (isEmpty(runningTasks)) {
            return null;
        }
        return runningTasks.get(0);
    }

    public static <E> boolean isEmpty(Collection<E> collection) {
        return (collection == null) || (collection.isEmpty());
    }

    public static void setVisibility(View view, int visibility) {
        if (view != null && view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    public static int dipToPixels(DisplayMetrics displayMetrics, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, displayMetrics);
    }

    public static void disableOverScroll(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            try {
                Class[] clazz = new Class[1];
                clazz[0] = Integer.TYPE;
                Method method = View.class.getMethod("setOverScrollMode", clazz);
                int i = ListView.class.getField("OVER_SCROLL_NEVER").getInt(view);
                Object[] obj = new Object[1];
                obj[0] = Integer.valueOf(i);
                method.invoke(view, obj);
            } catch (Exception localException) {
                logger.w("Error when call 'setOverScrollMode(int)' on the " + view.getClass().getName(), localException);
            }
        }
    }

    public static DisplayImageOptions getAvatarDisplayImageOptions(int cornerRadiusPixels) {
        return new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.ic_default_avatar)
                .showImageOnLoading(R.drawable.ic_default_avatar)
                .showImageForEmptyUri(R.drawable.ic_default_avatar)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels))
                .build();
    }

    public static String getDrawableUriFromResId(int resId) {
        return "drawable://" + resId;
    }

    public static String getFormattedDate(String template, Date date) {
        return new SimpleDateFormat(template, Locale.CHINA).format(date);
    }

    public static void showActionMenuItemTitle(MenuItem item) {
        int[] location = new int[2];
        Rect rect = new Rect();
        View view = item.getActionView();
        view.getLocationOnScreen(location);
        view.getWindowVisibleDisplayFrame(rect);
        Context context = view.getContext();
        int width = view.getWidth();
        int height = view.getHeight();
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        Toast toast = Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT);
        if (location[1] + height / 2 < rect.height()) {
            toast.setGravity(Gravity.TOP | Gravity.END, widthPixels - location[0] - width / 2, height);
        } else {
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
        }
        toast.show();
    }

    public static void startAnimation(final View view, Animation animation, final int visibility) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(visibility);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    public static void updateViewBackgroundResource(View view, int resid) {
        int left = view.getPaddingLeft();
        int top = view.getPaddingTop();
        int right = view.getPaddingRight();
        int bottom = view.getPaddingBottom();
        view.setBackgroundResource(resid);
        view.setPadding(left, top, right, bottom);
    }
}
