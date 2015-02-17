package gtg.virus.gtpr.entities;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import gtg.virus.gtpr.R;
import gtg.virus.gtpr.aaentity.AARemoteBook;

/**
 * Created by DavidLuvelleJoseph on 2/16/2015.
 */
public class RemoteShelf {

    private List<AARemoteBook> books;

    private int max;

    public final static int MAX = 3;

    private View mView = null;

    public RemoteShelf() {
        super();
        this.books = new ArrayList<AARemoteBook>();
        this.max = MAX;
    }


    public void addBook(AARemoteBook b){
        books.add(b);
    }

    public AARemoteBook getBook(int pos){
        return books.get(pos);
    }

    /**dv
     * @return the books
     */
    public List<AARemoteBook> getBooks() {
        return books;
    }

    /**
     * @param books the books to set
     */
    public void setBooks(List<AARemoteBook> books) {
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
