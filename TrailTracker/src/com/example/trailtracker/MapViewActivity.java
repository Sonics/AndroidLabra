package com.example.trailtracker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ZoomButtonsController;
import android.widget.ZoomButtonsController.OnZoomListener;

public class MapViewActivity extends MapActivity {
	
	private MapView mapView;
	private MapController mapController;
	private List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
	Path path = null;
	Path testPath = new Path();
	Point oldPoint = new Point();
	Button buttonBack;
	int maxLon;
	int minLon;
	int maxLat;
	int minLat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapview);
		
		Intent intent = getIntent();
		geoPoints = TrackingViewActivity.getGeopoints();
		
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		
		buttonBack = (Button) findViewById(R.id.backButton);
		buttonBack.setOnClickListener(backListener);
		
		GeoPoint geoPoint = new GeoPoint((int) (60.22600776 * 1e6), (int) (24.81882458 * 1e6));
		if(geoPoints.size() > 0) {
			mapController.setCenter(geoPoints.get(0));
		} else {
			mapController.setCenter(geoPoint);
		}
		mapController.setZoom(13);
		
		drawPath(geoPoints);
		
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
	
	private void drawPath(List<GeoPoint> geoPoints) {
		System.out.println("ALETAAN PIIRTELEM€€N");
		   List<Overlay> overlays = mapView.getOverlays();
		 
		   for (int i = 1; i < geoPoints.size(); i++) {
		    overlays.add(new MyOverlay(geoPoints.get(i - 1), geoPoints.get(i)));
		   }
		   /*GeoPoint gp = new GeoPoint((maxLon + minLon) / 2, (maxLat + minLat) / 2); 
		   mapController.setCenter(gp);
		   mapController.setZoom(13);*/
	}
	
	public OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//MapViewActivity mapViewActicity = new MapViewActivity(geoPoints);
			Intent trackScreen = new Intent(getApplicationContext(), TrackingViewActivity.class);
			startActivity(trackScreen);
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
	
	//Longitude: 24,82082458 Latitude: 60,22500776
}

