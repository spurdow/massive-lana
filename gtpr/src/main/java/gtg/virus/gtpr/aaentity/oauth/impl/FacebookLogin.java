package gtg.virus.gtpr.aaentity.oauth.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import java.util.Arrays;

import gtg.virus.gtpr.NavigationalShelfListViewActivity;
import gtg.virus.gtpr.R;
import gtg.virus.gtpr.aaentity.oauth.OAuthInterface;
import gtg.virus.gtpr.retrofit.Constants;
import gtg.virus.gtpr.retrofit.FBLoginService;
import gtg.virus.gtpr.retrofit.RemoteLogin;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by DavidLuvelleJoseph on 2/22/2015.
 */
public class FacebookLogin implements OAuthInterface{

    private static final String TAG = FacebookLogin.class.getSimpleName();
    private Context mContext;
   // private ProgressDialog mDialog;
    private Fragment mFragment;

    public FacebookLogin(Context mContext , Fragment fragment) {
        this.mContext = mContext;
        //this.mDialog = new ProgressDialog(mContext.getApplicationContext());
        this.mFragment = fragment;
    }


    @Override
    public void onCreate(Bundle instanceState) {
        //mDialog.setMessage(mContext.getString(R.string.please_wait));

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onActivityResult(Activity currentActivity, int requestCode, int resultCode, Intent data) {
/*        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }*/
        Session.getActiveSession().onActivityResult(currentActivity, requestCode, resultCode, data);

    }


    public void doLogin(){
        //mDialog.show();
        Session session = new Session(mContext.getApplicationContext());
        Session.setActiveSession(session);


        session.openForRead(new Session.OpenRequest(mFragment).setPermissions(Arrays.asList("email")).setCallback(new Session.StatusCallback() {

            @Override
            public void call(final Session session, SessionState sessionState, Exception e) {
                if (session.isOpened()) {

                    // make request to the /me API
                    Request.newMeRequest(session, new Request.GraphUserCallback() {


                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(final GraphUser user, Response response) {
                            if (user != null) {

                                final String email = user.getProperty("email").toString();

                                Log.w(TAG , "email is " + email);
                            }
                        }
                    }).executeAsync();
                } else if (session.isClosed()) {
                    Log.w(TAG, "Facebook is a different user");
                }
            }
        }));
    }

    private void proceedLogin(String email){
        if(email != null) {
            RestAdapter mRestAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.SERVER)
                    .build();

            FBLoginService loginService = mRestAdapter.create(FBLoginService.class);
            loginService.onFBLogin(email , new Callback<RemoteLogin>() {
                @Override
                public void success(RemoteLogin remoteLogin, retrofit.client.Response response) {
                    //mDialog.dismiss();;
                    if(remoteLogin.getStatus().equals(mContext.getString(R.string.success))){
                        remoteLogin.getEntity().getList().get(0).save();

                        Intent i = new Intent(mFragment.getActivity()  , NavigationalShelfListViewActivity.class);
                        mFragment.startActivity(i);
                    }else{
                        AlertDialog alert = new AlertDialog.Builder(mFragment.getActivity())
                                .setTitle(mContext.getString(R.string.retry))
                                .setMessage(mContext.getString(R.string.provide_correct_credentials))
                                .setPositiveButton(mContext.getString(R.string.ok) , new DialogInterface.OnClickListener() {
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

                    AlertDialog alert = new AlertDialog.Builder(mFragment.getActivity())
                            .setTitle(mContext.getString(R.string.retry))
                            .setMessage(mContext.getString(R.string.provide_correct_credentials))
                            .setPositiveButton(mContext.getString(R.string.ok) , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            }).create();

                    alert.show();
                }
            });

        }
    }
}
