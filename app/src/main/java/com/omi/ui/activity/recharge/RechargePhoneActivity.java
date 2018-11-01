package com.omi.ui.activity.recharge;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.omi.BR;
import com.omi.R;
import com.omi.databinding.ActivityRechargePhoneBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.JsonCallback;
import com.omi.ui.base.BaseActivity;
import com.omi.ui.widget.WatingDialog;
import com.omi.utils.CommonUtils;
import com.omi.utils.ContactsUtil;
import com.omi.utils.ToastUtil;
import com.omi.utils.pay.AliPayUtil;
import com.omi.utils.pay.PayStateCallback;

import org.json.JSONObject;

/**
 * Created by SensYang on 2017/04/28 16:46
 */

public class RechargePhoneActivity extends BaseActivity {
    private final int REQUEST_SELECT_CONTACE = 0x25;
    ActivityRechargePhoneBinding binding;
    private String phoneNum;
    private String inputMoney;
    private int selectAt = -1;
    private WatingDialog watingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recharge_phone);
        phoneNum = ApiByHttp.getInstance().getPhone();
        binding.setRechargePhoneActivity(this);
        watingDialog = new WatingDialog(this);
        watingDialog.setAnim(R.drawable.wating_anim);
        watingDialog.setContent("正在支付，请稍等...");
        watingDialog.setCancelable(false);
    }

    @Override
    public void topLeftClick(View view) {
        finish();
    }

    public void selectContact(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_SELECT_CONTACE);
    }

    public void rechargeClick(View view) {
        if (watingDialog.isShowing()) return;
        if (CommonUtils.isFastDoubleClick()) return;
        if (inputMoney == null || inputMoney.length() == 0 || inputMoney.equalsIgnoreCase("0")) {
            ToastUtil.showToast(R.string.wrong_money);
            return;
        }
        if (!ContactsUtil.isMobileNO(phoneNum)) {
            ToastUtil.showToast(R.string.wrong_phone);
            return;
        }
        watingDialog.showDelayed(10 * 1000);
        ApiByHttp.getInstance().rechargePhone(inputMoney, phoneNum, callback);
    }

    private JsonCallback callback = new JsonCallback() {
        @Override
        public void onSucceed(int what, JSONObject response) throws Exception {
            watingDialog.dismiss();
            if (response.getInt("result") == 200) {
                if (response.getJSONObject("data") != null) {
                    String orderstr = response.getJSONObject("data").getString("orderstr");
                    if (orderstr != null)
                        AliPayUtil.getInstance(RechargePhoneActivity.this, payStateCallback).startPay(orderstr);
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

    @Bindable
    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        if (phoneNum.equalsIgnoreCase(this.phoneNum)) return;
        this.phoneNum = phoneNum;
    }

    @Bindable
    public String getInputMoney() {
        return inputMoney;
    }

    public void setInputMoney(String inputMoney) {
        if (inputMoney.equalsIgnoreCase(this.inputMoney)) return;
        this.inputMoney = inputMoney;
        notifyPropertyChanged(BR.inputMinute);
        if (selectAt == -1) return;
        this.selectAt = -1;
        notifyPropertyChanged(BR.selectAt);
    }

    @Bindable
    public String getInputMinute() {
        if (inputMoney == null || inputMoney.length() == 0) return "0分钟";
        float money = Float.parseFloat(inputMoney);
        int minute = (int) (money / 0.08f);
        return minute + "分钟";
    }

    @Bindable
    public int getSelectAt() {
        return selectAt;
    }

    public void setSelectAt(int selectAt) {
        if (selectAt == this.selectAt) return;
        this.selectAt = selectAt;
        switch (selectAt) {
            case 0:
                this.inputMoney = "10";
                break;
            case 1:
                this.inputMoney = "20";
                break;
            case 2:
                this.inputMoney = "30";
                break;
            case 3:
                this.inputMoney = "50";
                break;
            case 4:
                this.inputMoney = "100";
                break;
            case 5:
                this.inputMoney = "200";
                break;
            case 6:
                this.inputMoney = "300";
                break;
            case 7:
                this.inputMoney = "500";
                break;
        }
        notifyPropertyChanged(BR.selectAt);
        notifyPropertyChanged(BR.inputMoney);
        notifyPropertyChanged(BR.inputMinute);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Cursor cursor = managedQuery(data.getData(), null, null, null, null);
            cursor.moveToFirst();
            String phoneNum = ContactsUtil.getContactPhone(this, cursor);
            if (phoneNum.startsWith("+")) phoneNum = phoneNum.substring(phoneNum.indexOf('1'));
            setPhoneNum(phoneNum);
            notifyPropertyChanged(BR.phoneNum);
        }
    }
}
