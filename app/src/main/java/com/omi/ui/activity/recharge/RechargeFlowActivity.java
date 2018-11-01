package com.omi.ui.activity.recharge;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.omi.R;
import com.omi.bean.recharge.Flow;
import com.omi.databinding.ActivityRechargeFlowBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.JsonCallback;
import com.omi.net.ResponseCallback;
import com.omi.ui.adapter.recharge.RechargeFlowAdapter;
import com.omi.ui.base.BaseActivity;
import com.omi.ui.widget.WatingDialog;
import com.omi.utils.CommonUtils;
import com.omi.utils.ToastUtil;
import com.omi.utils.pay.AliPayUtil;
import com.omi.utils.pay.PayStateCallback;

import org.json.JSONObject;

/**
 * Created by SensYang on 2017/04/28 17:51
 */

public class RechargeFlowActivity extends BaseActivity {
    ActivityRechargeFlowBinding binding;
    private String cardNum;
    private RechargeFlowAdapter adapter;
    private WatingDialog watingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recharge_flow);
        binding.setRechargeFlowActivity(this);
        adapter = new RechargeFlowAdapter();
        binding.setAdapter(adapter);
        ApiByHttp.getInstance().getFlowList(new ResponseCallback<Flow>() {
            @Override
            public void onSucceed(int what, Flow response) throws Exception {
                if (response == null || response.getDatalist() == null) return;
                adapter.addFlow(response.getDatalist());
                getHandler().sendEmptyMessage(0);
            }
        });
        watingDialog = new WatingDialog(this);
        watingDialog.setAnim(R.drawable.wating_anim);
        watingDialog.setContent("正在支付，请稍等...");
        watingDialog.setCancelable(false);
    }

    @Override
    protected void handlerPacketMsg(Message msg) {
        adapter.notifyDataSetChanged();
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        if (cardNum.equalsIgnoreCase(this.cardNum)) return;
        this.cardNum = cardNum;
    }

    @Override
    public void topLeftClick(View view) {
        finish();
    }


    public void rechargeClick(View view) {
        if (watingDialog.isShowing()) return;
        if (CommonUtils.isFastDoubleClick()) return;
        if (cardNum == null || cardNum.length() == 0) {
            ToastUtil.showToast("请输入卡号");
            return;
        }
        Flow.Data flow = adapter.getSelectFlow();
        if (flow != null) {
            watingDialog.showDelayed(10 * 1000);
            ApiByHttp.getInstance().rechargeFlow(flow.getMoney(), cardNum, flow.getKind(), callback);
        } else {
            ToastUtil.showToast("请选择流量包");
        }
    }

    private JsonCallback callback = new JsonCallback() {
        @Override
        public void onSucceed(int what, JSONObject response) throws Exception {
            watingDialog.dismiss();
            if (response.getInt("result") == 200) {
                if (response.getJSONObject("data") != null) {
                    String orderstr = response.getJSONObject("data").getString("orderstr");
                    if (orderstr != null)
                        AliPayUtil.getInstance(RechargeFlowActivity.this, payStateCallback).startPay(orderstr);
                }
            } else {
                ToastUtil.showToast(response.getString("msg"));
            }
        }

        @Override
        public void onFailed(int what, JSONObject response) throws Exception {
            ToastUtil.showToast("网络错误，请稍后重试");
            watingDialog.dismiss();
        }
    };

    private PayStateCallback payStateCallback = new PayStateCallback() {
        @Override
        public void onPaySuccess(String describe) {
            ToastUtil.showToast("支付成功");
        }

        @Override
        public void onPayWatting(String describe) {

        }

        @Override
        public void onPayFailed(String describe) {
            ToastUtil.showToast("支付失败");
        }
    };

}
