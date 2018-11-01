package com.omi.net;

import com.omi.R;
import com.omi.utils.ToastUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.tools.NetUtil;

/**
 * Created by SensYang on 2017/03/14 9:13
 */

public class NoHttpUtil {
    private RequestQueue requestQueue;
    private static NoHttpUtil instance;

    public static NoHttpUtil getInstance() {
        if (instance == null) {
            instance = new NoHttpUtil();
        }
        return instance;
    }

    public <T> void add(Request<T> request) {
        add(-1, request);
    }

    public <T> void add(int what, Request<T> request) {
        add(what, request, null);
    }

    public <T> void add(Request<T> request, OnResponseListener<T> responseListener) {
        add(-1, request, responseListener);
    }

    public <T> void add(int what, Request<T> request, OnResponseListener<T> responseListener) {
        if (NetUtil.isNetworkAvailable())
            requestQueue.add(what, request, responseListener);
        else ToastUtil.showToast(R.string.none_net_availabel);
    }

    private NoHttpUtil() {
        requestQueue = NoHttp.newRequestQueue(3);
    }

    public void cancel(Object sign) {
        requestQueue.cancelBySign(sign);
    }
}
