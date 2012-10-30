
package com.carit.flashman.amap;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import com.carit.flashman.R;
import com.carit.flashman.provider.BusLineRelevanceTable;
import com.carit.flashman.provider.BusLineTable;
import com.carit.flashman.provider.BusStationTable;

public class BusLineListAdapter extends CursorTreeAdapter {

    private Context mContext;

    private LayoutInflater mInflater;
    
    private boolean mIsSearchBusLine;

    public BusLineListAdapter(Cursor cursor, Context context,boolean isSearchBusLine) {
        super(cursor, context);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mIsSearchBusLine = isSearchBusLine;
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        if(mIsSearchBusLine){
        int index = groupCursor.getColumnIndex(BusLineTable.LINEID);
        Log.e("Adapter", "index = "+index);
        if (index != -1) {
            String lineId = groupCursor.getString(index);
            return ((BusLineSearch) mContext).managedQuery(BusLineRelevanceTable.CONTENT_URI,
                    new String[] {
                    BusLineRelevanceTable._ID,
                            BusLineRelevanceTable.BUSSTATIONID,
                            BusLineRelevanceTable.BUSSTATIONNAME,
                    }, BusLineRelevanceTable.BUSLINEID + "=" + lineId, null, null);
        } else
            return null;
        }else
            return null;

    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        if(mIsSearchBusLine){
        View view =  mInflater.inflate(
                R.layout.busline_list_item, parent, false);
        view.findViewById(R.id.map_view_busline).setOnClickListener((BusLineSearch)mContext);
        return view;
        }else{
            View view =  mInflater.inflate(
                    R.layout.busstation_list_item, parent, false);
            view.findViewById(R.id.map_view_busstation).setOnClickListener((BusLineSearch)mContext);
            view.findViewById(R.id.view_busline).setOnClickListener((BusLineSearch)mContext);
            view.findViewById(R.id.view_busline).setVisibility(View.VISIBLE);
            return view;
        }
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        if(mIsSearchBusLine){
        ((TextView) view.findViewById(android.R.id.text1)).setText(cursor.getString(cursor.getColumnIndex(BusLineTable.NAME)));
        BusLineData data = new BusLineData();
        data.setId(cursor.getString(cursor.getColumnIndex(BusLineTable.LINEID)));
        view.findViewById(R.id.map_view_busline).setTag(data);
        }else{
            ((TextView) view.findViewById(android.R.id.text1)).setText(cursor.getString(cursor.getColumnIndex(BusStationTable.NAME)));
            BusLineData data = new BusLineData();
            data.setLat(cursor.getDouble(cursor.getColumnIndex(BusStationTable.LAT)));
            data.setLng(cursor.getDouble(cursor.getColumnIndex(BusStationTable.LNG)));
            data.setName(cursor.getString(cursor.getColumnIndex(BusStationTable.NAME)));
            view.findViewById(R.id.map_view_busstation).setTag(data);  
        }

    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild,
            ViewGroup parent) {
        View view = mInflater.inflate(
                R.layout.busstation_list_item, parent, false);
        view.findViewById(R.id.map_view_busstation).setOnClickListener((BusLineSearch)mContext);
        view.findViewById(R.id.view_busline).setVisibility(View.GONE);
        return view;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        
        ((TextView) view.findViewById(android.R.id.text1)).setText(cursor.getString(cursor.getColumnIndex(BusLineRelevanceTable.BUSSTATIONNAME)));
        BusLineData data = new BusLineData();
        data.setId(cursor.getString(cursor.getColumnIndex(BusLineRelevanceTable.BUSSTATIONID)));
        data.setName(cursor.getString(cursor.getColumnIndex(BusLineRelevanceTable.BUSSTATIONNAME)));
        view.findViewById(R.id.map_view_busstation).setTag(data);  

    }
    
    class BusLineData{
        private double lat;
        private double lng;
        private String id;
        private String name;
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public double getLng() {
            return lng;
        }
        public void setLng(double lng) {
            this.lng = lng;
        }
        public double getLat() {
            return lat;
        }
        public void setLat(double lat) {
            this.lat = lat;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        
    }
    
    

}
