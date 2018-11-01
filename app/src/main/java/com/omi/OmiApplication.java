package com.omi;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.omi.config.Config;
import com.omi.net.easemob.ReceiveMessageCallbackVO;
import com.omi.ui.activity.main.MainActivity;
import com.omi.utils.Log;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SensYang on 2017/03/07 20:05
 */

public class OmiApplication extends MultiDexApplication {
    private ArrayList<Activity> activityList = new ArrayList<>();
    private static OmiApplication instance;
    private Resources resources;

    @Override
    public Resources getResources() {
        if (resources == null) resources = super.getResources();
        return resources;
    }

    public static OmiApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        NoHttp.initialize(this);
        Logger.setDebug(Config.ISDEBUG); // 开启NoHttp调试模式。
        Logger.setTag("Omi_NoHttp--->"); // 设置NoHttp打印Log的TAG。
        //友盟场景配置
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //禁止默认的页面统计方式，这样将不会再自动统计Activity。
        MobclickAgent.openActivityDurationTrack(false);
        //腾讯错误日志统计
        CrashReport.initCrashReport(getApplicationContext());
        initEasemob();//初始化环信
        //初始化表情云的token
//        BQMMApi.getInstance().getToken();
    }

    //初始化环信
    private void initEasemob() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //设置是否需要接受方已读确认
        options.setRequireAck(true);
        //设置需要接受方送达确认
        options.setRequireDeliveryAck(true);
        //按照server收到时间进行排序
        options.setSortMessageByServerTime(true);
        //设置退出(主动和被动退出)群组时是否删除聊天消息
        options.setDeleteMessagesAsExitGroup(true);
        //设置是否自动接受加群邀请
        options.setAutoAcceptGroupInvitation(true);
//        //设置使用https通信
//        options.setUseHttps(true);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(Config.ISDEBUG);
        EMClient.getInstance().chatManager().addMessageListener(ReceiveMessageCallbackVO.getInstance());
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }

    public void clearActivitys() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
    }

    /**
     * 关闭栈顶的activity
     *
     * @param closeActivity 被关闭的activity
     */
    public void closeActivity(Class<? extends Activity> closeActivity) {
        Activity activity = null;
        for (int i = activityList.size() - 1; i >= 0; i--) {
            activity = activityList.get(i);
            if (activity.getClass().getName().equalsIgnoreCase(closeActivity.getName())) {
                activity.finish();
                break;
            } else {
                activity = null;
            }
        }
        if (activity != null) activityList.remove(activity);
    }

    /**
     * 关闭该activity之上的所有activity 包含自身一起关闭
     */
    public void closeActivitysTopWithSelf(Collection<Class<? extends Activity>> closeActivity) {
        closeActivitysTopWithSelf((Class<? extends Activity>[]) closeActivity.toArray());
    }

    /**
     * 关闭该activity之上的所有activity 包含自身一起关闭
     */
    @SafeVarargs
    public final void closeActivitysTopWithSelf(Class<? extends Activity>... closeActivity) {
        for (Activity activity : activityList) {
            for (Class clazz : closeActivity) {
                if (activity.getClass() == clazz) {
                    closeActivityTop(true, clazz);
                    return;
                }
            }
        }
    }

    /**
     * 关闭该activity之上的所有activity
     *
     * @param withSelf 是否包含自身一起关闭
     */
    public void closeActivityTop(boolean withSelf, Class<? extends Activity> closeActivity) {
        Log.e("closeActivityTop--->" + closeActivity.getName());
        ArrayList<Activity> closedActivitieList = new ArrayList<>();
        boolean beganClose = false;
        for (Activity activity : activityList) {
            if (!beganClose) {
                if (activity.getClass().getName().equalsIgnoreCase(closeActivity.getName())) {
                    beganClose = true;
                    if (withSelf) {
                        closedActivitieList.add(activity);
                    }
                }
            } else {
                closedActivitieList.add(activity);
            }
        }
        Log.e("closeActivityTop--->开始关闭" + closedActivitieList.size() + "个activity  " + closedActivitieList);
        for (int i = closedActivitieList.size() - 1; i >= 0; i--) {
            closedActivitieList.get(i).finish();
        }
        Log.e("closeActivityTop--->关闭已完成");
        activityList.removeAll(closedActivitieList);
    }


    /**
     * 关闭两个Activity之间的所有Activity
     *
     * @param fromActivity activity堆栈中保留的第一个activity
     * @param toActivity   activity堆栈中保留的第二个activity
     */
    public void closeActivitysBetween(Class<? extends Activity> fromActivity, Class<? extends Activity> toActivity) {
        ArrayList<Activity> finishList = new ArrayList<>();
        String name;
        for (int i = activityList.size() - 1; i >= 0; i--) {
            Activity activity = activityList.get(i);
            name = activity.getClass().getName();
            if (name.equalsIgnoreCase(fromActivity.getName()) || name.equalsIgnoreCase(toActivity.getName())) {
                continue;
            }
            if (name.equalsIgnoreCase(fromActivity.getName()) || name.equalsIgnoreCase(toActivity.getName())) {
                break;
            } else if (name.equalsIgnoreCase(MainActivity.class.getName())) {
                break;
            } else {
                activity.finish();
                activity.overridePendingTransition(0, 0);
                finishList.add(activity);
            }
        }
        activityList.removeAll(finishList);
    }

    /**
     * 获取顶部Activity
     */
    public Activity getTopActivity() {
        if (activityList.size() > 0) return activityList.get(activityList.size() - 1);
        else return null;
    }

    /**
     * 在堆栈中获取Activity
     */
    public Activity getActivityFromTask(Class<? extends Activity> activityClass) {
        Activity activity;
        for (int i = activityList.size() - 1; i >= 0; i--) {
            activity = activityList.get(i);
            if (activity.getClass().getName().equalsIgnoreCase(activityClass.getName())) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 记录一条Activity信息
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 删除一条Activity的信息
     */
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    @Override
    public void onTerminate() {
        clearActivitys();
        super.onTerminate();
        System.exit(0);
    }
}
