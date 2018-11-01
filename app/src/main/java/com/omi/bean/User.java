package com.omi.bean;

import android.databinding.Bindable;

import com.omi.BR;
import com.omi.bean.base.BaseJson;
import com.omi.bean.base.ComparableBean;
import com.omi.utils.amap.AMapUtil;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Calendar;

/**
 * Created by SensYang on 2017/3/7 0007.
 */

public class User extends BaseJson<User.Data> {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data extends ComparableBean {
        /**
         * member_id : 2
         * member_nickname : 学友
         * member_constellation :
         * inserttime : 2016-10-11 17:26:49.0
         * member_sex : 南娜
         * member_areainfo : 九龙湾
         * member_avatar : https://omi178.com/data/upload/mobile/useravatar/18037380670.png
         * member_birthday : 2022-02-22
         * member_name : 18037380670
         * isproxy : 0
         */
        private String member_id;
        private String member_nickname;
        private String member_constellation;
        private String inserttime;
        private String member_sex;
        private String member_areainfo;
        private String member_avatar;
        private String member_birthday;
        private String member_name;
        private String isproxy;
        private String member_remark;
        private String member_age;
        private String member_signature;
        private String age;
        private int distance;
        private boolean isSelect = false;

        public void setUser(Data user) {
            member_id = user.getMember_id();
            member_nickname = user.getMember_nickname();
            member_constellation = user.getMember_constellation();
            inserttime = user.getInserttime();
            member_sex = user.getMember_sex();
            member_areainfo = user.getMember_areainfo();
            member_avatar = user.getMember_avatar();
            member_birthday = user.getMember_birthday();
            member_name = user.getMember_name();
            isproxy = user.getIsproxy();
            member_remark = user.getMember_remark();
            member_age = user.getMember_age();
            member_signature = user.getMember_signature();
        }

        @Bindable
        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            if (member_id != null && member_id.equalsIgnoreCase(this.member_id)) return;
            this.member_id = member_id;
            notifyPropertyChanged(BR.member_id);
        }

        @Bindable
        public String getMember_nickname() {
            return member_nickname;
        }

        public void setMember_nickname(String member_nickname) {
            if (member_nickname != null && member_nickname.equalsIgnoreCase(this.member_nickname))
                return;
            this.member_nickname = member_nickname;
            notifyPropertyChanged(BR.member_nickname);
        }

        @Bindable
        public String getMember_constellation() {
            return member_constellation;
        }

        public void setMember_constellation(String member_constellation) {
            if (member_constellation != null && member_constellation.equalsIgnoreCase(this.member_constellation))
                return;
            this.member_constellation = member_constellation;
            notifyPropertyChanged(BR.member_constellation);
        }

        @Bindable
        public String getInserttime() {
            return inserttime;
        }

        public void setInserttime(String inserttime) {
            if (inserttime != null && inserttime.equalsIgnoreCase(this.inserttime)) return;
            this.inserttime = inserttime;
            notifyPropertyChanged(BR.inserttime);
        }

        @Bindable
        public String getMember_sex() {
            return member_sex;
        }

        @Bindable
        public boolean isBoy() {
            return member_sex.equalsIgnoreCase("男");
        }

        public void setMember_sex(String member_sex) {
            if (member_sex != null && member_sex.equalsIgnoreCase(this.member_sex)) return;
            this.member_sex = member_sex;
            notifyPropertyChanged(BR.member_sex);
            notifyPropertyChanged(BR.boy);
        }

        @Bindable
        public String getMember_areainfo() {
            return member_areainfo;
        }

        public void setMember_areainfo(String member_areainfo) {
            if (member_areainfo != null && member_areainfo.equalsIgnoreCase(this.member_areainfo))
                return;
            this.member_areainfo = member_areainfo;
            notifyPropertyChanged(BR.member_areainfo);
        }

        @Bindable
        public String getMember_avatar() {
            return member_avatar;
        }

        public void setMember_avatar(String member_avatar) {
            if (member_avatar != null && member_avatar.equalsIgnoreCase(this.member_avatar)) return;
            this.member_avatar = member_avatar;
            notifyPropertyChanged(BR.member_avatar);
        }

        @Bindable
        public String getMember_birthday() {
            return member_birthday;
        }

        public void setMember_birthday(String member_birthday) {
            if (member_birthday != null && member_birthday.equalsIgnoreCase(this.member_birthday))
                return;
            this.member_birthday = member_birthday;
            notifyPropertyChanged(BR.member_birthday);
            resetAge();
        }

        @Bindable
        public String getMember_name() {
            return member_name;
        }

        public void setMember_name(String member_name) {
            if (member_name != null && member_name.equalsIgnoreCase(this.member_name)) return;
            this.member_name = member_name;
            notifyPropertyChanged(BR.member_name);
        }

        @Bindable
        public String getIsproxy() {
            return isproxy;
        }

        public void setIsproxy(String isproxy) {
            if (isproxy != null && isproxy.equalsIgnoreCase(this.isproxy)) return;
            this.isproxy = isproxy;
            notifyPropertyChanged(BR.isproxy);
        }

        @Bindable
        public String getMember_remark() {
            return member_remark;
        }

        public void setMember_remark(String member_remark) {
            if (member_remark != null && member_remark.equalsIgnoreCase(this.member_remark)) return;
            this.member_remark = member_remark;
            notifyPropertyChanged(BR.member_remark);
        }

        private void resetAge() {
            if (age != null) {
                setMember_age(age);
                return;
            }
            if (member_birthday == null) return;
            if (member_birthday.length() > 4) {
                int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(member_birthday.substring(0, 4));
                setMember_age(String.valueOf(age));
            }
        }

        @Bindable
        public String getMember_age() {
            if (member_age == null) resetAge();
            return member_age;
        }

        public void setMember_age(String member_age) {
            if (member_age != null && member_age.equalsIgnoreCase(this.member_age)) return;
            this.member_age = member_age;
            notifyPropertyChanged(BR.member_age);
        }

        @Bindable
        public String getMember_signature() {
            return member_signature;
        }

        public void setMember_signature(String member_signature) {
            if (member_signature != null && member_signature.equalsIgnoreCase(this.member_signature))
                return;
            this.member_signature = member_signature;
            notifyPropertyChanged(BR.member_signature);
        }

        @Override
        protected String getCompareString() {
            return member_nickname;
        }

        @Bindable
        public String getDistance() {
            return AMapUtil.getEnglishLength(distance);
        }

        public void setDistance(int distance) {
            if (distance == this.distance) return;
            this.distance = distance;
            notifyPropertyChanged(BR.distance);
        }

        @Override
        public String toString() {
            return "Data{" + "member_id='" + member_id + '\'' + ", member_nickname='" + member_nickname + '\'' + ", member_constellation='" + member_constellation + '\'' + ", inserttime='" + inserttime + '\'' + ", member_sex='" + member_sex + '\'' + ", member_areainfo='" + member_areainfo + '\'' + ", member_avatar='" + member_avatar + '\'' + ", member_birthday='" + member_birthday + '\'' + ", member_name='" + member_name + '\'' + ", isproxy='" + isproxy + '\'' + ", member_remark='" + member_remark + '\'' + ", member_age='" + member_age + '\'' + ", member_signature='" + member_signature + '\'' + '}';
        }

        public String getAge() { return age;}

        public void setAge(String age) { this.age = age;}

        @Bindable
        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            if (select == this.isSelect) return;
            isSelect = select;
            notifyPropertyChanged(BR.select);
        }
    }

}
