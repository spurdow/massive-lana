package gtg.virus.gtpr.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import gtg.virus.gtpr.R;
import gtg.virus.gtpr.aaentity.AAScheduled_Books;

/**
 * Created by DavidLuvelleJoseph on 2/23/2015.
 */
public class ScheduleListAdapter extends AbstractListAdapter<AAScheduled_Books> {


    SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy hh:mm a");

    public ScheduleListAdapter(Context context, List<AAScheduled_Books> lists) {
        super(context, lists);
    }

    public ScheduleListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getOverridedView(int position, View child, ViewGroup root) {
        ViewHolder v = null;
        if(child ==null){
            child = mInflater.inflate(R.layout.schedule_row , root, false);
            v = new ViewHolder(child);
            child.setTag(v);
        }else{
            v = (ViewHolder) child.getTag();
        }

        AAScheduled_Books aaScheduled_books = getObject(position);

        v.bookSample.setText(aaScheduled_books.book.title);
        v.startDate.setText(dateFormatter.format(aaScheduled_books.dateStarted));
        v.endDate.setText(dateFormatter.format(aaScheduled_books.endDate));

        return child;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    static class ViewHolder{
        @InjectView(R.id.image_book)
        ImageView bookImage;
        @InjectView(R.id.txt_book)
        TextView bookSample;
        @InjectView(R.id.txt_date_start)
        TextView startDate;
        @InjectView(R.id.txt_date_end)
        TextView endDate;

        ViewHolder(View child){
            ButterKnife.inject(this, child);
        }
    }
}
