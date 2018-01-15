package com.gsh.utils.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by longfei.zhang on 2015/7/3.
 */
public class ImageUtils {

    /**
     * 将这个路径的图片旋转再次保存
     *
     * @param path
     */
    public static void rotateSaveBitmap(String path) {
        int degree = readPictureDegree(path);
        Bitmap bitmap = rotaingImageView(getBitampByPath(path), degree);
        saveBitmap(bitmap, path);
    }

    /**
     * 保存图片到本地
     *
     * @param mBitmap 图片
     * @param path    图片路径
     */
    public static boolean saveBitmap(Bitmap mBitmap, String path) {
        if (mBitmap == null) {
            return false;
        }
        File f = new File(path);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fOut != null) {
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } else {
            return false;
        }
        try {
            if (fOut != null) {
                fOut.flush();
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
        return false;
    }

    /**
     * 保存图片到本地
     *
     * @param mBitmap 图片
     * @param path    图片路径
     */
    public static void saveBitmap(final Bitmap mBitmap, final String path, final SaveBitmapCallBack callBack) {
        if (mBitmap == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(path);
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (fOut != null) {
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                } else {
                    return;
                }
                try {
                    if (fOut != null) {
                        fOut.flush();
                        if(callBack!=null){
                            callBack.saveEnd(true);
                        }
                        return;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fOut != null) {
                            fOut.close();
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    }
                }
                if(callBack!=null){
                    callBack.saveEnd(false);
                }
            }
        }).start();

    }
    /*
   * 旋转图片
   * @param angle
   * @param bitmap
   * @return Bitmap
   */
    public static Bitmap rotaingImageView(Bitmap bitmap, int angle) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap getBitampByPath(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    /**
     * @param filePath 保存完图片，刷新相册，只能是单个图片路径
     * @param context
     */
    public static void scanPhotos(String filePath, Context context) {
        Intent intent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    /**
     * 保存图片介绍回调
     */
    public interface SaveBitmapCallBack{
        void saveEnd(boolean isSuccess);
    }
}
