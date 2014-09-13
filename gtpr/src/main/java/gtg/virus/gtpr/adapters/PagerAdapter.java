package gtg.virus.gtpr.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragments ;
	
	public PagerAdapter(FragmentManager fm, List<Fragment> frags) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.mFragments = frags;
	}
	
	public void add(Fragment frag){
		mFragments.add(frag);
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFragments.size();
	}


}
