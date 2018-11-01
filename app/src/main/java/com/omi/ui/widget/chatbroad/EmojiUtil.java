package com.omi.ui.widget.chatbroad;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.omi.OmiApplication;
import com.omi.R;
import com.omi.bean.chat.Emoji;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SensYang on 2017/03/25 9:23
 */

public class EmojiUtil {
    private static List<Emoji> emojiList = new ArrayList<>();
    //    private static List<Emoji> easeList = new ArrayList<>();

    public static List<Emoji> getEmojiList() {
        return emojiList;
    }

    static {
        //        emojiList.add(new Emoji(R.drawable.d_aini, "[爱你]"));
        //        emojiList.add(new Emoji(R.drawable.d_aoteman, "[奥特曼]"));
        //        emojiList.add(new Emoji(R.drawable.d_baibai, "[拜拜]"));
        //        emojiList.add(new Emoji(R.drawable.d_beishang, "[悲伤]"));
        //        emojiList.add(new Emoji(R.drawable.d_bishi, "[鄙视]"));
        //        emojiList.add(new Emoji(R.drawable.d_bizui, "[闭嘴]"));
        //        emojiList.add(new Emoji(R.drawable.d_chanzui, "[馋嘴]"));
        //        emojiList.add(new Emoji(R.drawable.d_chijing, "[吃惊]"));
        //        emojiList.add(new Emoji(R.drawable.d_dahaqi, "[哈欠]"));
        //        emojiList.add(new Emoji(R.drawable.d_dalian, "[打脸]"));
        //        emojiList.add(new Emoji(R.drawable.d_ding, "[顶]"));
        //        emojiList.add(new Emoji(R.drawable.d_doge, "[doge]"));
        //        emojiList.add(new Emoji(R.drawable.d_feizao, "[肥皂]"));
        //        emojiList.add(new Emoji(R.drawable.d_ganmao, "[感冒]"));
        //        emojiList.add(new Emoji(R.drawable.d_guzhang, "[鼓掌]"));
        //        emojiList.add(new Emoji(R.drawable.d_haha, "[哈哈]"));
        //        emojiList.add(new Emoji(R.drawable.d_haixiu, "[害羞]"));
        //        emojiList.add(new Emoji(R.drawable.d_han, "[汗]"));
        //        emojiList.add(new Emoji(R.drawable.d_hehe, "[微笑]"));
        //        emojiList.add(new Emoji(R.drawable.d_heixian, "[黑线]"));
        //        emojiList.add(new Emoji(R.drawable.d_heng, "[哼]"));
        //        emojiList.add(new Emoji(R.drawable.d_huaxin, "[色]"));
        //        emojiList.add(new Emoji(R.drawable.d_jiyan, "[挤眼]"));
        //        emojiList.add(new Emoji(R.drawable.d_keai, "[可爱]"));
        //        emojiList.add(new Emoji(R.drawable.d_kelian, "[可怜]"));
        //        emojiList.add(new Emoji(R.drawable.d_ku, "[酷]"));
        //        emojiList.add(new Emoji(R.drawable.d_kun, "[困]"));
        //        emojiList.add(new Emoji(R.drawable.d_landelini, "[白眼]"));
        //        emojiList.add(new Emoji(R.drawable.d_lei, "[泪]"));
        //        emojiList.add(new Emoji(R.drawable.d_miao, "[喵喵]"));
        //        emojiList.add(new Emoji(R.drawable.d_nanhaier, "[男孩儿]"));
        //        emojiList.add(new Emoji(R.drawable.d_nu, "[怒]"));
        //        emojiList.add(new Emoji(R.drawable.d_numa, "[怒骂]"));
        //        emojiList.add(new Emoji(R.drawable.d_numa, "[女孩儿]"));
        //        emojiList.add(new Emoji(R.drawable.d_qian, "[钱]"));
        //        emojiList.add(new Emoji(R.drawable.d_qinqin, "[亲亲]"));
        //        emojiList.add(new Emoji(R.drawable.d_shayan, "[傻眼]"));
        //        emojiList.add(new Emoji(R.drawable.d_shengbing, "[生病]"));
        //        emojiList.add(new Emoji(R.drawable.d_shenshou, "[草泥马]"));
        //        emojiList.add(new Emoji(R.drawable.d_shiwang, "[失望]"));
        //        emojiList.add(new Emoji(R.drawable.d_shuai, "[衰]"));
        //        emojiList.add(new Emoji(R.drawable.d_shuijiao, "[睡]"));
        //        emojiList.add(new Emoji(R.drawable.d_sikao, "[思考]"));
        //        emojiList.add(new Emoji(R.drawable.d_taikaixin, "[太开心]"));
        //        emojiList.add(new Emoji(R.drawable.d_touxiao, "[偷笑]"));
        //        emojiList.add(new Emoji(R.drawable.d_tu, "[吐]"));
        //        emojiList.add(new Emoji(R.drawable.d_tuzi, "[兔子]"));
        //        emojiList.add(new Emoji(R.drawable.d_wabishi, "[挖鼻]"));
        //        emojiList.add(new Emoji(R.drawable.d_weiqu, "[委屈]"));
        //        emojiList.add(new Emoji(R.drawable.d_xiaoku, "[笑cry]"));
        //        emojiList.add(new Emoji(R.drawable.d_xiongmao, "[熊猫]"));
        //        emojiList.add(new Emoji(R.drawable.d_xixi, "[嘻嘻]"));
        //        emojiList.add(new Emoji(R.drawable.d_xu, "[嘘]"));
        //        emojiList.add(new Emoji(R.drawable.d_yinxian, "[阴险]"));
        //        emojiList.add(new Emoji(R.drawable.d_yiwen, "[疑问]"));
        //        emojiList.add(new Emoji(R.drawable.d_youhengheng, "[右哼哼]"));
        //        emojiList.add(new Emoji(R.drawable.d_yun, "[晕]"));
        //        emojiList.add(new Emoji(R.drawable.d_zhajipijiu, "[炸鸡啤酒]"));
        //        emojiList.add(new Emoji(R.drawable.d_zhuakuang, "[抓狂]"));
        //        emojiList.add(new Emoji(R.drawable.d_zhutou, "[猪头]"));
        //        emojiList.add(new Emoji(R.drawable.d_zuiyou, "[最右]"));
        //        emojiList.add(new Emoji(R.drawable.d_zuohengheng, "[左哼哼]"));
        //        emojiList.add(new Emoji(R.drawable.f_geili, "[给力]"));
        //        emojiList.add(new Emoji(R.drawable.f_hufen, "[互粉]"));
        //        emojiList.add(new Emoji(R.drawable.f_jiong, "[囧]"));
        //        emojiList.add(new Emoji(R.drawable.f_meng, "[萌]"));
        //        emojiList.add(new Emoji(R.drawable.f_shenma, "[神马]"));
        //        emojiList.add(new Emoji(R.drawable.f_v5, "[威武]"));
        //        emojiList.add(new Emoji(R.drawable.f_xi, "[喜]"));
        //        emojiList.add(new Emoji(R.drawable.f_zhi, "[织]"));
        //        emojiList.add(new Emoji(R.drawable.h_buyao, "[NO]"));
        //        emojiList.add(new Emoji(R.drawable.h_good, "[good]"));
        //        emojiList.add(new Emoji(R.drawable.h_haha, "[haha]"));
        //        emojiList.add(new Emoji(R.drawable.h_lai, "[来]"));
        //        emojiList.add(new Emoji(R.drawable.h_ok, "[OK]"));
        //        emojiList.add(new Emoji(R.drawable.h_quantou, "[拳头]"));
        //        emojiList.add(new Emoji(R.drawable.h_ruo, "[弱]"));
        //        emojiList.add(new Emoji(R.drawable.h_woshou, "[握手]"));
        //        emojiList.add(new Emoji(R.drawable.h_ye, "[耶]"));
        //        emojiList.add(new Emoji(R.drawable.h_zan, "[赞]"));
        //        emojiList.add(new Emoji(R.drawable.h_zuoyi, "[作揖]"));
        //        emojiList.add(new Emoji(R.drawable.l_shangxin, "[伤心]"));
        //        emojiList.add(new Emoji(R.drawable.l_xin, "[心]"));
        //        emojiList.add(new Emoji(R.drawable.o_dangao, "[蛋糕]"));
        //        emojiList.add(new Emoji(R.drawable.o_feiji, "[飞机]"));
        //        emojiList.add(new Emoji(R.drawable.o_ganbei, "[干杯]"));
        //        emojiList.add(new Emoji(R.drawable.o_huatong, "[话筒]"));
        //        emojiList.add(new Emoji(R.drawable.o_lazhu, "[蜡烛]"));
        //        emojiList.add(new Emoji(R.drawable.o_liwu, "[礼物]"));
        //        emojiList.add(new Emoji(R.drawable.o_lvsidai, "[绿丝带]"));
        //        emojiList.add(new Emoji(R.drawable.o_weibo, "[围脖]"));
        //        emojiList.add(new Emoji(R.drawable.o_weiguan, "[围观]"));
        //        emojiList.add(new Emoji(R.drawable.o_yinyue, "[音乐]"));
        //        emojiList.add(new Emoji(R.drawable.o_zhaoxiangji, "[照相机]"));
        //        emojiList.add(new Emoji(R.drawable.o_zhong, "[钟]"));
        //        emojiList.add(new Emoji(R.drawable.w_fuyun, "[浮云]"));
        //        emojiList.add(new Emoji(R.drawable.w_shachenbao, "[沙尘暴]"));
        //        emojiList.add(new Emoji(R.drawable.w_taiyang, "[太阳]"));
        //        emojiList.add(new Emoji(R.drawable.w_weifeng, "[微风]"));
        //        emojiList.add(new Emoji(R.drawable.w_xianhua, "[鲜花]"));
        //        emojiList.add(new Emoji(R.drawable.w_xiayu, "[下雨]"));
        //        emojiList.add(new Emoji(R.drawable.w_yueliang, "[月亮]"));
        //扩充环信
        emojiList.add(new Emoji(R.drawable.ee_1, "[):]"));
        emojiList.add(new Emoji(R.drawable.ee_2, "[:D]"));
        emojiList.add(new Emoji(R.drawable.ee_3, "[;)]"));
        emojiList.add(new Emoji(R.drawable.ee_4, "[:-o]"));
        emojiList.add(new Emoji(R.drawable.ee_5, "[:p]"));
        emojiList.add(new Emoji(R.drawable.ee_6, "[(H)]"));
        emojiList.add(new Emoji(R.drawable.ee_7, "[:@]"));
        emojiList.add(new Emoji(R.drawable.ee_8, "[:s]"));
        emojiList.add(new Emoji(R.drawable.ee_9, "[:$]"));
        emojiList.add(new Emoji(R.drawable.ee_10, "[:(]"));
        emojiList.add(new Emoji(R.drawable.ee_11, "[:'(]"));
        emojiList.add(new Emoji(R.drawable.ee_12, "[:|]"));
        emojiList.add(new Emoji(R.drawable.ee_13, "[(a)]"));
        emojiList.add(new Emoji(R.drawable.ee_14, "[8o|]"));
        emojiList.add(new Emoji(R.drawable.ee_15, "[8-|]"));
        emojiList.add(new Emoji(R.drawable.ee_16, "[+o(]"));
        emojiList.add(new Emoji(R.drawable.ee_17, "[<o)]"));
        emojiList.add(new Emoji(R.drawable.ee_18, "[|-)]"));
        emojiList.add(new Emoji(R.drawable.ee_19, "[*-)]"));
        emojiList.add(new Emoji(R.drawable.ee_20, "[:-#]"));
        emojiList.add(new Emoji(R.drawable.ee_21, "[:-*]"));
        emojiList.add(new Emoji(R.drawable.ee_22, "[^o)]"));
        emojiList.add(new Emoji(R.drawable.ee_23, "[8-)]"));
        emojiList.add(new Emoji(R.drawable.ee_24, "[(|)]"));
        emojiList.add(new Emoji(R.drawable.ee_25, "[(u)]"));
        emojiList.add(new Emoji(R.drawable.ee_26, "[(S)]"));
        emojiList.add(new Emoji(R.drawable.ee_27, "[(*)]"));
        emojiList.add(new Emoji(R.drawable.ee_28, "[(#)]"));
        emojiList.add(new Emoji(R.drawable.ee_29, "[(R)]"));
        emojiList.add(new Emoji(R.drawable.ee_30, "[({)]"));
        emojiList.add(new Emoji(R.drawable.ee_31, "[(})]"));
        emojiList.add(new Emoji(R.drawable.ee_32, "[(k)]"));
        emojiList.add(new Emoji(R.drawable.ee_33, "[(F)]"));
        emojiList.add(new Emoji(R.drawable.ee_34, "[(W)]"));
        emojiList.add(new Emoji(R.drawable.ee_35, "[(D)]"));
    }

