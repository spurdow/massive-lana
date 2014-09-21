package gtg.virus.gtpr.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.radaee.pdf.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import gtg.virus.gtpr.R;
import gtg.virus.gtpr.db.Book;
import gtg.virus.gtpr.db.ScheduleHelper;
import gtg.virus.gtpr.db.ScheduledBooks;
import nl.siegmann.epublib.epub.EpubReader;

import static gtg.virus.gtpr.utils.Utilities.isEpub;
import static gtg.virus.gtpr.utils.Utilities.isMp3;
import static gtg.virus.gtpr.utils.Utilities.isPdf;
import static gtg.virus.gtpr.utils.Utilities.isTxt;
import static gtg.virus.gtpr.utils.Utilities.renderPage;


public class BookAdapter extends AbstractListAdapter<Book> {

    public interface OnEventListener {
        void onClick(BookAdapter adapter, Book book , int position );
        void onToggle(BookAdapter adapter , Book book, int position , boolean toggle , Switch toggleW);
    }

    protected OnEventListener mListener = null;


    public BookAdapter(Context context, List<Book> lists) {
        super(context, lists);
    }

    public void add(Book b){
        getList().add(b);
        notifyDataSetChanged();
    }


    public void setmListener(OnEventListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public View getOverridedView(final int position, View child, ViewGroup root) {

        ViewHolder mHolder = null;
        if(child == null){
            mHolder = new ViewHolder();
            child = mInflater.inflate(R.layout.pbook_list_row , null);
            mHolder.mImage = (ImageView) child.findViewById(R.id.img_menu_list);
            mHolder.mTitle = (TextView) child.findViewById(R.id.txt_menu_list_title);
            mHolder.details = (TextView) child.findViewById(R.id.txt_menu_list_details);
            mHolder.toggle = (Switch) child.findViewById(R.id.switch_toggle);

            child.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) child.getTag();
        }

        final Book book = getObject(position);

        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onClick(BookAdapter.this , book, position);
                }
            }
        });

        ScheduleHelper helper = new ScheduleHelper(getContext());
        ScheduledBooks sbook = helper.getSchedBook(book.getId());

        if(sbook == null){
            sbook = new ScheduledBooks();
            sbook.setBitWeek("0000000");
            sbook.setStatus(0);
            sbook.setInfo("Not scheduled yet.");
            sbook.setTimeToAlarm("-1");
            sbook.setBook_id(book.getId());
            sbook.setPath(book.getPath());
            helper.add(sbook);
        }
        
        mHolder.mTitle.setText(book.getTitle());
        boolean isScheduled = (sbook.getStatus()==1)?true:false;
        mHolder.toggle.setChecked(isScheduled);
        mHolder.details.setText(sbook.getInfo());

        String filePath = book.getPath();
        ///////////
        File file = new File(filePath);
        Bitmap page0 = null;
        if(isPdf(filePath)){
            Document mDoc = new Document();
            mDoc.Open(filePath, "");
            page0 = renderPage(mDoc , 100, 100);

        }else if(isEpub(filePath)){
            EpubReader epubReader = new EpubReader();
            nl.siegmann.epublib.domain.Book epubBook = null;
            try {
                epubBook = epubReader.readEpub(new FileInputStream(filePath));

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(epubBook != null){
                try {
                    page0 = BitmapFactory.decodeStream(epubBook.getCoverImage().getInputStream());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }else if(isTxt(filePath)){

            page0 = BitmapFactory.decodeResource(getContext().getResources() , R.drawable.ic_content_paste);


        }else if(isMp3(filePath)){
            MediaMetadataRetriever meta = new MediaMetadataRetriever();
            meta.setDataSource(filePath);

            byte[] art = meta.getEmbeddedPicture();

            try {
                page0 = BitmapFactory.decodeByteArray(art, 0, art.length);
            }catch(Exception ex){

            }

        }
        if(page0 != null){
            mHolder.mImage.setImageBitmap(page0);
        }


        /////////////
        final Switch t = mHolder.toggle;
        mHolder.toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mListener != null){
                    mListener.onToggle(BookAdapter.this , book , position , isChecked , t);

                }
            }
        });

        return child;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder{
        ImageView mImage;
        TextView mTitle;
        TextView details;
        Switch toggle;
    }
}
