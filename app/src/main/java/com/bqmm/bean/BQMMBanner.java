package com.bqmm.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by SensYang on 2017/04/01 23:23
 */

public class BQMMBanner extends BQMMBaseJson<BQMMBanner.Data> {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        /**
         * guid : 类型id 3a4cc2ac6edd42319dcfc2d4785f7b2e
         * createtime : 创建时间
         * updatetime : 更新时间
         * isActive : 是否可以使用 true
         * appId :
         * sdkType : im
         * banner_name : 首页广告位
         * emoticion_packages : [{"guid":"表情包唯一ID"}]
         * code : recommend| preloads|index
         * category_name : 分类名称
         * sdk_type : im 接口类型
         */
        private String guid;
        private long createtime;
        private long updatetime;
        private boolean isActive;
        private String appId;
        private String sdkType;
        private String banner_name;
        private String code;
        private String category_name;
        private String sdk_type;
        private List<BQMMEmoticionPackage.Data> emoticion_packages;

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
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

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getSdkType() {
            return sdkType;
        }

        public void setSdkType(String sdkType) {
            this.sdkType = sdkType;
        }

        public String getBanner_name() {
            return banner_name;
        }

        public void setBanner_name(String banner_name) {
            this.banner_name = banner_name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getSdk_type() {
            return sdk_type;
        }

        public void setSdk_type(String sdk_type) {
            this.sdk_type = sdk_type;
        }

        public List<BQMMEmoticionPackage.Data> getEmoticion_packages() {
            return emoticion_packages;
        }

        public void setEmoticion_packages(List<BQMMEmoticionPackage.Data> emoticion_packages) {
            this.emoticion_packages = emoticion_packages;
        }
    }
}
