package com.omi.bean.dynamic;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.omi.BR;
import com.omi.bean.base.BaseJson;
import com.omi.net.ApiByHttp;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SensYang on 2017/04/20 18:35
 */

public class Dynamic extends BaseJson<Dynamic.Data> {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data extends BaseObservable implements Serializable {
        private String member_id; //动态所属人 ID
        private String fbz_member_id; //动态发布人 ID
        private String fbz_member_avatar; //动态发布人头像
        private String fbz_member_name; //动态发布人账号
        private String fbz_member_nickname; //动态发布人昵称
        private String release_areaInfo; //动态发布地区
        private String release_textInfo; //动态内容
        private String release_time; //动态发布时间
        private Integer discover_didForward; //当前动态是否为转发
        private Integer discover_haveImage; //当前动态是否有图片
        private String discover_id; //动态ID
        private String discover_text_type; //动态文本类型
        private String be_forwarded_discover_id; //被转发动态 ID
        private String be_forwarded_member_avatar; //被转发动态发布者头像
        private String be_forwarded_member_id; //被转发动态发布者 id
        private String be_forwarded_member_name; //被转发动态发布者账号
        private String be_forwarded_member_nickname; //被转发动态发布者昵称
        private String be_forwarded_textInfo; //被转发动态内容
        private List<DynamicImages.Data> images; //动态图片
        private List<DynamicComments.Data> pinglun; //动态评论
        private List<DynamicLikes.Data> zan; //动态点赞

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getFbz_member_id() {
            return fbz_member_id;
        }

        public void setFbz_member_id(String fbz_member_id) {
            this.fbz_member_id = fbz_member_id;
        }

        public String getFbz_member_avatar() {
            return fbz_member_avatar;
        }

        public void setFbz_member_avatar(String fbz_member_avatar) {
            this.fbz_member_avatar = fbz_member_avatar;
        }

        public String getFbz_member_name() {
            return fbz_member_name;
        }

        public void setFbz_member_name(String fbz_member_name) {
            this.fbz_member_name = fbz_member_name;
        }

        public String getFbz_member_nickname() {
            return fbz_member_nickname;
        }

        public void setFbz_member_nickname(String fbz_member_nickname) {
            this.fbz_member_nickname = fbz_member_nickname;
        }

        public String getRelease_areaInfo() {
            return release_areaInfo;
        }

        public void setRelease_areaInfo(String release_areaInfo) {
            this.release_areaInfo = release_areaInfo;
        }

        public String getRelease_textInfo() {
            return release_textInfo;
        }

        public void setRelease_textInfo(String release_textInfo) {
            this.release_textInfo = release_textInfo;
        }

        public String getRelease_time() {
            return release_time;
        }

        public void setRelease_time(String release_time) {
            this.release_time = release_time;
        }

        public int getDiscover_didForward() {
            return discover_didForward;
        }

        public void setDiscover_didForward(int discover_didForward) {
            this.discover_didForward = discover_didForward;
        }

        public int getDiscover_haveImage() {
            return discover_haveImage;
        }

        public void setDiscover_haveImage(int discover_haveImage) {
            this.discover_haveImage = discover_haveImage;
        }

        public String getDiscover_id() {
            return discover_id;
        }

        public void setDiscover_id(String discover_id) {
            this.discover_id = discover_id;
        }

        public String getDiscover_text_type() {
            return discover_text_type;
        }

        public void setDiscover_text_type(String discover_text_type) {
            this.discover_text_type = discover_text_type;
        }

        public String getBe_forwarded_discover_id() {
            return be_forwarded_discover_id;
        }

        public void setBe_forwarded_discover_id(String be_forwarded_discover_id) {
            this.be_forwarded_discover_id = be_forwarded_discover_id;
        }

        public String getBe_forwarded_member_avatar() {
            return be_forwarded_member_avatar;
        }

        public void setBe_forwarded_member_avatar(String be_forwarded_member_avatar) {
            this.be_forwarded_member_avatar = be_forwarded_member_avatar;
        }

        public String getBe_forwarded_member_id() {
            return be_forwarded_member_id;
        }

        public void setBe_forwarded_member_id(String be_forwarded_member_id) {
            this.be_forwarded_member_id = be_forwarded_member_id;
        }

        public String getBe_forwarded_member_name() {
            return be_forwarded_member_name;
        }

        public void setBe_forwarded_member_name(String be_forwarded_member_name) {
            this.be_forwarded_member_name = be_forwarded_member_name;
        }

        public String getBe_forwarded_member_nickname() {
            return be_forwarded_member_nickname;
        }

        public void setBe_forwarded_member_nickname(String be_forwarded_member_nickname) {
            this.be_forwarded_member_nickname = be_forwarded_member_nickname;
        }

        public String getBe_forwarded_textInfo() {
            return be_forwarded_textInfo;
        }

        public void setBe_forwarded_textInfo(String be_forwarded_textInfo) {
            this.be_forwarded_textInfo = be_forwarded_textInfo;
        }

        public List<DynamicImages.Data> getImages() {
            return images;
        }

        public void setImages(List<DynamicImages.Data> images) {
            this.images = images;
        }

        @Bindable
        public List<DynamicComments.Data> getPinglun() {
            return pinglun;
        }

        public void setPinglun(List<DynamicComments.Data> pinglun) {
            this.pinglun = pinglun;
        }

        @Bindable
        public List<DynamicLikes.Data> getZan() {
            return zan;
        }

        public void setZan(List<DynamicLikes.Data> zan) {
            this.zan = zan;
            notifyPropertyChanged(BR.zan);
            notifyPropertyChanged(BR.zanNames);
            notifyPropertyChanged(BR.hasPraise);
        }


        private boolean isShowAll = false;

        @Bindable
        public boolean isShowAll() {
            return isShowAll;
        }

        public void setShowAll(boolean showAll) {
            isShowAll = showAll;
            notifyPropertyChanged(BR.showAll);
        }

        public String getTimeWithLocation() {
            return release_time + "  " + release_areaInfo;
        }

        /**
         * 是否赞过该评论
         */
        @Bindable
        public boolean isHasPraise() {
            for (DynamicLikes.Data data : zan) {
                if (ApiByHttp.getInstance().getMember_id().equalsIgnoreCase(data.getMember_id() + ""))
                    return true;
            }
            return false;
        }

        @Bindable
        public String getZanNames() {
            StringBuilder sb = new StringBuilder();
            boolean isStart = true;
            for (DynamicLikes.Data data : zan) {
                if (isStart) {
                    sb.append(data.getMember_nickname());
                } else {
                    sb.append("、" + data.getMember_nickname());
                }
                isStart = false;
            }
            if (sb.length() == 0) return null;
            return sb.toString();
        }
    }
}