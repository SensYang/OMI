package com.omi.utils.pay;

import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.omi.R;
import com.omi.utils.CommonUtils;


/**
 * Created by SensYang on 2016/6/15 0015.
 */
public class AliPayUtil {
    private static AliPayUtil instance;
    //支付结果回调接口
    private PayStateCallback payStateCallback;
    private Activity activity;

    /**
     * 设置支付结果监听
     */
    public AliPayUtil setPayStateCallback(PayStateCallback payStateCallback) {
        this.payStateCallback = payStateCallback;
        return this;
    }

    private AliPayUtil() {
    }

    public static AliPayUtil getInstance(Activity activity, PayStateCallback payStateCallback) {
        if (instance == null) instance = new AliPayUtil();
        instance.setActivity(activity);
        instance.setPayStateCallback(payStateCallback);
        return instance;
    }

    private void setActivity(Activity activity) {
        this.activity = activity;
    }

    /////////////////////////////

    /**
     * 调用支付宝SDK支付
     *
     * @param orderstr 编码后的订单信息
     */
    public void startPay(String orderstr) {
        if (CommonUtils.isFastDoubleClick()) return;
        new PayThread(orderstr).start();
    }

    class PayThread extends Thread {
        String orderstr;

        public PayThread(String orderstr) {
            this.orderstr = orderstr;
        }

        @Override
        public void run() {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(activity);
            String result = alipay.pay(orderstr, true);
            // 调用支付接口，获取支付结果
            payStateCallBack(result);
        }
    }

    private void payStateCallBack(String result) {
        AliPayResult aliPayResult = new AliPayResult(result);
        /**
         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看
         * https://doc.open.alipay.com/doc2/detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&docType=1
         * ) 建议商户依赖异步通知
         *  服务端验证安全
         */
        //        String resultInfo = aliPayResult.getResult();// 同步返回需要验证的信息

        String resultStatus = aliPayResult.getResultStatus();
        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
        if (TextUtils.equals(resultStatus, "9000")) {
            //ToastUtil.showToast("支付成功");
            if (payStateCallback != null) {
                payStateCallback.onPaySuccess(activity.getString(R.string.pay_success));
            }
        } else {
            // 判断resultStatus 为非"9000"则代表可能支付失败
            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
                //ToastUtil.showToast("支付结果确认中");
                if (payStateCallback != null) {
                    payStateCallback.onPayWatting(activity.getString(R.string.pay_waiting));
                }
            } else if (TextUtils.equals(resultStatus, "6001")) {
                //ToastUtil.showToast("支付结果确认中");
                if (payStateCallback != null) {
                    payStateCallback.onPayFailed(activity.getString(R.string.pay_cancle));
                }
            } else if (TextUtils.equals(resultStatus, "4000")) {
                if (payStateCallback != null) {
                    payStateCallback.onPayFailed(activity.getString(R.string.none_ali));
                }
            } else {
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                //ToastUtil.showToast("支付失败");
                if (payStateCallback != null) {
                    payStateCallback.onPayFailed(activity.getString(R.string.pay_fail));
                }
            }
        }
    }
}