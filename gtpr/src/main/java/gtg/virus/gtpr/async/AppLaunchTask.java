package gtg.virus.gtpr.async;

import java.io.File;
import java.io.IOException;

import gtg.virus.gtpr.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import static gtg.virus.gtpr.utils.Utilities.*;

public class AppLaunchTask extends AsyncTask<Void,String,Boolean>{

	private Context mContext;
	
	private static ProgressDialog mDialog;
	
	public final static int MAX_TRIES = 10;

	private static final String TAG = AppLaunchTask.class.getSimpleName();
	
	private int count_tries ;
	
	private AppLaunchListener mListener = null;
	
	public interface AppLaunchListener{
		void onStartTask();
		void onFinishTask();
	}
	
	public AppLaunchTask(Context mContext) {
		super();
		this.mContext = mContext;
	}


	
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		String dialog = "";
		if(isFirstLaunch(mContext)){
			dialog = mContext.getResources().getString(R.string.first_launch_dialog);
		}else{
			dialog = mContext.getResources().getString(R.string.launch_dialog);
		}
		
		mDialog = new ProgressDialog(mContext);
		mDialog.setMessage(dialog);
		mDialog.setIndeterminate(true);
		mDialog.show();
		
		if(mListener != null){
			mListener.onStartTask();
		}
		
		Log.w(TAG, "Starting loading...");
	}


	


	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}




	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		removeFirstLaunch(mContext);
        if(mDialog != null) {
            mDialog.dismiss();
        }
		if(mListener != null){
			mListener.onFinishTask();
		}
		
		Log.w(TAG, "Done loading Applaunch");
	}




	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		File fDirectory = new File(Environment.getExternalStorageDirectory() , STORAGE_SUFFIX);
		
		if(fDirectory.isDirectory()){
			return true;
		}
		File parent = fDirectory.getParentFile();
		while(!fDirectory.mkdir() || !fDirectory.isDirectory()){
			try {
				Log.w(TAG, "Creating directory...");
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	
		if(fDirectory.exists()){
			Log.w(TAG, "Directory exists");
			return true;
		}else{
			Log.w(TAG, "Directory did not exist");
		}
		
		return false;
	}

	public AppLaunchListener getListener() {
		return mListener;
	}

	public void setListener(AppLaunchListener mListener) {
		this.mListener = mListener;
	}
	
	

}
