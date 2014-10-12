package de.ur.mi.android.adventurerun.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.example.adventurerun.R;

import de.ur.mi.android.adventurerun.data.TutorialItem;

public class TutorialActivity extends FragmentActivity {

	  private ViewPager pager;
	  private TutorialPagerAdapter adapter;
	  private Button goOn;

	  @Override protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    setContentView(R.layout.tutorial_activity);
	    pager = (ViewPager) findViewById(R.id.tutorial_activity_pager);
	    goOn = (Button) findViewById(R.id.tutorial_activity_go_on);

	    pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	      @Override public void onPageScrolled(int i, float v, int i2) {

	      }

	      @Override public void onPageSelected(final int i) {
	        if (adapter.getCount() - 1 == i) {
	          goOn.setText("Fertig");
	          goOn.setOnClickListener(new View.OnClickListener() {
	            @Override public void onClick(View view) {
	              pager.setCurrentItem(i + 1, true);
	            }
	          });
	        } else {
	          goOn.setText("Weiter");
	          goOn.setOnClickListener(new View.OnClickListener() {
	            @Override public void onClick(View view) {
	            	Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
	            }
	          });
	        }
	      }

	      @Override public void onPageScrollStateChanged(int i) {

	      }
	    });

	    adapter = new TutorialPagerAdapter(getSupportFragmentManager());
	    pager.setAdapter(adapter);

	    // TODO add correct items

	    adapter.addItem(new TutorialItem(R.drawable.tutorial_logo, getString(R.string.tutorial_message1), getString(R.string.tutorial_headline1))); // correct drawable id and text (R.drawable.tutorial1)
	    adapter.addItem(new TutorialItem(R.drawable.tutorial_start, getString(R.string.tutorial_message2), getString(R.string.tutorial_headline2)));
	    adapter.addItem(new TutorialItem(R.drawable.tutorial_create, getString(R.string.tutorial_message3), getString(R.string.tutorial_headline3)));
	    adapter.addItem(new TutorialItem(R.drawable.tutorial_listview, getString(R.string.tutorial_message4), getString(R.string.tutorial_headline4)));
	    adapter.addItem(new TutorialItem(R.drawable.tutorial_run, getString(R.string.tutorial_message5), getString(R.string.tutorial_headline5)));

	    adapter.notifyDataSetChanged();
	  }

	  public class TutorialPagerAdapter extends FragmentStatePagerAdapter {

	    private List<TutorialItem> items;

	    public TutorialPagerAdapter(FragmentManager fm) {
	      super(fm);
	      items = new ArrayList<TutorialItem>();
	    }

	    @Override
	    public Fragment getItem(int i) {
	      return TutorialFragment.newInstance(items.get(i));
	    }

	    public void addItem(TutorialItem item) {
	      items.add(item);
	    }

	    @Override
	    public int getCount() {
	      return items == null ? 0 : items.size();
	    }

	    @Override
	    public CharSequence getPageTitle(int position) {
	      return "";
	    }
	  }
	}