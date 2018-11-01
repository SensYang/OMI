package com.omi.ui.activity.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;

import com.omi.R;
import com.omi.ui.base.BaseActivity;
import com.omi.ui.widget.OmiWebView;
import com.omi.ui.widget.TopNavigationBar;

import java.lang.reflect.Field;


/**
 * Created by SensYang on 2016/8/15 0015.
 * <br/>url &nbsp&nbsp&nbsp&nbsp &nbsp&nbsp&nbsp&nbsp &nbsp&nbsp&nbsp&nbsp &nbsp&nbsp&nbsp&nbsp 地址
 * <br/>webdetail &nbsp&nbsp&nbsp&nbsp &nbsp&nbsp&nbsp&nbsp html内容
 * <br/>title &nbsp&nbsp&nbsp&nbsp &nbsp&nbsp&nbsp&nbsp &nbsp&nbsp&nbsp&nbsp &nbsp&nbsp&nbsp 标题
 * <br/>topcolor &nbsp&nbsp&nbsp&nbsp &nbsp&nbsp&nbsp&nbsp &nbsp&nbsp 顶部状态栏颜色
 */
public class WebDetailActivity extends BaseActivity {
    TopNavigationBar topNavigationBar;
    OmiWebView webView;
    private String rightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String url = getIntent().getStringExtra("url");
        String webdetail = getIntent().getStringExtra("webdetail");
        if (url == null && webdetail == null) {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        setConfigCallback((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        String title = getIntent().getStringExtra("title");
        int topcolor = getIntent().getIntExtra("topcolor", 0);
        rightText = getIntent().getStringExtra("righttext");
        setContentView(R.layout.activity_web_detail);
        topNavigationBar = (TopNavigationBar) findViewById(R.id.topNavigationBar);
        webView = (OmiWebView) findViewById(R.id.webView);
        topNavigationBar.findViewById(R.id.rightLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightText != null) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        topNavigationBar.findViewById(R.id.leftIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                    checkWebView();
                } else {
                    finish();
                }
            }
        });
        topNavigationBar.findViewById(R.id.leftTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topNavigationBar.getLeftTV().setVisibility(View.GONE);
        if (topcolor != 0) {
            topNavigationBar.setBackgroundColor(topcolor);
        }
        if (title != null) {
            topNavigationBar.setCenterText(title);
        }
        if (rightText != null) {
            topNavigationBar.setRightText(rightText);
        }
        if (url != null) webView.loadUrl(url);
        else if (webdetail != null) webView.loadData(webdetail, "text/html; charset=UTF-8", null);
        webView.setLongClickable(false);
        webView.setOnLoadUrlListener(new OmiWebView.OnLoadUrlListener() {
            @Override
            public void onLoadUrl(String url) {
                checkWebView();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            checkWebView();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);
            if (null == configCallback) {
                return;
            }
            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onDestroy() {
        setConfigCallback(null);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void checkWebView() {
        if (webView.canGoBack()) {
            topNavigationBar.getLeftTV().setVisibility(View.VISIBLE);
        } else {
            topNavigationBar.getLeftTV().setVisibility(View.GONE);
        }
    }
}