package de.ur.mi.android.adventurerun.view;

import com.example.adventurerun.R;

import android.app.Activity;
import android.os.Bundle;

public class TrackDetailView extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trackdetailview);
		
		Bundle extras = getIntent().getExtras();
	}
}
