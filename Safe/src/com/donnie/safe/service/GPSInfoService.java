package com.donnie.safe.service;

import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**  
 * @Title: GPSInfoService.java
 * @Package com.donnie.safe.service
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年7月1日 下午2:42:08   
 * @version V1.0  
 */
public class GPSInfoService {
	
	private static GPSInfoService gpsInfoService;
	private LocationManager locationManager;
	private Context context;
	
	private GPSInfoService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	}
	
	public static GPSInfoService getInstance(Context context){
		if (gpsInfoService == null) {
			gpsInfoService = new GPSInfoService(context);
		}
		return gpsInfoService;
	}
	
	public void registerLocationChangeListener(){
		//List<String> providers = locationManager.getAllProviders();
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setSpeedRequired(false);
		String provider = locationManager.getBestProvider(criteria, true);
		locationManager.requestLocationUpdates(provider, 60000, 0, getListener());
	}
	
	public void unRegisterLocationChangeListener(){
		locationManager.removeUpdates(getListener());
	}
	
	private MyLocationListener listener;
	
	private MyLocationListener getListener(){
		if (listener == null) {
			listener = new MyLocationListener();
		}
		return listener;
	}
	
	public String getLastLocation(){
		return SafePreference.getStr(context, Const.LAST_LOCATION);
	}
	
	private final class MyLocationListener implements LocationListener{

		@Override//位置改变
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			
			String last_location = "longitude:"+longitude+",latitude:"+latitude;
			SafePreference.save(context, Const.LAST_LOCATION, last_location);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	}
}
