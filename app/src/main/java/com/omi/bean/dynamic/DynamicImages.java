package com.omi.bean.dynamic;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by SensYang on 2017/04/20 18:40
 */

public class DynamicImages {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data  implements Serializable {
        Integer discover_id; //动态图片所属动态ID
        Integer image_id; //动态图片ID
        String image_name; //动态图片名称

        public Integer getDiscover_id() {
            return discover_id;
        }

        public void setDiscover_id(Integer discover_id) {
            this.discover_id = discover_id;
        }

        public Integer getImage_id() {
            return image_id;
        }

        public void setImage_id(Integer image_id) {
            this.image_id = image_id;
        }

        public String getImage_name() {
            return image_name;
        }

        public void setImage_name(String image_name) {
            this.image_name = image_name;
        }
    }
}
