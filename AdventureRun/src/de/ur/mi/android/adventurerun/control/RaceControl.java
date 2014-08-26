/* TODO
 * - Gleicher oder ähnlicher Fehlerdialog wie im CreateView bei Verbindungsproblemen.
 * - Dynamische Anpassung des Toleranzbereiches für das Erreichen eines Checkpoints
 */

package de.ur.mi.android.adventurerun.control;

import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;

import android.content.Context;
import android.location.Location;
import de.ur.mi.android.adventurerun.data.Checkpoint;
import de.ur.mi.android.adventurerun.data.Track;
import de.ur.mi.android.adventurerun.helper.LocationController;
import de.ur.mi.android.adventurerun.helper.PositionListener;

public class RaceControl implements PositionListener {

	// Distanz in Metern, ab der ein Checkpoint als erreicht gewertet wird. Wird
	// als Konstante gesetzt, sollte aber - falls sich das als zu ungenau
	// herausstellen sollte - dynamisch geändert werden, basierend auf der GPS
	// Genauigkeit
	private static final float MIN_DISTANCE = 12;
	
	private RaceListener listener;
	private LocationController locationController;

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
		
		locationController = new LocationController(context, this);
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
	
	

	@Override
	public void onNewLocation(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGPSDisabled() {
		// TODO Auto-generated method stub
		
	}

}
