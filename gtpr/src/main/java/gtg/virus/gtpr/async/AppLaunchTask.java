package gtg.virus.gtpr.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import gtg.virus.gtpr.R;

import static gtg.virus.gtpr.utils.Utilities.AUDIO_STORAGE_SUFFIX;
import static gtg.virus.gtpr.utils.Utilities.isFirstLaunch;
import static gtg.virus.gtpr.utils.Utilities.removeFirstLaunch;

public class AppLaunchTask extends AsyncTask<Void,String,Boolean>{

	private Context mContext;
	
	private static ProgressDialog mDialog;
	
	public final static int MAX_TRIES = 10;

	private static final String TAG = AppLaunchTask.class.getSimpleName();
	
	private int count_tries ;
	
	private AppLaunchListener mListener = null;

    private String suffix;
	
	public interface AppLaunchListener{
		void onStartTask();
		void onFinishTask();
	}
	
	public AppLaunchTask(Context mContext , String suffix) {
		super();
		this.mContext = mContext;
        this.suffix = suffix;
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
        if(mDialog != null){
            mDialog.setMessage(values[0]);
        }
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
		File fDirectory = new File(Environment.getExternalStorageDirectory() , suffix);



		if(!fDirectory.isDirectory()){
            File parent = fDirectory.getParentFile();
            while(!fDirectory.mkdir() || !fDirectory.isDirectory()){
                try {
                    Log.w(TAG, "Creating directory...");
                    publishProgress("Creating pinbook directory...");
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if(fDirectory.exists()){
                Log.w(TAG, "Directory exists");
                publishProgress("Pinbook directory successfully created!");
            }else{
                publishProgress("Creation of Pinbook directory was unsuccessful!");
                Log.w(TAG, "Directory did not exist");
            }

        }


        File aDirectory = new File(Environment.getExternalStorageDirectory() , AUDIO_STORAGE_SUFFIX);
        if(aDirectory.isDirectory()){
            File aParent = aDirectory.getParentFile();
            while(!aDirectory.mkdir() || !aDirectory.isDirectory()){
                try{

                    publishProgress("Creating audio directory...");
                    Thread.sleep(1000L);
                    if(aDirectory.exists())
                        break;
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }

            if(aDirectory.exists()){
                publishProgress("Audio directory successfully created!");
                Log.w(TAG , "Audio Dir Exists");
            }else{
                publishProgress("Creation of audio directory encountered errors");
                Log.w(TAG , "Audio dir not exists");
            }

        }

        if(fDirectory.exists() && aDirectory.exists()){
            return true;
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
