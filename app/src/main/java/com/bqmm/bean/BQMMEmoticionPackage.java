package com.bqmm.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by SensYang on 2017/04/01 23:03
 */

public class BQMMEmoticionPackage extends BQMMBaseJson<BQMMEmoticionPackage.Data> {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        /**
         * guid : 表情包的guid
         * name : 表情包名字
         * banner : 表情包的banner广告
         * intro : 表情包的简介
         * briefIntro : 简单的介绍信息
         * copyright : 表情包的版权
         * author : 表情包的作者
         * createtime : 创建时间
         * updatetime : 更新时间
         * type : static表情包类型
         * cover : 表情封面图
         * promotion : 1
         * preload : 等待下载时的灰色占位图
         * source : designer| japanese 来源
         * industry : im行业
         * show : 是否显示
         * chat_icon : 聊天面板图标
         * is_emoji : false
         * recommend_pic : 推荐图下载的图标
         * sale_area : 销售地区
         * show_copyright : 是否需要显示版权
         * is_show : false
         */

        private String guid;
        private String name;
        private String banner;
        private String intro;
        private String briefIntro;
        private String copyright;
        private String author;
        private long createtime;
        private long updatetime;
        private String type;
        private String cover;
        private int promotion;
        private String preload;
        private String source;
        private String industry;
        private boolean show;
        private String chat_icon;
        private boolean is_emoji;
        private String recommend_pic;
        private String sale_area;
        private boolean show_copyright;
        private boolean is_show;
        private List<BQMMEmoticion.Data> emoticions;

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getBriefIntro() {
            return briefIntro;
        }

        public void setBriefIntro(String briefIntro) {
            this.briefIntro = briefIntro;
        }

        public String getCopyright() {
            return copyright;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public long getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(long updatetime) {
            this.updatetime = updatetime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getPromotion() {
            return promotion;
        }

        public void setPromotion(int promotion) {
            this.promotion = promotion;
        }

        public String getPreload() {
            return preload;
        }

        public void setPreload(String preload) {
            this.preload = preload;
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

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public String getChat_icon() {
            return chat_icon;
        }

        public void setChat_icon(String chat_icon) {
            this.chat_icon = chat_icon;
        }

        public boolean is_emoji() {
            return is_emoji;
        }

        public void setIs_emoji(boolean is_emoji) {
            this.is_emoji = is_emoji;
        }

        public String getRecommend_pic() {
            return recommend_pic;
        }

        public void setRecommend_pic(String recommend_pic) {
            this.recommend_pic = recommend_pic;
        }

        public String getSale_area() {
            return sale_area;
        }

        public void setSale_area(String sale_area) {
            this.sale_area = sale_area;
        }

        public boolean isShow_copyright() {
            return show_copyright;
        }

        public void setShow_copyright(boolean show_copyright) {
            this.show_copyright = show_copyright;
        }

        public boolean is_show() {
            return is_show;
        }

        public void setIs_show(boolean is_show) {
            this.is_show = is_show;
        }

        public List<BQMMEmoticion.Data> getEmoticions() {
            return emoticions;
        }

        public void setEmoticions(List<BQMMEmoticion.Data> emoticions) {
            this.emoticions = emoticions;
        }
    }
}
