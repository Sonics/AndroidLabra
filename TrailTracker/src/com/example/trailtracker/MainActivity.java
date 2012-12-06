package com.example.trailtracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Button buttonTracking;
	Button buttonRoutes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		buttonTracking = (Button) findViewById(R.id.buttonToTracking);
		buttonTracking.setOnClickListener(trackingListener);
		
		buttonRoutes = (Button) findViewById(R.id.buttonShowRoutes);
		buttonRoutes.setOnClickListener(routesListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public OnClickListener trackingListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent trackingScreen = new Intent(getApplicationContext(), TrackingViewActivity.class);
			startActivity(trackingScreen);
		}
	};
	
	public OnClickListener routesListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent routesScreen = new Intent(getApplicationContext(), DatabaseActivity.class);
			startActivity(routesScreen);
		}
	};

}
