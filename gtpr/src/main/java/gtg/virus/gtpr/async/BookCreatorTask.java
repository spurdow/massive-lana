package gtg.virus.gtpr.async;

import static gtg.virus.gtpr.utils.Utilities.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import gtg.virus.gtpr.R;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

import com.radaee.pdf.Document;
import com.radaee.pdf.Page;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import gtg.virus.gtpr.adapters.ShelfAdapter;
import gtg.virus.gtpr.entities.PBook;
import gtg.virus.gtpr.utils.Utilities;

public class BookCreatorTask extends AsyncTask<String, Void , PBook>{

	private static final String TAG = BookCreatorTask.class.getSimpleName();

	private Context mContext;
	
	private ShelfAdapter mAdapter;
	
	private Document mDoc;
	
	
	public BookCreatorTask(Context mContext, ShelfAdapter mAdapter) {
		super();
		this.mContext = mContext;
		this.mAdapter = mAdapter;
	}



	@Override
	protected PBook doInBackground(String... params) {
		// TODO Auto-generated method stub
		PBook newBook = null;
		File file = new File(params[0]);
		if(isPdf(params[0])){
			mDoc = new Document();
			mDoc.Open(params[0], "");
			Bitmap b = renderPage(mDoc , 100, 100);
			String title = mDoc.GetMeta("Title");
			String author = mDoc.GetMeta("Author");
			String subject = mDoc.GetMeta("Subject");
			String producer = mDoc.GetMeta("Producer");
			
			
			Log.i(TAG,"Title [" + title + "] Author [" + author + "]  Producer [" + producer + "]  Subject [" + subject +"]");
			newBook = new PBook(null, title, author, "Test", null,b);
			newBook.setPath(params[0]);
			newBook.isPdf = true;
			newBook.setFilename(file.getName());
			
		}else if(isEpub(params[0])){
			EpubReader epubReader = new EpubReader();
        	Book epubBook = null;
        	try {
				epubBook = epubReader.readEpub(new FileInputStream(params[0]));
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	if(epubBook != null){
        		newBook = new PBook();
        		final String title = epubBook.getTitle();
           		for(Author auth : epubBook.getMetadata().getAuthors()){
        			newBook.addAuthor(auth.getFirstname() + " " + auth.getLastname());
        		}
        		newBook.isEpub = true;
        		newBook.setTitle(title);
        		newBook.setPath(params[0]);
        		newBook.setFilename(file.getName());
        		Bitmap page0 = null;
        		try {
					page0 = BitmapFactory.decodeStream(epubBook.getCoverImage().getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		if(page0 != null){
        			newBook.setPage0(page0);
        		}	
        	}else if(isTxt(params[0])){
                newBook = new PBook();
                newBook.setTitle("TextFile");
                Bitmap page0 = BitmapFactory.decodeResource(mContext.getResources() , R.drawable.ic_content_paste);
                newBook.setPage0(page0);
                newBook.setPath(params[0]);
                newBook.setFilename(file.getName());
                Log.w(TAG , "txt file added");
            }
		}
		return newBook;
	}



	@Override
	protected void onPostExecute(PBook result) {
		// TODO Auto-generated method stub
		if(result == null) return;
		super.onPostExecute(result);
		mAdapter.addBook(result);
		bookCache.put(result.getFilename(), result);
		if(mDoc != null)
			mDoc.Close();

	}
	
	

}
