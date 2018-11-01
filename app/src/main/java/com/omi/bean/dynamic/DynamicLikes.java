package com.omi.bean.dynamic;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by SensYang on 2017/04/20 18:40
 */

public class DynamicLikes {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data  implements Serializable {
        Integer discover_id; //赞所属动态ID
        Integer zan_id; //赞ID
        Integer member_id; //点赞人ID
        String member_name; //点赞人账号
        String member_nickname; //点赞人昵称

        public Integer getDiscover_id() {
            return discover_id;
        }

        public void setDiscover_id(Integer discover_id) {
            this.discover_id = discover_id;
        }

        public Integer getZan_id() {
            return zan_id;
        }

        public void setZan_id(Integer zan_id) {
            this.zan_id = zan_id;
        }

        public Integer getMember_id() {
            return member_id;
        }

        public void setMember_id(Integer member_id) {
            this.member_id = member_id;
        }

        public String getMember_name() {
            return member_name;
        }

        public void setMember_name(String member_name) {
            this.member_name = member_name;
        }

        public String getMember_nickname() {
            return member_nickname;
        }

        public void setMember_nickname(String member_nickname) {
            this.member_nickname = member_nickname;
        }
    }
}
