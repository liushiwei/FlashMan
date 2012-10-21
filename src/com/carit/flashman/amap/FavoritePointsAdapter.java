package com.carit.flashman.amap;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carit.flashman.R;
import com.carit.flashman.dao.FavoritePoint;
import com.carit.flashman.provider.FavoritePointTable;
import com.carit.flashman.util.Common;

public class FavoritePointsAdapter extends CursorAdapter {
    
    private int mLayout;
    
    private Context mContext;

    public FavoritePointsAdapter(Context context, Cursor c, final int layout) {
        super(context, c,true);
        mLayout = layout;
        mContext = context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(FavoritePointTable.TITLE));
        String describe = cursor.getString(cursor.getColumnIndex(FavoritePointTable.DESCRIBE));
        TextView textView_title = (TextView) view.findViewById(R.id.title);
        textView_title.setText(title);
        textView_title = (TextView) view.findViewById(R.id.describe);
        textView_title.setText(describe);
        FavoritePoint point = null;
        if(view.getTag()==null)
            point = new FavoritePoint();
        else
            point = (FavoritePoint) view.getTag();
        point.setTitle(title);
        point.setDescribe(describe);
        point.setLat(cursor.getString(cursor.getColumnIndex(FavoritePointTable.LAT)));
        point.setLng(cursor.getString(cursor.getColumnIndex(FavoritePointTable.LNG)));
        point.setId(cursor.getLong(cursor.getColumnIndex(FavoritePointTable._ID)));
        view.setTag(point);
    }

    @Override
    public View newView(Context context, Cursor arg1, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(mLayout, parent, false);
                FavoritePoint tag = new FavoritePoint();
                view.setTag(tag);
                Button delete = (Button) view.findViewById(R.id.delete);
                delete.setOnClickListener((FavoritePointsActivity)mContext);
                return view;
    }

   
}
