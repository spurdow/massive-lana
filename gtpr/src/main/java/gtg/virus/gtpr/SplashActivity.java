package gtg.virus.gtpr;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import gtg.virus.gtpr.base.BaseActionBarActivity;
import gtg.virus.gtpr.entities.User;

public class SplashActivity extends BaseActionBarActivity {


    private static final String TAG = SplashActivity.class.getSimpleName();
    protected SplashRunnable mRunnable = null;
	
	protected final Handler mHandler = new Handler();
	
	public final static int MAX_DELAY = 3000;

    @InjectView(R.id.txt_title_one)
	TextView mTextView;

    public final static String DISPLAY_NAME = "display_name";
    public final static String PHOTO_ID = "photo_id";
    public final static String PHOTO_FILE_ID = "photo_file_id";
    public final static String PHOTO_URI = "photo_uri";
    public final static String PHOTO_THUMB_URI = "photo_thumb_uri";

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

    @Override
    protected boolean fullScreen() {
        return true;
    }

    @Override
    protected boolean homeButton() {
        return false;
    }

    @Override
    protected boolean displayHomeUp() {
        return false;
    }

    @Override
    protected boolean displayTitle() {
        return false;
    }

    @Override
    protected int resLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void provideOnCreate(Bundle savedInstanceState) {


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "gtg.virus.gtpr",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        ObjectAnimator animator = ObjectAnimator.ofFloat(mTextView, "y", 400f , 100f );
        animator.setDuration(1000);
        animator.start();

        mRunnable = new SplashRunnable();

        mHandler.postDelayed(mRunnable, MAX_DELAY);
    }

    private class SplashRunnable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			/*Intent i = new Intent(SplashActivity.this, NavigationalShelfListViewActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);*/
			User user = User.getUser();

            Map<String, String> userFromPhone = getList(SplashActivity.this);




/*            if(user != null && user.picture == null) {
                user.picture = userFromPhone.get(PHOTO_THUMB_URI);
                user.save();
            }

            if(user != null && user.getFullName() == null) {
                user.first_name = userFromPhone.get(DISPLAY_NAME);
                user.save();
            }*/
            //Utilities.saveUser(SplashActivity.this, user);



			if(user == null){
				// go to login
				Intent login = new Intent(SplashActivity.this,  LoginActivity.class);
				/*login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);*/
				startActivity(login);
			}else{
				// go to main
				Intent main = new Intent(SplashActivity.this,  NavigationalShelfListViewActivity.class);
				/*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);*/
				startActivity(main);
			}

            //finish();
		}
		
	}
    
}
