package gtg.virus.gtpr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

import com.commonsware.cwac.merge.MergeAdapter;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.radaee.pdf.Document;
import com.radaee.pdf.Global;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import gtg.virus.gtpr.adapters.ShelfAdapter;
import gtg.virus.gtpr.adapters.TitleListAdapter;
import gtg.virus.gtpr.async.AppLaunchTask;
import gtg.virus.gtpr.async.AppLaunchTask.AppLaunchListener;
import gtg.virus.gtpr.async.BookCreatorTask;
import gtg.virus.gtpr.entities.PBook;
import gtg.virus.gtpr.entities.Menu;
import gtg.virus.gtpr.entities.User;
import gtg.virus.gtpr.utils.Utilities;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import static gtg.virus.gtpr.utils.Utilities.*;
public class NavigationalShelfListViewActivity extends ActionBarActivity {

	private ListView mListView = null;
	
	private Document mDoc = null;

	private static final String TAG = NavigationalShelfListViewActivity.class.getSimpleName();

	private static final int REQUEST_CHOOSER = 12345;
	
	protected DrawerLayout mDrawerLayout = null;
	
	protected ListView mDrawerList = null;
	
	protected String[] titles = {"Schedule","Find Books","Sync"};
	
	private ActionBarDrawerToggle mDrawerToggle = null;
	
	private MergeAdapter mMergeAdapter = null;
	
	private ShelfAdapter mShelfAdapter = null;
	
	private ExecutorService mService = Executors.newFixedThreadPool(100);
	
	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Global.Init( this );
		setContentView(R.layout.activity_main);
				
		mListView = (ListView) findViewById(R.id.shelf_list_view);

		mShelfAdapter = new ShelfAdapter( this );
		mListView.setAdapter(mShelfAdapter);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    mDrawerList = (ListView) findViewById(R.id.left_drawer);

	    // merge adapter
	    mMergeAdapter = new MergeAdapter();
	    
	    final TextView userName = new TextView(this);
	    User user = getUser(this);
	    
	    if(user != null)
	    	userName.setText(user.getFullname());
	    else
	    	userName.setText("Not yet available.");
	    mMergeAdapter.addView(userName);
	    // main menu
	    addMainMenu();
	    
	    // settings
	    addSettings();

