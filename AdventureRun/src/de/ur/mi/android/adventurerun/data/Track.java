package de.ur.mi.android.adventurerun.data;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Track {

	private String name;
	private Date creationDate;
	private long timestamp;
	private ArrayList<Checkpoint> checkpoints;

	// Vorerst zwei Konstruktoren, abh�ngig davon, wie wir die Streckenbenennung
	// implementieren. Sobald wir uns da entschieden haben, kann man einen
	// Konstruktor ggf. entfernen
	
	// Ich w�rde sagen, der Nutzer kann einen Namen vergeben.
	public Track(ArrayList<Checkpoint> checkpoints) {
		this.checkpoints = checkpoints;
	}

	public Track(ArrayList<Checkpoint> checkpoints, String name, long timestamp) {
		this.checkpoints = checkpoints;
		
		if (timestamp == 0) {
			creationDate = new Date();
			this.timestamp = creationDate.getTime();
		} else {
			this.timestamp = timestamp;
		}

		setName(name);
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
	
	// Methode ist deprecated, bitte getAllCheckpoints als JSON verwenden.
	public String getAllCheckpointsString () {
		return checkpoints.toString();
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Creates a JSONObject from all checkpoints and returns the Object as a String (--> Database).
	 * @return JSONObject as a String.
	 */
	public String getAllCheckpointsJSON() {
		JSONObject checkpointsJSON = new JSONObject();
		try {
			checkpointsJSON.put("allCheckpoints", checkpoints);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return checkpointsJSON.toString().toString();
		
	}	

}
