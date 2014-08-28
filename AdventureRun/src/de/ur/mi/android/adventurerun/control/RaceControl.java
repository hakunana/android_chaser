/* TODO
 * - Gleicher oder ähnlicher Fehlerdialog wie im CreateView bei Verbindungsproblemen.
 * - Dynamische Anpassung des Toleranzbereiches für das Erreichen eines Checkpoints
 * - Auswahl der getBearing-Methode: In der letzten Studienleistung wurde einfach die
 * 		bearingTo-Methode verwendet, übergeben wurde die Location aus der Methode
 * 		getLastKnownLocation(). Hilfreich im Allgemeinen ist dazu das Handout bzgl. der Stud-Leistung.
 */

package de.ur.mi.android.adventurerun.control;

import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;

import android.content.Context;
import android.location.Location;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import de.ur.mi.android.adventurerun.data.Checkpoint;
import de.ur.mi.android.adventurerun.data.Track;
import de.ur.mi.android.adventurerun.helper.LocationController;
import de.ur.mi.android.adventurerun.helper.PositionListener;

public class RaceControl {

	// Distanz in Metern, ab der ein Checkpoint als erreicht gewertet wird. Wird
	// als Konstante gesetzt, sollte aber - falls sich das als zu ungenau
	// herausstellen sollte - dynamisch geändert werden, basierend auf der GPS
	// Genauigkeit
	private static final float MIN_DISTANCE = 12;
	
	private RaceListener listener;

	private Context context;
	private Track track;
	private ArrayList<Checkpoint> checkpoints, visitedCheckpoints;

	private boolean running = false;

	public RaceControl(Context context, Track track, RaceListener listener) {
		this.context = context;
		this.listener = listener;
		this.track = track;
		visitedCheckpoints = new ArrayList<Checkpoint>();
		this.checkpoints = track.getAllCheckpoints();
		
	}

	public void startRace() {
		listener.onRaceStarted();
		running = true;
	}

	public void stopRace() {
		listener.onRaceStopped();
		running = false;
	}


	public void checkCheckpoint(Location location) {
		for (Checkpoint c : checkpoints) {
			if (c.getDistance(location) <= MIN_DISTANCE) {
				visitedCheckpoints.add(c);
				checkpoints.remove(c);
				listener.onCheckpointReached();
			}
		}
		
		checkIfWon();
	}
	
	private void checkIfWon() {
		if (checkpoints.size() == 0) {
			listener.onRaceWon();
			stopRace();
		}
	}
	
	// Methode braucht die momentane Location (currentLocation):
	// Entweder übergeben oder per Listener in regelmäßigen Abständen liefern.
	public Checkpoint getNextCheckpoint (Location currentLocation) {
		Checkpoint nextCheckpoint;
		Location end = new Location ("end");
		
		float lastDistance = 100000;
		float currentDistance = 0;
		int arrayIndex = 0;
		for (int i = 0; i < checkpoints.size(); i++) {
			nextCheckpoint = checkpoints.get(i);
			end.setLatitude(nextCheckpoint.getLatitude());
			end.setLongitude(nextCheckpoint.getLongitude());
			
			currentDistance = currentLocation.distanceTo(end);
			
			if (currentDistance < lastDistance) {
				arrayIndex = i;
				lastDistance = currentDistance;
			}
		}
		
		return checkpoints.get(arrayIndex);
	}
	
	
	
	
	// Methode braucht die momentane Location (currentLocation) sowie den aktuellen Checkpoint:
	// Entweder übergeben oder per Listener in regelmäßigen Abständen liefern.
	// !!! Sollte die Funktion nichts taugen, können wir es auch mit der bearingTo-Methode
	// versuchen. (Siehe weiter unten)
	public float getBearing (Location currentLocation, Checkpoint currentCheckpoint) {
		Location destination = new Location ("destination");
		destination.setLatitude(currentCheckpoint.getLatitude());
		destination.setLongitude(currentCheckpoint.getLongitude());
		
		return currentLocation.bearingTo(destination);
	}
	
	// ALTERNATIVE zur Methode darüber:	
	//public double getBearing (Location currentLocation, Checkpoint currentCheckpoint) {
		//double bearing = 0;
		
		//double latLocation = Math.toRadians(currentLocation.getLatitude());
		//double latCheckpoint = Math.toRadians(currentCheckpoint.getLatitude());
		//double longLocation = currentLocation.getLongitude();
		//double longCheckpoint = currentCheckpoint.getLongitude();
		
		//double longDifference = Math.toRadians(longCheckpoint - longLocation);
		//double y = Math.sin(longDifference) * Math.cos(latCheckpoint);
		//double x = Math.cos(latLocation) * Math.sin(latCheckpoint) -
				//Math.sin(latLocation) * Math.cos(latCheckpoint) * Math.cos(longDifference);
		//bearing = (Math.toDegrees(Math.atan2(y, x)) + 360 ) % 360;
		
		//return bearing;
	//}
	
	


}
