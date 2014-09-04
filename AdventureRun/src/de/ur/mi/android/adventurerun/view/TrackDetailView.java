package de.ur.mi.android.adventurerun.view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adventurerun.R;

import de.ur.mi.android.adventurerun.data.Track;
import de.ur.mi.android.adventurerun.database.PrivateDatabaseTracks;
import de.ur.mi.android.adventurerun.helper.Constants;

public class TrackDetailView extends Activity {
	
	private PrivateDatabaseTracks db;
	
	private Context context;
	
	private int trackIndex;
	
	private Track track;
	
	private Button playTrack, renameTrack, deleteTrack;
	
	private TextView textviewTrackName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trackdetailview);
		
		this.context = this;
		
		ArrayList<Track> tracks = new ArrayList<Track>();
		Bundle bundle = getIntent().getExtras();
		
		db = new PrivateDatabaseTracks(this);
		db.open();

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
	}
	
	@Override
	protected void onDestroy() {
		db.close();
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
							public void onClick(DialogInterface dialog, int which) {
								String name = textField.getText().toString();
								track.setName(name);
								db.updateName(track);
								textviewTrackName.setText(track.getName());
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
		
		deleteTrack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				db.deleteTrack(track);
				finish();
			}
			
		});
		
	}
}
