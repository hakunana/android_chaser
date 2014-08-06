package de.ur.mi.android.adventurerun.control;

import java.util.ArrayList;

import android.location.Location;
import de.ur.mi.android.adventurerun.data.Checkpoint;
import de.ur.mi.android.adventurerun.data.Track;

public class CreateControl {

	private Track track;
	private ArrayList<Checkpoint> checkpoints;

	// Zu klären: Reicht es, wenn das Track Objekt erst mit Abschluss der
	// Strecke erzeugt wird, oder soll es direkt am Anfang erstellt werden und
	// die Checkpoint Liste fortlaufend bzw. am Ende aktualisiert werden? Da mir
	// spontan kein Fall eingefallen wäre, wieso das Track Objekt schon vor
	// Vollendung existieren sollte, hab ich es jetzt erst mal so gelöst.
	public CreateControl() {
		checkpoints = new ArrayList<Checkpoint>();
	}

	public void addCheckpoint(Location location) {
		Checkpoint checkpoint = new Checkpoint(location);
		checkpoints.add(checkpoint);
	}

	public void finishTrack() {
		track = new Track(checkpoints);
		// TRACK ZU DATENBANK HINZUFÜGEN
	}
}
