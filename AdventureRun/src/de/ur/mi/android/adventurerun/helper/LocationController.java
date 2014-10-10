package de.ur.mi.android.adventurerun.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationController implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private static final long UPDATE_TIME = 1500;
	private static final int FASTEST_INTERVAL = 800;

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
		locationClient.connect();
	}
	
	public void stop() {
		if (locationClient.isConnected()) {
			stopPeriodicUpdates();
		}
		locationClient.disconnect();
	}
	
	public void pause() {
		editor.putBoolean("KEY_UPDATES_ON", isActive);
		editor.commit();
	}
	
	public void resume() {
		if (prefs.contains("KEY_UPDATES_ON")) {
			isActive = prefs.getBoolean("KEY_UPDATES_ON", false);
		} else {
			editor.putBoolean("KEY_UPDATES_ON", false);
			editor.commit();
		}
	}

	public Location getLastKnownLocation() {
		return locationClient.getLastLocation();
	}
	
	public void startPeriodicUpdates() {
		locationClient.requestLocationUpdates(locationRequest, this);
	}
	
	public void stopPeriodicUpdates() {
		locationClient.removeLocationUpdates(this);
	}

	@Override
	public void onLocationChanged(Location newLocation) {
		location = newLocation;
		positionListener.onNewLocation(location);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Called by Location Services if the attempt to Location Services
		// fails.
		positionListener.onConnectionFailed(result);

	}

	// Aufgerufen, wenn mit GPS verbunden
	@Override
	public void onConnected(Bundle connectionHint) {
		positionListener.onConnected();
		startPeriodicUpdates();
	}

	@Override
	public void onDisconnected() {
		// Aufgerufen, wenn Verbindung zu Location Client einen Error ausgibt
		positionListener.onDisconnected();
	}

}
