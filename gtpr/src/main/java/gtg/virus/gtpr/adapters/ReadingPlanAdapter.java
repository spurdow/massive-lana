package gtg.virus.gtpr.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.radaee.pdf.Document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import gtg.virus.gtpr.R;
import gtg.virus.gtpr.aaentity.AABook;
import gtg.virus.gtpr.utils.Utilities;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

import static gtg.virus.gtpr.utils.Utilities.renderPage;

/**
 * Created by DavidLuvelleJoseph on 2/22/2015.
 */
public class ReadingPlanAdapter extends AbstractListAdapter<AABook> {

    private static final String TAG = ReadingPlanAdapter.class.getSimpleName();
    private Document mDoc;

    public ReadingPlanAdapter(Context context, List<AABook> lists) {
        super(context, lists);
    }

    public ReadingPlanAdapter(Context context) {
        super(context);
    }

    @Override
    public View getOverridedView(int position, View child, ViewGroup root) {
        ViewHolder v = null;

        if(child == null){
            child = mInflater.inflate(R.layout.reading_plan_row , root, false);
            v = new ViewHolder(child);
            child.setTag(v);
        }else{
            v = (ViewHolder) child.getTag();
        }

        AABook book = getObject(position);
        if(Utilities.isEpub(book.path)) {
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

            if (epubBook != null) {
                Bitmap page0 = null;
                try {
                    page0 = BitmapFactory.decodeStream(epubBook.getCoverImage().getInputStream());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    page0 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_content_paste);
                }

                v.bookImage.setImageBitmap(page0);
            }
        }else if(Utilities.isPdf(book.path)){

            mDoc = new Document();
            int index = mDoc.Open(book.path, "");

            if( index == 0) {
                Bitmap page0 = renderPage(mDoc, 100, 100);
                v.bookImage.setImageBitmap(page0);
            }

        }

        v.bookSample.setText(book.title);
        String status = "";
        switch(book.status){

            case 1 : status = getContext().getString(R.string.to_read);break;
            case 2 : status = getContext().getString(R.string.currently_reading);break;
            case 3 : status = getContext().getString(R.string.done_reading);break;
            case 4 : status = getContext().getString(R.string.reread); break;
        }

        v.bookStatus.setText(status);

        if(mDoc != null){
            mDoc.Close();
            mDoc = null;
        }

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
        TextView bookSample;
        @InjectView(R.id.txt_status)
        TextView bookStatus;

        ViewHolder(View child){
            ButterKnife.inject(this, child);
        }
    }
}