    public static float calculateInSampleSize(BitmapFactory.Options options, float reqWidth, float reqHeight) {
        // 源图片的高度和宽度
        float height = options.outHeight;
        float width = options.outWidth;
        float inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            float heightRatio = Math.round(height / reqHeight);
            float widthRatio = Math.round(width / reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(int resId, float reqWidth, float reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(OmiApplication.getInstance().getResources(), resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = (int) calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(OmiApplication.getInstance().getResources(), resId, options);
    }

    @TargetApi(Build.VERSION_CODES.DONUT)
    public static SpannableStringBuilder stringToEmoji(float textSize, String content) {
        if (content == null) return new SpannableStringBuilder("");
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        String regex = "\\[(\\S+?)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        Iterator<Emoji> iterator;
        Emoji emoji = null;
        while (matcher.find()) {
            String tempText = matcher.group();
            //            {//TODO 兼容环信的表情
            //                iterator = easeList.iterator();
            //                while (iterator.hasNext()) {
            //                    emoji = iterator.next();
            //                    if (tempText.equals(emoji.getEmojiString())) {
            //                        //转换为Span并设置Span的大小
            //                        Bitmap bitmap = decodeSampledBitmapFromResource(emoji.getImgRes(), textSize, textSize);
            //                        Drawable drawable = new BitmapDrawable(OmiApplication.getInstance().getResources(), bitmap);
            //                        drawable.setBounds(0, 0, (int) textSize, (int) textSize);
            //                        builder.setSpan(new ImageSpan(drawable), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //                        break;
            //                    }
            //                }
            //            }
            {//新的表情
                iterator = emojiList.iterator();
                while (iterator.hasNext()) {
                    emoji = iterator.next();
                    if (tempText.equals(emoji.getEmojiString())) {
                        //转换为Span并设置Span的大小
                        Bitmap bitmap = decodeSampledBitmapFromResource(emoji.getImgRes(), textSize, textSize);
                        Drawable drawable = new BitmapDrawable(OmiApplication.getInstance().getResources(), bitmap);
                        drawable.setBounds(0, 0, (int) textSize, (int) textSize);
                        builder.setSpan(new ImageSpan(drawable), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        break;
                    }
                }
            }
        }
        return builder;
    }
}
