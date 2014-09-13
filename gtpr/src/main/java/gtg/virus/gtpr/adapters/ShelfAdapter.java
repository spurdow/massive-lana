package gtg.virus.gtpr.adapters;

import gtg.virus.gtpr.GTGEpubViewer;
import gtg.virus.gtpr.GTGPdfViewer;
import gtg.virus.gtpr.R;
import gtg.virus.gtpr.entities.PBook;
import gtg.virus.gtpr.entities.Shelf;
import gtg.virus.gtpr.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import static gtg.virus.gtpr.utils.Utilities.*;
public class ShelfAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<Shelf> shelves;
	
	private List<PBook> books;
	
	private LayoutInflater inflater = null;
	
	public final static int MAX_CHARACTERS = 10;

	protected static final String TAG = ShelfAdapter.class.getSimpleName();
	
	/**
	 * @param mContext
	 * @param inflater
	 */
	public ShelfAdapter(Context mContext, 
			LayoutInflater inflater) {
		super();
		this.mContext = mContext;
		this.shelves = new ArrayList<Shelf>();
		this.inflater = LayoutInflater.from(mContext);
		this.books = new ArrayList<PBook>();
	}

	public ShelfAdapter(
			Context context) {
		// TODO Auto-generated constructor stub
		this(context, null);
	}

	
	public void addBook(PBook b){
		if(!shelves.isEmpty()){
			Shelf shelf = shelves.get(shelves.size()-1);
			int sizeOfShelf = shelf.getBooks().size();
			if(sizeOfShelf < shelf.getMax()){
				shelf.addBook(b);
			}else{
				Shelf newShelf = new Shelf();
				newShelf.addBook(b);
				shelves.add(newShelf);
			}
		}else{
			Shelf shelf = new Shelf();
			shelf.addBook(b);
			shelves.add(shelf);
		}
		
		books.add(b);
		
		((Activity) mContext).runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				notifyDataSetChanged();
			}
			
		});
		
		
	}

	
	/**
	 * 
	 * @param bookPosition
	 * @return PBook at position
	 */
	public PBook getBookAtPosition(int bookPosition){
		return books.get(bookPosition);

	}

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return shelves.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return shelves.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		
		if(convertView == null){
			convertView = inflater.inflate(R.layout.shelf_row, null);
			
			viewHolder = new ViewHolder();
			viewHolder.shelf_parent = (LinearLayout) convertView.findViewById(R.id.shelf_parent);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.shelf_parent.removeAllViews();
		}

		
		Shelf shelf = (Shelf) getItem(position);
		if(shelf != null){
			final int maxSize  = shelf.getBooks().size();
			
			for(int i = 0 ; i < maxSize ; i++){
				final PBook b = shelf.getBook(i);
				

				
				if(b != null){
				
					FrameLayout ff = (FrameLayout) viewHolder.shelf_parent.getChildAt(i);
					if(ff == null){
						ff = (FrameLayout) inflater.inflate(R.layout.shelf_item, null);
					}
					ff.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(Utilities.isEpub(b.getPath())){
                                Intent intent = new Intent(mContext, GTGEpubViewer.class);
                                intent.putExtra(PIN_EXTRA_PBOOK, b.toString());
                                mContext.startActivity(intent);
							}else if(Utilities.isPdf(b.getPath())){
								Intent intent = new Intent(mContext, GTGPdfViewer.class);
							    intent.putExtra(PIN_EXTRA_PBOOK, b.toString());
							    mContext.startActivity(intent);
							}

						}
						
					});

					TextView tv = (TextView) ff.findViewById(R.id.title);
					tv.setText(b.getTitle());

					if(b.getPage0() != null){
						ImageView image = (ImageView) ff.findViewById(R.id.thumbnail);
						image.setImageBitmap(b.getPage0());
					}
					
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT);
					
					params.leftMargin = 10;
					params.rightMargin = 10;
					params.gravity = Gravity.TOP;
				
					viewHolder.shelf_parent.addView(ff, params);
				

				}
			}

		}
			
		
		return convertView;
	}
	
	
	private class ViewHolder{
		LinearLayout shelf_parent;
	}

}
