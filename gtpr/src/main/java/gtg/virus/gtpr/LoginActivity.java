package gtg.virus.gtpr;

import java.util.Arrays;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;




import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;



public class LoginActivity extends ActionBarActivity {
	
	
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	private LoginFragment mFragment = null;



	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			mFragment = new LoginFragment();

			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, mFragment).commit();
		} else {
			// Or set the fragment from restored state info
			mFragment = (LoginFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
		
	}





	
	
	
	
	
}
