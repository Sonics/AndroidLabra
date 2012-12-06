package com.example.trailtracker;

import java.util.List;

import com.example.trailtracker.MapViewActivity.MyOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RouteInformationActivity extends MapActivity {

	List<GeoPoint> geoPoints;
	MapView mapView;
	Button buttonBack;
	Button buttonDelete;
	MapController mapController;
	String timestamp;
	RoutesDataSource datasource;
	List<Route> routes;
	TextView routeDate;
	TextView routeName;
	TextView routeTime;
	TextView routeDistance;
	TextView routeAvg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_information);
		
		Intent intent = getIntent();
		timestamp = intent.getExtras().getString("Timestamp");
		
		routeDate = (TextView) findViewById(R.id.thisRouteDate);
		routeName = (TextView) findViewById(R.id.thisRouteName);
		routeTime = (TextView) findViewById(R.id.thisRouteTime);
		routeDistance = (TextView) findViewById(R.id.thisRouteDistance);
		routeAvg = (TextView) findViewById(R.id.thisRouteAvgSpeed);
		
		datasource = new RoutesDataSource(this);
		datasource.open();
		
		routes = datasource.getOneRoute(timestamp);

		mapView = (MapView) findViewById(R.id.routeMapview);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		
		buttonBack = (Button) findViewById(R.id.routeBackButton);
		buttonBack.setOnClickListener(backListener);
		
		buttonDelete = (Button) findViewById(R.id.deleteRouteButton);
		buttonDelete.setOnClickListener(deleteListener);
		
		//GeoPoint geoPoint = new GeoPoint((int) (60.22600776 * 1e6), (int) (24.81882458 * 1e6));
		if(routes.size() > 0) {
			GeoPoint gp = new GeoPoint(routes.get(0).getLatitude(), routes.get(0).getLongitude());
			mapController.setCenter(gp);
		} 
		mapController.setZoom(13);
		
		drawPath();
		
		/*List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.androidman);
		TrailTrackerItemizedOverlay itemizedOverlay = new TrailTrackerItemizedOverlay(drawable, this);
		
		ArrayList<GeoPoint> geoPoints = new ArrayList();
		GeoPoint p1 = new GeoPoint((int) (60.22500776 * 1e6), (int) (24.82082458 * 1e6));
		GeoPoint p2 = new GeoPoint((int) (60.23800778 * 1e6), (int) (24.82682459 * 1e6));
		geoPoints.add(p1);
		geoPoints.add(p2);
		
		OverlayItem overlayItem = new OverlayItem(p1, "Jesh", "Koti");
		itemizedOverlay.addOverlay(overlayItem);
		overlayItem = new OverlayItem(p2, "Jesh", "Ei koti");
		itemizedOverlay.addOverlay(overlayItem);
		mapOverlays.add(itemizedOverlay);*/
	}
	
	private void drawPath() {
		System.out.println("ALETAAN PIIRTELEM€€N");
		   List<Overlay> overlays = mapView.getOverlays();
		   
		   if(routes.size() > 0) {
			   Route r = routes.get(0);
			   String temp = r.getTimestamp();
			   temp = temp.substring(0, 10);
			   routeDate.setText(temp);
			   routeName.setText(r.getRoute());
			   routeTime.setText(r.getTotalTime());
			   routeDistance.setText(r.getDistance() + " Km");
			   routeAvg.setText(r.getAvgSpeed() + " Km/h"); 
		   }		   
		 
		   for (int i = 1; i < routes.size(); i++) {
			   GeoPoint gp1 = new GeoPoint(routes.get(i).getLatitude(), routes.get(i).getLongitude());
			   GeoPoint gp2 = new GeoPoint(routes.get(i - 1).getLatitude(), routes.get(i - 1).getLongitude());
			   overlays.add(new MyOverlay(gp2, gp1));
		   }
		   /*GeoPoint gp = new GeoPoint((maxLon + minLon) / 2, (maxLat + minLat) / 2); 
		   mapController.setCenter(gp);
		   mapController.setZoom(13);*/
	}
	
	public OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			Intent databaseScreen = new Intent(getApplicationContext(), DatabaseActivity.class);
			startActivity(databaseScreen);
		}
	};
	
	public OnClickListener deleteListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			for(Route r : routes) {
				datasource.deleteRoute(r);
			}
			Intent databaseScreen = new Intent(getApplicationContext(), DatabaseActivity.class);
			startActivity(databaseScreen);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	class MyOverlay extends Overlay {
		private GeoPoint gp1;
		private GeoPoint gp2;
		
		public MyOverlay(GeoPoint gp1, GeoPoint gp2) {
			this.gp1 = gp1;
			this.gp2 = gp2;
		}
		
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		    Projection projection = mapView.getProjection();
		    Paint paint = new Paint();
		    paint.setColor(Color.RED);
		    paint.setStyle(Paint.Style.FILL_AND_STROKE);
		    paint.setStrokeJoin(Paint.Join.ROUND);
		    paint.setStrokeCap(Paint.Cap.ROUND);

		    Point point = new Point();
		    projection.toPixels(gp1, point);   
		    Point point2 = new Point();
		    projection.toPixels(gp2, point2);
		    paint.setStrokeWidth(6);
		    paint.setAlpha(120);
		    canvas.drawLine(point.x, point.y, point2.x, point2.y, paint);
		    super.draw(canvas, mapView, shadow);
		    
		    /*if(gp1.getLatitudeE6() > maxLat) {
		    	maxLat = gp1.getLatitudeE6();
		    }
		    if(gp1.getLatitudeE6() < minLat) {
		    	minLat = gp1.getLatitudeE6();
		    }
		    if(gp1.getLongitudeE6() > maxLon) {
		    	maxLat = gp1.getLongitudeE6();
		    }
		    if(gp1.getLatitudeE6() > minLon) {
		    	minLon = gp1.getLongitudeE6();
		    }*/
		}	
	}

}
