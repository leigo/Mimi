package com.leigo.android.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by Administrator on 2014/10/28.
 */
public class ImageUtils {

    private static final int DEFAULT_BIGGEST_THUMBNAIL_SIZE = 720;
    private static final int DEFAULT_COMPRESSED_IMAGE_MAX_SIZE = 200 * 1024;
    private static final int DEFAULT_COMPRESSED_IMAGE_QUALITY = 75;
    private static final Logger logger = new Logger(ImageUtils.class);

    public static Bitmap decodeFile(String pathName) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        // Calculate inSampleSize
        options.inSampleSize = sampleSize(options, options.outWidth, options.outHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inDither = false;
        return rotateBitmap(BitmapFactory.decodeFile(pathName, options), getExifOrientation(pathName));
    }

    public static File getScaledImageFile(String pathName, int dstSize) throws IOException {
        Bitmap bitmap = decodeFile(pathName);
        if (bitmap == null) {
            throw new IOException("Failed to decode file");
        }
        FileOutputStream fos = null;
        try {
            File file = FileUtils.getFile(new String[]{pathName});
            fos = FileUtils.openOutputStream(file);
            Bitmap.createScaledBitmap(bitmap, dstSize, dstSize, false).compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return file;
        } finally {
            IOUtils.closeQuietly(fos);
        }

    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int exifOrientation) {
        int degree = 0;
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
        }
        Matrix matrix = null;
        if (degree != 0 && bitmap != null) {
            matrix = new Matrix();
            matrix.setRotate(degree, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        }
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            if (bitmap != bm) {
                bitmap.recycle();
                bitmap = bm;
                return bitmap;
            }
        } catch (OutOfMemoryError e) {
            logger.e("We have no memory to rotate. Return the original bitmap.");
        }

        return bitmap;
    }


    public static int getExifOrientation(String filename) {
        int orientation = -1;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filename);
        } catch (IOException ex) {
            logger.e("cannot read exif", ex);
        }
        if (exif != null) {
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, orientation);
        }
        return orientation;
    }

    //获取单个文件MD5
    public static String getFileMD5(File file) {
        MessageDigest digest = null;
        FileInputStream fis = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            while ((len = fis.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(fis);
        }
        return new BigInteger(1, digest.digest()).toString(16);
    }

    public static int sampleSize(BitmapFactory.Options options, int outWidth, int outHeight) {
        // Raw height and width of image
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        int reqWidth = Math.min(outWidth, 720);
        int reqHeight = Math.min(outHeight, 720);

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
