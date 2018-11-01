package com.bqmm.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

/**
 * Created by SensYang on 2017/04/01 23:06
 */

public class BQMMKeyword extends BQMMBaseJson<BQMMKeyword.Data> {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        /**
         * update : 1
         * base_url : gif存储的基础位置
         */
        private int update;
        private String base_url;
        private Map<String, List<String>> data_list;

        public int getUpdate() {
            return update;
        }

        public void setUpdate(int update) {
            this.update = update;
        }

        public String getBase_url() {
            return base_url;
        }

        public void setBase_url(String base_url) {
            this.base_url = base_url;
        }

        public Map<String, List<String>> getData_list() {
            return data_list;
        }

        public void setData_list(Map<String, List<String>> data_list) {
            this.data_list = data_list;
        }
    }
}
