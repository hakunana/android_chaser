package de.ur.mi.android.adventurerun.control;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import de.ur.mi.android.adventurerun.data.Checkpoint;
import de.ur.mi.android.adventurerun.data.Track;
import de.ur.mi.android.adventurerun.helper.RaceListener;

public class RaceControl {

	// Distanz in Metern, ab der ein Checkpoint als erreicht gewertet wird. Wird
	// als Konstante gesetzt, sollte aber - falls sich das als zu ungenau
	// herausstellen sollte - dynamisch geändert werden, basierend auf der GPS
	// Genauigkeit
	
	private float minDistance = 8;
	
	private RaceListener listener;

	private ArrayList<Checkpoint> checkpoints, visitedCheckpoints;
	private int checkpointNum = 0;
	private int visited = 0;
	private long startTime;
	private long endTime;
	private long timeForTrack;

	private boolean running = false;

	public RaceControl(Context context, Track track, RaceListener listener) {
		this.listener = listener;
		visitedCheckpoints = new ArrayList<Checkpoint>();
		this.checkpoints = track.getAllCheckpoints();
		checkpointNum = checkpoints.size();
		
	}

	public void startRace() {
		listener.onRaceStarted();
		startTimer();
		running = true;
	}
	
	public void stopRace() {
		listener.onRaceStopped();
		endTimer();
		running = false;
	}

	private void startTimer() {
		startTime = System.currentTimeMillis();
	}

	private void endTimer() {
		endTime = System.currentTimeMillis();
	}

	public void checkCheckpoint(Location location, Checkpoint currentCheckpoint) {
		double latitudeCheckpoint = currentCheckpoint.getLatitude();
		double longitudeCheckpoint = currentCheckpoint.getLongitude();
		
		Location locationCheckpoint = new Location("Checkpoint");
		locationCheckpoint.setLatitude(latitudeCheckpoint);
		locationCheckpoint.setLongitude(longitudeCheckpoint);
		
		minDistance = (location.getAccuracy() + currentCheckpoint.getAccuracy()) / 2;
		
		if (locationCheckpoint.distanceTo(location) <= minDistance) {
			visitedCheckpoints.add(currentCheckpoint);
			checkpoints.remove(currentCheckpoint);
			listener.onCheckpointReached();
			visited++;
			checkIfWon();
		}
	}
	
	private void checkIfWon() {
		if (visited >= checkpointNum - 1) {
			listener.onRaceWon();
			stopRace();
		}
	}
	
	// Methode braucht die momentane Location (currentLocation):
	// Entweder übergeben oder per Listener in regelmäßigen Abständen liefern.
	public Checkpoint getNextCheckpoint (Location currentLocation) {
		Checkpoint nextCheckpoint;
		Location end = new Location("end");
		
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
	
	public ArrayList<Checkpoint> getAllCheckpoints() {
		return checkpoints;
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
	
	public long getScore() {
		timeForTrack = endTime - startTime;
		return timeForTrack;
	}


}
