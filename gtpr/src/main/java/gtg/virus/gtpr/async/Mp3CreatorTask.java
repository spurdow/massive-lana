package gtg.virus.gtpr.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.Log;

import com.radaee.pdf.Document;

import java.io.File;

import gtg.virus.gtpr.aaentity.AABook;
import gtg.virus.gtpr.adapters.Mp3ShelfAdapter;
import gtg.virus.gtpr.entities.PBook;

import static gtg.virus.gtpr.utils.Utilities.bookCache;
import static gtg.virus.gtpr.utils.Utilities.isMp3;

/**
 * Created by DavidLuvelleJoseph on 2/22/2015.
 */
public class Mp3CreatorTask  extends AsyncTask<String, Void , PBook> {

    private static final String TAG = BookCreatorTask.class.getSimpleName();

    private Context mContext;

    private Mp3ShelfAdapter mAdapter;

    private Document mDoc;




    public Mp3CreatorTask(Context mContext, Mp3ShelfAdapter mAdapter) {
        super();
        this.mContext = mContext;
        this.mAdapter = mAdapter;
    }

    @Override
    protected PBook doInBackground(String... params) {
        // TODO Auto-generated method stub
        PBook newBook = null;
        File file = new File(params[0]);
        if(isMp3(params[0])){
            newBook = new PBook();
            MediaMetadataRetriever meta = new MediaMetadataRetriever();
            meta.setDataSource(params[0]);

            byte[] art = meta.getEmbeddedPicture();
            Bitmap songImage = null;
            try {
                songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
            }catch(Exception ex){

            }
            newBook.setPage0(songImage);
            newBook.addAuthor(meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR));
            newBook.setFilename(file.getName());
            newBook.setTitle(file.getName());
            newBook.setPath(params[0]);

            meta.release();
            Log.w(TAG , "mp3 file added. " );
        }
        return newBook;
    }



    @Override
    protected void onPostExecute(PBook result) {
        // TODO Auto-generated method stub
        if(result == null) return;
        super.onPostExecute(result);

/*
        BookHelper mh = new BookHelper(mContext);
        gtg.virus.gtpr.db.Book b = mh.getBook(result.getPath());
*/

        AABook newBook = AABook.find(result.getPath());

        if(newBook == null){


            newBook = new AABook();
            newBook.title = result.getTitle();
            newBook.path = result.getPath();
            newBook.status = 1;

            long id = newBook.save();
            if(id > 0){
                mAdapter.addBook(result);
                bookCache.put(result.getFilename(), result);
            }

        }else{
            mAdapter.addBook(result);
            bookCache.put(result.getFilename(), result);
        }



        if(mDoc != null)
            mDoc.Close();

    }

}
