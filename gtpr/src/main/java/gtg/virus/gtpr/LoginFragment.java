package gtg.virus.gtpr;

import gtg.virus.gtpr.entities.User;
import gtg.virus.gtpr.utils.Utilities;

import java.util.Arrays;
import java.util.List;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;





import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class LoginFragment extends Fragment{

	private static final String TAG = LoginFragment.class.getSimpleName();

	private UiLifecycleHelper uiHelper;

	private final List<String> permissions;
	
	private Session mSession = null;

	public LoginFragment() {
		permissions = Arrays.asList("user_status");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
		Log.i(TAG, "LoginFragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);

		LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
		authButton.setFragment(this);
		authButton.setReadPermissions(permissions);
		

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		// For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }

		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "Request Code = " + requestCode + " || Result Code = " + resultCode   );
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if(exception != null){
			Log.i(TAG, "fb-exception : " +exception.toString());
		}

		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");
				if(mSession == null || isSessionChanged(session)){
					mSession = session;

						
						final User mmuser = Utilities.getUser(LoginFragment.this.getActivity());
						if(mmuser != null)
							Utilities.saveUser(LoginFragment.this.getActivity(), mmuser);

						Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
							
							@Override
							public void onCompleted(GraphUser user, Response response) {
								// TODO Auto-generated method stub
								if(mSession == Session.getActiveSession()){
									if(user != null){
										User muser = null;
										if(mmuser == null){
											muser = new User();
										}else{
											muser = mmuser;
										}
										muser.setFacebook_id(user.getId());
										muser.setFullname(user.getName());
										muser.setPhoto("http://graph.facebook.com/"+user.getId()+"/picture?type=large");
										muser.setStatus(1);

										if(LoginFragment.this.getActivity() != null && muser != null)
											Utilities.saveUser(LoginFragment.this.getActivity() , muser);

									}
									
								}
								if(response.getError() != null){
									//Toast.makeText(MainFragment.this.getActivity(), "Error : " + response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
									Log.w(TAG, "Error : " + response.getError());
								}
							}
						});
				    	request.executeAsync();
						
						
					Intent i = new Intent(LoginFragment.this.getActivity(),  NavigationalShelfListViewActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(i);
				}
			
			

		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}
	
	
	
	private boolean isSessionChanged(Session session) {

	    // Check if session state changed
	    if (mSession.getState() != session.getState())
	        return true;

	    // Check if accessToken changed
	    if (mSession.getAccessToken() != null) {
	        if (!mSession.getAccessToken().equals(session.getAccessToken()))
	            return true;
	    }
	    else if (session.getAccessToken() != null) {
	        return true;
	    }

	    // Nothing changed
	    return false;
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
				
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);

		}
		
	};

}
