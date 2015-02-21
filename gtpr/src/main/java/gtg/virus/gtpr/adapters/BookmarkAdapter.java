package gtg.virus.gtpr.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import gtg.virus.gtpr.R;
import gtg.virus.gtpr.aaentity.AABook;
import gtg.virus.gtpr.aaentity.AABookmark;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by DavidLuvelleJoseph on 2/21/2015.
 */
public class BookmarkAdapter extends AbstractListAdapter<AABookmark> {


    public BookmarkAdapter(Context context) {
        super(context);
    }

    @Override
    public View getOverridedView(int position, View child, ViewGroup root) {
        ViewHolder v = null;
        if(child == null){
            child = mInflater.inflate(R.layout.bookmark_list_row , root, false);
            v = new ViewHolder(child);
            child.setTag(v);
        }else{
            v = (ViewHolder) child.getTag();
        }

        AABookmark bookmark = getObject(position);

        AABook book = bookmark.book;

        EpubReader epubReader = new EpubReader();
        Book epubBook = null;
        try {
            epubBook = epubReader.readEpub(new FileInputStream(book.path));

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(epubBook != null){
            Bitmap page0 = null;
            try {
                page0 = BitmapFactory.decodeStream(epubBook.getCoverImage().getInputStream());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch(NullPointerException e){
                e.printStackTrace();
                page0 = BitmapFactory.decodeResource(getContext().getResources() , R.drawable.ic_content_paste);
            }

            v.bookImage.setImageBitmap(page0);
        }
        v.bookPage.setText("Page " + String.valueOf(bookmark.bookmark_page + 1));
        v.bookName.setText(book.title);
        v.bookSample.setText(bookmark.sentence_sample);

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
        TextView bookName;
        @InjectView(R.id.txt_sample)
        TextView bookSample;
        @InjectView(R.id.txt_page)
        TextView bookPage;

        ViewHolder(View child){
            ButterKnife.inject(this, child);
        }
    }
}
