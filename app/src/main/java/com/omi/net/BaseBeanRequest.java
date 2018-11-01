package com.omi.net;

import com.omi.utils.Log;
import com.yanzhenjie.nohttp.Binary;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.RestRequest;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;

/**
 * Created by SensYang on 2017/03/14 10:10
 */
public class BaseBeanRequest<T> extends RestRequest<T> {
    private final Class<T> beanClass;
    private String data;

    public BaseBeanRequest(Class<T> beanClass, String url) {
        this(beanClass, url, RequestMethod.GET);
    }

    public BaseBeanRequest(Class<T> beanClass, String url, RequestMethod requestMethod) {
        this(url, beanClass, url, requestMethod);
    }

    public BaseBeanRequest(Object sign, Class<T> beanClass, String url) {
        this(sign, beanClass, url, RequestMethod.GET);
    }

    public BaseBeanRequest(Object sign, Class<T> beanClass, String url, RequestMethod requestMethod) {
        super(url, requestMethod);
        this.beanClass = beanClass;
        setCancelSign(sign);
        if (url.startsWith(HttpConfig.MAIN_HOST)) {
            setSSLSocketFactory(SSLHelper.getDoubleSSLCertifcation());
            setHostnameVerifier(SSLHelper.verifier);
        }
        if (requestMethod == RequestMethod.POST && url.endsWith(".php")) {
            setContentType("application/x-www-form-urlencoded");
        }
    }

    @Override
    public T parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        String result = StringRequest.parseResponseString(responseHeaders, responseBody);
        Log.d(result);
        return JsonUtils.parserJson2Bean(result, beanClass);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public final void addParam(Object param) {
        try {
            Field[] fields = param.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(param);
                if (value != null) {
                    if (value instanceof Integer) {
                        add(field.getName(), (Integer) value);
                    } else if (value instanceof Long) {
                        add(field.getName(), (Long) value);
                    } else if (value instanceof Boolean) {
                        add(field.getName(), (Boolean) value);
                    } else if (value instanceof Character) {
                        add(field.getName(), (Character) value);
                    } else if (value instanceof Double) {
                        add(field.getName(), (Double) value);
                    } else if (value instanceof Float) {
                        add(field.getName(), (Float) value);
                    } else if (value instanceof Short) {
                        add(field.getName(), (Short) value);
                    } else if (value instanceof Byte) {
                        add(field.getName(), (Byte) value);
                    } else if (value instanceof String) {
                        add(field.getName(), (String) value);
                    } else if (value instanceof Binary) {
                        add(field.getName(), (Binary) value);
                    } else if (value instanceof File) {
                        String fileName = ((File) value).getName();
                        addHeader(Headers.HEAD_KEY_CONTENT_DISPOSITION, "form-data;name=\"" + fileName + "\";fileName=\"" + fileName + "\"");
                        add(field.getName(), (File) value);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
}