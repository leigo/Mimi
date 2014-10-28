package com.leigo.android.controller.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.leigo.android.mimi.R;
import com.leigo.android.model.helper.FileHelper;
import com.leigo.android.util.ContextToast;
import com.leigo.android.util.ImageUtils;
import com.leigo.android.util.Utils;
import com.leigo.android.view.CropImageView;
import com.leigo.android.view.ProgressingDialog;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2014/10/13.
 */
public class CropImageActivity extends Activity {

    private static final String EXTRA_PATH = "path";

    private CropImageView cropImageView;

    private ContextToast contextToast;

    private DisplayMetrics displayMetrics;

    private String imagePath;

    public static void startFrom(Activity activity, String path) {
        Intent intent = new Intent(activity, CropImageActivity.class);
        intent.putExtra(EXTRA_PATH, path);
        activity.startActivityForResult(intent, 4);
    }

    public void clickOnCancel(View v) {
        finish();
    }

    public void clickOnChoose(View paramView) {
        saveImageTask();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        cropImageView = (CropImageView) findViewById(R.id.crop_image);
        imagePath = getIntent().getExtras().getString(EXTRA_PATH);
        progressImageTask();
    }

    private void progressImageTask() {
        ProgressingDialog progressDialog = new ProgressingDialog(this);
        progressDialog.show();
        Bitmap bitmap = ImageUtils.decodeFile(imagePath);
        if (bitmap == null) {
            ContextToast.show(this, R.string.toast_image_data_invalid, Toast.LENGTH_SHORT);
            CropImageActivity.this.finish();
        } else {
            cropImageView.setBitmap(bitmap);
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void saveImageTask() {
        Bitmap bitmap = cropImageView.getCroppedImage();
        File file = new File(FileHelper.getOutputMediaDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fos = null;
        try {
            try {
                fos = FileUtils.openOutputStream(file);
            } catch (IOException e) {
                contextToast.show(R.string.toast_process_image_failed, Toast.LENGTH_SHORT);
            }
            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                contextToast.show(R.string.toast_process_image_failed, Toast.LENGTH_SHORT);
            }
        } finally {
            IOUtils.closeQuietly(fos);
            bitmap.recycle();
        }
        IOUtils.closeQuietly(fos);
        bitmap.recycle();
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
        sendBroadcast(intent);
    }
}
