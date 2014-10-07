package de.ur.mi.android.adventurerun.view;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adventurerun.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import de.ur.mi.android.adventurerun.data.Checkpoint;
import de.ur.mi.android.adventurerun.data.Track;
import de.ur.mi.android.adventurerun.database.PrivateDatabaseScores;
import de.ur.mi.android.adventurerun.database.PrivateDatabaseTracks;
import de.ur.mi.android.adventurerun.helper.Constants;

public class TrackDetailView extends FragmentActivity implements
		OnMapLoadedCallback {

	private PrivateDatabaseTracks db;
	private PrivateDatabaseScores dbScores;

	private Context context;

	private int trackIndex;

	private Track track;

	private Button playTrack, renameTrack, deleteTrack;

	private TextView textviewTrackName;

	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trackdetailview);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		this.context = this;

		ArrayList<Track> tracks = new ArrayList<Track>();
		Bundle bundle = getIntent().getExtras();

		db = new PrivateDatabaseTracks(this);
		db.open();
		dbScores = new PrivateDatabaseScores(this);
		dbScores.open();

		if (bundle.getInt(Constants.KEY_INTENT_TRACKVIEW) != -1) {
			trackIndex = bundle.getInt(Constants.KEY_INTENT_TRACKVIEW);
			tracks = db.allTracks();

			track = tracks.get(trackIndex);

			String trackName = track.getName();
			int numberOfCheckpoints = track.countCheckpoints();

			textviewTrackName = (TextView) findViewById(R.id.track_name);
			textviewTrackName.setText(trackName);

			TextView textviewCheckpoints = (TextView) findViewById(R.id.number_of_checkpoints);
			textviewCheckpoints.append(" " + numberOfCheckpoints);
		}

		initButtons();
		initMap();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void initMap() {
		FragmentManager fmanager = getSupportFragmentManager();
		Fragment fragment = fmanager.findFragmentById(R.id.map_fragment);
		SupportMapFragment supportMapFragment = (SupportMapFragment) fragment;
		map = supportMapFragment.getMap();
		map.setMyLocationEnabled(true);
		map.animateCamera(CameraUpdateFactory.zoomTo(16));

		map.setOnMapLoadedCallback(this);
	}

	@Override
	public void onMapLoaded() {
		ArrayList<Checkpoint> checkpoints = track.getAllCheckpoints();
		LatLngBounds.Builder builder = new LatLngBounds.Builder();

		for (Checkpoint c : checkpoints) {
			CircleOptions circle = new CircleOptions();
			LatLng latLng = new LatLng(c.getLatitude(), c.getLongitude());
			circle.center(latLng);
			circle.radius(c.getAccuracy());

			// ÄNDERN: in XML colors Farben abspeichern!
			circle.fillColor(0x6024E35E);
			circle.strokeWidth(2);

			map.addCircle(circle);
			builder.include(circle.getCenter());
		}

		LatLngBounds bounds = builder.build();

		// 5: Abstand in Pixeln vom Rand
		CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, 15);
		map.animateCamera(update);
		return;
	}

	@Override
	protected void onDestroy() {
		db.close();
		dbScores.close();
		super.onDestroy();
	}

	private void initButtons() {
		playTrack = (Button) findViewById(R.id.play_track);
		renameTrack = (Button) findViewById(R.id.rename_track);
		deleteTrack = (Button) findViewById(R.id.delete_track);

		setOnClickListeners();
	}

	private void setOnClickListeners() {
		playTrack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, RaceView.class);
				intent.putExtra(Constants.KEY_INTENT_TRACKVIEW, trackIndex);
				startActivity(intent);
			}

		});

		renameTrack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(R.string.button_rename_track_title);
				builder.setMessage(R.string.button_rename_track_message);
				final EditText textField = new EditText(context);
				textField.setText(track.getName());
				builder.setView(textField);
				builder.setCancelable(true);

				builder.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String name = textField.getText().toString();
								track.setName(name);
								db.updateName(track);
								dbScores.updateName(track);
								textviewTrackName.setText(track.getName());
							}

						});

				builder.setNegativeButton(R.string.button_cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

				builder.show();
			}

		});

		deleteTrack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(R.string.button_track_delete_title);
				builder.setMessage(R.string.button_track_delete_message);
				builder.setCancelable(false);
				
				builder.setPositiveButton(R.string.button_ok, 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								db.deleteTrack(track);
								dbScores.deleteScoreList(track);
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
			
		});



	}
}
