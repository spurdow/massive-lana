package gtg.virus.gtpr.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gtg.virus.gtpr.R;
import gtg.virus.gtpr.entities.Menu;

public class MenuAdapter extends AbstractListAdapter<Menu> {
	
	
	private LayoutInflater mInflater = null;

    public boolean isMenu = true;

	public MenuAdapter(Context context, List<Menu> lists) {
		super(context, lists);
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getOverridedView(int position, View child, ViewGroup root) {
		// TODO Auto-generated method stub
		ViewHolder mView = null;
		if(child == null){
			child = mInflater.inflate(R.layout.menu_list_row, null);
			mView = new ViewHolder();
			mView.mImgView = (ImageView) child.findViewById(R.id.img_menu_list);
			mView.mTitle = (TextView) child.findViewById(R.id.txt_menu_list_title);
			child.setTag(mView);
		}else{
			mView = (ViewHolder) child.getTag();
		}



        Drawable drawable = getObject(position).getImage();
		if(drawable != null){

            mView.mImgView.setImageDrawable(drawable);
		    if(isMenu){
                mView.mImgView.getDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            }
        }
		
		mView.mTitle.setText(getObject(position).getTitle());


		return child;
	}
		
	
	
	private class ViewHolder{
		private ImageView mImgView;
		private TextView mTitle;

	}

}
