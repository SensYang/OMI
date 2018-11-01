package com.omi.bean.account;

import com.omi.bean.base.BaseJson;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by SensYang on 2017/04/03 18:47
 */

public class Member extends BaseJson<Member.Data> {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private String member_id;

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }
    }
}
