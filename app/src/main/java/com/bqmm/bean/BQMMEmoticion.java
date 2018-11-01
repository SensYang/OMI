package com.bqmm.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by SensYang on 2017/04/01 23:17
 */

public class BQMMEmoticion extends BQMMBaseJson<BQMMEmoticion.Data> {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        /**
         * guid : 当前表情的位置id
         * thumbail : 表情的缩略图
         * tags :
         * keyword :
         * source : 表情来源japanese
         * industry :
         * emo_text : 表情的 key 文字
         * emo_code : 表情的code
         * main_image : 表情的gif地址
         * is_emoji : false
         * display_order : 0
         * package_id : 该表情所属表情包的guid
         * sdk_type :
         * is_trend :
         */

        private String guid;
        private String thumbail;
        private String tags;
        private String keyword;
        private String source;
        private String industry;
        private String emo_text;
        private String emo_code;
        private String main_image;
        private boolean is_emoji;
        private int display_order;
        private String package_id;
        private String sdk_type;
        private boolean is_trend;

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getThumbail() {
            return thumbail;
        }

        public void setThumbail(String thumbail) {
            this.thumbail = thumbail;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getEmo_text() {
            return emo_text;
        }

        public void setEmo_text(String emo_text) {
            this.emo_text = emo_text;
        }

        public String getEmo_code() {
            return emo_code;
        }

        public void setEmo_code(String emo_code) {
            this.emo_code = emo_code;
        }

        public String getMain_image() {
            return main_image;
        }

        public void setMain_image(String main_image) {
            this.main_image = main_image;
        }

        public boolean isIs_emoji() {
            return is_emoji;
        }

        public void setIs_emoji(boolean is_emoji) {
            this.is_emoji = is_emoji;
        }

        public int getDisplay_order() {
            return display_order;
        }

        public void setDisplay_order(int display_order) {
            this.display_order = display_order;
        }

        public String getPackage_id() {
            return package_id;
        }

        public void setPackage_id(String package_id) {
            this.package_id = package_id;
        }

        public String getSdk_type() {
            return sdk_type;
        }

        public void setSdk_type(String sdk_type) {
            this.sdk_type = sdk_type;
        }

        public boolean is_emoji() {
            return is_emoji;
        }

        public boolean is_trend() {
            return is_trend;
        }

        public void setIs_trend(boolean is_trend) {
            this.is_trend = is_trend;
        }
    }
}
