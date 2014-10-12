package de.ur.mi.android.adventurerun.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.adventurerun.R;

import de.ur.mi.android.adventurerun.data.Track;
import de.ur.mi.android.adventurerun.database.PrivateDatabaseTracks;
import de.ur.mi.android.adventurerun.helper.Constants;
import de.ur.mi.android.adventurerun.helper.TrackAdapter;
import de.ur.mi.android.adventurerun.helper.TrackListListener;

public class MainActivity extends Activity implements TrackListListener {

	private PrivateDatabaseTracks db;
	private ArrayList<Track> tracks;
	private TrackAdapter track_adapter;

	View emptyView;

	private String [] navigationTitles;
	private DrawerLayout navigationDrawerLayout;
	private ListView navigationList;
	
	private final String tutorialScreenShownPref = "tutorialScreenShown";
	private SharedPreferences prefs;
	
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		openTutorialActivity();
		
		setContentView(R.layout.trackview);
		initNavigationDrawer();
		this.context = this;
		tracks = new ArrayList<Track>();
		
		initDB();
		initUI();
		initList();
	}

	private void openTutorialActivity() {
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean tutorialScreenShown = prefs.getBoolean(tutorialScreenShownPref,  false);
		
		if (!tutorialScreenShown) {
			Intent intent = new Intent (MainActivity.this, TutorialActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void initNavigationDrawer() {
		navigationTitles = getResources().getStringArray(R.array.drawer_items);
		navigationDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		navigationList = (ListView) findViewById(R.id.left_drawer);
		
		navigationList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item,
				navigationTitles));
		navigationList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
					case 0: {
						Intent intent = new Intent (MainActivity.this, CreateView.class);
						startActivity (intent);
						finish();
						break;
					}
					case 1: {
						break;
					}
					case 2: {
						Intent intent = new Intent (MainActivity.this, MainActivity.class);
						startActivity (intent);
						finish();
						break;
					}
					case 3: {
						break;
					}
				}
			}
		});
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case R.id.action_create: {
			Intent i = new Intent(MainActivity.this, CreateView.class);
			startActivity(i);
			break;
		}
		}
		return super.onOptionsItemSelected(item);
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
		emptyView = findViewById(R.id.emptyView);
		list.setEmptyView(emptyView);
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
