package de.ur.mi.android.adventurerun.data;

import android.location.Location;

public class Checkpoint {

	private Location location;
	private double latitude;
	private double longitude;
	
	/**
	 * Generates a object of type Checkpoint.
	 * @param location
	 */
	public Checkpoint(Location location) {
		this.location = location;
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}
	
	/**
	 * Generates a object of type Checkpoint. Use this constructor for DB-methods!
	 * @param latitude
	 * @param longitude
	 */
	
	public Checkpoint (double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Returns the distance of the checkpoint to the param dest.
	 * CAUTION: Distance from the checkpoint to the given position!
	 * @param dest			the object to compare with
	 * @return distance 	in float
	 */
	
	// Methode ist deprecated und funktioniert ausschließlich, wenn der Checkpoint noch nicht abgespeichert
	// wurde.
	public float getDistance(Location dest) {
		return location.distanceTo(dest);
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public double getLatitude () {
		return latitude;
	}
	
	
	/*
	 * Die Methode getBearing ist nicht mehr nötig und ist nicht in jedem Fall funktionsfähig.
	 */

	//public float getBearing(Location dest) {
		//return location.bearingTo(dest);
	//}

}
