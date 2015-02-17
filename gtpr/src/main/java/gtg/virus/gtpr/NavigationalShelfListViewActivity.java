package gtg.virus.gtpr;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.commonsware.cwac.merge.MergeAdapter;
import com.radaee.pdf.Global;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gtg.virus.gtpr.adapters.MenuAdapter;
import gtg.virus.gtpr.entities.Menu;
import gtg.virus.gtpr.entities.User;
import gtg.virus.gtpr.retrofit.Constants;
import gtg.virus.gtpr.service.AudioService;

import static gtg.virus.gtpr.utils.Utilities.bookCache;
public class NavigationalShelfListViewActivity extends ActionBarActivity {

	private static final String TAG = NavigationalShelfListViewActivity.class.getSimpleName();

	protected DrawerLayout mDrawerLayout = null;
	
	protected ListView mDrawerList = null;
	
	protected String[] titles = {"Schedule","Find Books","Sync"};
	
	private ActionBarDrawerToggle mDrawerToggle = null;
	
	private MergeAdapter mMergeAdapter = null;

	private ExecutorService mService = Executors.newFixedThreadPool(100);
	
	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this);
		Global.Init( this );
		setContentView(R.layout.activity_main);
				
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    mDrawerList = (ListView) findViewById(R.id.left_drawer);

	    // merge adapter
	    mMergeAdapter = new MergeAdapter();


        makeProfileView();

	    // main menu
	    addMainMenu();
	    
	    // settings
	    addSettings();

	    // Set the adapter for the list view
	    mDrawerList.setAdapter(mMergeAdapter);
	    // Set the list's click listener
	    mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             * <mPaint/>
             * Implementers can call getItemAtPosition(position) if they need
             * to access the data associated with the selected item.
             *
             * @param parent   The AdapterView where the click happened.
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w(TAG , "POSITION : " + position);

                itemClick(position);
            }
        });
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
		
	/*	AppLaunchTask task = new AppLaunchTask(this);
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
				Utilities.walkdir(f , data , Utilities.pdfSlashPattern);

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
		task.execute(null,null,null);*/

        Intent service = new Intent(this , AudioService.class);
        startService(service);

    }

    private void itemClick(int position){

        Fragment frag = null;
        switch(position){
            case 0 :
                //getSupportFragmentManager().beginTransaction().remove()
                break;
            case 1 : break;
            case 2 :
                frag = new MyBookShelf();
                setTitle(getString(R.string.mybookshelf));
                break;
            case 3 :
                frag = new MyRemoteBookShelf();
                setTitle(getString(R.string.myremotebookshelf));
                break;
            case 4 :
              /*  Intent i = new Intent(this , AudioListView.class);
                startActivity(i);*/
                break;
            case 5 :
                //Intent s = new Intent(this , ScheduledBooksView.class);
                //startActivity(s);
                break;

            case 8:
                /*Intent x = new Intent(this , Help.class);
                startActivity(x);*/
                break;
        }

        if(frag != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame , frag)
                    .commit();
        }
        mDrawerLayout.closeDrawers();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void makeProfileView(){

        View profileView = this.getLayoutInflater().inflate(R.layout.menu_list_row, null);
        ImageView profilePicture = (ImageView) profileView.findViewById(R.id.img_menu_list);
        TextView userNameView = (TextView) profileView.findViewById(R.id.txt_menu_list_title);
        final User user = User.getUser();

        userNameView.setText(user.getFullName());

        if(user != null && user.picture != null){
            Picasso.with(this).load(user.picture).placeholder(R.drawable.com_facebook_profile_default_icon).error(R.drawable.com_facebook_profile_default_icon).into(profilePicture);

        }else if(user != null && user.image != null){
            //Picasso.with(this).load(user.getPhoto()).error(R.drawable.com_facebook_profile_default_icon).into(profilePicture);
/*            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                profileView.setBackground(getResources().getDrawable(R.drawable.com_facebook_profile_default_icon));
            }else{
                profileView.setBackgroundResource(R.drawable.com_facebook_profile_default_icon);
            }*/

            final String image  = Constants.SERVER +"/"+ user.image;
            Picasso.with(this).load(image).placeholder(R.drawable.com_facebook_profile_default_icon).error(R.drawable.com_facebook_profile_default_icon).into(profilePicture);

        }

        //imgLoader.displayImage(user.getPhoto(), profilePicture, options);

        if(user != null) {
            userNameView.setText(user.first_name + " " + user.last_name);
        }
        mMergeAdapter.addView(profileView);

    }


	private void addMainMenu(){

		final String mainMenu = "Main Menu";
		final LayoutInflater inflater = LayoutInflater.from(this);
		final View v = inflater.inflate(R.layout.layout_preference_section_header, null);
		final TextView txtView = (TextView) v.findViewById(R.id.list_item_section_text);
		txtView.setText(mainMenu);
		mMergeAdapter.addView(v);
		List<Menu> mMenu = new ArrayList<Menu>();
		mMenu.add(new Menu(getResources().getDrawable(R.drawable.mybookshelf_selector) , "My BookShelf"));
		mMenu.add(new Menu(getResources().getDrawable(R.drawable.onlinebookshelf_selector) , "My Online BookShelf"));
		mMenu.add(new Menu(getResources().getDrawable(R.drawable.bookmark_selector) , "My Bookmarks"));
		mMenu.add(new Menu(getResources().getDrawable(R.drawable.annotation_selector) , "My Annotations"));
        mMenu.add(new Menu(getResources().getDrawable(R.drawable.audio_selector) , "My Audio"));
        mMenu.add(new Menu(getResources().getDrawable(R.drawable.schedule_selector) , "My Scheduled Reading"));
        mMenu.add(new Menu(getResources().getDrawable(R.drawable.readinglogs_selector) , "My Reading Logs"));

		
		final MenuAdapter mAdapter = new MenuAdapter(this, mMenu);
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
		mMenu.add(new Menu(getResources().getDrawable(R.drawable.setting_selector) , "Settings"));
		mMenu.add(new Menu(getResources().getDrawable(R.drawable.ic_menu_help) , "Help"));
		final MenuAdapter mAdapter = new MenuAdapter(this, mMenu);
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
		//this.getMenuInflater().inflate(R.menu.main_menu, menu);
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
	        /*Intent getContentIntent = FileUtils.createGetContentIntent();

	        Intent intent = Intent.createChooser(getContentIntent, "Select a file");
	        startActivityForResult(intent, REQUEST_CHOOSER);
	    	break;*/

             break;
        case R.id.menu_record:
             Intent i = new Intent(this , AudioBookMaker.class);
             startActivity(i);
             break;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		bookCache.clear();

        stopService(new Intent(this , AudioService.class));

        Global.RemoveTmp();
	}


    public void setPageTitle(String title){
        getSupportActionBar().setTitle(title);
    }

	
}
