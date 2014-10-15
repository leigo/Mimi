package com.leigo.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
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

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2014/8/21.
 */
public class Utils {
    private static final String DEFAULT_TEMPLATE_URL_SUFFIX = "/resources/background/1.jpg";
    private static final String DRAWABLE_URI_SCHEME = "drawable://";

    private static final long ONE_DAY = 24 * 60 * 60 * 1000;
    private static final long ONE_HOUR = 60 * 60 * 1000;
    private static final long ONE_MINUTE = 60 * 1000;
    private static final long ONE_MONTH = 30 * 24 * 60 * 60 * 1000;
    private static final long ONE_SECOND = 1000;
    private static final long ONE_YEAR = 12 * 30 * 24 * 60 * 60 * 1000;

    private static final Logger logger = new Logger(Utils.class);
    private static StringBuilder reusableBuilder;

    public static boolean beforeYesterday(long milliseconds) {
        return System.currentTimeMillis() - milliseconds > 24 * 60 * 60 * 1000;
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

    private static Bitmap decodeFile(String pathName) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, opts);
        opts.inSampleSize = sampleSize(opts, opts.outWidth, opts.outHeight);
        opts.inJustDecodeBounds = false;
        opts.inDither = false;
        return rotateBitmap(BitmapFactory.decodeFile(pathName, opts), getExifOrientation(pathName));
    }

    // Rotates the bitmap by the specified degree.
    // If a new bitmap is created, the original bitmap is recycled.
    public static Bitmap rotateBitmap(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees,
                    (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(
                        b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return b;
    }

    public static int sampleSize(BitmapFactory.Options opts, int width, int height) {
        int w = opts.outWidth;
        int h = opts.outHeight;
        int k = 1;
        int m = Math.min(width, 720);
        int n = Math.min(height, 720);
        if (w > m || h > n) {
            int ww = w / 2;
            int hh = h / 2;
            while (ww / k > m && hh / k > n) {
                k *= 2;
            }
        }
        return k;
    }


    public static String getDrawableUriFromResId(int resId) {
        return DRAWABLE_URI_SCHEME + resId;
    }

    private static int getExifOrientation(String filename) {
        ExifInterface exif;
        int orientation = -1;
        try {
            exif = new ExifInterface(filename);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            logger.e("cannot read exif", e);
            e.printStackTrace();
        }
        return orientation;
    }

    public static boolean isDefaultSecretTemplate(String template) {
        return template.endsWith(DEFAULT_TEMPLATE_URL_SUFFIX);
    }

    public static void hideSoftKeyboard(InputMethodManager inputMethodManager, IBinder token) {
        inputMethodManager.hideSoftInputFromInputMethod(token, 0);
    }

    public static void setVisibility(View view, int visibility) {
        if (view != null && view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
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
}
