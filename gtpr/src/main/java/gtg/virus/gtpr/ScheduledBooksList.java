package gtg.virus.gtpr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.InjectView;
import gtg.virus.gtpr.adapters.ScheduleListAdapter;
import gtg.virus.gtpr.base.BaseFragment;

/**
 * Created by DavidLuvelleJoseph on 2/23/2015.
 */
public class ScheduledBooksList extends BaseFragment {

    @InjectView(R.id.schedule_list)
    protected ListView mListView;

    private ScheduleListAdapter mAdapter;


    @Override
    protected boolean hasOptions() {
        return true;
    }

    @Override
    protected void provideOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAdapter = new ScheduleListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected int resourceId() {
        return R.layout.scheduled_list;
    }
}
