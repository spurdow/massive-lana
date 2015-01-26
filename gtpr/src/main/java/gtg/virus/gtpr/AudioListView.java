package gtg.virus.gtpr;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import gtg.virus.gtpr.adapters.AudioListAdapter;
import gtg.virus.gtpr.entities.Audio;
import gtg.virus.gtpr.service.AudioService;
import gtg.virus.gtpr.utils.Utilities;

public class AudioListView extends ActionBarActivity implements AudioListAdapter.OnRefreshList {

    private static final String TAG = AudioListView.class.getSimpleName();
    private ListView mListView;

    private AudioListAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_books_main_layout);

        mListView = (ListView) findViewById(R.id.audio_list);

        mAdapter = new AudioListAdapter(this);
        mAdapter.setmRef(this);

        mListView.setAdapter(mAdapter);

        new AsyncFind().execute(null,null,null);




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <mPaint/>
     * <mPaint>Derived classes should call through to the base class for it to
     * perform the default menu handling.</mPaint>
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

    @Override
    public void refresh(int pos) {
        final int size= mAdapter.getCount();

        for(int i = 0; i < size ; i++){
            if(i == pos){
                Audio a = mAdapter.getObject(i);
                a.setIsPlay(true);
            }else{
                Audio a = mAdapter.getObject(i);
                a.setIsPlay(false);
            }
        }

        mAdapter.notifyDataSetChanged();
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
                Log.w(TAG , "key " + e.getKey() + " val " + e.getValue());
                mAdapter.add(a);
            }


            return null;
        }
    }
}
