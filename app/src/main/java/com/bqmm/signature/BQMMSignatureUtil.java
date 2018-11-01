package com.bqmm.signature;

import com.bqmm.config.BQMMHttpConfig;
import com.omi.utils.MD5;

import java.util.List;
import java.util.Map;

/**
 * Created by SensYang on 2017/03/31 19:32
 */

public class BQMMSignatureUtil {
    public static String generateSignature(String path, Map<String, Object> parameters) {
        String fullurl = BQMMHttpConfig.MAIN_HOST + BQMMHttpConfig.VERSION + path + buildQueryString(parameters, true);
        return MD5.encode(fullurl).toUpperCase();
    }

    public static String buildQueryString(Map<String, Object> parameters, boolean sortKey) {
        if (parameters == null) {
            return "";
        }
        StringBuilder queryString = new StringBuilder();
        List<String> sortedKeyList;
        if (sortKey) {
            sortedKeyList = new java.util.ArrayList();
            sortedKeyList.addAll(parameters.keySet());
            java.util.Collections.sort(sortedKeyList);
            for (String key : sortedKeyList) {
                queryString.append(key).append("=").append((String) parameters.get(key)).append("&");
            }
        } else {
            for (String key : parameters.keySet()) {
                queryString.append(key).append("=").append((String) parameters.get(key)).append("&");
            }
        }
        queryString.delete(queryString.length() - 1, queryString.length());
        return queryString.toString();
    }
}
