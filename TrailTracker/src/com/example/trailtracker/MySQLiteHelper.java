package com.example.trailtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	private static String TABLE_NAME = "routes";
	private static String DATABASE_NAME = "routes.db";
	private static String COLUMN_ROUTE = "route";
	private static String COLUMN_ID = "id";
	private static String COLUMN_TIMESTAMP = "timestamp";
	private static String COLUMN_LATITUDE = "latitude";
	private static String COLUMN_LONGITUDE = "longitude";
	private static String COLUMN_DISTANCE = "distance";
	private static String COLUMN_AVGSPEED = "avgspeed";
	private static String COLUMN_TOTALTIME = "totaltime";
	private static int VERSION = 10;
	private static String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_ID 
			+ " integer primary key autoincrement, " + COLUMN_TIMESTAMP + " text, "
			+ COLUMN_ROUTE + " text, " + COLUMN_LATITUDE + " integer, " 
			+ COLUMN_LONGITUDE + " integer, " + COLUMN_DISTANCE + " real, " 
			+ COLUMN_AVGSPEED + " real, " + COLUMN_TOTALTIME +  " text);";

	public MySQLiteHelper(Context context) {
		super(context, TABLE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		    onCreate(db);
		
	}

	public static String getTABLE_NAME() {
		return TABLE_NAME;
	}

	public static String getDATABASE_NAME() {
		return DATABASE_NAME;
	}

	public static String getCOLUMN_ROUTE() {
		return COLUMN_ROUTE;
	}

	public static String getCOLUMN_ID() {
		return COLUMN_ID;
	}

	public static int getVERSION() {
		return VERSION;
	}

	public static String getDATABASE_CREATE() {
		return DATABASE_CREATE;
	}

	public static String getCOLUMN_LATITUDE() {
		return COLUMN_LATITUDE;
	}

	public static String getCOLUMN_LONGITUDE() {
		return COLUMN_LONGITUDE;
	}

	public static String getCOLUMN_TIMESTAMP() {
		return COLUMN_TIMESTAMP;
	}

	public static String getCOLUMN_DISTANCE() {
		return COLUMN_DISTANCE;
	}

	public static String getCOLUMN_AVGSPEED() {
		return COLUMN_AVGSPEED;
	}

	public static String getCOLUMN_TOTALTIME() {
		return COLUMN_TOTALTIME;
	}
	
}
