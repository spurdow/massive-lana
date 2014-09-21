package gtg.virus.gtpr.adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gtg.virus.gtpr.R;
import gtg.virus.gtpr.entities.PBook;

public class PBookAdapter extends AbstractListAdapter<PBook> {


    public PBookAdapter(Context context, List<PBook> lists) {
        super(context, lists);
    }


    @Override
    public View getOverridedView(int position, View child, ViewGroup root) {

        ViewHolder mHolder = null;
        if(child == null){
            mHolder = new ViewHolder();
            child = mInflater.inflate(R.layout.menu_list_row , null);
            mHolder.mImage = (ImageView) child.findViewById(R.id.img_menu_list);
            mHolder.mTitle = (TextView) child.findViewById(R.id.txt_menu_list_title);
        }

        return null;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    private class ViewHolder{
        ImageView mImage;
        TextView mTitle;
    }
}
