package gtg.virus.gtpr.entities;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import gtg.virus.gtpr.R;

public class Shelf {
	
	private List<PBook> books;
	
	private int max;
	
	public final static int MAX = 3;

	private View mView = null;

	public Shelf() {
		super();
		this.books = new ArrayList<PBook>();
		this.max = MAX;
	}
	
	
	public void addBook(PBook b){
		books.add(b);
	}
	
	public PBook getBook(int pos){
		return books.get(pos);
	}

	/**dv
	 * @return the books
	 */
	public List<PBook> getBooks() {
		return books;
	}

	/**
	 * @param books the books to set
	 */
	public void setBooks(List<PBook> books) {
		this.books = books;
	}
	
	
	
	/**
	 * @return the max
	 */
	public int getMax() {
		return max;
	}


	/**
	 * @param max the max to set
	 */
	public void setMax(int max) {
		this.max = max;
	}
	
	
	


	/**
	 * @return the mView
	 */
	public View getmView() {
		return mView;
	}


	/**
	 * @param mView the mView to set
	 */
	public void setmView(View mView) {
		this.mView = mView;
	}


	public int resId(){
		return R.layout.shelf_item;
	}
	
	
}
