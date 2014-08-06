package de.ur.mi.android.adventurerun.view;

import android.app.Activity;
import android.os.Bundle;

import com.example.adventurerun.R;

import de.ur.mi.android.adventurerun.control.CreateControl;

public class CreateView extends Activity {
	
	private CreateControl control;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createview);
		
		control = new CreateControl();
	}
	
}
