package com.omi.ui.widget;

import android.content.Context;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import com.omi.ui.widget.window.WaringDialog;

import java.lang.reflect.Field;

/**
 * Created by SensYang on 2016/8/15 0015.
 */
public class OmiWebView extends WebView {
    private WaringDialog waringDialog;

    public OmiWebView(Context context) {
        this(context, null);
    }

    public OmiWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
        setWebViewClient(webViewClient);
    }

    public OmiWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context.getApplicationContext(), attrs, defStyleAttr);
        initWebView();
        setWebViewClient(webViewClient);
    }

    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (onLoadUrlListener != null) {
                onLoadUrlListener.onLoadUrl(url);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //接受证书
            handler.proceed();
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setWebViewClient(null);
        waringDialog = null;
        onLoadUrlListener = null;
    }

    private OnLoadUrlListener onLoadUrlListener;

    public void setOnLoadUrlListener(OnLoadUrlListener onLoadUrlListener) {
        this.onLoadUrlListener = onLoadUrlListener;
    }

    public interface OnLoadUrlListener {
        void onLoadUrl(String url);
    }

    /**
     * 初始化网页
     */
    private void initWebView() {
        WebSettings settings = getSettings();
        settings.setDefaultTextEncodingName("UTF-8");//设置默认为utf-8
        //启用支持javascript
        settings.setJavaScriptEnabled(true);
        //不使用缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //自适应手机屏幕大小
        settings.setUseWideViewPort(true);
        /*Android 4.4 以上的版本的webview的内核改了，由webkit改为chromium*/
        if (VersionUtil.getAndroidVersion() < 19) {
            // 自适应屏幕
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        } else {
            // 扩大比例的缩放
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
        }
        setZoomControlGone();
    }

    /**
     * 高版本取消网页的放大按钮
     */
    private void setZoomControlGone() {
        try {
            Class classType = WebView.class;
            Field field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(this);
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            field.set(this, mZoomButtonsController);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}