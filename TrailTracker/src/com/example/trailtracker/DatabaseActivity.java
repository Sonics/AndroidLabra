package com.example.trailtracker;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.google.android.maps.GeoPoint;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class DatabaseActivity extends Activity {
	
	private RoutesDataSource datasource;
	ListView routeList;
	Button buttonBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
		
		Intent intent = getIntent();
		
		datasource = new RoutesDataSource(this);
		datasource.open();
		
		routeList = (ListView) findViewById(R.id.list);
		showRoutes();
		
		buttonBack = (Button) findViewById(R.id.listBackButton);
		buttonBack.setOnClickListener(backListener);
		
		routeList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				Route r = (Route) routeList.getItemAtPosition(position);
				Intent routeInformation = new Intent(getApplicationContext(), RouteInformationActivity.class);
				routeInformation.putExtra("Timestamp", r.getTimestamp());
				startActivity(routeInformation);
			}		
		});
	}
	
	public OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(mainScreen);
		}
	};
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_test_database, menu);
		return true;
	}
	
	public void showRoutes() {
		
		List<Route> routesTemp = datasource.getAllRoutenames();
		List<Route> routes = new ArrayList<Route>();
		if(routesTemp.size() > 0) {
			int pos = 0;
			for(int i = routesTemp.size() - 1; i >= 0; i--) {
				routes.add(pos, routesTemp.get(i));
				pos++;
			}
			ArrayAdapter<Route> adapter = new ArrayAdapter<Route>(this, android.R.layout.simple_list_item_1, routes);
			routeList.setAdapter(adapter);
		}
		
	}

}
