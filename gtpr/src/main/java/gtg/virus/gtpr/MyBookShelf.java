package gtg.virus.gtpr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
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
import gtg.virus.gtpr.adapters.ShelfAdapter;
import gtg.virus.gtpr.async.AddNewBookTask;
import gtg.virus.gtpr.async.AppLaunchTask;
import gtg.virus.gtpr.async.BookCreatorTask;
import gtg.virus.gtpr.base.BaseFragment;
import gtg.virus.gtpr.entities.PBook;
import gtg.virus.gtpr.utils.Utilities;

import static gtg.virus.gtpr.utils.Utilities.STORAGE_SUFFIX;
import static gtg.virus.gtpr.utils.Utilities.bookCache;

/**
 * Created by DavidLuvelleJoseph on 2/15/2015.
 */
public class MyBookShelf extends BaseFragment implements ShelfAdapter.OnViewClick {

    public static final String TAG  = MyBookShelf.class.getSimpleName();
    private static final int REQUEST_CHOOSER = 12345;

    @InjectView(R.id.shelf_list_view)
    protected ListView mListView;

    private ShelfAdapter mShelfAdapter = null;

    @Override
    protected boolean hasOptions() {
        return true;
    }

    @Override
    protected void provideOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mShelfAdapter = new ShelfAdapter(getActivity());
        mListView.setAdapter(mShelfAdapter);
        mShelfAdapter.setmClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppLaunchTask task = new AppLaunchTask(getActivity() , STORAGE_SUFFIX);
		task.setListener(new AppLaunchTask.AppLaunchListener(){

			@Override
			public void onStartTask() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinishTask() {
				// TODO Auto-generated method stub

				HashMap<String ,String> data = new HashMap<String , String>();
				File f = new File(Environment.getExternalStorageDirectory() , Utilities.STORAGE_SUFFIX);
				Utilities.walkdir(f, data, Utilities.pdfSlashPattern);

				if(data.size() > 0){
					for(Map.Entry<String,String> e : data.entrySet()){
						BookCreatorTask bookTask = new BookCreatorTask(getActivity(), mShelfAdapter);
						bookTask.execute(e.getValue());
						Log.i(TAG, "Val " + e.getValue());
					}

				}else{
					Utilities.makeText(getActivity()  , "You dont have ebook/s yet.");
					//Toast.makeText(NavigationalShelfListViewActivity.this, "You dont have pdf's yet", Toast.LENGTH_LONG).show();
				}
			}

		});
		task.execute(null,null,null);

    }

    @Override
    protected int resourceId() {
        return R.layout.shelf_fragment;
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
                                mShelfAdapter.addBook(pbook);
                            }
                        }).execute(path);
                    }
                }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_bookshelf_menu , menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
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
    public void bookClick(PBook book, int position) {
        Utilities.makeText(getActivity() , "Position " + position);
    }
}
