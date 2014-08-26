package de.ur.mi.android.adventurerun.view;

import com.example.adventurerun.R;

import de.ur.mi.android.adventurerun.control.RaceControl;
import de.ur.mi.android.adventurerun.control.RaceListener;
import de.ur.mi.android.adventurerun.data.Track;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class RaceView extends Activity implements RaceListener {

	RaceControl raceControl;
	Track currentTrack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.raceview);
		
		//initController();
	}
	

	private void initController() {
		raceControl = new RaceControl (this, currentTrack, this);
		
	}

	private void adjustCompass () {
		ImageView compass = (ImageView) findViewById(R.id.imageView_compass);
		compass.setRotation(0);
		//compass.setRotation(raceControl.getBearing(currentLocation, currentCheckpoint));
	}


	@Override
	public void onCheckpointReached() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onRaceWon() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onRaceStarted() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onRaceStopped() {
		// TODO Auto-generated method stub
		
	}

	



}
