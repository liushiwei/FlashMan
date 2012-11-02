
package com.carit.flashman.provider;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.carit.flashman.R;

public class FlashManProvider extends ContentProvider {
    private static final String TAG = "FlashManProvider";

    private static final String DATABASE_NAME = "FlashMan.db";

    private static final int DATABASE_VERSION = 3;
    
    public static final String AUTHORITY = "com.carit.flashman.provider.FlashManProvider";

    private DatabaseHelper mOpenHelper;
    
    private StaticDataBaseHelper mStaticDBHelper;

    private static final UriMatcher sUriMatcher;


    private static final int LOCATION_TABLE_ID = 1;

    public static final int TABLE_NO = 1;

    @Override
    public boolean onCreate() {
        Log.e(TAG, "Database Create!");
        mOpenHelper = new DatabaseHelper(getContext());
        mStaticDBHelper = new StaticDataBaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        // Get the database and run the query
        SQLiteDatabase db =null;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case LocationTable.TABLE_NO:
                qb.setTables(LocationTable.TABLE_NAME);
                qb.setProjectionMap(LocationTable.tableProjectionMap);
                db = mOpenHelper.getReadableDatabase();
                break;

            case CityTable.TABLE_NO:
                qb.setTables(CityTable.TABLE_NAME);
                qb.setProjectionMap(CityTable.tableProjectionMap);
                db = mOpenHelper.getReadableDatabase();
                break;
            case FavoritePointTable.TABLE_NO:
                qb.setTables(FavoritePointTable.TABLE_NAME);
                qb.setProjectionMap(FavoritePointTable.tableProjectionMap);
                db = mOpenHelper.getReadableDatabase();
                break;
            case PoiTable.TABLE_NO:
                qb.setTables(PoiTable.TABLE_NAME);
                qb.setProjectionMap(PoiTable.tableProjectionMap);
                db = mOpenHelper.getReadableDatabase();
                break;
            case BusLineTable.TABLE_NO:
                qb.setTables(BusLineTable.TABLE_NAME);
                qb.setProjectionMap(BusLineTable.tableProjectionMap);
                db = mStaticDBHelper.getReadableDatabase();
                break;
            case BusLineRelevanceTable.TABLE_NO:
                qb.setTables(BusLineRelevanceTable.TABLE_NAME);
                qb.setProjectionMap(BusLineRelevanceTable.tableProjectionMap);
                db = mStaticDBHelper.getReadableDatabase();
                break;
            case BusStationTable.TABLE_NO:
                qb.setTables(BusStationTable.TABLE_NAME);
                qb.setProjectionMap(BusStationTable.tableProjectionMap);
                db = mStaticDBHelper.getReadableDatabase();
                break;
             
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // If no sort order is specified use the default
        String orderBy=null;
        if (!TextUtils.isEmpty(sortOrder)) {
            orderBy = sortOrder;
        }

        
//        if(match!=BusLineTable.TABLE_NO&&match!=BusLineRelevanceTable.TABLE_NO&&match!=BusStationTable.TABLE_NO)
//        db = mOpenHelper.getReadableDatabase();
//        else
//        db = mStaticDBHelper.getReadableDatabase();
        Log.e(TAG, "selection  = "+selection);
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
            case FavoritePointTable.TABLE_NO:
                return FavoritePointTable.CONTENT_TYPE;
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
            case FavoritePointTable.TABLE_NO:
                rowId = db.insert(FavoritePointTable.TABLE_NAME, "_id", value);
                if (rowId > 0) {
                    Uri noteUri = ContentUris.withAppendedId(FavoritePointTable.CONTENT_URI, rowId);
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
            case FavoritePointTable.TABLE_NO:
                count = db.delete(FavoritePointTable.TABLE_NAME, selection, selectionArgs);
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
            //db.execSQL(BusLineTable.CREATE_SQL);
            //db.execSQL(BusStationTable.CREATE_SQL);
            //db.execSQL(BusLineRelevanceTable.CREATE_SQL);
            db.execSQL(PoiTable.CREATE_SQL);
            db.execSQL(FavoritePointTable.CREATE_SQL);
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
    
    public class StaticDataBaseHelper extends SQLiteOpenHelper{
        
        //The Android's default system path of your application database.
        private static final String DB_PATH = "/sdcard/";
     
        private static final String DB_NAME = "dbfile.db";
     
        private SQLiteDatabase myDataBase; 
     
        private final Context myContext;
     
        /**
         * Constructor
         * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
         * @param context
         */
        public StaticDataBaseHelper(Context context) {
     
            super(context, DB_NAME, null, 1);
            this.myContext = context;
        }   
     
      /**
         * Creates a empty database on the system and rewrites it with your own database.
         * */
        public void createDataBase() throws IOException{
     
            boolean dbExist = checkDataBase();
     
            if(dbExist){
                //do nothing - database already exist
            }else{
     
                //By calling this method and empty database will be created into the default system path
                   //of your application so we are gonna be able to overwrite that database with our database.
                this.getReadableDatabase();
     
                try {
     
                    copyDataBase();
     
                } catch (IOException e) {
     
                    throw new Error("Error copying database");
     
                }
            }
     
        }
     
        /**
         * Check if the database already exist to avoid re-copying the file each time you open the application.
         * @return true if it exists, false if it doesn't
         */
        private boolean checkDataBase(){
     
            SQLiteDatabase checkDB = null;
     
            try{
                String myPath = DB_PATH + DB_NAME;
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
     
            }catch(SQLiteException e){
     
                //database does't exist yet.
     
            }
     
            if(checkDB != null){
     
                checkDB.close();
     
            }
     
            return checkDB != null ? true : false;
        }
     
        /**
         * Copies your database from your local assets-folder to the just created empty database in the
         * system folder, from where it can be accessed and handled.
         * This is done by transfering bytestream.
         * */
        private void copyDataBase() throws IOException{
     
            //Open your local db as the input stream
            InputStream myInput = myContext.getAssets().open(DB_NAME);
     
            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;
     
            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
     
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }
     
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
     
        }
     
        public void openDataBase() throws SQLException{
     
            //Open the database
            String myPath = DB_PATH + DB_NAME;
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
     
        }
     
        @Override
        public synchronized void close() {
     
                if(myDataBase != null)
                    myDataBase.close();
     
                super.close();
     
        }
     
        @Override
        public void onCreate(SQLiteDatabase db) {
     
        }
     
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     
        }

        @Override
        public synchronized SQLiteDatabase getReadableDatabase() {
            try{
                String myPath = DB_PATH + DB_NAME;
                myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
                return myDataBase;
     
            }catch(SQLiteException e){
     
                return null;
     
            }
        }

        @Override
        public synchronized SQLiteDatabase getWritableDatabase() {
            try{
                String myPath = DB_PATH + DB_NAME;
                myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
                return myDataBase;
            }catch(SQLiteException e){
     
                return null;
     
            }
        }
     
            // Add your public helper methods to access and get content from the database.
           // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
           // to you to create adapters for your views.
        
        
     
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, LocationTable.TABLE_NAME, LocationTable.TABLE_NO);
        sUriMatcher.addURI(AUTHORITY, PoiTable.TABLE_NAME, PoiTable.TABLE_NO);
        sUriMatcher.addURI(AUTHORITY, BusLineTable.TABLE_NAME, BusLineTable.TABLE_NO);
        sUriMatcher.addURI(AUTHORITY, BusStationTable.TABLE_NAME, BusStationTable.TABLE_NO);
        sUriMatcher.addURI(AUTHORITY, BusLineRelevanceTable.TABLE_NAME, BusLineRelevanceTable.TABLE_NO);
        sUriMatcher.addURI(AUTHORITY, CityTable.TABLE_NAME, CityTable.TABLE_NO);
        sUriMatcher.addURI(AUTHORITY, FavoritePointTable.TABLE_NAME, FavoritePointTable.TABLE_NO);
        sUriMatcher.addURI(AUTHORITY, LocationTable.TABLE_NAME + "/#",
                LOCATION_TABLE_ID);

        
    }

}
