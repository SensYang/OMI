package com.omi.bean.account;

import com.omi.bean.base.ComparableBean;

/**
 * Created by SensYang on 2017/04/05 10:28
 */

public class Contact extends ComparableBean {
    private int contact_id;
    private String contact_name;
    private String phone;
    private String compareString;
    private boolean isOmiFriend = false;

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isOmiFriend() {
        return isOmiFriend;
    }

    public void setOmiFriend(boolean omiFriend) {
        isOmiFriend = omiFriend;
    }

    public void setCompareString(String compareString) {
        this.compareString = compareString;
    }

    @Override
    protected String getCompareString() {
        return compareString;
    }
}