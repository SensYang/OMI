package com.omi.bean.base;

import android.databinding.BaseObservable;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by SensYang on 2016/6/3 0003.
 * 数据库表的基础类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseBean extends BaseObservable implements Serializable, Cloneable {
    // 设置为主键,自增
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column("_pid")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
