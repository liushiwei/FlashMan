package com.carit.flashman.amap;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carit.flashman.R;
import com.carit.flashman.dao.FavoritePoint;
import com.carit.flashman.provider.FavoritePointTable;

public class FavoritePointsAdapter extends CursorAdapter {
    
    private int mLayout;

    public FavoritePointsAdapter(Context context, Cursor c, final int layout) {
        super(context, c, false);
        mLayout = layout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(cursor.getString(cursor.getColumnIndex(FavoritePointTable.TITLE)));
        title = (TextView) view.findViewById(R.id.describe);
        title.setText(cursor.getString(cursor.getColumnIndex(FavoritePointTable.DESCRIBE)));
        
    }

    @Override
    public View newView(Context context, Cursor arg1, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(mLayout, parent, false);
                FavoritePoint tag = new FavoritePoint();
                view.setTag(tag);
                return view;
    }

}
