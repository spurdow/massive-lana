package gtg.virus.gtpr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.InjectView;
import gtg.virus.gtpr.adapters.ShelfAdapter;
import gtg.virus.gtpr.base.BaseFragment;

/**
 * Created by DavidLuvelleJoseph on 2/15/2015.
 */
public class MyBookShelf extends BaseFragment {


    @InjectView(R.id.shelf_list_view)
    protected ListView mListView;

    private ShelfAdapter mShelfAdapter = null;

    @Override
    protected void provideOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mShelfAdapter = new ShelfAdapter(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected int resourceId() {
        return R.layout.shelf_fragment;
    }
}
