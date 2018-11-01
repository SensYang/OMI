package com.bqmm.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by SensYang on 2017/04/01 22:56
 */

public class BQMMCheck extends BQMMBaseJson<BQMMCheck.Data> {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private int newEmojiCount;
        private String newEmojiCountTimestamp;

        public int getNewEmojiCount() {
            return newEmojiCount;
        }

        public void setNewEmojiCount(int newEmojiCount) {
            this.newEmojiCount = newEmojiCount;
        }

        public String getNewEmojiCountTimestamp() {
            return newEmojiCountTimestamp;
        }

        public void setNewEmojiCountTimestamp(String newEmojiCountTimestamp) {
            this.newEmojiCountTimestamp = newEmojiCountTimestamp;
        }
    }
}
