package gtg.virus.gtpr;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import gtg.virus.gtpr.adapters.AudioListAdapter;
import gtg.virus.gtpr.async.AddNewBookTask;
import gtg.virus.gtpr.base.BaseFragment;
import gtg.virus.gtpr.entities.Audio;
import gtg.virus.gtpr.entities.PBook;
import gtg.virus.gtpr.service.AudioService;
import gtg.virus.gtpr.utils.Utilities;

import static gtg.virus.gtpr.utils.Utilities.bookCache;

public class AudioListView extends BaseFragment implements AudioListAdapter.OnRefreshList {

    private static final String TAG = AudioListView.class.getSimpleName();
    private static final int REQUEST_CHOOSER = 12345;
    @InjectView(R.id.audio_list)
    protected ListView mListView;

    private AudioListAdapter mAdapter;




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
            case android.R.id.home: getActivity().finish(); break;
            case R.id.menu_add:
                // Create the ACTION_GET_CONTENT Intent
                Intent getContentIntent = FileUtils.createGetContentIntent();

                Intent intent = Intent.createChooser(getContentIntent, "Select a file");
                startActivityForResult(intent, REQUEST_CHOOSER);
                break;
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

    @Override
    protected boolean hasOptions() {
        return true;
    }

    @Override
    protected void provideOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAdapter = new AudioListAdapter(getActivity());
        mAdapter.setmRef(this);

        mListView.setAdapter(mAdapter);

        new AsyncFind().execute(null,null,null);
    }

    @Override
    protected int resourceId() {
        return R.layout.audio_books_main_layout;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.audio_list_view , menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHOOSER:
                if (resultCode == Activity.RESULT_OK) {
                    final Uri uri = data.getData();
                    final String path = FileUtils.getPath(getActivity(), uri);
                    final File newFile = new File(path);

                    if (bookCache.containsKey(newFile.getName())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Warning");
                        builder.setMessage("The system found that this file is already in your list.If you continue file will NOT be appended.");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                    } else {
                        new AddNewBookTask(getActivity(), new AddNewBookTask.OnFinishTask() {
                            @Override
                            public void onFinish(PBook pbook) {
                                //mShelfAdapter.addBook(pbook);
                            }
                        } , AddNewBookTask.State.Mp3).execute(path);
                    }
                }
        }
    }
}
