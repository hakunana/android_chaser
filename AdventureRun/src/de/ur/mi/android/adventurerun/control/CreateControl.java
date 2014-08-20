package de.ur.mi.android.adventurerun.control;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import de.ur.mi.android.adventurerun.data.Checkpoint;
import de.ur.mi.android.adventurerun.data.Track;
import de.ur.mi.android.adventurerun.database.PrivateDatabaseTracks;

public class CreateControl {

	private Track track;
	private ArrayList<Checkpoint> checkpoints;
	private String name;
	private Context context;
	private PrivateDatabaseTracks db;
	
	public CreateControl(Context context) {
		this.context = context;
		checkpoints = new ArrayList<Checkpoint>();
		
		db = new PrivateDatabaseTracks(context);
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
		
		db.open();
		db.insertTrack(track);
		db.close();
	}
}
