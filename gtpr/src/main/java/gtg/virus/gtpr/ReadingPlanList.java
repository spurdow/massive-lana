package gtg.virus.gtpr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import butterknife.InjectView;
import gtg.virus.gtpr.aaentity.AABook;
import gtg.virus.gtpr.adapters.ReadingPlanAdapter;
import gtg.virus.gtpr.base.BaseFragment;

/**
 * Created by DavidLuvelleJoseph on 2/22/2015.
 */
public class ReadingPlanList extends BaseFragment {


    @InjectView(R.id.readingplan_list)
    protected ListView mListView;

    private ReadingPlanAdapter mAdapter;

    @Override
    protected boolean hasOptions() {
        return false;
    }

    @Override
    protected void provideOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAdapter = new ReadingPlanAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        List<AABook> books = AABook.findAll();

        for(AABook aaBook : books){
            mAdapter.add(aaBook);
        }
    }

    @Override
    protected int resourceId() {
        return R.layout.reading_plan_list;
    }
}
