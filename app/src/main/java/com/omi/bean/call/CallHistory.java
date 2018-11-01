package com.omi.bean.call;

import com.omi.bean.base.BaseBean;

/**
 * Created by SensYang on 2017/04/20 16:57
 */

public class CallHistory extends BaseBean {
    private String callName;
    private String callNumber;
    private String callTime;

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }
}
