package com.bqmm.net;

import com.bqmm.bean.BQMMBanner;
import com.bqmm.bean.BQMMBaseJson;
import com.bqmm.bean.BQMMCheck;
import com.bqmm.bean.BQMMEmoticion;
import com.bqmm.bean.BQMMEmoticionPackage;
import com.bqmm.bean.BQMMKeyword;
import com.bqmm.bean.BQMMToken;
import com.bqmm.config.BQMMConfig;
import com.bqmm.config.BQMMHttpConfig;
import com.omi.net.JsonUtils;
import com.omi.net.NoHttpUtil;
import com.omi.net.ResponseCallback;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.HashMap;

/**
 * Created by SensYang on 2017/04/01 22:18
 */

public class BQMMApi {
    private static BQMMApi instance = new BQMMApi();

    public static BQMMApi getInstance() {
        return instance;
    }

    /**
     * 获取token
     */
    public void getToken() {
        BQMMRequest request = BQMMRequest.getInstance(BQMMToken.class, BQMMHttpConfig.URL_GET_TOKEN);
        request.add("appid", BQMMConfig.BQMM_APP_ID);
        request.add("op", "refresh");
        request.add("secret", BQMMConfig.BQMM_APP_SECRET);
        NoHttpUtil.getInstance().add(request, new ResponseCallback<BQMMToken>() {
            @Override
            public void onSucceed(int what, Response<BQMMToken> response) {
                BQMMToken token = response.get();
                if (token != null && token.getData() != null && token.getData().getAccess_token() != null) {
                    BQMMRequest.setToken(token.getData().getAccess_token());
                }
            }
        });
    }

    /**
     * 检查新的emoji
     */
    public void checkPackage(ResponseCallback<BQMMCheck> callback) {
        BQMMRequest request = BQMMRequest.getInstance(BQMMCheck.class, BQMMHttpConfig.URL_GET_CHECKPACKAGE);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 获取推荐和首次下载
     */
    public void getRecommendPreloads(ResponseCallback<BQMMBanner> callback) {
        BQMMRequest request = BQMMRequest.getInstance(BQMMBanner.class, BQMMHttpConfig.URL_GET_PRELOADS);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 获取搜索关键词
     */
    public void getKeyword(ResponseCallback<BQMMKeyword> callback, long lastTime) {
        BQMMRequest request = BQMMRequest.getInstance(BQMMKeyword.class, BQMMHttpConfig.URL_GET_KEYS + lastTime);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 获取单个表情包详情
     */
    public void getPackageInfo(ResponseCallback<BQMMEmoticionPackage> callback, String guid) {
        BQMMRequest request = BQMMRequest.getInstance(BQMMEmoticionPackage.class, BQMMHttpConfig.URL_GET_PACKAGE + guid);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 获取多个表情包详情
     */
    public void getPackageInfo(ResponseCallback<BQMMEmoticionPackage> callback, String... guids) {
        String ids = "";
        for (String gui : guids) {
            if (ids.length() == 0) {
                ids += gui;
            } else {
                ids += "$" + gui;
            }
        }
        BQMMRequest request = BQMMRequest.getInstance(BQMMEmoticionPackage.class, BQMMHttpConfig.URL_GET_PACKAGE_BATCH + ids);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 反馈表情包的状态
     *
     * @param guid   表情包id
     * @param action 状态 download
     */
    public void callback(ResponseCallback<BQMMBaseJson<String>> callback, String guid, String action) {
        BQMMRequest.setAction(action);
        BQMMRequest request = BQMMRequest.getInstance(BQMMBaseJson.class, BQMMHttpConfig.URL_POST_CALLBACK + guid, RequestMethod.POST);
        NoHttpUtil.getInstance().add(request, callback);
        BQMMRequest.setAction(null);
    }

    /**
     * 获取表情商店广告栏
     */
    public void getBanner(ResponseCallback<BQMMBanner> callback) {
        BQMMRequest request = BQMMRequest.getInstance(BQMMBanner.class, BQMMHttpConfig.URL_GET_AD_BANNER);
        NoHttpUtil.getInstance().add(request, callback);
    }

    /**
     * 获取表情商店列表
     */
    public void getCategory(ResponseCallback<BQMMBanner> callback) {
        BQMMRequest request = BQMMRequest.getInstance(BQMMBanner.class, BQMMHttpConfig.URL_GET_CATEGORY);
        NoHttpUtil.getInstance().add(request, callback);
    }

    public void getEmojiInfo(ResponseCallback<BQMMEmoticion> callback, String... emojiGuids) {
        if (emojiGuids.length == 0) return;
        BQMMRequest request = BQMMRequest.getInstance(BQMMEmoticion.class, BQMMHttpConfig.URL_POST_EMOJI, RequestMethod.POST);
        HashMap<String, String[]> codes = new HashMap<>(1);
        codes.put("codes", emojiGuids);
        request.setData(JsonUtils.parserBean2Json(codes));
        NoHttpUtil.getInstance().add(request, callback);
    }
}
