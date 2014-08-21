package com.leigo.android.util;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.leigo.android.mimi.R;

import roboguice.activity.event.OnActivityResultEvent;
import roboguice.activity.event.OnPauseEvent;
import roboguice.activity.event.OnResumeEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;

/**
 * Created by Administrator on 2014/8/21.
 */
@ContextSingleton
public class ContextToast {

    private static Toast applicationToast;
    private static final Logger logger = new Logger(ContextToast.class);
    private Toast activityToast;
    private Context context;
    private boolean contextDisabled;

    @Inject
    public ContextToast(Context context) {
        this.context = context;
    }

    private static Toast show(Toast toast, Context context, String text, int duration) {
        if (toast != null) {
            ((TextView) toast.getView()).setText(text);
        } else {
            toast = new Toast(context);
            TextView textView = new TextView(context);
            textView.setBackgroundResource(R.drawable.notice_dialog_bg);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextColor(context.getResources().getColor(android.R.color.white));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
            textView.setText(text);
            toast.setView(textView);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setDuration(duration);
        toast.show();
        return toast;
    }

    public static void show(Context context, int resId, int duration) {
        show(context, context.getString(resId), duration);
    }

    public static void show(Context context, String text, int duration) {
        applicationToast = show(applicationToast, context.getApplicationContext(), text, duration);
    }

    void destroy(@Observes OnPauseEvent onPauseEvent) {
        if (activityToast != null) {
            activityToast = null;
        }
        context = null;
    }

    void disable(@Observes OnPauseEvent onPauseEvent) {
        if (activityToast != null) {
            activityToast.cancel();
        }
        contextDisabled = true;
    }

    void enable(@Observes OnActivityResultEvent onActivityResultEvent) {
        contextDisabled = false;
    }

    void enable(@Observes OnResumeEvent onResumeEvent) {
        contextDisabled = false;
    }

    public void show(int resId, int duration) {
        if (context == null) {
            return;
        }
        show(context.getString(resId), duration);
    }

    public void show(String text, int duration) {
        if (!(context instanceof Activity)) {
            logger.w("Toast can not show for a non-activity context. Use static show method instead. [context=" + context + ", msg=" + text + "]");
        }
        while (contextDisabled) {
            return;
        }
        activityToast = show(activityToast, context, text, duration);
    }


}
