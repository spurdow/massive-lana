package gtg.virus.gtpr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

import butterknife.InjectView;
import gtg.virus.gtpr.entities.User;
import gtg.virus.gtpr.parent.ParentFragment;

public class LoginFragment extends ParentFragment {

	private static final String TAG = LoginFragment.class.getSimpleName();

	private UiLifecycleHelper uiHelper;

	private final List<String> permissions;
	
	private Session mSession = null;

    @InjectView(R.id.authButton)
    protected LoginButton authButton;

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
    public int resId() {
        return R.layout.fragment_main;
    }

    @Override
    public boolean useButterKnife() {
        return true;
    }

    @Override
    public void overrideSetUpView(View createdView , LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        authButton.setFragment(this);
        authButton.setReadPermissions(permissions);
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

						
						final User mmuser = User.findById(User.class , (long) 1);


						Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
							
							@Override
							public void onCompleted(GraphUser user, Response response) {
								// TODO Auto-generated method stub
								if(mSession == Session.getActiveSession()){
									if(user != null){
										User muser = new User();


										muser.setFacebook_id(user.getId());
										muser.setFullname(user.getName());
										muser.setStatus(1);
                                        muser.save();
                                        Intent i = new Intent(LoginFragment.this.getActivity(),  NavigationalShelfListViewActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
									}
									
								}
								if(response.getError() != null){
									//Toast.makeText(MainFragment.this.getActivity(), "Error : " + response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
									Log.w(TAG, "Error : " + response.getError());
                                    Toast.makeText(LoginFragment.this.getActivity() , "Something went wrong! try again." , Toast.LENGTH_SHORT).show();
                                }
							}
						});
				    	request.executeAsync();
						
						

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
