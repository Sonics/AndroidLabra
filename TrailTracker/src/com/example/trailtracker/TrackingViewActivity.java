package com.example.trailtracker;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.trailtracker.MapViewActivity.MyOverlay;
import com.google.android.maps.GeoPoint;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TrackingViewActivity extends Activity {
	
	Chronometer chronometer;
	private long timeWhenStopped = 0;
	private boolean listenChanges = false;
	private static List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
	TextView distance;
	TextView average;
	String timestamp;
	Button buttonStart;
	Button buttonStop;
	Button buttonPause;
	Button buttonResume;
	Button buttonSave;
	Button buttonMainPage;
	Button buttonEnd;
	Button buttonCancel;
	EditText editText;
	double distanceTotal = 0;
	double avgSpeedTotal;
	private RoutesDataSource datasource;
	DecimalFormat df = new DecimalFormat("#.##");
	
	public static List<GeoPoint> getGeopoints() {
		return geoPoints;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracking_view);
		
		Intent intent = getIntent();
		
		// Kuuntelija sille, onko tullut uutta gps-pistettä
		LocationListener locationListener = new MyLocationListener();
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Minimiaika 1 sekunti ja minimietäisyys 2 metriä
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 4, locationListener);
		
		datasource = new RoutesDataSource(this);
		datasource.open();
		
		chronometer = (Chronometer) findViewById(R.id.chronometer);
		
		distance = (TextView) findViewById(R.id.distanceAmount);
		distance.setText("0.00 Km");
		
		average = (TextView) findViewById(R.id.averageSpeed);
		average.setText("0.00 Km/h");
		
		buttonStart = (Button) findViewById(R.id.buttonStart);
		buttonStart.setOnClickListener(startListener);
		
		buttonStop = (Button) findViewById(R.id.buttonStop); 
		buttonStop.setOnClickListener(stopListener);
		
		buttonEnd = (Button) findViewById(R.id.buttonEnd); 
		buttonEnd.setOnClickListener(endListener);
		
		buttonCancel = (Button) findViewById(R.id.buttonCancel); 
		buttonCancel.setOnClickListener(cancelListener);
		
		buttonPause = (Button) findViewById(R.id.buttonPause);
		buttonResume = (Button) findViewById(R.id.buttonResume);
		buttonSave = (Button) findViewById(R.id.buttonSave);
		
		buttonMainPage = (Button) findViewById(R.id.backToMainpage);
		buttonMainPage.setOnClickListener(mainPageListener);
		
		editText = (EditText) findViewById(R.id.editText);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tracking_view, menu);
		return true;
	}
	

	public OnClickListener startListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			timestamp = simpleDateFormat.format(new Date());
			
			chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
			chronometer.start();
			
			buttonStart.setVisibility(View.INVISIBLE);		
			buttonMainPage.setVisibility(View.INVISIBLE);		
			buttonPause.setVisibility(View.VISIBLE);
			buttonPause.setOnClickListener(pauseListener);
			
			listenChanges = true;
		}
	}; 
	
	public OnClickListener stopListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
			chronometer.stop();
			listenChanges = false;
			buttonPause.setVisibility(View.INVISIBLE);
			buttonResume.setVisibility(View.VISIBLE);
			buttonResume.setOnClickListener(resumeListener);
			buttonStop.setVisibility(View.INVISIBLE);
			buttonEnd.setVisibility(View.VISIBLE);
			buttonEnd.setOnClickListener(endListener);
		}		
	};
	
	public OnClickListener pauseListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
			chronometer.stop();
			
			buttonPause.setVisibility(View.INVISIBLE);	
			buttonResume.setVisibility(View.VISIBLE);
			buttonResume.setOnClickListener(resumeListener);		
			listenChanges = false;
		}		
	};
	
	public OnClickListener resumeListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
			chronometer.start();
			
			buttonResume.setVisibility(View.INVISIBLE);
			buttonPause.setVisibility(View.VISIBLE);
			buttonSave.setVisibility(View.INVISIBLE);
			buttonEnd.setVisibility(View.VISIBLE);
			editText.setVisibility(View.INVISIBLE);
			
			listenChanges = true;
		}
	}; 
	
	public OnClickListener endListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			buttonResume.setVisibility(View.INVISIBLE);
			buttonEnd.setVisibility(View.INVISIBLE);
			editText.setVisibility(View.VISIBLE);
			buttonSave.setVisibility(View.VISIBLE);
			buttonSave.setOnClickListener(saveListener);
			buttonCancel.setVisibility(View.VISIBLE);
		}
	};
	
	public OnClickListener cancelListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(mainScreen);
		}
	};
	
	public OnClickListener saveListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			addGeoPointsToDatabase();
			Intent routesScreen = new Intent(getApplicationContext(), DatabaseActivity.class);
			startActivity(routesScreen);
		}
	};
	
	public OnClickListener mainPageListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(mainScreen);
		}
	};
	
	public void addGeoPointsToDatabase() {	
		String name = editText.getText().toString();
		if(name.equals("")) {
			name = "Undefined trail name";
		}
		String time = calculateTime(getChronometerTime());
		
		for (int i = 0; i < geoPoints.size(); i++) {
			Route r = datasource.createRoute(timestamp, name, geoPoints.get(i).getLatitudeE6(), 
											geoPoints.get(i).getLongitudeE6(), Double.valueOf(df.format(distanceTotal / 1000)), Math.round(avgSpeedTotal * 10) / 10, time);
		}
	}
	
	public long getChronometerTime() {
		Long number = SystemClock.elapsedRealtime() - chronometer.getBase();
		//number = (number / 1000) % 60;
		number /= 1000;
		return number;
	}

	public String calculateTime(long time) {
	    long hours = TimeUnit.SECONDS.toHours(time);
	    long minutes = TimeUnit.SECONDS.toMinutes(time) - 
	                  TimeUnit.HOURS.toMinutes(hours);
	    long seconds = TimeUnit.SECONDS.toSeconds(time) -
	                  TimeUnit.HOURS.toSeconds(hours) - 
	                  TimeUnit.MINUTES.toSeconds(minutes);
	    String hoursString = "";
	    String minutesString = "";
	    String secondsString = "";
	    if(hours == 0) {
	    	hoursString = "00"; 
	    } else if (hours > 0 && hours < 10) {
	    	hoursString = "0" + String.valueOf(hours);
	    } else {
	    	hoursString = String.valueOf(hours);
	    }
	    
	    if(minutes == 0) {
	    	minutesString = "00";
	    } else if (minutes > 0 && minutes < 10) {
	    	minutesString = "0" + String.valueOf(minutes);
	    } else {
	    	minutesString = String.valueOf(minutes);
	    }
	    
	    if(seconds == 0) {
	    	secondsString = "00";
	    } else if (seconds > 0 && seconds < 10) {
	    	secondsString = "0" + String.valueOf(seconds);
	    } else {
	    	secondsString = String.valueOf(seconds);
	    }
	    
	    return hoursString + ":" + minutesString + ":" + secondsString;
	}
	
	class MyLocationListener implements LocationListener {
		
		Location oldLocation = new Location("Location A");
		Location newLocation = new Location("Location B");
		int NUMBER = 0;
		double thisDistance;
		DecimalFormat df = new DecimalFormat("#.##");

		@Override
		public void onLocationChanged(Location location) {
			Double longitude = location.getLongitude();
			Double latitude = location.getLatitude();

			//lisää if average == 0.00 then text 0.00 jne

			if(listenChanges) {
				average.setText(df.format(countAverageSpeed()) + " Km/h");
				newLocation.setLongitude(longitude);
				newLocation.setLatitude(latitude);
				if(NUMBER > 0) {
					thisDistance = newLocation.distanceTo(oldLocation);
					distanceTotal += thisDistance;
				}
				GeoPoint newGp = new GeoPoint((int) (latitude * 1e6), (int) (longitude * 1e6));
				geoPoints.add(newGp);
				distance.setText(df.format(distanceTotal / 1000) + " Km");
				NUMBER++;
				avgSpeedTotal = countAverageSpeed();
				System.out.println("GEOPOINT ADDED");
			}
			oldLocation.setLongitude(longitude);
			oldLocation.setLatitude(latitude);
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "GPS disabled",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "GPS enabled",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
		
		public double countAverageSpeed() {
			System.out.println("DISTANCETOTAL: " + distanceTotal);
			System.out.println("TIMETOTAL: " + getChronometerTime());
			return ((distanceTotal * 60 * 60) / 1000) / getChronometerTime();
		}
	}
}
