package gtg.virus.gtpr;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.radaee.pdf.Document;
import com.radaee.pdf.Page.Annotation;
import com.radaee.reader.PDFReader;
import com.radaee.reader.PDFReader.PDFReaderListener;
import com.radaee.view.PDFVPage;

import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import gtg.virus.gtpr.aaentity.AABook;
import gtg.virus.gtpr.aaentity.AABookmark;
import gtg.virus.gtpr.entities.PBook;
import gtg.virus.gtpr.utils.Utilities;

import static gtg.virus.gtpr.utils.Utilities.PIN_EXTRA_PBOOK;

public class GTGPdfViewer extends AbstractViewer implements PDFReaderListener , AbstractViewer.OnActionBarItemClick{

	private static final String TAG = GTGPdfViewer.class.getSimpleName();

	private Document mDoc = new Document();

	private PDFReader mReader = null;

    @InjectView(R.id.toggle_bookmark)
    protected ToggleButton mToggle;

    /**
     * @return the layout from R.
     */
    @Override
    protected int getContentViewResId() {
        return R.layout.pdf_main_layout;
    }

    @Override
    protected void initializeResources(Bundle saveInstanceState) {
        mReader = (PDFReader) findViewById(R.id.pdf_reader_view);

        mPageNo = (TextView) findViewById(R.id.pdf_reader_pageno);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String gsonExtra =  extras.getString(PIN_EXTRA_PBOOK);

            mBook = new Gson().fromJson(gsonExtra, PBook.class);
        }



        mDoc.Open(mBook.getPath(), null);
        mReader.PDFOpen(mDoc, false, this);

        mPageCount = mDoc.GetPageCount();

        mPageNo.setText("0/"+mPageCount);

        maxPage = mPageCount;

        current_book = AABook.find(mBook.getPath());

        setCurrentPage(0);
        setAbsClickListener(this);
    }

    @Override
    protected void changeViewItem(MenuItem item) {

    }


    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "OnDestroy");
		super.onDestroy();
		if(mDoc != null){
			mDoc.Close();
		}
		
	}

    public void checkBookMark(boolean conditional) {
        mToggle.setChecked(conditional);
    }
    @OnCheckedChanged(R.id.toggle_bookmark)
    void onBookMarked(boolean conditional){
        if(conditional){
            AABookmark aaBookmark =AABookmark.findBookMark(current_page , current_book);
            if(aaBookmark == null){
                aaBookmark = new AABookmark();
                aaBookmark.book = current_book;
                aaBookmark.bookmark_page = current_page;
                aaBookmark.sentence_sample = "";
                long id = aaBookmark.save();
                if(id > 0){
                    Utilities.makeText(this, getString(R.string.success));
                    checkBookMark(true);
                }else{
                    Utilities.makeText(this, getString(R.string.failed_to_bookmark ));
                    checkBookMark(false);
                }
            }
        }else{
            AABookmark aaBookmark =AABookmark.findBookMark(current_page , current_book);
            if(aaBookmark != null){
                aaBookmark.delete();

            }

            checkBookMark(false);
        }
        Log.w(TAG , "Bookmarked!");
    }
	


	/************************************************************************
	 * 			Listener Event for Pdf Page and User Events
	 *************************************************************************/

	@Override
	public void OnPageModified(int pageno) {
		// TODO Auto-generated method stub
		Log.i(TAG, "OnPageModified " + pageno);
	}

	@Override
	public void OnPageChanged(int pageno) {
		// TODO Auto-generated method stub
		Log.i(TAG, "OnPageChanged " + pageno);
		mPageNo.setText(pageno+"/"+mPageCount);
        setCurrentPage(pageno);



        AABookmark aaBookmark =AABookmark.findBookMark(pageno , current_book);
        if(aaBookmark != null){
            //Utilities.makeText(this, getString(R.string.success));
            checkBookMark(true);

        }else{
            checkBookMark(false);
        }
	}

	@Override
	public void OnAnnotClicked(PDFVPage vpage, Annotation annot) {
		// TODO Auto-generated method stub
		Log.i(TAG, "OnAnnotClicked");
	}

	@Override
	public void OnSelectEnd(String text) {
		// TODO Auto-generated method stub
		Log.i(TAG, "OnSelectEnd " + text);
	}

	@Override
	public void OnOpenURI(String uri) {
		// TODO Auto-generated method stub
		Log.i(TAG, "OnOpenURI " + uri);
	}

	@Override
	public void OnOpenJS(String js) {
		// TODO Auto-generated method stub
		Log.i(TAG, "OnOpenJS " + js);
	}

	@Override
	public void OnOpenMovie(String path) {
		// TODO Auto-generated method stub
		Log.i(TAG, "OnOpenMovie " + path);
	}

	@Override
	public void OnOpenSound(int[] paras, String path) {
		// TODO Auto-generated method stub
		Log.i(TAG, "OnOpenSound " + path);
	}

	@Override
	public void OnOpenAttachment(String path) {
		// TODO Auto-generated method stub
		Log.i(TAG, "OnOpenAttachment " + path);
	}

	@Override
	public void OnOpen3D(String path) {
		// TODO Auto-generated method stub
		Log.i(TAG, "OnOpen3D " + path);
	}


    @Override
    public void onItemClick(MenuItem item) {

    }

    @Override
    public void onItemClick(int resId) {

    }

    @Override
    public void onSearch() {

    }

    @Override
    public void onSetAlarm() {

    }

    @Override
    public void onViewDetails() {

    }
}
