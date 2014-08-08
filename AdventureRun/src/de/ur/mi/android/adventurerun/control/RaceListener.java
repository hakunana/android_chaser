package de.ur.mi.android.adventurerun.control;

public interface RaceListener {
	public void onCheckpointReached();
	public void onRaceWon();
	public void onRaceStarted();
	public void onRaceStopped();
}
