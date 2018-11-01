package com.omi.net;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.omi.BR;
import com.omi.bean.User;
import com.omi.bean.UserVO;
import com.omi.bean.account.Member;
import com.omi.bean.base.BaseJson;
import com.omi.bean.dynamic.Dynamic;
import com.omi.bean.recharge.Flow;
import com.omi.utils.Log;
import com.omi.utils.MD5;
import com.omi.utils.amap.LocationUtil;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by SensYang on 2017/03/14 13:17
 */

public class ApiByHttp extends BaseObservable {
    private static ApiByHttp instance;
    private String phone;
    private String member_id;
    private User.Data user;

    public static ApiByHttp getInstance() {
        if (instance == null) instance = new ApiByHttp();
        return instance;
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        Log.e("setPhone--->" + phone);
        if (this.phone != null && this.phone.equalsIgnoreCase(phone)) return;
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public User.Data getUser() {
        return user;
    }

    public void setUser(User.Data user) {
        Log.e("setUser--->" + user);
        this.user = user;
        notifyPropertyChanged(BR.user);
    }

    @Bindable
    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        Log.e("setMember_id--->" + member_id);
        this.member_id = member_id;
        notifyPropertyChanged(BR.member_id);
    }

    /**
     * 登录
     */
    public void login(String phone, String password, ResponseCallback<Member> callback) {
        OMiBeanRequest request = new OMiBeanRequest(Member.class, HttpConfig.URL_GET_LOGIN);
        request.add("name", phone);
        request.add("pwd", MD5.encode(password));
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 获取验证码
     */
    public void getVerification(String phone, ResponseCallback<BaseJson> callback) {
        OMiBeanRequest request = new OMiBeanRequest(Member.class, HttpConfig.URL_POST_REGISTER_CODE, RequestMethod.POST);
        request.add("phone", phone);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 注册
     */
    public void register(String username, String password, String smscode, ResponseCallback<BaseJson> callback) {
        OMiBeanRequest request = new OMiBeanRequest(Member.class, HttpConfig.URL_POST_REGISTER);
        request.add("username", username);
        request.add("password", MD5.encode(password));
        request.add("password_confirm", MD5.encode(password));
        request.add("smscode", smscode);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 找回密码
     */
    public void findPassword(String username, String password, String smscode, ResponseCallback<BaseJson> callback) {
        OMiBeanRequest request = new OMiBeanRequest(Member.class, HttpConfig.URL_POST_FINDRPASS, RequestMethod.POST);
        request.add("username", username);
        request.add("password", MD5.encode(password));
        request.add("password_confirm", MD5.encode(password));
        request.add("smscode", smscode);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 设置用户资料
     */
    public void setUserInfo(UserVO userVO, ResponseCallback<BaseJson> callback) {
        OMiBeanRequest request = new OMiBeanRequest(BaseJson.class, HttpConfig.URL_POST_SETUSERINFO, RequestMethod.POST);
        request.add("member_name", userVO.getMember_name());
        request.add("member_nickname", userVO.getMember_nickname());
        request.add("member_sex", userVO.getMember_sex());
        request.add("member_birthday", userVO.getMember_birthday());
        request.add("member_signature", userVO.getMember_signature());
        request.add("member_areainfo", userVO.getMember_areainfo());
        request.add("member_avatar", userVO.getMember_avatar());
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 获取用户资料
     */
    public void getUserInfo(String username, ResponseCallback<User> callback) {
        OMiBeanRequest request = new OMiBeanRequest(User.class, HttpConfig.URL_GET_USERINFO);
        request.add("username", username);
        NoHttpUtil.getInstance().add(request, callback);
    }
    ////////////////////////////////////

    /**
     * 打电话
     */
    public void callPhone(String callPhone, JsonCallback callback) {
        JsonRequest request = new JsonRequest(HttpConfig.MAIN_HOST + HttpConfig.URL_CALL_PHONE, RequestMethod.POST);
        request.add("member_id", member_id);
        request.add("caller", phone);
        request.add("called", callPhone);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 增加好友
     */
    public void addFriend(String friendname, JsonCallback callback) {
        OMiBeanRequest request = new OMiBeanRequest(User.class, HttpConfig.URL_POST_ADD_CONTACT, RequestMethod.POST);
        request.add("username", ApiByHttp.getInstance().getPhone());
        request.add("friendname", friendname);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 更新位置
     */
    public void updateLocation(double longitude, double latitude) {
        JsonRequest request = new JsonRequest(HttpConfig.MAIN_HOST + HttpConfig.URL_GET_UPDATE_LOCATION, RequestMethod.GET);
        request.add("longitude", longitude);
        request.add("latitude", latitude);
        NoHttpUtil.getInstance().add(request);
    }

    /**
     * 发布动态
     */
    public void publishDynamic(Dynamic.Data dynamic, JsonCallback callback) {
        JsonRequest request = new JsonRequest(HttpConfig.MAIN_HOST + HttpConfig.URL_POST_PUBLISH_DYNAMIC, RequestMethod.POST);
        request.addParam(dynamic);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 发布动态的图片
     */
    public void publishDynamicImage(String discover_id, String imagebase64str, String image_name, JsonCallback callback) {
        JsonRequest request = new JsonRequest(HttpConfig.MAIN_HOST + HttpConfig.URL_POST_PUBLISH_IMAGE, RequestMethod.POST);
        request.add("discover_id", Integer.parseInt(discover_id));
        request.add("imagebase64str", imagebase64str);
        request.add("image_name", image_name);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 获取自己的动态
     */
    public void getSlefDynamic(ResponseCallback<Dynamic> callback) {
        OMiBeanRequest request = new OMiBeanRequest(Dynamic.class, HttpConfig.URL_GET_SELF_DYNAMIC);
        request.add("id", member_id);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 获取动态
     */
    public void getDynamic(String member_id, ResponseCallback<Dynamic> callback) {
        OMiBeanRequest request = new OMiBeanRequest(Dynamic.class, HttpConfig.URL_GET_DYNAMIC);
        request.add("id", member_id);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 赞动态
     */
    public void zanDynamic(String discover_id, JsonCallback callback) {
        JsonRequest request = new JsonRequest(HttpConfig.MAIN_HOST + HttpConfig.URL_POST_DYNAMIC_ZAN, RequestMethod.POST);
        request.add("discover_id", Integer.parseInt(discover_id));
        request.add("member_id", member_id);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 删除动态赞
     */
    public void delZanDynamic(String discover_id, int zan_id, JsonCallback callback) {
        JsonRequest request = new JsonRequest(HttpConfig.MAIN_HOST + HttpConfig.URL_POST_DEL_DYNAMIC_ZAN, RequestMethod.POST);
        request.add("discover_id", Integer.parseInt(discover_id));
        request.add("member_id", member_id);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 删除动态
     */
    public void delDynamic(String discover_id, JsonCallback callback) {
        JsonRequest request = new JsonRequest(HttpConfig.MAIN_HOST + HttpConfig.URL_POST_DEL_DYNAMIC, RequestMethod.POST);
        request.add("discover_id", Integer.parseInt(discover_id));
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 评论动态
     */
    public void commentDynamic(String discover_id, String comment_info, JsonCallback callback) {
        JsonRequest request = new JsonRequest(HttpConfig.MAIN_HOST + HttpConfig.URL_POST_COMMENT_DYNAMIC, RequestMethod.POST);
        request.add("discover_id", Integer.parseInt(discover_id));
        request.add("member_id", member_id);
        request.add("comment_info", comment_info);
        request.add("be_commented_member_id", "0");
        request.add("be_commented_text_id", "0");
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 获取附近的人
     */
    public void getNearPeople(int minage, int maxage, String sex, ResponseCallback<User> callback) {
        OMiBeanRequest request = new OMiBeanRequest(User.class, HttpConfig.URL_GET_NEAR_PEOPLE);
        request.add("member_id", member_id);
        request.add("longitude", LocationUtil.getInstance().getLocation().getLongitude());
        request.add("latitude", LocationUtil.getInstance().getLocation().getLatitude());
        //        request.add("longitude", 113.6380666383);
        //        request.add("latitude", 34.7201951529);
        //        request.add("maxcount", -1);
        //        request.add("num", 0);
        request.add("minage", Math.min(minage, maxage));
        request.add("maxage", Math.max(minage, maxage));
        request.add("sex", sex);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 充话费
     */
    public void rechargePhone(String total_amount, String phone, JsonCallback callback) {
        JsonRequest request = new JsonRequest(HttpConfig.MAIN_HOST + HttpConfig.URL_GET_RECHARGE_PHONE);
        request.add("total_amount", Float.parseFloat("0" + total_amount));
        request.add("name", user.getMember_name());
        request.add("account", phone);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 获取流量列表
     */
    public void getFlowList(ResponseCallback<Flow> callback) {
        OMiBeanRequest request = new OMiBeanRequest(Flow.class, HttpConfig.URL_GET_FLOW_LIST);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 充流量
     */
    public void rechargeFlow(String total_amount, String cardid, String kind, JsonCallback callback) {
        JsonRequest request = new JsonRequest(HttpConfig.MAIN_HOST + HttpConfig.URL_GET_RECHARGE_PHONE);
        request.add("total_amount", Float.parseFloat("0" + total_amount));
        request.add("name", user.getMember_name());
        request.add("cardid", cardid);
        request.add("kind", kind);
        request.add("account", user.getMember_name());
        NoHttpUtil.getInstance().add(request, callback);
    }
}