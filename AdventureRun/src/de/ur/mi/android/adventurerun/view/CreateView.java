package de.ur.mi.android.adventurerun.view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventurerun.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import de.ur.mi.android.adventurerun.control.CreateControl;
import de.ur.mi.android.adventurerun.data.Checkpoint;
import de.ur.mi.android.adventurerun.helper.LocationController;
import de.ur.mi.android.adventurerun.helper.PositionListener;

public class CreateView extends FragmentActivity implements PositionListener {

	private CreateControl control;
	private LocationController locationController;

	private Button buttonStartFinish, buttonAddCheckpoint,
			buttonDeleteCheckpoint;

	private Location currentLocation;

	private boolean createStarted = false;
	private boolean gpsAvailable = false;
	private static final float MIN_ACCURACY_CHECKPOINT = 20;

	private GoogleMap map;

	private LatLng latLng;

	private ArrayList<CircleOptions> circles = new ArrayList<CircleOptions>();

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createview);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);

		control = new CreateControl(this);
		locationController = new LocationController(this, this);

		initButtons();

		setupMap();
	}


	private void setupMap() {
		FragmentManager fmanager = getSupportFragmentManager();
		Fragment fragment = fmanager.findFragmentById(R.id.map_fragment);
		SupportMapFragment supportMapFragment = (SupportMapFragment) fragment;
		map = supportMapFragment.getMap();
		map.setMyLocationEnabled(true);
		map.animateCamera(CameraUpdateFactory.zoomTo(16));
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
		buttonDeleteCheckpoint = (Button) findViewById(R.id.button_delete_checkpoint);

		setOnClickListeners();
	}

	private void setOnClickListeners() {
		buttonStartFinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!createStarted) {
					startNewTrack();
				} else if (control.getCheckpointNum() < 2) {
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

		buttonDeleteCheckpoint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteLastCheckpoint();

			}

		});

	}

	private void deleteLastCheckpoint() {
		if (createStarted == true && control.getCheckpointNum() > 0) {

			confirmDelete();

		}
	}

	private void confirmDelete() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.button_checkpoint_delete_title);
		builder.setMessage(R.string.button_checkpoint_delete_message);
		builder.setCancelable(false);

		builder.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						control.deleteLastCheckpoint();
						updateCheckpointNum();
						if (control.getCheckpointNum() < 2) {
							buttonStartFinish.setText(R.string.button_abort_track);
						}
						updateMap();
					}

					private void updateMap() {
						map.clear();
						ArrayList<Checkpoint> checkpoints = control.getAllCheckpoints();
						circles = new ArrayList<CircleOptions>();

						for (Checkpoint c : checkpoints) {
							CircleOptions circle = new CircleOptions();
							LatLng latLng = new LatLng(c.getLatitude(), c.getLongitude());
							circle.center(latLng);
							circle.radius(c.getAccuracy());

							circle.fillColor(0x6024E35E);
							circle.strokeWidth(2);

							circles.add(circle);
							map.addCircle(circle);
						}
						
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

	private void startNewTrack() {
		createStarted = true;
		buttonStartFinish.setText(R.string.button_abort_track);
		locationController.startUpdates();
	}

	private void addCheckpoint() {
		if(createStarted && gpsAvailable) {
			if (MIN_ACCURACY_CHECKPOINT > currentLocation.getAccuracy()) {
				control.addCheckpoint(currentLocation);
				addCheckpointOnMap();
				updateCheckpointNum();
			} else {
				informAboutAccuracy();
			}
		}

		if (control.getCheckpointNum() > 1) {
			buttonStartFinish.setText(R.string.button_finish_track);
		}
	}

	private void informAboutAccuracy() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.button_inform_accuracy_title);
		builder.setMessage(R.string.button_inform_accuracy_message);
		builder.setCancelable(false);

		builder.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});


		builder.show();
		
	}


	private void addCheckpointOnMap() {
		CircleOptions circle = new CircleOptions();
		circle.center(latLng);
		circle.radius(currentLocation.getAccuracy());

		circle.fillColor(0x6024E35E);
		circle.strokeWidth(2);

		circles.add(circle);
		map.addCircle(circle);
	}

	private void updateCheckpointNum() {
		int checkpointNum = control.getCheckpointNum();
		TextView tv = (TextView) findViewById(R.id.textView_checkpointNum);
		tv.setText(R.string.textView_checkpointNum);
		tv.append("" + checkpointNum);
	}

	private void finishTrack() {
		if (createStarted == true && control.getCheckpointNum() > 1) {
			isTrackFinished();
		}
	}

	private void abortTrack() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.button_track_abort_title);
		builder.setMessage(R.string.button_track_abort_message);
		builder.setCancelable(false);

		builder.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
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

		builder.setPositiveButton(R.string.button_name_and_save,
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
		if (!gpsAvailable && (createStarted == true)) {
			gpsAvailable = true;
			buttonAddCheckpoint.setVisibility(View.VISIBLE);
		}

		currentLocation = location;
		updateCamera();
	}

	private void updateCamera() {
		double latitude = currentLocation.getLatitude();
		double longitude = currentLocation.getLongitude();
		latLng = new LatLng(latitude, longitude);
		if (circles.size() < 2) {
			map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
		} else {

			LatLngBounds.Builder builder = new LatLngBounds.Builder();
			builder.include(latLng);
			for (CircleOptions circle : circles) {
				builder.include(circle.getCenter());
			}
			LatLngBounds bounds = builder.build();

			CameraUpdate update = CameraUpdateFactory
					.newLatLngBounds(bounds, 5);

			map.animateCamera(update);
		}

	}

	@Override
	public void onGPSDisabled() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.info_gps_title);
		builder.setMessage(R.string.info_gps_message);

		builder.setCancelable(false);

		builder.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.show();

	}

	@Override
	public void onConnected() {
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (result.hasResolution()) {
			try {
				result.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (IntentSender.SendIntentException e) {
				e.printStackTrace();
			}
		} else {
			showErrorDialog(result.getErrorCode());
		}
	}

	private void showErrorDialog(int errorCode) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_error_title);
		builder.setMessage(R.string.dialog_error_message + errorCode);

		builder.setCancelable(false);

		builder.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				break;
			}
		}
	}

}
