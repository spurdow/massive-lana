package gtg.virus.gtpr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import butterknife.InjectView;
import gtg.virus.gtpr.aaentity.AABookmark;
import gtg.virus.gtpr.adapters.BookmarkAdapter;
import gtg.virus.gtpr.parent.ParentFragment;

/**
 * Created by DavidLuvelleJoseph on 2/21/2015.
 */
public class BookmarkList extends ParentFragment {

    @InjectView(R.id.bookmark_list)
    ListView mListView;

    private BookmarkAdapter mAdapter;

    @Override
    public int resId() {
        return R.layout.bookmark_list;
    }

    @Override
    public boolean useButterKnife() {
        return true;
    }

    @Override
    public void overrideSetUpView(View createdView, LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        mAdapter = new BookmarkAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        List<AABookmark> bookmarks = AABookmark.loadAll();

        for(AABookmark bMark : bookmarks){
            mAdapter.add(bMark);
        }
        mAdapter.notifyDataSetChanged();
    }
}
