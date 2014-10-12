package de.ur.mi.android.adventurerun.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adventurerun.R;

import de.ur.mi.android.adventurerun.data.TutorialItem;

public class TutorialFragment extends Fragment {

	private TutorialItem item;

	private TextView explanation;
	private ImageView image;
	private TextView headline;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tutorial_fragment, container,
				false);
		explanation = (TextView) view
				.findViewById(R.id.tutorial_fragment_explanation);
		image = (ImageView) view.findViewById(R.id.tutorial_fragment_image);
		headline = (TextView) view
				.findViewById(R.id.tutorial_fragment_headline);

		explanation.setText(item.getExplanation());
		image.setImageResource(item.getDrawableRessourceId());
		headline.setText(item.getHeadline());

		return view;
	}

	public void setItem(TutorialItem item) {
		this.item = item;
	}

	public static TutorialFragment newInstance(TutorialItem item) {
		TutorialFragment fragment = new TutorialFragment();
		fragment.setItem(item);
		return fragment;
	}
}