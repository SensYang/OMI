package com.omi.net;

/**
 * Created by SensYang on 2017/03/07 20:02
 */
public class HttpConfig {
    public static final String MAIN_HOST = "https://omi178.com";
    /*注册验证码*/
    public static final String URL_POST_REGISTER_CODE = "/wap/tmpl/member/sendsmsregistercode.php";
    /*登录*/
    public static final String URL_GET_LOGIN = "/wap/tmpl/member/login.php";
    /*注册*/
    public static final String URL_POST_REGISTER = "/wap/tmpl/member/enroll2.php";
    /*找回密码*/
    public static final String URL_POST_FINDRPASS = "/wap/tmpl/member/resetpwd.php";
    /*修改密码*/
    public static final String URL_POST_ALTERPASS = "/tm/setpwd.php";
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*打电话*/
    public static final String URL_CALL_PHONE = "/interface/phone_api.php";
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*获取好友列表*/
    public static final String URL_POST_LIST_CONTACTS = ":8443/m/getfriends.do";
    /*增加好友*/
    public static final String URL_POST_ADD_CONTACT = ":8443/m/addfriend.do";
    /*获取联系人详情*/
    public static final String URL_GET_USERINFO = ":8443/omi/getuserinfo.jsp";
    /*设置个人资料*/
    public static final String URL_POST_SETUSERINFO = "/interface/setmemberinfo.php";
    /*发布动态*/
    public static final String URL_POST_PUBLISH_DYNAMIC = ":8443/m/pyq_put_dt.do";
    /*发布动态的图片*/
    public static final String URL_POST_PUBLISH_IMAGE = ":8443/m/pyq_put_image.do";
    /*获取自己的动态*/
    public static final String URL_GET_SELF_DYNAMIC = ":8443/m/pyq_get_dt_self.do";
    /*获取好友动态*/
    public static final String URL_GET_DYNAMIC = ":8443/m/pyq_get_dt.do";
    /*赞动态*/
    public static final String URL_POST_DYNAMIC_ZAN = ":8443/m/pyq_put_zan.do";
    /*删除赞动态*/
    public static final String URL_POST_DEL_DYNAMIC_ZAN = ":8443/m/pyq_del_zan.do";
    /*评论动态*/
    public static final String URL_POST_COMMENT_DYNAMIC = ":8443/m/pyq_put_pinglun.do";
    /*删除动态*/
    public static final String URL_POST_DEL_DYNAMIC = ":8443/m/delete_dt.do";
    /*更新位置*/
    public static final String URL_GET_UPDATE_LOCATION = "/interface/nearby_update_position.php";
    /*获取附近人*/
    public static final String URL_GET_NEAR_PEOPLE = "/interface/nearby_get_person.php";
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*充话费*/
    public static final String URL_GET_RECHARGE_PHONE = "/alipay-sdk-new/sign.php";
    /*获取流量列表*/
    public static final String URL_GET_FLOW_LIST = ":8443/m/getll.do";
}