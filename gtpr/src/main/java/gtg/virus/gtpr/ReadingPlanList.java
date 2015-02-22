package gtg.virus.gtpr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import gtg.virus.gtpr.base.BaseFragment;

/**
 * Created by DavidLuvelleJoseph on 2/22/2015.
 */
public class ReadingPlanList extends BaseFragment {



    @Override
    protected boolean hasOptions() {
        return false;
    }

    @Override
    protected void provideOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    protected int resourceId() {
        return R.layout.reading_plan_list;
    }
}
