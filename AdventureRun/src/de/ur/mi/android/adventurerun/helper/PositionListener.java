package de.ur.mi.android.adventurerun.helper;

import android.location.Location;

import com.google.android.gms.common.ConnectionResult;

public interface PositionListener {
	public void onNewLocation(Location location);
	public void onConnected();
	public void onDisconnected();
	public void onConnectionFailed(ConnectionResult result);
	
}
