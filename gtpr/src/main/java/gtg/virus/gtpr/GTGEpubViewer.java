package gtg.virus.gtpr;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import gtg.virus.gtpr.entities.PBook;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

import static gtg.virus.gtpr.utils.Utilities.PIN_EXTRA_PBOOK;

public class GTGEpubViewer extends ActionBarActivity {

    private final static String TAG = GTGEpubViewer.class.getSimpleName();

    private WebView mViewPage ;

    private PBook mBook ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.epub_main_layout);

        mViewPage = (WebView) findViewById(R.id.epub_web_view);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String gsonExtra =  extras.getString(PIN_EXTRA_PBOOK);

            mBook = new Gson().fromJson(gsonExtra, PBook.class);
        }

        EpubReader epubReader = new EpubReader();
        Book epubBook = null;
        try {
            epubBook = epubReader.readEpub(new FileInputStream(mBook.getPath()));

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final String baseUrl = Environment.getExternalStorageDirectory().getPath()  + "/";
        String data = "";
        try {
            data = new String(epubBook.getContents().get(2).getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String type = "text/html";
        final String encoding = "UTF-8";

        mViewPage.loadDataWithBaseURL(baseUrl , data , type , encoding , null );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.pdf_viewer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){

            case android.R.id.home: finish(); break;

            case R.id.opt_menu_search: break;

            case R.id.opt_menu_set_alarm: break;

            case R.id.opt_menu_view: break;
        }
        return super.onOptionsItemSelected(item);
    }

}