	    // Set the adapter for the list view
	    mDrawerList.setAdapter(mMergeAdapter);
/*	    // Set the list's click listener
	    mDrawerList.setOnItemClickListener(new DrawerItemClickListener(){
	    	
	    });
	    */
	    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(R.string.drawer_close);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(R.string.drawer_open);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		AppLaunchTask task = new AppLaunchTask(this);
		task.setListener(new AppLaunchListener(){

			@Override
			public void onStartTask() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onFinishTask() {
				// TODO Auto-generated method stub
				
				HashMap<String ,String> data = new HashMap<String , String>();
				File f = new File(Environment.getExternalStorageDirectory() , STORAGE_SUFFIX);
				Utilities.walkdir(f , data);

				if(data.size() > 0){
					for(Entry<String,String> e : data.entrySet()){
						BookCreatorTask bookTask = new BookCreatorTask(NavigationalShelfListViewActivity.this, mShelfAdapter);
						bookTask.execute(e.getValue());
						Log.i(TAG, "Val " + e.getValue());
					}
					
				}else{
					Crouton.makeText(NavigationalShelfListViewActivity.this, "You dont have pdf's yet", Style.INFO).show();
					//Toast.makeText(NavigationalShelfListViewActivity.this, "You dont have pdf's yet", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		task.execute(null,null,null);
		
		
	}
	

	private void addMainMenu(){

		final String mainMenu = "Main Menu";
		final LayoutInflater inflater = LayoutInflater.from(this);
		final View v = inflater.inflate(R.layout.layout_preference_section_header, null);
		final TextView txtView = (TextView) v.findViewById(R.id.list_item_section_text);
		txtView.setText(mainMenu);
		mMergeAdapter.addView(v);
		List<Menu> mMenu = new ArrayList<Menu>();
		mMenu.add(new Menu(BitmapFactory.decodeResource(getResources(),R.drawable.ic_all_books) , "Bookmarked Books"));
		mMenu.add(new Menu(BitmapFactory.decodeResource(getResources(),R.drawable.ic_annotate1) , "Annotated Books"));
		mMenu.add(new Menu(BitmapFactory.decodeResource(getResources(),R.drawable.ic_audio_play) , "Audio Books"));
		mMenu.add(new Menu(BitmapFactory.decodeResource(getResources(),R.drawable.ic_menu_calendar) , "Schedule Books"));

		
		final TitleListAdapter mAdapter = new TitleListAdapter(this, mMenu);
		mMergeAdapter.addAdapter(mAdapter);
	
	}
	
	private void addSettings(){

		final String settings = "Settings";
		final LayoutInflater inflater = LayoutInflater.from(this);
		final View v = inflater.inflate(R.layout.layout_preference_section_header, null);
		final TextView txtView = (TextView) v.findViewById(R.id.list_item_section_text);
		txtView.setText(settings);
		mMergeAdapter.addView(v);
		List<Menu> mMenu = new ArrayList<Menu>();
		mMenu.add(new Menu(BitmapFactory.decodeResource(getResources(),R.drawable.ic_action_settings) , "Settings"));
		mMenu.add(new Menu(BitmapFactory.decodeResource(getResources(),R.drawable.ic_menu_help) , "Help"));
		final TitleListAdapter mAdapter = new TitleListAdapter(this, mMenu);
		mMergeAdapter.addAdapter(mAdapter);
	
	}
	
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
	    super.onPostCreate(savedInstanceState);
	    // Sync the toggle state after onRestoreInstanceState has occurred.
	    mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	

	


	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		this.getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Pass the event to ActionBarDrawerToggle, if it returns
	    // true, then it has handled the app icon touch event
	    if (mDrawerToggle.onOptionsItemSelected(item)) {
	        return true;
	    }
	    // Handle your other action bar items...
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
        case REQUEST_CHOOSER:   
            if (resultCode == RESULT_OK) {
            	final Uri uri = data.getData();
            	final String path = FileUtils.getPath(NavigationalShelfListViewActivity.this, uri);
            	final File newFile= new File(path);
            	
            	if(bookCache.containsKey(newFile.getName())){
            		AlertDialog.Builder builder = new AlertDialog.Builder(this);
            		builder.setTitle("Warning");
            		builder.setMessage("The system found that this file is already in your list.If you continue file will be appended. Are you sure you want to continue?");
            		builder.setPositiveButton("Yes", new OnClickListener(){

						@Override
						public void onClick(final DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
		            		/**
		            		 * Asynctask saving pdf in storage
		            		 */
							new AsyncTask<Void, Void,PBook>(){

			            		private ProgressDialog mProgress;
								@Override
								protected void onPreExecute() {
									// TODO Auto-generated method stub
									super.onPreExecute();
									mProgress = new ProgressDialog(NavigationalShelfListViewActivity.this);
									mProgress.setMessage("Please wait...");
									mProgress.setIndeterminate(true);
									mProgress.show();
								}

								@Override
								protected PBook doInBackground(Void... params) {
									// TODO Auto-generated method stub
									// load the file to the pinbook repo

									File newPdf = new File(path);
									Log.w(TAG, "File " + newPdf.getName());
									final String[] arr_file = newPdf.getName().split("[.]");
									final String filename = arr_file[0] + bookCache.size() + "." + arr_file[1];
									File toStorage = new File(Environment.getExternalStorageDirectory() +"/"+ STORAGE_SUFFIX + "/" + filename );

									try {
										copy( newPdf , toStorage );
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										// unsuccessful copy
										makeText("Theres a problem with the disk.");
										return null;
									}
									
					                // Get the File path from the Uri
					                
					                PBook b = null;
					                Log.i(TAG, "Path " + path);
					                // Alternatively, use FileUtils.getFile(Context, Uri)
					                if (path != null && isValideBook(path) && FileUtils.isLocal(path)) {
					                    if(isPdf(path)){
					                    	
					                    	
					                    	mDoc = new Document();
					                    	int index = mDoc.Open(path, "");
					 
					                    	if( index == 0){
						                    	String author = mDoc.GetMeta("Author");
						                    	String title = mDoc.GetMeta("Title");
						                    	String subject = mDoc.GetMeta("Subject");
						                    
						                    	Log.i(TAG,"Meta " + author + " " + title + " " + subject);
						                    	b = new PBook();
						                    	b.addAuthor(author);
						                    	b.setTitle(title);
						                    	b.setPath(path);;
						                    	Bitmap page0 = renderPage(mDoc , 100, 100);
						                    	b.setPage0(page0);
						                    	b.isPdf= true;
						                    	b.setFilename(newPdf.getName());
						                    	
						                    	// we are not accepting file if the file doesnt give us the title
						                    	
						                    	if(title == null){
						                    		makeText("File is invalid");
						                    		b = null;
						                    	}else if(title.equals("")){
						                    		makeText("File is invalid");
						                    		b = null;
						                    	}
						                    	
					                    	}else if(index == -1){
					                    		makeText("Password needed");
					                    	}else if(index == -2){
					                    		makeText("Unknown Encryption");
					                    	}else if(index == -3){
					                    		makeText("Damage or Invalid Format");
					                    	}else if(index == -10){
					                    		makeText("Access Denied");
					                    	}
					                    	
					                    }else if(isEpub(path)){
					                    	EpubReader epubReader = new EpubReader();
					                    	Book epubBook = null;
					                    	try {
												epubBook = epubReader.readEpub(new FileInputStream(path));
											} catch (FileNotFoundException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
					                    	if(epubBook != null){
					                    		b = new PBook();
					                    		final String title = epubBook.getTitle();
					                    		for(Author auth : epubBook.getMetadata().getAuthors()){
					                    			b.addAuthor(auth.getFirstname() + " " + auth.getLastname());
					                    		}
					                    		b.isEpub = true;
					                    		b.setTitle(title);
					                    		b.setPath(path);
					                    		b.setFilename(newPdf.getName());
					                    		Bitmap page0 = null;
					                    		try {
													page0 = BitmapFactory.decodeStream(epubBook.getCoverImage().getInputStream());
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
					                    		if(page0 != null){
					                    			b.setPage0(page0);
					                    		}
					                    	}
					                    }else if(isTxt(path)){
                                            b = new PBook();
                                            b.setTitle("TextFile");
                                            Bitmap page0 = BitmapFactory.decodeResource(getResources() , R.drawable.ic_content_paste);
                                            b.setPage0(page0);
                                            b.setPath(path);
                                            b.setFilename(newPdf.getName());
                                            Log.w(TAG , "txt file added");
                                        }
					                }else if(!isValideBook(path)){
					                	makeText( "File is not a pdf/epub/txt");
					                }
					                Log.w(TAG, "doInBackground");
									return b;
								}
								
								private void makeText(final String msg){
									runOnUiThread(new Runnable(){

										@Override
										public void run() {
											// TODO Auto-generated method stub
											Crouton.makeText(NavigationalShelfListViewActivity.this, msg, Style.INFO).show();
											//Toast.makeText(NavigationalShelfListViewActivity.this, msg, Toast.LENGTH_SHORT).show();
										}
										
									});
								}
								@Override
								protected void onPostExecute(PBook result) {
									// TODO Auto-generated method stub
									super.onPostExecute(result);
									if(result != null){
										mShelfAdapter.addBook(result);
										bookCache.put(result.getFilename(), result);
									}
									mProgress.dismiss();
									dialog.dismiss();
								}
								
								
			            		
			            	}.executeOnExecutor(mService, null,null,null);	
						}

            		});
            		builder.setNegativeButton("No", new OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
            			
            		});
            		
            		final AlertDialog alert = builder.create();
            		alert.show();
            	}
            	else{
            		/**
            		 * Asynctask saving pdf in storage
            		 */
            		new AsyncTask<Void, Void,PBook>(){

	            		private ProgressDialog mProgress;
						@Override
						protected void onPreExecute() {
							// TODO Auto-generated method stub
							super.onPreExecute();
							mProgress = new ProgressDialog(NavigationalShelfListViewActivity.this);
							mProgress.setMessage("Please wait...");
							mProgress.setIndeterminate(true);
							mProgress.show();
						}

						@Override
						protected PBook doInBackground(Void... params) {
							// TODO Auto-generated method stub
							// load the file to the pinbook repo
							
							File newPdf = new File(path);
							Log.w(TAG, "File " + newPdf.getName());
							File toStorage = new File(Environment.getExternalStorageDirectory() +"/"+ STORAGE_SUFFIX + "/" + newPdf.getName() );

							try {
								copy( newPdf , toStorage );
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								// unsuccessful copy
								makeText("Theres a problem with the disk.");
								return null;
							}

			                // Get the File path from the Uri
			                
			                PBook b = null;
			                Log.i(TAG, "Path " + path);
			                // Alternatively, use FileUtils.getFile(Context, Uri)
			                if (path != null && isValideBook(path) && FileUtils.isLocal(path)) {
                                Log.w(TAG , "Valid Book");
			                    if(isPdf(path)){
			                    	
			                    	
			                    	mDoc = new Document();
			                    	int index = mDoc.Open(path, "");
			 
			                    	if( index == 0){
				                    	String author = mDoc.GetMeta("Author");
				                    	String title = mDoc.GetMeta("Title");
				                    	String subject = mDoc.GetMeta("Subject");
				                    
				                    	Log.i(TAG,"Meta " + author + " " + title + " " + subject);
				                    	b = new PBook();
				                    	b.addAuthor(author);
				                    	b.setTitle(title);
				                    	b.setPath(path);;
				                    	Bitmap page0 = renderPage(mDoc , 100, 100);
				                    	b.setPage0(page0);
				                    	b.isPdf= true;
				                    	b.setFilename(newPdf.getName());
				                    	
				                    	// we are not accepting file if the file doesnt give us the title
				                    	
				                    	if(title == null){
				                    		makeText("File is invalid");
				                    		b = null;
				                    	}else if(title.equals("")){
				                    		makeText("File is invalid");
				                    		b = null;
				                    	}
				                    	
			                    	}else if(index == -1){
			                    		makeText("Password needed");
			                    	}else if(index == -2){
			                    		makeText("Unknown Encryption");
			                    	}else if(index == -3){
			                    		makeText("Damage or Invalid Format");
			                    	}else if(index == -10){
			                    		makeText("Access Denied");
			                    	}
			                    	
			                    }else if(isEpub(path)){
			                    	EpubReader epubReader = new EpubReader();
			                    	Book epubBook = null;
			                    	try {
										epubBook = epubReader.readEpub(new FileInputStream(path));
									} catch (FileNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
			                    	if(epubBook != null){
			                    		b = new PBook();
			                    		final String title = epubBook.getTitle();
			                    		for(Author auth : epubBook.getMetadata().getAuthors()){
			                    			b.addAuthor(auth.getFirstname() + " " + auth.getLastname());
			                    		}
			                    		b.isEpub = true;
			                    		b.setTitle(title);
			                    		b.setPath(path);
			                    		b.setFilename(newPdf.getName());
			                    		Bitmap page0 = null;
			                    		try {
											page0 = BitmapFactory.decodeStream(epubBook.getCoverImage().getInputStream());
											
			                    		} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
			                    		if(page0 != null){
			                    			b.setPage0(page0);
			                    		}
			                    	}
			                    }else if(isTxt(path)){
                                    b = new PBook();
                                    b.setTitle("TextFile");
                                    Bitmap page0 = BitmapFactory.decodeResource(getResources() , R.drawable.ic_content_paste);
                                    b.setPage0(page0);
                                    b.setPath(path);
                                    b.setFilename(newPdf.getName());
                                    Log.w(TAG , "txt file added");
                                }
			                }else if(!isValideBook(path)){
			                	makeText( "File is not a pdf/epub/txt");
			                }
			                Log.w(TAG, "doInBackground");
							return b;
						}
						
						private void makeText(final String msg){
							runOnUiThread(new Runnable(){

								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Toast.makeText(NavigationalShelfListViewActivity.this, msg, Toast.LENGTH_SHORT).show();
									Crouton.makeText(NavigationalShelfListViewActivity.this, msg, Style.INFO).show();
								}
								
							});
						}
						@Override
						protected void onPostExecute(PBook result) {
							// TODO Auto-generated method stub
							super.onPostExecute(result);
							if(result != null){
								mShelfAdapter.addBook(result);
								bookCache.put(result.getFilename(), result);
							}
							mProgress.dismiss();
						}
						
						
	            		
	            	}.executeOnExecutor(mService, null,null,null);	
            	}
	        }
            break;
    }
		super.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mDoc != null){
			mDoc.Close();
			mDoc = null;
		}
		
		bookCache.clear();
		
		Global.RemoveTmp();
	}
	

	
}
