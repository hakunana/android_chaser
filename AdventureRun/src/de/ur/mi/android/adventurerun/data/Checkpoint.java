package de.ur.mi.android.adventurerun.data;

import android.location.Location;

public class Checkpoint {

	private double latitude;
	private double longitude;
	private float accuracy;
	
	/**
	 * Generates a object of type Checkpoint.
	 * @param location
	 */
	public Checkpoint(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		accuracy = location.getAccuracy();
	}
	
	/**
	 * Generates a object of type Checkpoint. Use this constructor for DB-methods!
	 * @param latitude
	 * @param longitude
	 * @param accuracy
	 */	
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
	public float getDistance(Location dest) {
		Location checkpoint = new Location("checkpoint");
		checkpoint.setLatitude(latitude);
		checkpoint.setLongitude(longitude);
		
		return checkpoint.distanceTo(dest);
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
