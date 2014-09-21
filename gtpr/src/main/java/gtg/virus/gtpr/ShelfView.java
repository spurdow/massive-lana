package gtg.virus.gtpr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import gtg.virus.gtpr.adapters.ShelfAdapter;
import gtg.virus.gtpr.async.BookCreatorTask;
import gtg.virus.gtpr.entities.PBook;
import gtg.virus.gtpr.utils.Utilities;

import static gtg.virus.gtpr.utils.Utilities.STORAGE_SUFFIX;

public class ShelfView extends ActionBarActivity implements ShelfAdapter.OnViewClick {

    private static final String TAG = ShelfView.class.getSimpleName();
    private ListView mListView = null;

    private ShelfAdapter mShelfAdapter = null;

    public final static String BOOK_ID = "_book_id";

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_view);

        mListView = (ListView) findViewById(R.id.shelf_list_view);

        mShelfAdapter = new ShelfAdapter( this );
        mShelfAdapter.setmClickListener(this);

        mListView.setAdapter(mShelfAdapter);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading...");
        mDialog.show();

        new AsyncTask<Void , Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                HashMap<String, String> data = new HashMap<String, String>();
                File f = new File(Environment.getExternalStorageDirectory(), STORAGE_SUFFIX);
                Utilities.walkdir(f, data, Utilities.pdfSlashPattern);

                if (data.size() > 0) {
                    for (Map.Entry<String, String> e : data.entrySet()) {
                        BookCreatorTask bookTask = new BookCreatorTask(ShelfView.this, mShelfAdapter);
                        bookTask.execute(e.getValue());
                        Log.i(TAG, "Val " + e.getValue());
                    }

                } else {
                    Crouton.makeText(ShelfView.this, "You dont have pdf's yet", Style.INFO).show();
                    //Toast.makeText(NavigationalShelfListViewActivity.this, "You dont have pdf's yet", Toast.LENGTH_LONG).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mDialog.dismiss();
            }
        }.execute(null , null, null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: finish();break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void bookClick(PBook book, int position) {
        Intent i = new Intent();
        i.putExtra(BOOK_ID , book.toString());
        setResult(RESULT_OK , i);
        finish();
    }
}

