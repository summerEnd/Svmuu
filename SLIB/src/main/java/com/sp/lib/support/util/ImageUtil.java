package com.sp.lib.support.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 对图片操作的工具类
 */
@SuppressWarnings("unused")
public class ImageUtil {

    /**
     * 对图片进行base64编码
     *
     * @param bitmap 用来进行编码的bitmap，如果为null则返回null
     * @return base64编码的bitmap
     */
    public static String base64Encode(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bytes = bos.toByteArray();

        try {
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "data:image/png;base64," + Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 以一个bitmap的中心为圆点，以radius为半径，去截取图片
     *
     * @param src    将要被处理的图片
     * @param radius 单位px
     * @return 处理后的推按
     */
    public static Bitmap roundBitmap(Bitmap src, int radius) {

        int output_size = radius * 2;
        if (src == null) {
            //如果图片不存在，就创建一个色块
            ColorDrawable drawable = new ColorDrawable(Color.GRAY);
            src = Bitmap.createBitmap(
                    output_size,
                    output_size,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(src);
            drawable.setBounds(0, 0, output_size, output_size);
            drawable.draw(canvas);
        }
        int src_w = src.getWidth();
        int src_h = src.getHeight();

        //按照图片宽高较小的值来计算缩放比例。即，如果宽小就按照宽缩放，反之亦然。
        float scale = output_size / (float) Math.min(src_h, src_w);

        //缩放后的Bitmap
        Bitmap scaledSrc = Bitmap.createScaledBitmap(src, (int) (src_w * scale), (int) (src_h * scale), false);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        Bitmap result = Bitmap.createBitmap(output_size, output_size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        //canvas.drawARGB(0,0,0,0);//背景透明效果
        canvas.drawCircle(radius, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        float translate_x = (result.getWidth() - scaledSrc.getWidth()) / 2;
        float translate_y = (result.getHeight() - scaledSrc.getHeight()) / 2;

        canvas.save();
        canvas.translate(translate_x, translate_y);
        canvas.drawBitmap(scaledSrc, 0, 0, paint);
        canvas.restore();

        if (src != scaledSrc && !scaledSrc.isRecycled())
            scaledSrc.recycle();

        return result;
    }

    public static Bitmap convertView2Bitmap(View v) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap result = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        v.draw(canvas);
        return result;
    }

    /**
     * 耗时方法，不可放在UI线程上
     *
     * @param url 网络图片的url
     * @return bitmap
     */
    public static Bitmap getImageFromWeb(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
