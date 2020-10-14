package io.drew.record.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.drew.base.MyLog;

/**
 * 截图
 *
 * @Author: KKK
 * @CreateDate: 2020/8/12 10:02 AM
 */
public class ShotUtils {
    private static final String IMAGE_FILE_NAME_TEMPLATE = "Image%s.jpg";
    private static final String IMAGE_FILE_PATH_TEMPLATE = "%s/%s";

    public interface ShotCallback {
        void onShotComplete(String savePath, Bitmap bitmap);
    }

    /**
     * view截图
     *
     * @return
     */
    public static void viewShot(@NonNull final View v, @Nullable final ShotCallback shotCallback) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(),
                v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        // end
        String savePath = createImagePath(v.getContext());
        try {
            compressAndGenImage(bitmap, savePath);
            MyLog.d("--->截图保存地址：" + savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != shotCallback) {
            shotCallback.onShotComplete(savePath, bitmap);
        }
//        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                } else {
//                    v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                }
//                // 核心代码start
//                Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
//                Canvas c = new Canvas(bitmap);
//                v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
//                v.draw(c);
//                // end
//                String savePath = createImagePath(v.getContext());
//                try {
//                    compressAndGenImage(bitmap, savePath);
//                    MyLog.d("--->截图保存地址：" + savePath);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (null != shotCallback) {
//                    shotCallback.onShotComplete(savePath);
//                }
//            }
//        });
    }

    public static String createImagePath(Context context) {
        //判断sd卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //文件名
            long systemTime = System.currentTimeMillis();
            String imageDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date(systemTime));
            String mFileName = String.format(IMAGE_FILE_NAME_TEMPLATE, imageDate);

            //文件全名
            String mstrRootPath = context.getExternalCacheDir().getAbsolutePath();
            String filePath = String.format(IMAGE_FILE_PATH_TEMPLATE, mstrRootPath, mFileName);
            File file = new File(filePath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return filePath;
        }
        return "";
    }

    public static void compressAndGenImage(Bitmap image, String outPath) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
//        int options = 70;
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);

        // Generate compressed image file
        FileOutputStream fos = new FileOutputStream(outPath);
        fos.write(os.toByteArray());
        MyLog.d("compressAndGenImage--->文件大小：" + os.size() + "，压缩比例：" + options);
        fos.flush();
        fos.close();
    }

    //保存图片到图库
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hualeme";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
