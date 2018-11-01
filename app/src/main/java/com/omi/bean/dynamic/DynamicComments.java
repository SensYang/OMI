package com.omi.bean.dynamic;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by SensYang on 2017/04/20 18:40
 */

public class DynamicComments {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data  implements Serializable {
        Integer be_commented_member_id; //被评论者ID
        Integer be_commented_member_name; //被评论者账号
        String be_commented_member_nickname; //被评论者昵称
        Integer be_commented_text_id; //被评论动态ID
        Integer comment_id; //评论ID
        String comment_info; //评论内容
        Integer discover_id; //评论所属动态ID
        Integer member_id; //评论者ID
        String member_name; //评论者账号
        String member_nickname; //评论者昵称

        public Integer getBe_commented_member_id() {
            return be_commented_member_id;
        }

        public void setBe_commented_member_id(Integer be_commented_member_id) {
            this.be_commented_member_id = be_commented_member_id;
        }

        public Integer getBe_commented_member_name() {
            return be_commented_member_name;
        }

        public void setBe_commented_member_name(Integer be_commented_member_name) {
            this.be_commented_member_name = be_commented_member_name;
        }

        public String getBe_commented_member_nickname() {
            return be_commented_member_nickname;
        }

        public void setBe_commented_member_nickname(String be_commented_member_nickname) {
            this.be_commented_member_nickname = be_commented_member_nickname;
        }

        public Integer getBe_commented_text_id() {
            return be_commented_text_id;
        }

        public void setBe_commented_text_id(Integer be_commented_text_id) {
            this.be_commented_text_id = be_commented_text_id;
        }

        public Integer getComment_id() {
            return comment_id;
        }

        public void setComment_id(Integer comment_id) {
            this.comment_id = comment_id;
        }

        public String getComment_info() {
            return comment_info;
        }

        public void setComment_info(String comment_info) {
            this.comment_info = comment_info;
        }

        public Integer getDiscover_id() {
            return discover_id;
        }

        public void setDiscover_id(Integer discover_id) {
            this.discover_id = discover_id;
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
