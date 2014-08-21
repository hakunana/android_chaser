package de.ur.mi.android.adventurerun.view;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.adventurerun.R;

import de.ur.mi.android.adventurerun.data.Checkpoint;
import de.ur.mi.android.adventurerun.data.Track;
import de.ur.mi.android.adventurerun.database.PrivateDatabaseTracks;
import de.ur.mi.android.adventurerun.helper.TrackAdapter;

public class TrackView extends Activity {
	
	private PrivateDatabaseTracks db;
	private ArrayList<Track> tracks;
	private TrackAdapter track_adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trackview);
		
		tracks = new ArrayList<Track>();
		
		initDB();
		initUI();
		initList();
	}
	
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}

	private void initDB() {
		db = new PrivateDatabaseTracks(this);
		db.open();
		
		Checkpoint checkpoint1 = new Checkpoint(123.5, 123.5);
		ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
		checkpoints.add(checkpoint1);

	}

	private void initUI() {
		ListView list = (ListView) findViewById(R.id.track_list);
		track_adapter = new TrackAdapter(this, tracks);
		list.setAdapter(track_adapter);
	}

	private void initList() {
		tracks.addAll(db.allTracks());
		track_adapter.notifyDataSetChanged();
	}
	
}
