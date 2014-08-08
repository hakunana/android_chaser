package de.ur.mi.android.adventurerun.data;

import java.util.ArrayList;

public class Track {

	private String name;
	private ArrayList<Checkpoint> checkpoints;

	// Vorerst zwei Konstruktoren, abhängig davon, wie wir die Streckenbenennung
	// implementieren. Sobald wir uns da entschieden haben, kann man einen
	// Konstruktor ggf. entfernen
	
	// Ich würde sagen, der Nutzer kann einen Namen vergeben.
	public Track(ArrayList<Checkpoint> checkpoints) {
		this.checkpoints = checkpoints;
	}

	public Track(ArrayList<Checkpoint> checkpoints, String name) {
		this.checkpoints = checkpoints;
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
	
	//Noch nicht implementiert!!! Mit JSON arbeiten.
	public String getAllCheckpointsJSON() {
		return null;
		
	}	

}
