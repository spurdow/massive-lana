package gtg.virus.gtpr.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import gtg.virus.gtpr.R;
import gtg.virus.gtpr.aaentity.AARemoteBook;
import gtg.virus.gtpr.entities.RemoteShelf;

/**
 * Created by DavidLuvelleJoseph on 2/16/2015.
 */
public class RemoteShelfAdapter extends BaseAdapter {
    private Context mContext;

    private List<RemoteShelf> shelves;

    private List<AARemoteBook> books;

    private LayoutInflater inflater = null;

    public final static int MAX_CHARACTERS = 10;

    protected static final String TAG = ShelfAdapter.class.getSimpleName();


    public interface OnViewClick{
        void bookClick(AARemoteBook book , int position);
    }

    /**
     * @param mContext
     * @param inflater
     */
    public RemoteShelfAdapter(Context mContext,
                        LayoutInflater inflater) {
        super();
        this.mContext = mContext;
        this.shelves = new ArrayList<RemoteShelf>();
        this.inflater = LayoutInflater.from(mContext);
        this.books = new ArrayList<AARemoteBook>();
    }

    public RemoteShelfAdapter(
            Context context) {
        // TODO Auto-generated constructor stub
        this(context, null);
    }


    public void addBook(AARemoteBook b){
        if(!shelves.isEmpty()){
            RemoteShelf shelf = shelves.get(shelves.size()-1);
            int sizeOfShelf = shelf.getBooks().size();
            if(sizeOfShelf < shelf.getMax()){
                shelf.addBook(b);
            }else{
                RemoteShelf newShelf = new RemoteShelf();
                newShelf.addBook(b);
                shelves.add(newShelf);
            }
        }else{
            RemoteShelf shelf = new RemoteShelf();
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
    public AARemoteBook getBookAtPosition(int bookPosition){
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


        RemoteShelf shelf = (RemoteShelf) getItem(position);
        if(shelf != null){
            final int maxSize  = shelf.getBooks().size();

            for(int i = 0 ; i < maxSize ; i++){
                final AARemoteBook b = shelf.getBook(i);



                if(b != null){

                    FrameLayout ff = (FrameLayout) viewHolder.shelf_parent.getChildAt(i);
                    if(ff == null){
                        ff = (FrameLayout) inflater.inflate(R.layout.shelf_item, null);
                    }
                    ff.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {

                            if(mClickListener != null){
                                mClickListener.bookClick(b , position);
                                return;
                            }
                        }

                    });

                    TextView tv = (TextView) ff.findViewById(R.id.title);
                    tv.setText(b.title);


                    String uri = b.path + ""+ b.fileName;
                    ImageView image = (ImageView) ff.findViewById(R.id.thumbnail);
                    Picasso.with(mContext).load(uri).error(R.drawable.ic_all_books).into(image);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);

                    params.leftMargin = 10;
                    params.rightMargin = 10;
                    params.gravity = Gravity.TOP;

                    viewHolder.shelf_parent.addView(ff, params);


                }
            }

        }


        return convertView;
    }

    protected OnViewClick mClickListener = null;

    public void setmClickListener(OnViewClick mClickListener) {
        this.mClickListener = mClickListener;
    }

    private class ViewHolder{
        LinearLayout shelf_parent;
    }
}
