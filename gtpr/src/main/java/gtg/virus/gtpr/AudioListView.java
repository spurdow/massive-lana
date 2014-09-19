package gtg.virus.gtpr;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import gtg.virus.gtpr.adapters.AudioListAdapter;
import gtg.virus.gtpr.entities.Audio;
import gtg.virus.gtpr.service.AudioService;
import gtg.virus.gtpr.utils.Utilities;

public class AudioListView extends ActionBarActivity {

    private ListView mListView;

    private AudioListAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_books_main_layout);

        mListView = (ListView) findViewById(R.id.audio_list);

        mAdapter = new AudioListAdapter(this);

        mListView.setAdapter(mAdapter);

        new AsyncFind().execute(null,null,null);

        Intent service = new Intent(this , AudioService.class);
        startService(service);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService(new Intent(this , AudioService.class));


    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p/>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: finish(); break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class AsyncFind extends AsyncTask<Void , Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            HashMap<String , String> data = new HashMap<String, String>();
            File f = new File(AudioService.ABSOLUTE_PATH);
            Utilities.walkdir(f , data ,Utilities.audioPattern );

            for(Map.Entry<String, String> e : data.entrySet()){
                Audio a = new Audio();
                a.setDetails("test");
                a.setTitle(e.getKey());
                a.setPath(e.getValue());
                mAdapter.add(a);
            }


            return null;
        }
    }
}
