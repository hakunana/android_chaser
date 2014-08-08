package de.ur.mi.android.adventurerun.control;

import java.util.ArrayList;

import android.location.Location;
import de.ur.mi.android.adventurerun.data.Checkpoint;
import de.ur.mi.android.adventurerun.data.Track;
import de.ur.mi.android.adventurerun.helper.LocationController;
import de.ur.mi.android.adventurerun.helper.PositionListener;

public class RaceControl {

	// Distanz in Metern, ab der ein Checkpoint als erreicht gewertet wird. Wird
	// als Konstante gesetzt, sollte aber - falls sich das als zu ungenau
	// herausstellen sollte - dynamisch geändert werden, basierend auf der GPS
	// Genauigkeit
	private static final float MIN_DISTANCE = 8;
	
	private RaceListener listener;

	private Track track;
	private ArrayList<Checkpoint> checkpoints, visitedCheckpoints;

	private boolean running = false;

	public RaceControl(Track track, RaceListener listener) {
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
		if (checkpoints.isEmpty()) {
			listener.onRaceWon();
			stopRace();
		}
	}

}
