package com.omi.utils.amap;

import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkStep;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by SensYang on 2016/7/6 0006.
 */
public class AMapUtil {
    public static final String Kilometer = "\u516c\u91cc";// "公里";
    public static final String Meter = "\u7c73";// "米";

    public static String getFriendlyTime(int second) {
        if (second > 3600) {
            int hour = second / 3600;
            int miniate = (second % 3600) / 60;
            return hour + "小时" + miniate + "分钟";
        }
        if (second >= 60) {
            int miniate = second / 60;
            return miniate + "分钟";
        }
        return second + "秒";
    }

    public static String getLength(int lenMeter) {
        if (lenMeter > 10000) // 10 km
        {
            int dis = lenMeter / 1000;
            return dis + "";// "公里";
        }

        if (lenMeter > 1000) {
            float dis = (float) lenMeter / 1000;
            DecimalFormat fnum = new DecimalFormat("##0.0");
            return fnum.format(dis);
        }

        if (lenMeter > 100) {
            int dis = lenMeter / 50 * 50;
            return dis + "";
        }

        int dis = lenMeter / 10 * 10;
        if (dis == 0) {
            dis = 10;
        }
        return dis + "";
    }

    public static String getEnglishLength(int lenMeter) {
        if (lenMeter > 1000) {
            return getLength(lenMeter) + "km";
        }
        return getLength(lenMeter) + "m";
    }

    public static String getFriendlyLength(int lenMeter) {
        if (lenMeter > 1000) {
            return getLength(lenMeter) + Kilometer;
        }
        return getLength(lenMeter) + Meter;
    }

    /**
     * 获取公交线路标题
     */
    public static String getBusPathTitle(BusPath busPath) {
        if (busPath == null) {
            return String.valueOf("");
        }
        List<BusStep> busSetps = busPath.getSteps();
        if (busSetps == null) {
            return String.valueOf("");
        }
        StringBuilder sb = new StringBuilder();
        for (BusStep busStep : busSetps) {
            if (busStep.getBusLines().size() > 0) {
                RouteBusLineItem busline = busStep.getBusLines().get(0);
                if (busline == null) {
                    continue;
                }
                String buslineName = getSimpleBusLineName(busline.getBusLineName());
                sb.append(buslineName);
                sb.append(" > ");
            }
        }
        return sb.substring(0, sb.length() - 3);
    }

    /**
     * 获取公交线路简介
     */
    public static String getBusPathDes(BusPath busPath) {
        if (busPath == null) {
            return String.valueOf("");
        }
        long second = busPath.getDuration();
        String time = getFriendlyTime((int) second);
        float subDistance = busPath.getDistance();
        String subDis = getFriendlyLength((int) subDistance);
        float walkDistance = busPath.getWalkDistance();
        String walkDis = getFriendlyLength((int) walkDistance);
        return String.valueOf(time + " | " + subDis + " | 步行" + walkDis);
    }

    /**
     * 获取驾车路线标题
     */
    public static String getDrivePathTitle(DrivePath drivePath) {
        if (drivePath == null) {
            return String.valueOf("");
        }
        List<DriveStep> driveSteps = drivePath.getSteps();
        if (driveSteps == null) {
            return String.valueOf("");
        }
        StringBuilder sb = new StringBuilder();
        boolean start = true;
        for (DriveStep driveStep : driveSteps) {
            if (driveStep.getRoad() != null && driveStep.getRoad().length() > 0)
                if (start) {
                    sb.append("途径 ").append(driveStep.getRoad());
                    start = false;
                } else {
                    sb.append(" 和 ").append(driveStep.getRoad());
                    break;
                }
        }
        return sb.toString();
    }

    /**
     * 获取驾车路线简介
     */
    public static String getDrivePathDes(DrivePath drivePath) {
        if (drivePath == null) {
            return String.valueOf("");
        }
        long second = drivePath.getDuration();
        String time = getFriendlyTime((int) second);
        float subDistance = drivePath.getDistance();
        String subDis = getFriendlyLength((int) subDistance);
        return String.valueOf(time + " | " + subDis);
    }

    /**
     * 获取步行路线标题
     */
    public static String getWalkPathTitle(WalkPath walkPath) {
        if (walkPath == null) {
            return String.valueOf("");
        }
        List<WalkStep> walkSteps = walkPath.getSteps();
        if (walkSteps == null) {
            return String.valueOf("");
        }
        StringBuilder sb = new StringBuilder();
        boolean start = true;
        for (WalkStep walkStep : walkSteps) {
            if (walkStep.getRoad() != null && walkStep.getRoad().length() > 0)
                if (start) {
                    sb.append("途径 ").append(walkStep.getRoad());
                    start = false;
                } else {
                    sb.append(" 和 ").append(walkStep.getRoad());
                    break;
                }
        }
        return sb.toString();
    }

    /**
     * 获取步行路线简介
     */
    public static String getWalkPathDes(WalkPath walkPath) {
        if (walkPath == null) {
            return String.valueOf("");
        }
        long second = walkPath.getDuration();
        String time = getFriendlyTime((int) second);
        float subDistance = walkPath.getDistance();
        String subDis = getFriendlyLength((int) subDistance);
        return String.valueOf(time + " | " + subDis);
    }

    public static String getSimpleBusLineName(String busLineName) {
        if (busLineName == null) {
            return String.valueOf("");
        }
        return busLineName.replaceAll("\\(.*?\\)", "");
    }

}
