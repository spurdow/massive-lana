package gtg.virus.gtpr;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import gtg.virus.gtpr.entities.User;
import gtg.virus.gtpr.utils.Utilities;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashActivity extends Activity {


    private static final String TAG = SplashActivity.class.getSimpleName();
    protected SplashRunnable mRunnable = null;
	
	protected final Handler mHandler = new Handler();
	
	public final static int MAX_DELAY = 3000;
	
	private TextView mTextView;

    public final static String DISPLAY_NAME = "display_name";
    public final static String PHOTO_ID = "photo_id";
    public final static String PHOTO_FILE_ID = "photo_file_id";
    public final static String PHOTO_URI = "photo_uri";
    public final static String PHOTO_THUMB_URI = "photo_thumb_uri";
	
    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);    
        setContentView(R.layout.activity_splash);
        
/*        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("gtg.virus.gtpr", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
*/        
        mTextView  = (TextView) findViewById(R.id.txt_title_one);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Pacifico.ttf");  
        mTextView.setTypeface(tf,Typeface.BOLD);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mTextView, "y", 400f , 100f );
        animator.setDuration(1000);
        animator.start();
        
        mRunnable = new SplashRunnable();
        
        mHandler.postDelayed(mRunnable, MAX_DELAY);
    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		mHandler.removeCallbacks(mRunnable);
	}

    public static Map<String , String> getList(Context context){
        final Map<String , String> kv = new HashMap<String , String>();
        kv.put(DISPLAY_NAME, "");
        kv.put(PHOTO_ID, "");
        kv.put(PHOTO_FILE_ID, "");
        kv.put(PHOTO_URI, "");
        kv.put(PHOTO_THUMB_URI, "");

        Cursor c = context.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        int count = c.getCount();
        String[] columnNames = c.getColumnNames();
        boolean b = c.moveToFirst();
        int position = c.getPosition();
        if (count == 1 && position == 0) {
            for (int j = 0; j < columnNames.length; j++) {
                String columnName = columnNames[j];
                String columnValue = c.getString(c.getColumnIndex(columnName));

                // consume the values here
                if(kv.containsKey(columnName)){
                    kv.put(columnName, columnValue);

                }Log.i(TAG, "Col " + columnName + " " + columnValue);
            }
        }
        c.close();
        return kv;
    }
	
	private class SplashRunnable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent i = new Intent(SplashActivity.this, NavigationalShelfListViewActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
			User user = Utilities.getUser(SplashActivity.this);

            Map<String, String> userFromPhone = getList(SplashActivity.this);

            if(user == null){
                user = new User();

            }

            user.setPhoto(userFromPhone.get(PHOTO_THUMB_URI));
            user.setFullname(userFromPhone.get(DISPLAY_NAME));
            Utilities.saveUser(SplashActivity.this, user);


/*			if(user == null){
				// go to login
				Intent i = new Intent(SplashActivity.this,  LoginActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
			}else{
				// go to main
				Intent i = new Intent(SplashActivity.this,  NavigationalShelfListViewActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
			}*/
		}
		
	}
    
}
