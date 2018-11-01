# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-ignorewarnings

-keep class com.omi.bean.**{*;}    #不进行混淆编译

-allowaccessmodification        #优化时允许访问并修改有修饰符的类和类的成员
-keepattributes Exceptions,InnerClasses     #保护给定的可选属性
-dontskipnonpubliclibraryclasses        #指定不去忽略非公共的库类。
-dontskipnonpubliclibraryclassmembers       # 指定不去忽略包可见的库类的成员。

-optimizationpasses 10          # 指定代码的压缩级别 循环迭代次数
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-dontwarn net.sourceforge.pinyin4j.**
-dontwarn com.yolanda.nohttp.**
-keep class com.yolanda.nohttp.**{*;}
-keep class com.litesuits.orm.db.annotation.**{*;}
-keep class com.litesuits.orm.db.enums.**{*;}

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

-keep class com.omi.bean.**{*;}    #不进行混淆编译
-keep class com.bqmm.bean.**{*;}    #不进行混淆编译

-keep class **.R$* {
    *;
}

-keep class * implements java.io.Serializable       # 保持 Serializable 不被混淆
-keepclasseswithmembernames class * {           # 保持 native 方法不被混淆
  native <methods>;
}

-keepclasseswithmembers class * {            # 保持自定义控件类不被混淆
  public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {            # 保持自定义控件类不被混淆
  public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {    #保持类成员
 public void *(android.view.View);
}

-keepclassmembers enum * {                  # 保持枚举 enum 类不被混淆
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {    # 保持 Parcelable 不被混淆
  public static final android.os.Parcelable$Creator *;
}

##############################  Glide  ################################
-keep public class * implements com.bumptech.glide.module.GlideModule
##############################  环信  ################################
-keep class com.hyphenate.** {*;}
-keep class com.superrtc.** {*;}
##############################  高德  ################################
#3D 地图
-keep class com.amap.api.mapcore.**{*;}
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.*{*;}
#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
#搜索
-keep class com.amap.api.services.**{*;}
#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}
##############################  支付宝  ################################
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}