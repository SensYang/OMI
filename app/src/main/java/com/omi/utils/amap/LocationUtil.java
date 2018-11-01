package com.omi.utils.amap;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.omi.OmiApplication;

/**
 * 高的定位工具类
 */
public class LocationUtil implements AMapLocationListener {
    private static LocationUtil instance;
    private AMapLocationClient mlocationClient;
    private PoiItem poiItem;
    private AMapLocation amapLocation;
    private String city;
    private String locationInfo;

    private LocationUtil() {
    }

    public String getCity() {
        return city;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public static LocationUtil getInstance() {
        if (instance == null) {
            instance = new LocationUtil();
        }
        return instance;
    }

    /**
     * 定位监听
     */
    private AMapLocationListener listener;

    /**
     * 设置定位监听器
     */
    public void setLocationListener(AMapLocationListener locationListener) {
        this.listener = locationListener;
    }

    /**
     * 定位监听
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                if (amapLocation.getLatitude() != 0 && amapLocation.getLongitude() != 0) {
                    //定位成功回调信息，设置相关消息
                    //amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    //amapLocation.getLatitude();//获取纬度
                    //amapLocation.getLongitude();//获取经度
                    //amapLocation.getAccuracy();//获取精度信息
                    //amapLocation.get Time();//定位时间
                    //amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    //amapLocation.getCountry();//国家信息
                    //amapLocation.getProvince();//省信息
                    //amapLocation.getCity();//城市信息
                    //amapLocation.getDistrict();//城区信息
                    //amapLocation.getStreet();//街道信息
                    //amapLocation.getStreetNum();//街道门牌号信息
                    //amapLocation.getCityCode();//城市编码
                    //amapLocation.getAdCode();//地区编码
                    this.amapLocation = amapLocation;
                    this.city = amapLocation.getCity();
                    //                    locationInfo
                    if (amapLocation.getProvince() != null) {
                        locationInfo = amapLocation.getProvince();
                        if (amapLocation.getCity() != null) {
                            locationInfo += amapLocation.getCity();
                            if (amapLocation.getDistrict() != null) {
                                locationInfo += amapLocation.getDistrict();
                                if (amapLocation.getStreet() != null) {
                                    locationInfo += amapLocation.getStreet();
                                }
                            }
                        }
                    }
                    if (listener != null) {
                        listener.onLocationChanged(amapLocation);
                    }
                }
            }
        }
    }

    /**
     * 开始获取位置信息
     */
    public void startLocation() {
        if (null == mlocationClient) {
            //初始化定位
            mlocationClient = new AMapLocationClient(OmiApplication.getInstance().getApplicationContext());
            setLocationOption();
        }
        //设置定位回调监听
        mlocationClient.setLocationListener(this);

        if (mlocationClient.isStarted()) {
            mlocationClient.stopLocation();
        }
        mlocationClient.startLocation();
    }

    /**
     * 设置获取位置信息的相关参数
     */
    private void setLocationOption() {
        //初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否主动刷新WIFI，默认为主动刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(60 * 1000);
        //设置退出时是否杀死service
        mLocationOption.setKillProcess(true);
        //给定位客户端对象设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
    }

    /**
     * 结束位置信息获取
     */
    public void stopLocation() {
        if (mlocationClient != null && mlocationClient.isStarted()) {
            mlocationClient.stopLocation();
        }
    }

    /**
     * 获取位置信息的经纬度对象
     */
    public AMapLocation getLocation() {
        return amapLocation;
    }

    /**
     * 获取当前位置的城市信息
     */
    public PoiItem getPoiItem() {
        return poiItem;
    }

    /**
     * 反解坐标监听器
     */
    public interface OnDecodeLocationListener {
        void onDecodeLocation(PoiItem poiItem);
    }

    private OnDecodeLocationListener onDecodeLocationListener;

    public void setOnDecodeLocationListener(OnDecodeLocationListener onDecodeLocationListener) {
        this.onDecodeLocationListener = onDecodeLocationListener;
    }

    /**
     * 根据经纬度反解出当前的位置信息
     */
    public void decodeLocation(OnDecodeLocationListener listener) {
        this.onDecodeLocationListener = listener;
        GeocodeSearch geocodeSearch = new GeocodeSearch(OmiApplication.getInstance().getApplicationContext());
        geocodeSearch.setOnGeocodeSearchListener(new OnGeocodeSearchListener() {

            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int errorCode) {
                if (null != regeocodeResult && errorCode == 0) {
                    RegeocodeAddress regecodeAddress = regeocodeResult.getRegeocodeAddress();
                    double latitude = amapLocation == null ? 0 : amapLocation.getLatitude();
                    double longitude = amapLocation == null ? 0 : amapLocation.getLongitude();
                    LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
                    poiItem = new PoiItem("firstPoi", latLonPoint, regecodeAddress.getCity(), regecodeAddress.getProvince() + regecodeAddress.getCity());
                    if (onDecodeLocationListener != null)
                        onDecodeLocationListener.onDecodeLocation(regecodeAddress.getPois().get(0));
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int errorCode) {

            }
        });
        double latitude = amapLocation == null ? 0 : amapLocation.getLatitude();
        double longitude = amapLocation == null ? 0 : amapLocation.getLongitude();
        LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(latLonPoint, 1000, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
    }

    /**
     * 计算地球上任意两点(经纬度)距离
     *
     * @param long1 第一点经度
     * @param lat1  第一点纬度
     * @param long2 第二点经度
     * @param lat2  第二点纬度
     * @return 返回距离 单位：米
     */
    public double calculateDistance(double long1, double lat1, double long2, double lat2) {
        double a, b, R;
        R = 6378137; // 地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
        return d;
    }

    /**
     * 计算地球上任意两点(经纬度)距离
     *
     * @param long1 第一点经度
     * @param lat1  第一点纬度
     * @param long2 第二点经度
     * @param lat2  第二点纬度
     * @return 返回距离 单位：千米
     */
    public double calculateKilometreDistance(double long1, double lat1, double long2, double lat2) {
        return calculateDistance(long1, lat1, long2, lat2) / 1000;
    }
}