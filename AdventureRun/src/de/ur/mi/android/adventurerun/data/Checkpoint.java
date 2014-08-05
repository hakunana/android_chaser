package de.ur.mi.android.adventurerun.data;

import android.location.Location;

public class Checkpoint {

	private Location location;

	public Checkpoint(Location location) {
		this.location = location;
	}

	public float getDistance(Location dest) {
		return location.distanceTo(dest);
	}

	// Gibt Richtung zur�ck - ACHTUNG: Richtung VOM CHECKPOINT aus gesehen zur
	// angegebenen Location dest
	
	/* Chris: Ist diese Methode nicht redundant? Entscheidend ist doch die Richtung von der aktuellen
	 * Position des L�ufers zum n�chstgelegenen Checkpoint.
	*/
	public float getBearing(Location dest) {
		return location.bearingTo(dest);
	}

}
