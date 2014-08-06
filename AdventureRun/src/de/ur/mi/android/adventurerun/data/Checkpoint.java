package de.ur.mi.android.adventurerun.data;

import android.location.Location;

public class Checkpoint {

	private Location location;

	/**
	 * Generates a object of type Checkpoint.
	 * @param location
	 */
	public Checkpoint(Location location) {
		this.location = location;
	}

	/**
	 * Returns the distance of the checkpoint to the param dest.
	 * CAUTION: Distance from the checkpoint to the given position!
	 * @param dest			the object to compare with
	 * @return distance 	in float
	 */
	public float getDistance(Location dest) {
		return location.distanceTo(dest);
	}

	// Gibt Richtung zurück - ACHTUNG: Richtung VOM CHECKPOINT aus gesehen zur
	// angegebenen Location dest
	
	/* Chris: Ist diese Methode nicht redundant? Entscheidend ist doch die Richtung von der aktuellen
	 * Position des Läufers zum nächstgelegenen Checkpoint.
	*/
	
	
	/**
	 * Method returns the bearing of the checkpoint to the param dest.
	 * @param dest			the object to compare with
	 * @return Bearing 		in float.
	 */
	public float getBearing(Location dest) {
		return location.bearingTo(dest);
	}

}
