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

	public void addCheckpoint(Checkpoint checkpoint) {
		checkpoints.add(checkpoint);
	}

	public void removeCheckpoint(Checkpoint checkpoint) {
		checkpoints.remove(checkpoint);
	}

	public int countCheckpoints() {
		return checkpoints.size();
	}

	// Setzt den Namen der Strecke - wird kein Name übergeben, wird "Unnamed"
	// als Name verwendet. Um Doppelnamen zu vermeiden sollten wir ggf.
	// implementieren, dass das Datum/die Uhrzeit im Streckennamen enthalten ist
	public void setName(String name) {
		if (name != "") {
			this.name = name;
		}
		name = "Unnamed";
	}
	
	public String getName() {
		return name;
	}

}
