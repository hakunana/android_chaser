package de.ur.mi.android.adventurerun.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adventurerun.R;

import de.ur.mi.android.adventurerun.control.CreateControl;
import de.ur.mi.android.adventurerun.helper.LocationController;
import de.ur.mi.android.adventurerun.helper.PositionListener;

public class CreateView extends Activity implements PositionListener {

	private CreateControl control;
	private LocationController locationController;

	private Button buttonStartFinish, buttonAddCheckpoint;

	private Location currentLocation;

	private boolean createStarted;
	private boolean gpsAvailable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createview);

		control = new CreateControl();
		locationController = new LocationController(this, this);

		initButtons();
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
		checkGPS();
		if (gpsAvailable == true) {
			createStarted = true;
			buttonStartFinish.setText(R.string.button_finish_track);
		}
		locationController.startUpdates();
	}

	private void checkGPS() {
		Location location = locationController.getLastKnownLocation();
		if (location == null) {
			gpsAvailable = false;
			setGPSInfo();
		} else {
			gpsAvailable = true;
		}
		
	}

	private void setGPSInfo() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.info_gps_title);
		builder.setMessage(R.string.info_gps_message);

		builder.setCancelable(false);
		
		builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.show();
	}

	private void addCheckpoint() {
		if (createStarted == true && gpsAvailable == true) {
			currentLocation = locationController.getLastKnownLocation();
			control.addCheckpoint(currentLocation);
			updateCheckpointNum();
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

						goToMenu();
					}

				});
		builder.show();
	}

	private void goToMenu() {
		finish();
	}

	// Könnte überflüssig werden, da dieser im CreateModus bis jetzt nicht
	// aufgerufen wird.
	@Override
	public void onNewLocation(Location location) {
		currentLocation = location;
	}

}
