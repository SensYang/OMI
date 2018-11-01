package com.omi;

import android.support.annotation.NonNull;

import com.bqmm.net.ParamMap;
import com.bqmm.signature.BQMMSignatureUtil;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Bean bean = new Bean();
        Bean bean1 = new Bean();
        System.out.println(bean.hashCode());
        System.out.println(bean1.hashCode());
    }

    public class Bean {
    }

    @Test
    public void main() {
        String temp = "/api/v1/checkpackage/new?device_no=39681cc9-5ae4-46bb-9d93-9f3c3732eb36&access_token=15f308220cd802181df9ec67342770d5&signature=8E4C838ADEED23505FCDE5CA8BE6BFA9&region=null&sdk_version=1.7.8&app_id=274ee4a1c8e047f38ec094583d9866e8&timestamp=1490958701315&os=Android5.1&provider=sdk";
//        System.out.println(url);

//        System.out.println("表情轮播:");
//        System.out.println(getUrl(URL_GET_CATEGORY));
//        System.out.println("检查新的emoji:");
//        System.out.println(getUrl(URL_GET_CHECKPACKAGE));
//        System.out.println("keyword:");
//        System.out.println(getUrl(URL_GET_KEYWORD));
//        System.out.println("batch:");
//        System.out.println(getUrl(URL_GET_PACKAGE + "36e9351463ed4f2fa140c0cd6f8f9b53"));//+guid
//        System.out.println("URL_GET_INDEX:");
//        System.out.println(getUrl(URL_GET_INDEX));//+guid


        try {
            System.out.println("开始请求");
            URL url = new URL(getUrl("/emoji/codes"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            connection.setRequestMethod("POST");
            // 设置请求的超时时间
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            // 传递的数据
            String data = "{\"codes\":[\"yhegkd\"]}";
            // 设置请求的头  
            connection.setRequestProperty("Connection", "keep-alive");
            // 设置请求的头  
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            // 设置请求的头  
            connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
            // 设置请求的头  
            connection.setRequestProperty("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 5.1; vivo X6Plus D Build/LMY47I)");
            connection.setDoOutput(true); // 发送POST请求必须设置允许输出
            connection.setDoInput(true); // 发送POST请求必须设置允许输入  
            //setDoInput的默认值就是true 
//获取输出流  
            OutputStream os = connection.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            if (connection.getResponseCode() == 200) {
                // 获取响应的输入流对象  
                InputStream is = connection.getInputStream();
                // 创建字节输出流对象  
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 定义读取的长度  
                int len = 0;
                // 定义缓冲区  
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取  
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中  
                    baos.write(buffer, 0, len);
                }
                // 释放资源  
                is.close();
                baos.close();
                // 返回字符串  
                String result = new String(baos.toByteArray());
                System.out.println(result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static final String DOMAIN = "http://sop.biaoqingmm.com/api/v1";


    /*表情轮播*/
    public static final String URL_GET_RECOMMEND_PRELOADS = "/category/code/recommend$preloads";
    /*检查新的Emoji数量*/
    public static final String URL_GET_CHECKPACKAGE = "/checkpackage/new";

    public static final String URL_GET_KEYWORD = "/keyword/load/0";

    /*获取数据包详情*/
    public static final String URL_GET_BATCH = "/package/batch/";
    /*获取数据包详情*/
    public static final String URL_GET_PACKAGE = "/package/";
    /*检查数据包*/
    public static final String URL_GET_PACKAGE_CALLBACK = "/callback/package/";
    /*首页广告位*/
    public static final String URL_GET_INDEX = "/ad_banner/index";
    /*分类*/
    public static final String URL_GET_CATEGORY = "/category";
    /*推荐表情*/
    public static final String URL_GET_RECOMMEND = "/category/code/recommend";
    public static final String URL_GET_PRELOAD = "/category/code/preload";

    public static final String URL_POST_ = "";
    public static final String URL_POST_EVENTS = "/callback/events";
    public static final String URL_POST_EMOJIS = "/callback/emojis";
    public static final String URL_POST_SMALL_EMO_CODE = "/emoji/small_emo_code";
    public static final String URL_POST_BIG_EMO_CODE = "/emoji/big_emo_code";


//http://sop.biaoqingmm.com/api/v1/api/v1/package/batch/09bfcdbcc0224b65b69ea23dc3759275
// ?timestamp=1490964241812&device_no=4077cf50-af89-4f69-ae32-73fe6cc536a4&os=Androidnull&app_id=274ee4a1c8e047f38ec094583d9866e8&sdk_version=1.7.8&provider=sdk&access_token=15f308220cd802181df9ec67342770d5&signature=3FFC64D616FE9B2099E0F9468FCC377C
//http://sop.biaoqingmm.com/api/v1/package/batch/36e9351463ed4f2fa140c0cd6f8f9b53$5d2f4a2ef9a44114bf066d9feb9c9916$4efb9ed027c9463c80523805e8472316?timestamp=1490964489502&device_no=c55b118a-7486-44df-a365-2f1a75a61641&os=Android5.1&app_id=274ee4a1c8e047f38ec094583d9866e8&sdk_version=1.7.8&provider=sdk&access_token=15f308220cd802181df9ec67342770d5&signature=A54C041767F1221DEB5F187CC1734EA1

// ?device_no=39681cc9-5ae4-46bb-9d93-9f3c3732eb36&access_token=15f308220cd802181df9ec67342770d5&signature=4975D48CAC76A5CE619C0B805C392223&region=null&sdk_version=1.7.8&app_id=274ee4a1c8e047f38ec094583d9866e8&timestamp=1490958701503&os=Android5.1&provider=sdk

    //GET /update/query?model=PD1501D&imei=868964027420692&ms=1&mtype=no&version=PD1501D_N_PD1501DMA_1.22.3&protocalversion=3.0&public_model=vivo+X6Plus+D&isSilenceSupport=true&elapsedtime=50993091&st1=50892732&st2=50890656&sf=1&sn1=%2C46007&sn2=46007&nt=WIFI&sm1=UNKNOWN&sm2=null&srm1=4&srm2=0&emmcid=150100524331344d42018cc6d48ab2df&radiotype=D&flag=0&autodl=1
    private String getUrl(String path) {
        return DOMAIN + path + getUrlParam(path);
    }

    private String getUrlParam(String path) {
        String app_id = "274ee4a1c8e047f38ec094583d9866e8";
        String os = "Android5.1";//+ Build.VERSION.RELEASE;
        String token = "15f308220cd802181df9ec67342770d5";

// ?timestamp=1491071738379
// &device_no=8a1e80b5-7efe-452d-84c2-510e0b7928f2
// &signature=F37AEA35B25AEF1D416DA1232AEFF5F1
// &signature=A73718CF59FBBF47B05D346673B85314

// ?timestamp=1491071864704
// &device_no=6c4f3037-574d-4a36-8adf-0c62b0e08b2c
// &signature=A73718CF59FBBF47B05D346673B85314

        ParamMap parameters = new ParamMap();
        parameters.put("timestamp", String.valueOf(System.currentTimeMillis()));
        parameters.put("device_no", UUID.randomUUID().toString());
        parameters.put("os", os);
        parameters.put("app_id", app_id);
        parameters.put("sdk_version", "1.7.8");
        parameters.put("provider", "sdk");
        parameters.put("access_token", token);
        parameters.put("region", null);
        parameters.put("signature", BQMMSignatureUtil.generateSignature(path, parameters.getParamMap()));
        return parameters.toParamString();
    }

    private void print(@NonNull String out) {
        System.out.println(out);
    }
}