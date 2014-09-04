package de.ur.mi.android.adventurerun.view;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.adventurerun.R;

import de.ur.mi.android.adventurerun.data.Track;
import de.ur.mi.android.adventurerun.database.PrivateDatabaseTracks;
import de.ur.mi.android.adventurerun.helper.Constants;
import de.ur.mi.android.adventurerun.helper.TrackAdapter;
import de.ur.mi.android.adventurerun.helper.TrackListListener;

public class TrackView extends Activity implements TrackListListener {
	
	private PrivateDatabaseTracks db;
	private ArrayList<Track> tracks;
	private TrackAdapter track_adapter;
	
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trackview);
		
		this.context = this;
		tracks = new ArrayList<Track>();
		
		initDB();
		initUI();
		initList();
	}
	
	@Override
	protected void onResume() {
		initList();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}

	private void initDB() {
		db = new PrivateDatabaseTracks(this);
		db.open();
	}

	private void initUI() {
		ListView list = (ListView) findViewById(R.id.track_list);
		track_adapter = new TrackAdapter(this, tracks, this, db);
		list.setAdapter(track_adapter);
	}

	private void initList() {
		tracks.clear();
		tracks.addAll(db.allTracks());
		track_adapter.notifyDataSetChanged();
	}

	
	@Override
	public void onTrackDeleted() {
		initList();
	}

	@Override
	public void onRaceViewStarted(int trackIndex) {
		Intent intent = new Intent(context, RaceView.class);
		intent.putExtra(Constants.KEY_INTENT_TRACKVIEW, trackIndex);
		startActivity(intent);
	}

	@Override
	public void onTrackDetailViewStarted(int trackIndex) {
		Intent intent = new Intent(context, TrackDetailView.class);
		intent.putExtra(Constants.KEY_INTENT_TRACKVIEW, trackIndex);
		startActivity(intent);
	}
	
}
