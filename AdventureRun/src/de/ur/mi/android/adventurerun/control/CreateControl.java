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

	public CreateControl(Context context) {
		this.context = context;
		checkpoints = new ArrayList<Checkpoint>();
		
	}

	public void addCheckpoint(Location location) {
		Checkpoint checkpoint = new Checkpoint(location);
		checkpoints.add(checkpoint);
	}
	
	public int getCheckpointNum() {
		return checkpoints.size();
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void finishTrack() {
		track = new Track(checkpoints, name, 0);
		
		PrivateDatabaseTracks.open();
		PrivateDatabaseTracks.insertTrack(track);
		PrivateDatabaseTracks.close();
	}
}
