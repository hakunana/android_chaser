package de.ur.mi.android.adventurerun.data;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;

import android.util.Log;

public class Track {

	private String name;
	private Date creationDate;
	private long timestamp;
	private ArrayList<Checkpoint> checkpoints;

	public Track(ArrayList<Checkpoint> checkpoints, String name, long timestamp) {
		this.checkpoints = checkpoints;
	
		setName(name);
		setTimestamp(timestamp);
	}

	private void setTimestamp(long timestamp) {
		if (timestamp == 0) {
			creationDate = new Date();
			this.timestamp = creationDate.getTime();
		} else {
			this.timestamp = timestamp;
		}
	}

	/**
	 * Adds the checkpoint to the ArrayList of the track.
	 * @param checkpoint	Checkpoint that will be added
	 */
	public void addCheckpoint(Checkpoint checkpoint) {
		checkpoints.add(checkpoint);
	}

	/**
	 * Removes the checkpoint from the ArrayList of the track.
	 * @param checkpoint	Checkpoint that will be removed from the ArrayList
	 */
	public void removeCheckpoint(Checkpoint checkpoint) {
		checkpoints.remove(checkpoint);
	}

	
	public int countCheckpoints() {
		return checkpoints.size();
	}

	/**
	 * Sets the name of the track to the given String.
	 * If the String is empty, the name will be "Unnamed".
	 * @param name		Name that will be used
	 */
	public void setName(String name) {
		if (name != "") {
			this.name = name;
		}
		name = "Unnamed";
	}
	
	/**
	 * Returns the name of the track.
	 * @return Name of the track.
	 */
	public String getName() {
		return name;
	}
	
	public ArrayList<Checkpoint> getAllCheckpoints() {
		return checkpoints;
	}
	
	
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Creates a JSONObject from all checkpoints and returns the Object as a String (--> Database).
	 * @return JSONObject as a String.
	 */
	public String getAllCheckpointsJSON() {
		ArrayList<Double> jsonArrayList = new ArrayList<Double> ();
		fillArrayList(jsonArrayList);
		JSONArray js = new JSONArray (jsonArrayList);
		return js.toString();
		
	}

	private void fillArrayList(ArrayList<Double> jsonArrayList) {
		for (int i = 0; i < checkpoints.size() ; i++) {
			double latitude = checkpoints.get(i).getLatitude();
			double longitude = checkpoints.get(i).getLongitude();
			double accuracy = (double) checkpoints.get(i).getAccuracy();
			jsonArrayList.add(latitude);
			jsonArrayList.add(longitude);
			jsonArrayList.add(accuracy);
		}
		
	}	

}
