package de.ur.mi.android.adventurerun.helper;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationController implements LocationListener {

	// UPDATE_TIME und UPDATE_DISTANCE werden als Konstanten gespeichert - man
	// muss schauen, ob es später vielleicht sinnvoller wäre, die Werte ändern
	// zu können
	public static final long UPDATE_TIME = 2000;
	public static final float UPDATE_DISTANCE = 3;

	private Context context;
	private Location location;
	private PositionListener positionListener;
	private LocationManager locationManager;

	private String provider;
	
	private boolean isActive;

	public LocationController(Context context, PositionListener positionListener) {
		this.context = context;
		this.positionListener = positionListener;
		init();
	}

	private void init() {
		setProvider();
		String service = Context.LOCATION_SERVICE;

		locationManager = (LocationManager) context.getSystemService(service);
		location = locationManager.getLastKnownLocation(provider);
	}

	// Location Provider festlegen - im Moment wird der Standardprovider
	// ausgewählt. Ich habe die Methode ausgelagert, damit man ggf. nachträglich
	// den Provider manuell bzw. nach Kriterien auswählen kann
	private void setProvider() {
		provider = LocationManager.GPS_PROVIDER;
	}

	public void start() {
		if (!isActive) {
			isActive = true;
			locationManager.requestLocationUpdates(provider, UPDATE_TIME,
					UPDATE_DISTANCE, this);
		}
	}

	public void stop() {
		if (isActive) {
			isActive = false;
			locationManager.removeUpdates(this);
		}
	}

	public Location getLastKnownLocation() {
		return location;
	}

	@Override
	public void onLocationChanged(Location newLocation) {
		location = newLocation;
		updateLocationInformation();
	}

	private void updateLocationInformation() {
		if (positionListener == null) {
			return;
		}
		positionListener.onNewLocation(location);
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}
