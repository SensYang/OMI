package com.bqmm.config;

/**
 * 表情云协议
 * Created by SensYang on 2017/04/01 21:49
 */
public class BQMMHttpConfig {
    public static final String VERSION = "/v1";
    public static final String MAIN_HOST = "http://sop.biaoqingmm.com/api";
    /*获取token*/
    public static final String URL_GET_TOKEN = "/token";
    /*检查更新*/
    public static final String URL_GET_CHECKPACKAGE = "/checkpackage/new";
    /*获取推荐下载列表*/
    public static final String URL_GET_PRELOADS = "/category/code/recommend$preloads";
    /*获取搜索关键字列表*/
    public static final String URL_GET_KEYS = "/keyword/load/";
    /*获取单个表情包详情*/
    public static final String URL_GET_PACKAGE = "/package/";
    /*获取多个表情包详情*/
    public static final String URL_GET_PACKAGE_BATCH = "/package/batch/";
    /*表情商店广告位*/
    public static final String URL_GET_AD_BANNER = "/ad_banner/index";
    /*表情商店列表*/
    public static final String URL_GET_CATEGORY = "/category";
    /*获取多个表情详情*/
    public static final String URL_POST_EMOJI = "/emoji/codes";
    /*状态回复*/
    public static final String URL_POST_CALLBACK = "/callback/package/";
}
