package gtg.virus.gtpr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.InjectView;
import gtg.virus.gtpr.adapters.ShelfAdapter;
import gtg.virus.gtpr.base.BaseFragment;

/**
 * Created by DavidLuvelleJoseph on 2/16/2015.
 */
public class MyRemoteBookShelf extends BaseFragment {

    @InjectView(R.id.shelf_list_view)
    protected ListView mListView;

    private ShelfAdapter mShelfAdapter;

    @Override
    protected boolean hasOptions() {
        return false;
    }

    @Override
    protected void provideOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    protected int resourceId() {
        return R.layout.shelf_fragment;
    }
}
