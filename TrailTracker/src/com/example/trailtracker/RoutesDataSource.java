package com.example.trailtracker;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RoutesDataSource {

	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.getCOLUMN_ID(), MySQLiteHelper.getCOLUMN_TIMESTAMP(),
		      MySQLiteHelper.getCOLUMN_ROUTE(), MySQLiteHelper.getCOLUMN_LATITUDE(), MySQLiteHelper.getCOLUMN_LONGITUDE(),
		      MySQLiteHelper.getCOLUMN_DISTANCE(), MySQLiteHelper.getCOLUMN_AVGSPEED(), MySQLiteHelper.getCOLUMN_TOTALTIME()};
	
	
	public RoutesDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Route createRoute(String timestamp, String route, int latitude, int longitude, double distance, double avgSpeed, String totaltime) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.getCOLUMN_ROUTE(), route);
		values.put(MySQLiteHelper.getCOLUMN_TIMESTAMP(), timestamp);
		values.put(MySQLiteHelper.getCOLUMN_LATITUDE(), latitude);
		values.put(MySQLiteHelper.getCOLUMN_LONGITUDE(), longitude);
		values.put(MySQLiteHelper.getCOLUMN_DISTANCE(), distance);
		values.put(MySQLiteHelper.getCOLUMN_AVGSPEED(), avgSpeed);
		values.put(MySQLiteHelper.getCOLUMN_TOTALTIME(), totaltime);
		long insertId = database.insert(MySQLiteHelper.getTABLE_NAME(), null, values);
		Cursor cursor = database.query(MySQLiteHelper.getTABLE_NAME(),
		        allColumns, MySQLiteHelper.getCOLUMN_ID() + " = " + insertId, null,
		        null, null, null);
		cursor.moveToFirst();
		Route newRoute = cursorToRoute(cursor);
		cursor.close();
		return newRoute;
	}
	
	public void deleteRoute(Route route) {
		long id = route.getId();
		System.out.println("Route deleted: " + id + " " + route.toString() + " lon " + route.getLongitude() + " lat " + route.getLatitude());
		database.delete(MySQLiteHelper.getTABLE_NAME(), MySQLiteHelper.getCOLUMN_ID() + " = " + id, null);
	}
	
	public List<Route> getOneRoute(String timestamp) {
		List<Route> routes = new ArrayList<Route>();
		Cursor cursor = database.query(MySQLiteHelper.getTABLE_NAME(), allColumns, MySQLiteHelper.getCOLUMN_TIMESTAMP() + " = '" + timestamp + "'", null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Route route = cursorToRoute(cursor);
			routes.add(route);
			cursor.moveToNext();
		}
		cursor.close();
		return routes;
	}
	
	public List<Route> getAllRoutes() {
		List<Route> routes = new ArrayList<Route>();
		Cursor cursor = database.query(true, MySQLiteHelper.getTABLE_NAME(), allColumns, null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Route route = cursorToRoute(cursor);
			routes.add(route);
			cursor.moveToNext();
		}
		cursor.close();
		return routes;
	}
	
	public List<Route> getAllRoutenames() {
		List<Route> routes = new ArrayList<Route>();
		//Cursor cursor = database.query(MySQLiteHelper.getTABLE_NAME(), allColumns, null, null, null, null, null);
		String[] thisColumn = {MySQLiteHelper.getCOLUMN_TIMESTAMP()};
		Cursor cursor = database.query(true, MySQLiteHelper.getTABLE_NAME(), allColumns, null, null, MySQLiteHelper.getCOLUMN_TIMESTAMP(), null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Route route = cursorToRoute(cursor);
			routes.add(route);
			cursor.moveToNext();
		}
		cursor.close();
		return routes;
	}

	private Route cursorToRoute(Cursor cursor) {
		Route route = new Route();
		route.setId(cursor.getLong(0));
		route.setTimestamp(cursor.getString(1));
		route.setRoute(cursor.getString(2));
		route.setLatitude(cursor.getInt(3));
		route.setLongitude(cursor.getInt(4));
		route.setDistance(cursor.getDouble(5));
		route.setAvgSpeed(cursor.getDouble(6));
		route.setTotalTime(cursor.getString(7));
		return route;
	}
}
