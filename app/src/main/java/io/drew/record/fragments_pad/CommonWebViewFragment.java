package io.drew.record.fragments_pad;


import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.record.R;
import io.drew.record.base.BaseFragment;

public class CommonWebViewFragment extends BaseFragment {

    @BindView(R.id.wv)
    protected WebView wv;
    @BindView(R.id.relay_back)
    protected RelativeLayout relay_back;
    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.progress)
    protected ProgressBar progress;

    private String url;

    public CommonWebViewFragment(String url) {
        this.url = url;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        title.setText(getResources().getString(R.string.app_name));
        relay_back.setVisibility(View.GONE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            rule_view.getSettings().setSafeBrowsingEnabled(false);
//        }
        //重新加载 点击网页里面的链接还是在当前的webview里跳转。不跳到浏览器那边
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            // 重写此方法能够让webview处理https请求
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //Android 7.0 WebView 部分机型打不开
                //过滤掉 部分错误
                if (error.getPrimaryError() == SslError.SSL_DATE_INVALID
                        || error.getPrimaryError() == SslError.SSL_EXPIRED
                        || error.getPrimaryError() == SslError.SSL_INVALID
                        || error.getPrimaryError() == SslError.SSL_UNTRUSTED) {
                    handler.proceed();
                } else {
                    handler.cancel();
                }
                super.onReceivedSslError(view, handler, error);
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                MyLog.e("error=" + errorCode + "---" + description);
                if (errorCode == ERROR_HOST_LOOKUP) {
                    ToastManager.showDiyShort("网络连接出错，请检查网络");
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progress.setVisibility(View.GONE);
                }
            }
        });
        wv.getSettings().setJavaScriptEnabled(true);
        //自适应屏幕
        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.getSettings().setLoadWithOverviewMode(true);
        //设置可以支持缩放
        wv.getSettings().setSupportZoom(false);
        //扩大比例的缩放
        wv.getSettings().setUseWideViewPort(false);
        //设置是否出现缩放工具
        wv.getSettings().setBuiltInZoomControls(false);
        wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);//开启硬件加速
        wv.loadUrl(url);
    }

}
