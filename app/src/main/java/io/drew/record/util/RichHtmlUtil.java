package io.drew.record.util;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/22 6:27 PM
 */

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.widget.TextView;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 加载富文本工具类
 */
public class RichHtmlUtil {
    TextView tv;

    public RichHtmlUtil(TextView txt_content, final String infointro) {
        tv = txt_content;
        final MyHandler myHandler = new MyHandler(tv);
        Thread t = new Thread(new Runnable() {
            Message msg = Message.obtain();

            @Override
            public void run() {
                /**
                 * 要实现图片的显示需要使用Html.fromHtml的一个重构方法：public static Spanned
                 * fromHtml (String source, Html.ImageGetterimageGetter,
                 * Html.TagHandler
                 * tagHandler)其中Html.ImageGetter是一个接口，我们要实现此接口，在它的getDrawable
                 * (String source)方法中返回图片的Drawable对象才可以。
                 */
                Html.ImageGetter imageGetter = new Html.ImageGetter() {

                    @Override
                    public Drawable getDrawable(String source) {
                        // TODO Auto-generated method stub
                        URL url;
                        Drawable drawable = null;
                        try {
                            url = new URL(source);
                            drawable = Drawable.createFromStream(url.openStream(), null);
                            if (drawable != null) {
                                int iPicWidth = drawable.getIntrinsicWidth();
                                int iPicHeight = drawable.getIntrinsicHeight();

                                int newwidth = tv.getMeasuredWidth();
                                double a = div(newwidth, iPicWidth, 2);
                                double newheight = iPicHeight * a;//图片宽度和高度等比缩放
                                drawable.setBounds(0, 0, newwidth, (int) newheight);
                            }
                        } catch (MalformedURLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return drawable;
                    }
                };
                CharSequence test = Html.fromHtml(infointro, imageGetter, null);
                msg.what = 0x101;
                msg.obj = test;
                myHandler.sendMessage(msg);
            }
        });
        t.start();
    }

    /*
     * Handler
     * 类应该应该为static类型，否则有可能造成泄露。在程序消息队列中排队的消息保持了对目标Handler类的应用。如果Handler是个内部类，那
     * 么它也会保持它所在的外部类的引用。为了避免泄露这个外部类，应该将Handler声明为static嵌套类，并且使用对外部类的弱应用。
     */
    private static class MyHandler extends Handler {
        TextView tv;

        public MyHandler(TextView tv) {
            this.tv = tv;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x101) {
                tv.setText((CharSequence) msg.obj);
            }
            super.handleMessage(msg);
        }
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public double div(int v1, int v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
