package gtg.virus.gtpr.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import gtg.virus.gtpr.GTGAudioListener;
import gtg.virus.gtpr.R;
import gtg.virus.gtpr.entities.Audio;
import gtg.virus.gtpr.entities.PBook;
import gtg.virus.gtpr.entities.Shelf;
import gtg.virus.gtpr.utils.Utilities;

import static gtg.virus.gtpr.utils.Utilities.PIN_EXTRA_PBOOK;

/**
 * Created by DavidLuvelleJoseph on 2/22/2015.
 */
public class Mp3ShelfAdapter extends BaseAdapter {

    private Context mContext;

    private List<Shelf> shelves;

    private List<PBook> books;

    private LayoutInflater inflater = null;

    public final static int MAX_CHARACTERS = 10;

    protected static final String TAG = ShelfAdapter.class.getSimpleName();


    public interface OnViewClick{
        void bookClick(PBook book , int position);
    }

    /**
     * @param mContext
     * @param inflater
     */
    public Mp3ShelfAdapter(Context mContext,
                        LayoutInflater inflater) {
        super();
        this.mContext = mContext;
        this.shelves = new ArrayList<Shelf>();
        this.inflater = LayoutInflater.from(mContext);
        this.books = new ArrayList<PBook>();
    }

    public Mp3ShelfAdapter(
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
                    ff.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {

                            if(mClickListener != null){
                                mClickListener.bookClick(b , position);
                                return;
                            }



                            if(Utilities.isMp3(b.getPath())){

                                List<Audio> audios = new ArrayList<Audio>();


                                /**
                                 *

                                 HashMap<String , String> data = new HashMap<String, String>();
                                 File f = new File(AudioService.ABSOLUTE_PATH);
                                 Utilities.walkdir(f , data ,Utilities.audioPattern );

                                 for(Map.Entry<String, String> e : data.entrySet()){
                                 Audio a = new Audio();
                                 a.setDetails("test");
                                 a.setTitle(e.getKey());
                                 a.setPath(e.getValue());

                                 */
                                for(PBook p : books){
                                    if(Utilities.isMp3(p.getPath())){
                                        Audio a = new Audio();
                                        a.setDetails("test");
                                        a.setTitle(p.getTitle());
                                        a.setPath(p.getPath());

                                        if(a.getPath().equals(b.getPath())){
                                            a.setIsPlay(true);
                                        }else{
                                            a.setIsPlay(false);
                                        }

                                        audios.add(a);
                                    }

                                }

                                Intent intent = new Intent(mContext, GTGAudioListener.class);
                                intent.putExtra(PIN_EXTRA_PBOOK , new Gson().toJson(audios, new TypeToken<List<Audio>>(){}.getType()));
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
