package com.bqmm.net;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by SensYang on 2016/7/5 0005.
 */
public class ParamMap {
    LinkedHashMap<String, Object> paramMap;

    public ParamMap() {
        paramMap = new LinkedHashMap<>();
    }

    public ParamMap put(String name, Object value) {
        if (value == null) {
            paramMap.remove(name);
            return this;
        }
        if (value instanceof Number) {
            // deviate from the original by checking all Numbers, not just floats & doubles
            checkDouble(((Number) value).doubleValue());
        }
        paramMap.put(checkName(name), value);
        return this;
    }

    private double checkDouble(double d) throws NumberFormatException {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new NumberFormatException("Forbidden numeric value: " + d);
        }
        return d;
    }

    private String checkName(String name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException("Name must be non-null");
        }
        return name;
    }

    public String toJsonString() {
        try {
            JSONStringer stringer = new JSONStringer();
            writeTo(stringer);
            return stringer.toString();
        } catch (JSONException e) {
            return null;
        }
    }

    void writeTo(JSONStringer stringer) throws JSONException {
        stringer.object();
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            stringer.key(entry.getKey()).value(entry.getValue());
        }
        stringer.endObject();
    }

    /**
     * 默认返回jsonString
     */
    @Deprecated
    @Override
    public String toString() {
        return toJsonString();
    }

    public String toParamString() {
        StringBuilder stringBuffer = new StringBuilder();
        boolean isFirst = true;
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            if (isFirst) {
                stringBuffer.append("?");
                isFirst = false;
            } else {
                stringBuffer.append("&");
            }
            stringBuffer.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return stringBuffer.toString();
    }

    public HashMap<String, Object> getParamMap() {
        return paramMap;
    }
}
