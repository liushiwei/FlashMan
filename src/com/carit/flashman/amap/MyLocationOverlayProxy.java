package com.carit.flashman.amap;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.amap.mapapi.map.MapView;
import com.carit.flashman.util.Common;

/**
 * 
 *自定义MyLocationOverlay。更换MyLocation图标。
 */
public class MyLocationOverlayProxy extends com.amap.mapapi.map.MyLocationOverlay{

	 private Context mContext;
	 public MyLocationOverlayProxy(Context context, MapView mapView) {
		super(context, mapView);

		mContext = context;
	 }
	
	

	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub	
        Toast.makeText(mContext,"Location provider = "+location.getProvider(), Toast.LENGTH_SHORT).show();
        if(!com.map.projection.Projection.isInit()){
            Toast.makeText(mContext,"Projection.isInit false", Toast.LENGTH_SHORT).show();
        }
        if (LocationManager.GPS_PROVIDER.equals(location.getProvider())&&com.map.projection.Projection.isInit()) {
            double [] latlng =Common.adjustLatLng(location.getLatitude(), location.getLongitude());
            location.setLatitude(latlng[0]);
            location.setLongitude(latlng[1]);
        }
        Common.saveLocation(location);
		super.onLocationChanged(location);
	}
	

	
	

}
