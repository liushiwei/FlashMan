
package com.carit.flashman.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.carit.flashman.R;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class FlashManProvider extends ContentProvider {
    private static final String TAG = "FlashManProvider";

    private static final String DATABASE_NAME = "FlashMan.db";

    private static final int DATABASE_VERSION = 3;
    
    public static final String AUTHORITY = "com.carit.flashman.provider.FlashManProvider";

    private DatabaseHelper mOpenHelper;

    private static final UriMatcher sUriMatcher;


    private static final int LOCATION_TABLE_ID = 1;

    public static final int TABLE_NO = 1;

    @Override
    public boolean onCreate() {
        Log.e(TAG, "Database Create!");
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case LocationTable.TABLE_NO:
                qb.setTables(LocationTable.TABLE_NAME);
                qb.setProjectionMap(LocationTable.tableProjectionMap);
                break;

            case CityTable.TABLE_NO:
                qb.setTables(CityTable.TABLE_NAME);
                qb.setProjectionMap(CityTable.tableProjectionMap);
                break;
            case PoiTable.TABLE_NO:
                qb.setTables(PoiTable.TABLE_NAME);
                qb.setProjectionMap(PoiTable.tableProjectionMap);
                break;
            case BusLineTable.TABLE_NO:
                qb.setTables(BusLineTable.TABLE_NAME);
                qb.setProjectionMap(BusLineTable.tableProjectionMap);
                break;
            case BusLineRelevanceTable.TABLE_NO:
                qb.setTables(BusLineRelevanceTable.TABLE_NAME);
                qb.setProjectionMap(BusLineRelevanceTable.tableProjectionMap);
                break;
            case BusStationTable.TABLE_NO:
                qb.setTables(BusStationTable.TABLE_NAME);
                qb.setProjectionMap(BusStationTable.tableProjectionMap);
                break;
             
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // If no sort order is specified use the default
        String orderBy=null;
        if (!TextUtils.isEmpty(sortOrder)) {
            orderBy = sortOrder;
        }

        // Get the database and run the query
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
        // Tell the cursor what uri to watch, so it knows when its source data
        // changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case LocationTable.TABLE_NO:
                return LocationTable.CONTENT_TYPE;
            case PoiTable.TABLE_NO:
                return PoiTable.CONTENT_TYPE;
            case BusLineTable.TABLE_NO:
                return BusLineTable.CONTENT_TYPE;
            case BusLineRelevanceTable.TABLE_NO:
                return BusLineRelevanceTable.CONTENT_TYPE;
            case BusStationTable.TABLE_NO:
                return BusStationTable.CONTENT_TYPE;
            case CityTable.TABLE_NO:
                return CityTable.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
//        if (sUriMatcher.match(uri) != LocationTable.TABLE_NO) {
//            throw new IllegalArgumentException("Unknown URI " + uri);
//        }

        ContentValues value;
        if (values != null) {
            value = new ContentValues(values);
        } else {
            value = new ContentValues();
        }
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = 0;
        switch (sUriMatcher.match(uri)) {
            case LocationTable.TABLE_NO:
                rowId = db.insert(LocationTable.TABLE_NAME, "_id", value);
                if (rowId > 0) {
                    Uri noteUri = ContentUris.withAppendedId(LocationTable.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(noteUri, null);
                    return noteUri;
                }

                break;
            case PoiTable.TABLE_NO:
                rowId = db.insert(PoiTable.TABLE_NAME, "_id", value);
                if (rowId > 0) {
                    Uri noteUri = ContentUris.withAppendedId(PoiTable.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(noteUri, null);
                    return noteUri;
                }

                break;
            case BusLineTable.TABLE_NO:
                rowId = db.insert(BusLineTable.TABLE_NAME, "_id", value);
                if (rowId > 0) {
                    Uri noteUri = ContentUris.withAppendedId(BusLineTable.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(noteUri, null);
                    return noteUri;
                }

                break;
            case BusLineRelevanceTable.TABLE_NO:
                rowId = db.insert(BusLineRelevanceTable.TABLE_NAME, "_id", value);
                if (rowId > 0) {
                    Uri noteUri = ContentUris.withAppendedId(BusLineRelevanceTable.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(noteUri, null);
                    return noteUri;
                }

                break;
            case BusStationTable.TABLE_NO:
                rowId = db.insert(BusStationTable.TABLE_NAME, "_id", value);
                if (rowId > 0) {
                    Uri noteUri = ContentUris.withAppendedId(BusStationTable.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(noteUri, null);
                    return noteUri;
                }

                break;
            case CityTable.TABLE_NO:
                rowId = db.insert(CityTable.TABLE_NAME, "_id", value);
                if (rowId > 0) {
                    Uri noteUri = ContentUris.withAppendedId(CityTable.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(noteUri, null);
                    return noteUri;
                }

                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case LocationTable.TABLE_NO:
                count = db.delete(LocationTable.TABLE_NAME, selection, selectionArgs);
                break;
            case PoiTable.TABLE_NO:
                count = db.delete(PoiTable.TABLE_NAME, selection, selectionArgs);
                break;
            case BusLineTable.TABLE_NO:
                count = db.delete(BusLineTable.TABLE_NAME, selection, selectionArgs);
                break;
            case BusLineRelevanceTable.TABLE_NO:
                count = db.delete(BusLineRelevanceTable.TABLE_NAME, selection, selectionArgs);
                break;
            case BusStationTable.TABLE_NO:
                count = db.delete(BusStationTable.TABLE_NAME, selection, selectionArgs);
                break;
            case CityTable.TABLE_NO:
                count = db.delete(CityTable.TABLE_NAME, selection, selectionArgs);
                break;
//            case LOCATION_TABLE_ID:
//                String callLogId = uri.getPathSegments().get(1);
//                count = db.delete(LocationTable.TABLE_NAME, LocationTable._ID + "=" + callLogId
//                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
//                        selectionArgs);
//                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private Context context;
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context =context; 
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.e(TAG, "Database Create!");
            db.execSQL(LocationTable.CREATE_SQL);
            db.execSQL(BusLineTable.CREATE_SQL);
            db.execSQL(BusStationTable.CREATE_SQL);
            db.execSQL(BusLineRelevanceTable.CREATE_SQL);
            db.execSQL(PoiTable.CREATE_SQL);
            db.execSQL(CityTable.CREATE_SQL);
            db.execSQL(CityTable.INDEX_SQL);
            try {
                insertFromFile(db,context,R.raw.city_table);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
        /**
         * This reads a file from the given Resource-Id and calls every line of it as a SQL-Statement
         * 
         * @param context
         *  
         * @param resourceId
         *  e.g. R.raw.food_db
         * 
         * @return Number of SQL-Statements run
         * @throws IOException
         */
        public int insertFromFile(SQLiteDatabase db,Context context, int resourceId) throws IOException {
            // Reseting Counter
            int result = 0;

            // Open the resource
            InputStream insertsStream = context.getResources().openRawResource(resourceId);
            BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

            // Iterate through lines (assuming each insert has its own line and theres no other stuff)
            while (insertReader.ready()) {
                String insertStmt = insertReader.readLine();
                db.execSQL(insertStmt);
                result++;
            }
            insertReader.close();

            // returning number of inserted rows
            return result;
        }
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, LocationTable.TABLE_NAME, LocationTable.TABLE_NO);
        sUriMatcher.addURI(AUTHORITY, PoiTable.TABLE_NAME, PoiTable.TABLE_NO);
        sUriMatcher.addURI(AUTHORITY, BusLineTable.TABLE_NAME, BusLineTable.TABLE_NO);
        sUriMatcher.addURI(AUTHORITY, BusStationTable.TABLE_NAME, BusStationTable.TABLE_NO);
        sUriMatcher.addURI(AUTHORITY, BusLineRelevanceTable.TABLE_NAME, BusLineRelevanceTable.TABLE_NO);
        sUriMatcher.addURI(AUTHORITY, CityTable.TABLE_NAME, CityTable.TABLE_NO);
        sUriMatcher.addURI(AUTHORITY, LocationTable.TABLE_NAME + "/#",
                LOCATION_TABLE_ID);

        
    }

}
