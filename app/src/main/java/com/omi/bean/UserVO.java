package com.omi.bean;

import android.databinding.Bindable;

import com.omi.BR;
import com.omi.bean.base.BaseBean;
import com.omi.utils.Log;

import java.util.Calendar;

/**
 * Created by SensYang on 2017/04/04 14:16
 */

public class UserVO extends BaseBean {
    private String avatar;
    private String member_name;
    private String member_nickname;
    private String member_sex;
    private String member_birthday;
    private String member_signature;
    private String member_areainfo;
    private String member_age;
    private String member_avatar;

    public void setUser(User.Data user) {
        setMember_name(user.getMember_name());
        setAvatar(user.getMember_avatar());
        setMember_nickname(user.getMember_nickname());
        setMember_sex(user.getMember_sex());
        setMember_birthday(user.getMember_birthday());
        setMember_signature(user.getMember_signature());
        setMember_areainfo(user.getMember_areainfo());
    }

    @Bindable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        if (avatar != null && avatar.equalsIgnoreCase(this.avatar)) return;
        this.avatar = avatar;
        notifyPropertyChanged(BR.avatar);
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
        Log.e("member_birthday--->" + member_birthday);
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

    private void resetAge() {
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
        Log.e("member_age--->" + member_age);
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

}
