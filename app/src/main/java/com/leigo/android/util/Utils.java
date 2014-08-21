package com.leigo.android.util;

import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ListView;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2014/8/21.
 */
public class Utils {

    private static final Logger logger = new Logger(Utils.class);

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


    public static void setVisibility(View view, int visibility) {
        if (view.getVisibility() != visibility) {
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
}
