package com.carit.flashman.amap;

import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Projection;
import com.carit.flashman.R;
import com.carit.flashman.R.drawable;
import com.carit.flashman.provider.LocationTable;

/**
 * 
 *自定义MyLocationOverlay。更换MyLocation图标。
 */
public class MyLocationOverlayProxy extends com.amap.mapapi.map.MyLocationOverlay{
//	 private Location mLocation;
//	 protected final Paint mPaint = new Paint();
//	 protected final Paint mCirclePaint = new Paint();
//	 private Bitmap gps_marker=null;
//	 private Point mMapCoords = new Point();
//	 private final float gps_marker_CENTER_X;
//	 private final float gps_marker_CENTER_Y;
//     private final LinkedList<Runnable> mRunOnFirstFix = new LinkedList<Runnable>();
	 private Context mContext;
	 public MyLocationOverlayProxy(Context context, MapView mapView) {
		super(context, mapView);
//		gps_marker = ((BitmapDrawable) context.getResources().getDrawable(
//				R.drawable.icon_locr_light)).getBitmap();
//		gps_marker_CENTER_X = gps_marker.getWidth() / 2 - 0.5f;
//		gps_marker_CENTER_Y= gps_marker.getHeight() / 2 - 0.5f;
		mContext = context;
	 }
	
	
//	@Override
//	public boolean runOnFirstFix(final Runnable runnable) {
//		if (mLocation != null) {
//			new Thread(runnable).start();
//			return true;
//		} else {
//			mRunOnFirstFix.addLast(runnable);
//			return false;
//		}
//    }
//	
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub	
        Toast.makeText(mContext,"Location provider = "+location.getProvider(), Toast.LENGTH_SHORT).show();
        Log.e("MyLocationOverlayProx", "com.map.projection.Projection.isInit() ="+com.map.projection.Projection.isInit());
        if(!com.map.projection.Projection.isInit()){
            Toast.makeText(mContext,"Projection.isInit false", Toast.LENGTH_SHORT).show();
        }
        if (!"lbs".equals(location.getProvider())&&com.map.projection.Projection.isInit()) {
            double [] latlng =com.map.projection.Projection.adjustLatLng(location.getLatitude(), location.getLongitude());
            location.setLatitude(latlng[0]);
            location.setLongitude(latlng[1]);
        }
        saveLocation(location);
		super.onLocationChanged(location);
	}
	
//	
//	@Override
//	protected void drawMyLocation(Canvas canvas,  MapView mapView, final Location mLocation,
//			GeoPoint point, long time) {
//	    Projection pj=mapView.getProjection();
//        if (mLocation != null) {
//                mMapCoords=pj.toPixels(point, null);
//				final float radius = pj.metersToEquatorPixels(mLocation.getAccuracy());	
//				this.mCirclePaint.setAntiAlias(true);
//				this.mCirclePaint.setARGB(35, 131, 182, 222);
//				this.mCirclePaint.setAlpha(50);
//				this.mCirclePaint.setStyle(Style.FILL);
//				canvas.drawCircle(mMapCoords.x, mMapCoords.y, radius, this.mCirclePaint);
//				this.mCirclePaint.setARGB(225, 131, 182, 222);
//				this.mCirclePaint.setAlpha(150);
//				this.mCirclePaint.setStyle(Style.STROKE);
//				canvas.drawCircle(mMapCoords.x, mMapCoords.y, radius, this.mCirclePaint);				
//				canvas.drawBitmap(gps_marker, mMapCoords.x-gps_marker_CENTER_X, mMapCoords.y-gps_marker_CENTER_Y, this.mPaint);
//		}
//  }
//	
	private void saveLocation(Location location) {
        ContentValues values = new ContentValues();
        values.put(LocationTable.TIME, location.getTime());
        values.put(LocationTable.LAT, location.getLatitude());
        values.put(LocationTable.LNG, location.getLongitude());
        Uri uri = mContext.getContentResolver()
                .insert(LocationTable.CONTENT_URI, values);
    }
	

}
