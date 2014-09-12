package gtg.virus.gtpr;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import gtg.virus.gtpr.entities.User;
import gtg.virus.gtpr.utils.Utilities;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashActivity extends Activity {

	
	protected SplashRunnable mRunnable = null;
	
	protected final Handler mHandler = new Handler();
	
	public final static int MAX_DELAY = 3000;
	
	private TextView mTextView;
	
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
    
    
	
	private class SplashRunnable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent i = new Intent(SplashActivity.this, NavigationalShelfListViewActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
/*			User user = Utilities.getUser(SplashActivity.this);
			if(user == null){
				// go to login
				Intent i = new Intent(SplashActivity.this,  LoginActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
			}else{
				// go to main
				Intent i = new Intent(SplashActivity.this,  NavigationalShelfListViewActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
			}
*/		}
		
	}
    
}
