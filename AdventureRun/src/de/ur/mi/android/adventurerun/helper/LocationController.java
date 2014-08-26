package de.ur.mi.android.adventurerun.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationController implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	// UPDATE_TIME und UPDATE_DISTANCE werden als Konstanten gespeichert - man
	// muss schauen, ob es später vielleicht sinnvoller wäre, die Werte ändern
	// zu können
	private static final long UPDATE_TIME = 2000;
	private static final float UPDATE_DISTANCE = 3;
	private static final int FASTEST_INTERVAL = 1000;

	private Context context;
	private Location location;
	private PositionListener positionListener;
	private LocationRequest locationRequest;
	private LocationClient locationClient;

	private Editor editor;
	private SharedPreferences prefs;

	private boolean isActive;

	public LocationController(Context context, PositionListener positionListener) {
		this.context = context;
		this.positionListener = positionListener;
		init();
	}

	private void init() {
		checkIfGPSEnabled();
		
		locationRequest = LocationRequest.create();
		locationRequest.setInterval(UPDATE_TIME);
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setFastestInterval(FASTEST_INTERVAL);

		isActive = false;
		
		prefs = context.getSharedPreferences("SharedPreferences",
				Context.MODE_PRIVATE);
		editor = prefs.edit();

		locationClient = new LocationClient(context, this, this);
	}

	public void checkIfGPSEnabled() {
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			positionListener.onGPSDisabled();
		}	
	}
	
	public void startUpdates() {
		isActive = true;
		startPeriodicUpdates();
	}
	
	public void stopUpdates() {
		isActive = false;
		stopPeriodicUpdates();
	}
	
	public void start() {
		Log.e("DEBUG", "Connecting client");
		locationClient.connect();
	}
	
	public void stop() {
		Log.e("DEBUG", "Sopping client");
		if (locationClient.isConnected()) {
			stopPeriodicUpdates();
		}
		locationClient.disconnect();
	}
	
	public void pause() {
		Log.e("DEBUG", "Paused");
		editor.putBoolean("KEY_UPDATES_ON", isActive);
		editor.commit();
	}
	
	public void resume() {
		Log.e("DEBUG", "onResume aufgerufen");
		if (prefs.contains("KEY_UPDATES_ON")) {
			isActive = prefs.getBoolean("KEY_UPDATES_ON", false);
		} else {
			editor.putBoolean("KEY_UPDATES_ON", false);
			editor.commit();
		}
	}

	public Location getLastKnownLocation() {
		Log.e("DEBUG", "Returning last location");
		return locationClient.getLastLocation();
	}
	
	public void startPeriodicUpdates() {
		Log.e("DEBUG", "Periodic Updates started");
		locationClient.requestLocationUpdates(locationRequest, this);
	}
	
	public void stopPeriodicUpdates() {
		Log.e("DEBUG", "Periodic Updates stopped");
		locationClient.removeLocationUpdates(this);
	}

	@Override
	public void onLocationChanged(Location newLocation) {
		location = newLocation;
		
		positionListener.onNewLocation(location);
		
		Log.e("DEBUG", "Location retrieved: " + location.getLatitude() + " - " + location.getLongitude());
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Called by Location Services if the attempt to Location Services
		// fails.
		positionListener.onConnectionFailed(result);
		Log.e("DEBUG", "Connection failed");

	}

	// Aufgerufen, wenn mit GPS verbunden
	@Override
	public void onConnected(Bundle connectionHint) {
		positionListener.onConnected();
		Log.e("DEBUG", "Connected");
		startPeriodicUpdates();
	}

	@Override
	public void onDisconnected() {
		// Aufgerufen, wenn Verbindung zu Location Client einen Error ausgibt
		positionListener.onDisconnected();
		Log.e("DEBUG", "Disconnected");
	}

}
