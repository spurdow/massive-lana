package gtg.virus.gtpr.adapters;

import java.io.IOException;
import java.util.List;
import java.util.Properties;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.TextView;


public abstract class AbstractListAdapter<E> extends BaseAdapter implements
		Filterable , IGeneric<E>{

	private Context context;
	private List<E> lists;
	private List<E> backupList;
	protected LayoutInflater mInflater;

	public AbstractListAdapter(Context context, List<E> lists) {
		this.context = context;
		this.lists = lists;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (lists == null)
			return 0;
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lists.get(arg0);
	}
	
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return getOverridedView(arg0, arg1, arg2);
	}

	/**
	 * returns the Override view provided by its subclass
	 */
	public abstract View getOverridedView(int position, View child,
			ViewGroup root);

	public Context getContext() {
		return context;
	}

	public void setList(List<E> list) {
		this.lists = list;
	}

	public void setBackupList(List<E> fList) {
		this.backupList = fList;
	}

	public List<E> getList() {
		return lists;
	}

	public List<E> getBackupList() {
		return backupList;
	}

	@Override
	public E getObject(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	/**
	 * set new typeface to TextViews
	 * @param mTextViews
	 */
	protected void setTypeFace(TextView... mTextViews){
		Typeface mTypeFace = createDefaultTypeface();
		for(TextView mTextView : mTextViews){
			setTypeFace(mTextView,  mTypeFace);
		}
	}
	
	
	/**
	 * default typeface
	 * @param mTextView
	 */
	protected void setTypeFace(TextView mTextView){
		setTypeFace(mTextView , createDefaultTypeface());
	}
	/**
	 * new type face
	 * @param mTextView
	 * @param mTypeFace
	 */
	protected void setTypeFace(TextView mTextView, Typeface mTypeFace){
		mTextView.setTypeface(mTypeFace);
	}
	/**
	 * create default type face
	 * @return
	 */
	protected Typeface createDefaultTypeface(){
		Properties props = new Properties();
		String default_font = null;
		try {
			props.load(context.getResources().getAssets().open("properties/fontconfig.properties"));
			default_font = props.getProperty("defaultfontname");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			default_font = "2Dumb.ttf";
		}
		
		return Typeface.createFromAsset(context.getAssets() , "fonts/" + default_font );
	}

}
