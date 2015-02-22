package gtg.virus.gtpr;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.InjectView;
import gtg.virus.gtpr.aaentity.AAScheduled_Books;
import gtg.virus.gtpr.adapters.ScheduleListAdapter;
import gtg.virus.gtpr.base.BaseFragment;

/**
 * Created by DavidLuvelleJoseph on 2/23/2015.
 */
public class ScheduledBooksList extends BaseFragment {

    private static final String TAG = ScheduledBooksList.class.getSimpleName();
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

        List<AAScheduled_Books> books = AAScheduled_Books.list();

        for(AAScheduled_Books book : books){
            mAdapter.add(book);
        }
    }

    @Override
    protected int resourceId() {
        return R.layout.scheduled_list;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.schedule_list_menu , menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    SimpleDateFormat format  = new SimpleDateFormat("MMM d , yyyy");

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_add:


                CaldroidFragment fragment = CaldroidFragment.newInstance("Select a date" , 3 , 2015);
                fragment.setCaldroidListener(new CaldroidListener() {
                    @Override
                    public void onSelectDate(Date date, View view) {
                        Log.w(TAG , "Date selected " + format.format(date));
                    }
                });
                fragment.show(getActivity().getSupportFragmentManager() , TAG);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
