package com.omi.ui.activity.utils.amap;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.omi.R;
import com.omi.databinding.ActivitySelectLocationBinding;
import com.omi.net.ApiByHttp;
import com.omi.ui.base.BaseActivity;
import com.omi.utils.amap.LocationUtil;

import java.util.List;


/**
 * Created by SensYang on 2016/12/3.
 */

public class LocationSelectActivity extends BaseActivity implements LocationSource, GeocodeSearch.OnGeocodeSearchListener, AMap.OnCameraChangeListener {
    ActivitySelectLocationBinding binding;
    private AMap aMap;
    //搜索
    private GeocodeSearch geocoderSearch;
    private LatLng changedLatLng;
    private Intent resultIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_location);
        binding.mapView.onCreate(savedInstanceState);
        setUpMap();
        double latitude, longitude;
        if ((latitude = getIntent().getDoubleExtra("latitude", -1)) != -1 && (longitude = getIntent().getDoubleExtra("longitude", -1)) != -1) {
            aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latitude, longitude), 17, 0, 0)), 500, null);
        } else {
            backToLocation();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap = binding.mapView.getMap();
        if (aMap == null) return;
        aMap.setLocationSource(this);
        //设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14.0f));

        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setCompassEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        aMap.setOnCameraChangeListener(this);
    }


    @Override
    protected void onDestroy() {
        if (binding.mapView != null) binding.mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        LocationUtil.getInstance().stopLocation();
        if (binding.mapView != null) binding.mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (binding.mapView != null) binding.mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        LocationUtil.getInstance().startLocation();
        if (binding.mapView != null) binding.mapView.onResume();
        super.onResume();
    }

    public void topLeftClick(View view) {
        finish();
    }

    public void topRightClick(View view) {
        if (changedLatLng == null) return;
        resultIntent.putExtra("latitude", changedLatLng.latitude);
        resultIntent.putExtra("longitude", changedLatLng.longitude);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void onBackLocationClick(View view) {
        backToLocation();
    }

    private void backToLocation() {
        AMapLocation location;
        if ((location = LocationUtil.getInstance().getLocation()) != null) {
            aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), 17, 0, 0)), 500, null);
        } else {
            LocationUtil.getInstance().setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation location) {
                    ApiByHttp.getInstance().updateLocation(LocationUtil.getInstance().getLocation().getLongitude(), LocationUtil.getInstance().getLocation().getLatitude());
                    aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), 17, 0, 0)), 500, null);
                    LocationUtil.getInstance().stopLocation();
                    LocationUtil.getInstance().setLocationListener(null);
                }
            });
            LocationUtil.getInstance().startLocation();
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int errorCode) {
        if (errorCode == 1000) {
            RegeocodeAddress regecodeAddress = regeocodeResult.getRegeocodeAddress();
            String province = regecodeAddress.getProvince();
            String city = regecodeAddress.getCity();
            String area = regecodeAddress.getDistrict();
            String road = regecodeAddress.getFormatAddress().replace(province, "").replace(city, "").replace(area, "");
            resultIntent.putExtra("province", province);
            resultIntent.putExtra("city", city);
            resultIntent.putExtra("area", area);
            resultIntent.putExtra("road", road);
            binding.locationTV.setText(road);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int errorCode) {
        if (errorCode == 0) {
            List<GeocodeAddress> regecodeAddress = geocodeResult.getGeocodeAddressList();
            StringBuilder buffer = new StringBuilder();
            for (GeocodeAddress geocodeAddress : regecodeAddress) {
                buffer.append(geocodeAddress.getFormatAddress()).append("").append(geocodeAddress.getLatLonPoint().toString()).append("\n");
            }
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        changedLatLng = cameraPosition.target;
        //latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(new LatLonPoint(changedLatLng.latitude, changedLatLng.longitude), 3000, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(regeocodeQuery);
    }
}
