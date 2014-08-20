package de.ur.mi.android.adventurerun.view;

// Die MainActivity dient als Einstiegspunkt, soll aber direkt auf CreateView �bergehen.

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.adventurerun.R;

public class MainActivity extends Activity {
	
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.context = this;

		Button createTrack = (Button) findViewById(R.id.open_create_view);
		createTrack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CreateView.class);
				startActivity(intent);
			}
			
		});
		
		Button playTrack = (Button) findViewById(R.id.open_race_view);
		playTrack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, RaceView.class);
				startActivity(intent);
			}
			
		});
		
		Button viewTracks = (Button) findViewById(R.id.open_track_view);
		viewTracks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, TrackView.class);
				startActivity(intent);
			}
			
		});
	}

}
