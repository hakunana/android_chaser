package de.ur.mi.android.adventurerun.view;

// Die MainActivity dient als Einstiegspunkt, soll aber direkt auf CreateView übergehen.

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.adventurerun.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = new Intent(this, CreateView.class);
		startActivity(intent);
	}

}
