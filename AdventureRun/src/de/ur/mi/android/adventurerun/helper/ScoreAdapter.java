package de.ur.mi.android.adventurerun.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.adventurerun.R;

public class ScoreAdapter extends ArrayAdapter<Long> {

	private Long[] scores;
	private Context context;

	public ScoreAdapter(Context context, Long[] scores) {
		super(context, R.id.score_list, scores);

		this.context = context;
		this.scores = scores;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.score_item, null);
		}

		final Long score = scores[position];

		if (score != null) {
			String time = new SimpleDateFormat("mm:ss").format(new Date(score));
			TextView textViewScore = (TextView) v.findViewById(R.id.score);
			textViewScore.setText((position + 1) + ". " + time);
		}

		return v;
	}
}
