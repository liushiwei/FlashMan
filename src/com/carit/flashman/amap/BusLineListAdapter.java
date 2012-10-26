
package com.carit.flashman.amap;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import com.carit.flashman.provider.BusLineRelevanceTable;
import com.carit.flashman.provider.BusLineTable;

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
                            BusLineRelevanceTable.BUSSTATIONNAME
                    }, BusLineRelevanceTable.BUSLINEID + "=" + lineId, null, null);
        } else
            return null;
        }else
            return null;

    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        TextView view = (TextView) mInflater.inflate(
                android.R.layout.simple_expandable_list_item_1, parent, false);

        return view;
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(BusLineTable.NAME)));

    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild,
            ViewGroup parent) {
        TextView view = (TextView) mInflater.inflate(
                android.R.layout.simple_expandable_list_item_1, parent, false);
        return view;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        ((TextView) view).setText(cursor.getString(cursor
                .getColumnIndex(BusLineRelevanceTable.BUSSTATIONNAME)));

    }
    
    

}
