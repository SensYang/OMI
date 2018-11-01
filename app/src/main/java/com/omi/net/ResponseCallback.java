package com.omi.net;

import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by SensYang on 2017/03/14 14:02
 */

public class ResponseCallback<T> implements OnResponseListener<T> {
    @Override
    public void onStart(int what) {

    }

    @Deprecated
    @Override
    public void onSucceed(int what, Response<T> response) {
        if (response != null) try {
            onSucceed(what, response.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSucceed(int what, T response) throws Exception {}

    @Deprecated
    @Override
    public void onFailed(int what, Response<T> response) {
        if (response != null) try {
            onFailed(what, response.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onFailed(int what, T response) throws Exception {

    }

    @Override
    public void onFinish(int what) {

    }
}
