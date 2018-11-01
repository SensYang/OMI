package com.omi.bean.recharge;

import com.omi.bean.base.BaseJson;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by SensYang on 2017/04/28 18:25
 */

public class Flow extends BaseJson<Flow.Data> {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        /**
         * amount : 1.00
         * money : 5.00
         * kind : 001
         * id : 1
         */
        private String amount;
        private String money;
        private String kind;
        private String id;

        public String getAmount() { return amount;}

        public void setAmount(String amount) { this.amount = amount;}

        public String getMoney() { return money;}

        public void setMoney(String money) { this.money = money;}

        public String getKind() { return kind;}

        public void setKind(String kind) { this.kind = kind;}

        public String getId() { return id;}

        public void setId(String id) { this.id = id;}

        public String getFlowCount() {
            if (amount != null && amount.length() > 0) {
                int count = (int) Float.parseFloat(amount);
                return count + "M";
            }
            return "0M";
        }

        public String getFlowPrice() {
            if (money != null && money.length() > 0) {
                int count = (int) Float.parseFloat(money);
                return count + "元";
            }
            return "0元";
        }
    }
}
