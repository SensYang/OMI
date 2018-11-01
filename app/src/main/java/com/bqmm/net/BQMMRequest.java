package com.bqmm.net;

import android.os.Build;

import com.bqmm.config.BQMMConfig;
import com.bqmm.config.BQMMHttpConfig;
import com.bqmm.signature.BQMMSignatureUtil;
import com.omi.net.JsonUtils;
import com.omi.utils.Log;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.RestRequest;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by SensYang on 2017/04/01 22:40
 */

public class BQMMRequest<T> extends RestRequest<T> {
    private final Class<T> beanClass;
    private String data;

    public static <T> BQMMRequest getInstance(Class<T> beanClass, String url) {
        return new BQMMRequest(beanClass, getUrl(url), RequestMethod.GET);
    }

    public static <T> BQMMRequest getInstance(Class<T> beanClass, String url, RequestMethod requestMethod) {
        return new BQMMRequest(beanClass, getUrl(url), requestMethod);
    }

    private BQMMRequest(Class<T> beanClass, String url, RequestMethod requestMethod) {
        super(url, requestMethod);
        this.beanClass = beanClass;
        setContentType(Headers.HEAD_VALUE_ACCEPT_APPLICATION_JSON);
        setAccept(Headers.HEAD_VALUE_ACCEPT_APPLICATION_JSON);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public void onWriteRequestBody(OutputStream writer) throws IOException {
        if (data != null) {
            byte[] stringData = data.getBytes();
            setHeader(Headers.HEAD_KEY_CONTENT_LENGTH, String.valueOf(stringData.length));
            writer.write(stringData);
            writer.flush();
        }
        super.onWriteRequestBody(writer);
    }

    @Override
    public T parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        String result = StringRequest.parseResponseString(responseHeaders, responseBody);
        Log.d(result);
        return JsonUtils.parserJson2Bean(result, beanClass);
    }

    private static String getUrl(String path) {
        return BQMMHttpConfig.MAIN_HOST + BQMMHttpConfig.VERSION + path + getUrlParam(path);
    }

    private static String getUrlParam(String path) {
        String os = "Android" + Build.VERSION.RELEASE;
        ParamMap parameters = new ParamMap();
        parameters.put("access_token", token);
        parameters.put("device_no", UUID.randomUUID().toString());
        parameters.put("os", os);
        parameters.put("app_id", BQMMConfig.BQMM_APP_ID);
        parameters.put("sdk_version", "1.7.8");
        parameters.put("provider", "sdk");
        parameters.put("action", action);
        parameters.put("region", null);
        parameters.put("timestamp", String.valueOf(System.currentTimeMillis()));
        parameters.put("signature", BQMMSignatureUtil.generateSignature(path, parameters.getParamMap()));
        return parameters.toParamString();
    }

    private static String token;
    private static String action;

    public static void setToken(String token) {
        BQMMRequest.token = token;
    }

    public static void setAction(String action) {
        BQMMRequest.action = action;
    }
}