package de.ur.mi.android.adventurerun.data;

import android.location.Location;

public class Checkpoint {

	private Location location;
	private double latitude;
	private double longitude;
	
	// Standardm‰ﬂig - zum Testen - auf 10 gesetzt, bis in Datenbank implementiert
	private float accuracy = 10;
	
	/**
	 * Generates a object of type Checkpoint.
	 * @param location
	 */
	public Checkpoint(Location location) {
		this.location = location;
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		accuracy = location.getAccuracy();
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
	
	public Checkpoint (double latitude, double longitude, double accuracy) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.accuracy = (float) accuracy;
	}

	/**
	 * Returns the distance of the checkpoint to the param dest.
	 * CAUTION: Distance from the checkpoint to the given position!
	 * @param dest			the object to compare with
	 * @return distance 	in float
	 */
	
	// Methode ist deprecated und funktioniert ausschlieﬂlich, wenn der Checkpoint noch nicht abgespeichert
	// wurde.
	public float getDistance(Location dest) {
		return location.distanceTo(dest);
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public float getAccuracy() {
		return accuracy;
	}

}
