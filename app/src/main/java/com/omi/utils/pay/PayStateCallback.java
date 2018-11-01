package com.omi.utils.pay;

/**
 * 支付结果回调接口
 * Created by SensYang on 2016/6/15 0015.
 */
public interface PayStateCallback {
    /**
     * 支付成功
     */
    void onPaySuccess(String describe);

    /**
     * 支付结果确认中
     */
    void onPayWatting(String describe);

    /**
     * 支付失败
     */
    void onPayFailed(String describe);
}
