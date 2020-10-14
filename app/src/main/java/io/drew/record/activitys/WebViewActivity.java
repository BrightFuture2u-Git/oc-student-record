package io.drew.record.activitys;


import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.BindView;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.record.R;
import io.drew.record.base.BaseActivity;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.wv)
    protected WebView wv;
    @BindView(R.id.progress)
    protected ProgressBar progress;
    private String url;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initData() {
        url = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(url)) finish();
    }

    @Override
    protected void initView() {
        initActionBar(getResources().getString(R.string.app_name), true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient() {
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
                    finish();
                    ToastManager.showDiyShort("网络连接出错，请检查网络");
                }
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progress.setVisibility(View.VISIBLE);
            }
            @Override
            public void onLoadResource(WebView view, String url) {
                if (url.contains("jsbridge://exitWeb")) {
                    finish();
                }
                super.onLoadResource(view, url);
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
