package gtg.virus.gtpr;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.InjectView;
import butterknife.OnClick;
import gtg.virus.gtpr.aaentity.oauth.impl.FacebookLogin;
import gtg.virus.gtpr.parent.ParentFragment;
import gtg.virus.gtpr.retrofit.Constants;
import gtg.virus.gtpr.retrofit.RemoteLogin;
import gtg.virus.gtpr.retrofit.UserLoginService;
import info.hoang8f.widget.FButton;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginFragment extends ParentFragment {

	private static final String TAG = LoginFragment.class.getSimpleName();


    @InjectView(R.id.fb_button)
    protected Button authButton;

    @InjectView(R.id.button)
    protected FButton loginButton;

    @InjectView(R.id.username)
    protected TextView mUsername;

    @InjectView(R.id.password)
    protected TextView mPassword;

    private FacebookLogin fbLogin ;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        fbLogin = new FacebookLogin(getActivity() , this);
        fbLogin.onCreate(savedInstanceState);
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

    }

    @Override
	public void onResume() {
		super.onResume();
        fbLogin.onResume();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        fbLogin.onActivityResult(getActivity() , requestCode , resultCode , data);
	}

	@Override
	public void onPause() {
		super.onPause();
        fbLogin.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}


    @OnClick(R.id.fb_button)
    void onAuthButton(){
        fbLogin = new FacebookLogin(getActivity() , this);

        fbLogin.doLogin();
    }

    @OnClick(R.id.button)
    void onLogin(){
        final String username = mUsername.getText().toString();
        final String password = mPassword.getText().toString();

        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){

            final ProgressDialog dialog  = new ProgressDialog(getActivity());
            dialog.setMessage(getString(R.string.please_wait));
            dialog.show();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.SERVER)
                    .build();

            UserLoginService userLoginService = restAdapter.create(UserLoginService.class);
            userLoginService.login(username, password , new Callback<RemoteLogin>() {
                @Override
                public void success(RemoteLogin remoteLogin, Response response) {
                    //Toast.makeText(getActivity() , "success" , Toast.LENGTH_SHORT).show();
                    //Log.w(TAG, remoteLogin.getStatus() + " " + remoteLogin.getMessage() + " " + remoteLogin.getEntity().getListOfUsers().size());
                    dialog.dismiss();

                    if(remoteLogin.getStatus().equals(getString(R.string.success))){
                        remoteLogin.getEntity().getList().get(0).save();

                        Intent i = new Intent(getActivity()  , NavigationalShelfListViewActivity.class);
                        startActivity(i);
                    }else{
                        AlertDialog alert = new AlertDialog.Builder(getActivity())
                                .setTitle(getString(R.string.retry))
                                .setMessage(getString(R.string.provide_correct_credentials))
                                .setPositiveButton(getString(R.string.ok) , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }
                                }).create();

                        alert.show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity() , "fail" , Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }else{
            //SweetAlert
            AlertDialog alert = new AlertDialog.Builder(getActivity())
                                .setTitle(getString(R.string.retry))
                                .setMessage(getString(R.string.provide_correct_credentials))
                                .setPositiveButton(getString(R.string.ok) , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }
                                }).create();

            alert.show();
        }
    }
	


}
