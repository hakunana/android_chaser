package de.ur.mi.android.adventurerun.control;

import java.util.ArrayList;

import android.location.Location;
import de.ur.mi.android.adventurerun.data.Checkpoint;
import de.ur.mi.android.adventurerun.data.Track;

public class CreateControl {

	private Track track;
	private ArrayList<Checkpoint> checkpoints;
	private String name;
	
	public CreateControl() {
		checkpoints = new ArrayList<Checkpoint>();
	}

	public void addCheckpoint(Location location) {
		Checkpoint checkpoint = new Checkpoint(location);
		checkpoints.add(checkpoint);
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void finishTrack() {
		track = new Track(checkpoints, name, 0);
		// TRACK ZU DATENBANK HINZUFÜGEN
	}
}
