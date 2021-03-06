
package com.carit.flashman.amap;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.ItemizedOverlay.OnFocusChangeListener;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Projection;
import com.carit.flashman.FlashManApplication;
import com.carit.flashman.R;

@SuppressWarnings("rawtypes")
public class PathOverlay extends ItemizedOverlay<OverlayItem> implements OnFocusChangeListener{
    private List<GeoPoint> points;

    //private Directions mDirections;

    private Paint paint;
    
    private Drawable mPin;
    
    private MapView mMapView;
    private MapController mMapCtrl;
    private View mPopView;
    
    private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

  

    public PathOverlay(List<GeoPoint> points,final List<OverlayItem> overlays,Drawable pin) {
        super(boundCenterBottom(pin));
        this.points = points;
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAlpha(150);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(4);
        mOverlays = overlays;
        
    }
    
    public PathOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
    }
    public PathOverlay(List<GeoPoint> points,MapView mapView, MapController mapCtrl,Drawable pin) {
        super(boundCenterBottom(pin));
        this.points = points;
        paint = new Paint();
        setOnFocusChangeListener(this);
        paint.setColor(Color.BLUE);
        paint.setAlpha(150);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(4);
        mPin = pin;
        mMapView = mapView;
        mMapCtrl = mapCtrl;
        mPopView = View.inflate(FlashManApplication.getContext(), R.layout.long_press_popup, null);
        mMapView.addView(mPopView, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
                MapView.LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.BOTTOM_CENTER));
        mPopView.setVisibility(View.GONE);
        populate(); // Add this
    }

    /*public PathOverlay(final Directions directions,Drawable pin) {
        super(boundCenterBottom(pin));
        mDirections = directions;
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAlpha(150);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);  
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(4);
        if (mDirections != null && mDirections.getRoutes() != null) {
            Log.e("MapMode", mDirections.getStatus());
            this.points = decodePoly(mDirections.getRoutes()[0].getOverview_polyline().getPoints());
            
        }
    }*/

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        /*if (!shadow) {// 不是绘制shadow层
            Projection projection = mapView.getProjection();
            if (points != null && points.size() >= 2) {// 画线
                Point start = new Point();
                projection.toPixels(points.get(0), start);// 需要转换坐标
                for (int i = 1; i < points.size(); i++) {
                    Point end = new Point();
                    projection.toPixels(points.get(i), end);
                    canvas.drawLine(start.x, start.y, end.x, end.y, paint);// 绘制到canvas上即可
                    start = end;
                }
            }
        }*/
        //if(shadow){
            Projection proj = mapView.getProjection();

            /*Clear the old path at first*/
            Path path = new Path();
            /* The first tap */
            Point tempPoint = new Point();
            for(int i=0;i<points.size();i++){
                tempPoint = proj.toPixels(points.get(i), null);
                if(i<1){
                    path.moveTo(tempPoint.x, tempPoint.y);
                }
                else{
                    path.lineTo(tempPoint.x, tempPoint.y);
                }
            }
            /* If indeed is a polygon just close the perimeter */
            canvas.drawPath(path, paint);
            
        //}
        super.draw(canvas, mapView, shadow);
        boundCenterBottom(mPin);
    }

    public List<GeoPoint> getPoints() {
        return points;
    }

    public void setPoints(List<GeoPoint> points) {
        this.points = null;
        this.points = points;
    }

    private List<GeoPoint> decodePoly(String encoded) {

        List<GeoPoint> poly = new ArrayList<GeoPoint>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            GeoPoint p = new GeoPoint((int) (((double) lat / 1E5) * 1E6),
                    (int) (((double) lng / 1E5) * 1E6));
            poly.add(p);
        }

        return poly;
    }

   /* public Directions getDirections() {
        return mDirections;
    }

    public void setDirections(Directions directions) {
        this.points = null;
        this.mDirections = null;
        this.mDirections = directions;
        if (mDirections != null && mDirections.getRoutes() != null) {
            Log.e("MapMode", mDirections.getStatus());
            this.points = decodePoly(mDirections.getRoutes()[0].getOverview_polyline().getPoints());
            mOverlays.clear();
            
        }
        
    }
*/
    @Override
    protected OverlayItem createItem(int i) {
        return mOverlays.get(i);
    }

    @Override
    public int size() {
        return mOverlays.size();
    }
    
    public void addOverlay(OverlayItem item) {
        mOverlays.add(item);
        populate();
    }

    public void removeOverlay(int location) {
        mOverlays.remove(location);
    }
    
    public List<OverlayItem> getOverlays() {
        return mOverlays;
    }

    public void setOverlays(List<OverlayItem> overlays) {
        this.mOverlays = overlays;
        populate();
    }
    
    public void cleanOverlayItem() {
        points.clear();
        mOverlays.clear();
        populate();
    }

    @Override
    public void onFocusChanged(ItemizedOverlay overlay, OverlayItem newFocus) {
        if (null != newFocus) {
            MapView.LayoutParams params = (MapView.LayoutParams) mPopView.getLayoutParams();
            params.x = mPin.getBounds().centerX();//Y轴偏移
            params.y = - mPin.getBounds().height();//Y轴偏移
            params.point = newFocus.getPoint();
            mMapCtrl.animateTo(newFocus.getPoint());
            //mPopView.findViewById(R.id.ImageButtonRight).setTag(newFocus.getPoint());
            TextView title_TextView = (TextView) mPopView.findViewById(R.id.ImageButton01);
            title_TextView.setText(Html.fromHtml(newFocus.getTitle()));
            TextView desc_TextView = (TextView) mPopView.findViewById(R.id.TextView02);
            if(null == newFocus.getSnippet() || "".equals(newFocus.getSnippet())){
                desc_TextView.setVisibility(View.GONE);
            }else{
                desc_TextView.setText(newFocus.getSnippet());
                desc_TextView.setVisibility(View.VISIBLE);
            }
            mMapView.updateViewLayout(mPopView, params);
            mPopView.setVisibility(View.VISIBLE);
        }else{
            mPopView.setVisibility(View.GONE);
        }
        
    }

}
