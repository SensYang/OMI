package com.omi.net;

import com.omi.utils.Log;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONObject;

/**
 * Created by SensYang on 2017/04/16 8:14
 */

public class JsonRequest extends BaseBeanRequest<JSONObject> {

    public JsonRequest(String url) {
        super(JSONObject.class, url);
    }

    public JsonRequest(String url, RequestMethod requestMethod) {
        super(JSONObject.class, url, requestMethod);
    }

    public JsonRequest(Object sign, String url) {
        super(sign, JSONObject.class, url);
    }

    public JsonRequest(Object sign, String url, RequestMethod requestMethod) {
        super(sign, JSONObject.class, url, requestMethod);
    }

    @Override
    public JSONObject parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        String jsonStr = StringRequest.parseResponseString(responseHeaders, responseBody);
        Log.d(jsonStr);
        return new JSONObject(jsonStr);
    }
}
