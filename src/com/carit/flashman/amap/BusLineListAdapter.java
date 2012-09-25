
package com.carit.flashman.amap;

import com.carit.flashman.provider.BusLineRelevanceTable;
import com.carit.flashman.provider.BusLineTable;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CursorTreeAdapter;
import android.widget.SimpleCursorTreeAdapter;

public class BusLineListAdapter extends CursorTreeAdapter {

    private Context mContext;

    public BusLineListAdapter(Cursor cursor, Context context) {
        super(cursor, context);
        mContext = context;
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        String lineId = groupCursor.getString(groupCursor.getColumnIndex(BusLineTable.LINEID));
        Cursor busStations = mContext.getContentResolver().query(BusLineRelevanceTable.CONTENT_URI,
                new String[] {
                    BusLineRelevanceTable.BUSSTATIONID
                }, BusLineRelevanceTable.BUSLINEID + "=" + lineId, null, null);
        if (busStations.moveToFirst()){
            String where = "";
            do {
                int busStationsId = busStations.getInt(busStations.getColumnIndex(BusLineRelevanceTable.BUSSTATIONID));
                where+=busStationsId+",";
            } while (busStations.moveToNext());
        }else{
            return null;    
        }
        return null;
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        // TODO Auto-generated method stub

    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild,
            ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        // TODO Auto-generated method stub

    }

}
