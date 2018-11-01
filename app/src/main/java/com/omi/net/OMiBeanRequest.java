package com.omi.net;

import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by SensYang on 2017/03/14 10:10
 */
public class OMiBeanRequest<T> extends BaseBeanRequest<T> {

    public OMiBeanRequest(Class<T> beanClass, String url) {
        this(beanClass, url, RequestMethod.GET);
    }

    public OMiBeanRequest(Class<T> beanClass, String url, RequestMethod requestMethod) {
        this(null, beanClass, url, requestMethod);
    }

    public OMiBeanRequest(Object sign, Class<T> beanClass, String url) {
        this(sign, beanClass, url, RequestMethod.GET);
    }

    public OMiBeanRequest(Object sign, Class<T> beanClass, String url, RequestMethod requestMethod) {
        super(sign, beanClass, HttpConfig.MAIN_HOST + url, requestMethod);
    }

}