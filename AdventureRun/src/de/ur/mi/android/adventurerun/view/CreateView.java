package de.ur.mi.android.adventurerun.view;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.adventurerun.R;

import de.ur.mi.android.adventurerun.control.CreateControl;
import de.ur.mi.android.adventurerun.helper.LocationController;
import de.ur.mi.android.adventurerun.helper.PositionListener;


public class CreateView extends Activity implements PositionListener {

	private CreateControl control;
	private LocationController locationController;

	private Button buttonStart, buttonAddCheckpoint, buttonFinishTrack;

	private Location currentLocation;

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
		locationController.start();
	}

	private void addCheckpoint() {
		control.addCheckpoint(currentLocation);
	}

	private void finishTrack() {
		locationController.stop();

		// Muss noch implementiert werden: Track kann nur abgeschlossen werden,
		// wenn zuvor auch gestartet wurde. Problem ggf. überflüssig, wenn wir
		// das Interface anpassen (das 3-Button Interface ist nur zur
		// Implementierung der Funktionsweise)
		control.finishTrack();
	}

	@Override
	public void onNewLocation(Location location) {
		currentLocation = location;
	}

}
