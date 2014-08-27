/* TODO
 * - LocationController vollständig implementieren (bis jetzt checkForService drin)
 * - Proximity Alerts einsetzen
 */


package de.ur.mi.android.adventurerun.view;

import java.util.ArrayList;

import com.example.adventurerun.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.ur.mi.android.adventurerun.control.RaceControl;
import de.ur.mi.android.adventurerun.control.RaceListener;
import de.ur.mi.android.adventurerun.data.Track;
import de.ur.mi.android.adventurerun.database.PrivateDatabaseTracks;
import de.ur.mi.android.adventurerun.helper.Constants;
import de.ur.mi.android.adventurerun.helper.LocationController;
import de.ur.mi.android.adventurerun.view.CreateView.ErrorDialogFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RaceView extends FragmentActivity implements RaceListener {

	private RaceControl raceControl;
	private LocationController locationController;
	private Track currentTrack;
	private PrivateDatabaseTracks db;
	private String trackName = "unknown";
	TextView textViewTrackName;
	Button buttonStart;
	
	Location currentLocation;
	
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final String DIALOG_ERROR = "dialog_error";
	
	private boolean raceStarted = false;
	private boolean gpsAvailable = false;
	
	private static final float TEXT_SIZE_COUNTDOWN = 50;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.raceview);
		
		checkForService();
		
		initDB();
		getTrack();
		initController();
		initButtons();
	}
	
	

	private void initDB() {
		db = new PrivateDatabaseTracks(this);
		db.open();
		
	}


	private void getTrack() {
		ArrayList <Track> tracks = new ArrayList <Track> ();
		Bundle bundle = getIntent().getExtras();
		
		if (bundle.getInt(Constants.KEY_INTENT_TRACKVIEW) != -1) {
			int trackIndex = bundle.getInt(Constants.KEY_INTENT_TRACKVIEW);
			tracks = db.allTracks();
			currentTrack = tracks.get(trackIndex);
			trackName = currentTrack.getName();
			textViewTrackName = (TextView) findViewById(R.id.textView_trackName);
			textViewTrackName.setText(trackName);
		}
		
	}


	private void initController() {
		raceControl =  new RaceControl (this, currentTrack, this);
	}
	
	
	private void initButtons() {
		buttonStart = (Button) findViewById(R.id.button_start_run_track);
		
		setOnClickListener();
		
	}
	
	
	private void setOnClickListener() {
		buttonStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startCountdown();	
			}

			private void startCountdown() {
				final AlertDialog builder = new AlertDialog.Builder(RaceView.this).create();
				builder.setTitle(R.string.button_track_started_dialog_title);
				final TextView text = new TextView(RaceView.this);
				builder.setView(text);
				text.setTextColor(Color.RED);
				text.setTextSize(TEXT_SIZE_COUNTDOWN);
				text.setGravity(Gravity.CENTER_HORIZONTAL);
				builder.setCancelable(true);
				builder.show();
				
				new CountDownTimer(10000, 1000) {
					
					@Override
					public void onTick(long millisUntilFinished) {
						text.setText("" + (millisUntilFinished / 1000));	
					}
					
					// Ein Piepton o.Ä. wäre an der Stelle super! ;)
					@Override
					public void onFinish() {
						raceControl.startRace();
						builder.dismiss();
					}
				}.start();

			}
		});
		
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

	private boolean checkForService() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode == ConnectionResult.SUCCESS) {
			Log.e("DEBUG", "Google Play Services available");
			return true;
		} else {
			ConnectionResult connectionResult = new ConnectionResult(resultCode, null);
			int errorCode = connectionResult.getErrorCode();
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			
			if (errorDialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(getSupportFragmentManager(), "Location Updates");
			}
			return false;
		}
		
	}


}
