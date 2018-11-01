package com.bqmm.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by SensYang on 2017/04/01 22:56
 */

public class BQMMToken extends BQMMBaseJson<BQMMToken.Data> {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        /**
         * access_token : 返回请求token
         * expires_in : 过期时间
         */
        private String access_token;
        private int expires_in;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }
    }
}
