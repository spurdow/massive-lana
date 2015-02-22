package gtg.virus.gtpr.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import java.util.List;

import gtg.virus.gtpr.aaentity.AAScheduled_Books;

/**
 * Created by DavidLuvelleJoseph on 2/23/2015.
 */
public class ScheduleListAdapter extends AbstractListAdapter<AAScheduled_Books> {


    public ScheduleListAdapter(Context context, List<AAScheduled_Books> lists) {
        super(context, lists);
    }

    public ScheduleListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getOverridedView(int position, View child, ViewGroup root) {
        return null;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
