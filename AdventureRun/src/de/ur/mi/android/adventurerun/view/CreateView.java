package de.ur.mi.android.adventurerun.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
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

	private Button buttonStart, buttonAddCheckpoint, buttonFinishTrack;

	private Location currentLocation;
	
	private boolean createStarted;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createview);

		
		
		control = new CreateControl(this);
		locationController = new LocationController(this, this);
		

		initButtons();
	}

	private void initButtons() {
		buttonStart = (Button) findViewById(R.id.button_start_create_track);
		buttonAddCheckpoint = (Button) findViewById(R.id.button_add_checkpoint);
		buttonFinishTrack = (Button) findViewById(R.id.button_finish_track);

		setOnClickListeners();
	}

	private void setOnClickListeners() {
		buttonStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startNewTrack();
			}
		});

		buttonAddCheckpoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addCheckpoint();
			}
		});

		buttonFinishTrack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finishTrack();
			}
		});
	}

	private void startNewTrack() {
		createStarted = true;
	}

	private void addCheckpoint() {
		if (createStarted == true) {
			locationController.start();
			currentLocation = locationController.getLastKnownLocation();
			control.addCheckpoint(currentLocation);	
			locationController.stop();
			updateCheckpointNum();
		}

	}

	private void updateCheckpointNum() {
		int checkpointNum = control.getCheckpointNum();
		TextView checkpointNumView = (TextView) findViewById(R.id.textView_checkpointNum);
		checkpointNumView.setText(R.string.textView_checkpointNum + String.valueOf(checkpointNum));
		
	}

	private void finishTrack() {
		if (createStarted == true) {
			isTrackFinished();

			
		}
		
	}

	/**
	 * A dialog asks the user if the track is finished. Only if the user tips OK, the track will
	 * be stored and gets a name. The dialog is not cancelable.
	 */
	private void isTrackFinished() {
		AlertDialog.Builder builder = new AlertDialog.Builder (this);
		builder.setTitle(R.string.button_track_finished_title);
		builder.setMessage(R.string.button_track_finished_message);
		
		builder.setCancelable(false);
		
		builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				locationController.stop();
				getName();
				control.finishTrack();
			}
		});
		
		builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.show();
	}

	/**
	 * This method asks the user for a name of the created track in a AlertDialog. The dialog
	 * is not cancelable.
	 */
	private void getName() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.button_set_track_name_title);
		builder.setMessage(R.string.button_set_track_name_message);
		final EditText textField = new EditText(this);
		builder.setView(textField);
		
		builder.setCancelable(false);
		
		builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String name = textField.getText().toString();
				control.setName(name);
				
			}
		});
		builder.show();
	}

	// Könnte überflüssig werden, da dieser im CreateModus bis jetzt nicht aufgerufen wird.
	@Override
	public void onNewLocation(Location location) {
		currentLocation = location;
	}

}
