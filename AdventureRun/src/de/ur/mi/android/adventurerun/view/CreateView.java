/* TODO
 * - Button zum Löschen des letzten Checkpoints
 * - Karte mit aktueller Position und Checkpoints
 * - Anzahl Checkpoint Anzeige ist noch verbuggt
 * - Statistiken
 * - Strecke erstellen abbrechen
 */


package de.ur.mi.android.adventurerun.view;


import android.app.AlertDialog;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adventurerun.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.ur.mi.android.adventurerun.control.CreateControl;
import de.ur.mi.android.adventurerun.helper.LocationController;
import de.ur.mi.android.adventurerun.helper.PositionListener;

public class CreateView extends FragmentActivity implements PositionListener {

	private CreateControl control;
	private LocationController locationController;

	private Button buttonStartFinish, buttonAddCheckpoint;

	private Location currentLocation;

	private boolean createStarted = false;
	private boolean gpsAvailable = false;
	private boolean enoughCheckpoints = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createview);

		control = new CreateControl(this);
		locationController = new LocationController(this, this);

		initButtons();
		
		setupMap();
	}

	private void setupMap() {
		FragmentManager fmanager = getSupportFragmentManager();
		Fragment fragment = fmanager.findFragmentById(R.id.map_fragment);
		SupportMapFragment supportMapFragment = (SupportMapFragment) fragment;
		GoogleMap map = supportMapFragment.getMap();
	}

	@Override
	public void onStart() {
		locationController.start();
		super.onStart();
	}
	
	@Override
	public void onStop() {
		locationController.stop();
		super.onStop();
	}
	
	@Override
	public void onPause() {
		locationController.pause();
		super.onPause();
	}
	
	@Override
	public void onResume() {
		locationController.resume();
		super.onResume();
	}

	private void initButtons() {
		buttonStartFinish = (Button) findViewById(R.id.button_start_finish_create_track);
		buttonAddCheckpoint = (Button) findViewById(R.id.button_add_checkpoint);

		setOnClickListeners();
	}

	private void setOnClickListeners() {
		buttonStartFinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!createStarted) {
					startNewTrack();
				} else if (enoughCheckpoints) {
					abortTrack();
				} else {
					finishTrack();
				}
			}
		});

		buttonAddCheckpoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addCheckpoint();
			}
		});

	}

	private void startNewTrack() {
		createStarted = true;
		Log.e("DEBUG", "Setting text to " + R.string.button_abort_track);
		buttonStartFinish.setText(R.string.button_abort_track);
		locationController.startUpdates();
	}

	private void addCheckpoint() {
		if (createStarted && gpsAvailable) {
			control.addCheckpoint(currentLocation);
			updateCheckpointNum();
		}
		
		if (control.getCheckpointNum() > 1) {
			buttonStartFinish.setText(R.string.button_finish_track);
		}
	}

	private void updateCheckpointNum() {
		int checkpointNum = control.getCheckpointNum();
		Integer num = Integer.valueOf(checkpointNum);
		TextView checkpointNumView = (TextView) findViewById(R.id.textView_checkpointNum);
		checkpointNumView.setText(R.string.textView_checkpointNum
				+ num.toString());

	}

	private void finishTrack() {
		if (createStarted == true && control.getCheckpointNum() > 1) {
			isTrackFinished();
		}
	}
	
	private void abortTrack() {
		// Strecke erstellen abbrechen
	}

	/**
	 * A dialog asks the user if the track is finished. Only if the user tips
	 * OK, the track will be stored and gets a name. The dialog is not
	 * cancelable.
	 */
	private void isTrackFinished() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.button_track_finished_title);
		builder.setMessage(R.string.button_track_finished_message);

		builder.setCancelable(false);

		builder.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						getName();
					}
				});

		builder.setNegativeButton(R.string.button_cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		builder.show();
	}

	/**
	 * This method asks the user for a name of the created track in a
	 * AlertDialog. The dialog is not cancelable.
	 */
	private void getName() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.button_set_track_name_title);
		builder.setMessage(R.string.button_set_track_name_message);
		final EditText textField = new EditText(this);
		builder.setView(textField);

		builder.setCancelable(false);

		builder.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String name = textField.getText().toString();
						control.setName(name);
						control.finishTrack();
						locationController.stopUpdates();
						
						createStarted = false;
						gpsAvailable = false;

						goToMenu();
					}

				});
		builder.show();
	}

	private void goToMenu() {
		finish();
	}

	@Override
	public void onNewLocation(Location location) {
		if (!gpsAvailable) {
			gpsAvailable = true;
			buttonAddCheckpoint.setVisibility(View.VISIBLE);
		}
		
		currentLocation = location;
	}
	

	public static class MapFragment extends FragmentActivity {

		
		private void setupMap() {
			GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment)).getMap();
			map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
		}
	}


}
