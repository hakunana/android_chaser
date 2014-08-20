package de.ur.mi.android.adventurerun.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.adventurerun.R;

import de.ur.mi.android.adventurerun.data.Track;

public class TrackAdapter extends ArrayAdapter<Track> {
	
	private ArrayList<Track> tracks;
	private Context context;
	
	public TrackAdapter(Context context, ArrayList<Track> tracks) {
		super(context, R.id.track_list, tracks);
		
		this.context = context;
		this.tracks = tracks;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.track_item, null);
		}
		
		Track track = tracks.get(position);
		
		if (track != null) {
			TextView trackName = (TextView) v.findViewById(R.id.track_name);
			trackName.setText(track.getName());
			
			TextView timestamp = (TextView) v.findViewById(R.id.track_timestamp);
			timestamp.setText(formatTimestamp(track.getTimestamp()));
		}
		
		return v;
	}

	private String formatTimestamp(long timestamp) {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(timestamp);
	}
}
