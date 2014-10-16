package com.leigo.android.model.helper;

import android.os.Environment;

import com.leigo.android.util.Logger;

import java.io.File;

/**
 * Created by Administrator on 2014/10/16.
 */
public class FileHelper {

    private static final Logger logger = new Logger(FileHelper.class);

    public static File getOutputMediaDirectory() {
        File file = new File(Environment.getExternalStorageDirectory(), "mimi");
        if ((!file.exists()) && (!file.mkdirs())) {
            logger.d("failed to create 'mimi' directory, return sdcard root directory");
            file = Environment.getExternalStorageDirectory();
        }
        return file;
    }
}
