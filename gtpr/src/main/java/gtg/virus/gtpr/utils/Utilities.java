package gtg.virus.gtpr.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.radaee.pdf.Document;
import com.radaee.pdf.Matrix;
import com.radaee.pdf.Page;

import gtg.virus.gtpr.entities.PBook;
import gtg.virus.gtpr.entities.User;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;

public final class Utilities {
	
	// the global tag to access shared file folders
	public final static String GLOBAL_TAG = Utilities.class.getPackage().getName();
	private static final String TAG = GLOBAL_TAG;
	
	public final static String STORAGE_SUFFIX = "Pinbook";

    public final static String AUDIO_STORAGE_SUFFIX = "Pinbook_Media";
	
	public final static String pdfPattern = "[a-zA-Z0-9,.-_]*.(pdf|epub|txt)";
	
	public final static String pdfSlashPattern = "[^!.]*.(pdf|epub|txt)";

    public final static String audioPattern = "[^!.]*.(mp3|ogg|3gp)";
	
	public final static String PIN_EXTRA_PBOOK = "_pbook_extra";
	
	public final static Map<String , PBook> bookCache = new ConcurrentHashMap<String , PBook>();
	
	
	/**
	 * 
	 * @param context
	 * @return SharedPreferences with GLOBAL_TAG used as name
	 */
	public static SharedPreferences getSharedPref(Context context){
		SharedPreferences shared = context.getSharedPreferences(GLOBAL_TAG, Context.MODE_PRIVATE);
		return shared;
	}
	
	/**
	 * 
	 * @param context
	 * @return User class 
	 */
	public static User getUser(Context context){
		final String USER = "_user_tag";
		User user = null;
		SharedPreferences shared = getSharedPref(context);
		
		String str_user = shared.getString(USER, null);
		user = new Gson().fromJson(str_user, User.class);
		
		if(user == null){
			Log.i(TAG, "User is null");
		}else{
			Log.i(TAG, "User is not null");
		}
		
		return user;
	}
	/**
	 * Saves user
	 * @param context
	 * @param user
	 */
	public static void saveUser(Context context , User user){
		final String USER = "_user_tag";
		final SharedPreferences shared = getSharedPref(context);
		final SharedPreferences.Editor editor = shared.edit();
		Log.i(TAG, "Saving user");
		String str_user = user.toString();
		boolean c = editor.putString(USER, str_user).commit();
		Log.i(TAG, "User saved... " + c);
	}
	
	public static boolean isValideBook(String name){
		return Pattern.matches(pdfSlashPattern, name);
	}
	
	public static void walkdir(File dir , HashMap<String , String> data , String pattern) {

		    File listFile[] = dir.listFiles();

		    if (listFile != null) {
		        for (int i = 0; i < listFile.length; i++) {

		            if (listFile[i].isDirectory()) {
		                walkdir(listFile[i] , data , pattern);
		            } else {
		              if (Pattern.matches(pattern, listFile[i].getAbsolutePath())){
		                  // add to hashmap
		            	  data.put(listFile[i].getName(), listFile[i].getAbsolutePath());
		            	  Log.i(TAG, "Not Test " + listFile[i].getName()  +" " + listFile[i].getAbsolutePath());
		              }
		              //Log.i(TAG,Pattern.matches(pdfPattern, listFile[i].getName()) + " Name = " + listFile[i].getName());
		            }
		        }
		    }    
		}
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	/**
	 * 
	 * @param doc
	 * @return created bitmap
	 */
	public static Bitmap renderPage(Document doc , int iw, int ih){
		Bitmap bitmap = null;
		if(doc.is_opened()){
			Page page = doc.GetPage(0);
			float w = doc.GetPageWidth(0); 
			float h = doc.GetPageHeight(0);
			try{
				bitmap = Bitmap.createBitmap(iw	, ih, Bitmap.Config.ARGB_8888);
				bitmap.eraseColor(0);
				float ratiox = iw/h;
				float ratioy = ih/w;
				if( ratiox > ratioy ) ratiox = ratioy;
				if( !page.RenderThumb(bitmap) )
				{
					Canvas canvas = new Canvas(bitmap);
					Paint paint = new Paint();
					paint.setARGB(255, 255, 255, 255);
					canvas.drawRect((iw - w * ratiox)/2, (ih - h * ratiox)/2,
							(iw + w * ratiox)/2, (ih + h * ratiox)/2, paint);
					Matrix mat = new Matrix( ratiox, -ratiox, (iw - w * ratiox)/2, (ih + h * ratiox)/2 );
					page.RenderToBmp(bitmap, mat);
					mat.Destroy();
	
				}
				
			}catch(Exception ex){
				
			}
		}
		return bitmap;
	}
	
	public final static String first_launch_key = "_fl";
	/**
	 * 
	 * @param context
	 * @return boolean if firstlaunch or not
	 */
	public static boolean isFirstLaunch(Context context){
		SharedPreferences shared = getSharedPref(context);
		if(shared.contains(first_launch_key)){
			return shared.getBoolean(first_launch_key, true);
		}else{
			
			return true;
		}
	}
	/**
	 * remove first launch
	 * @param context
	 */
	public static void removeFirstLaunch(Context context){
		SharedPreferences shared = getSharedPref(context);
		shared.edit().putBoolean(first_launch_key, false).commit();
	}
	
	public static boolean isEpub(String path){
		final String regularExpression = "[^!]*.epub";
		return Pattern.matches(regularExpression, path);
	}
	
	public static boolean isPdf(String path){
		final String regularExpression = "[^!]*.pdf";
		return Pattern.matches(regularExpression, path);
	}

    public static boolean isTxt(String path){
        final String regularExpression = "[^!]*.txt";
        return Pattern.matches(regularExpression , path);
    }

	public static void copy(File src, File dst) throws IOException {
/*	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();*/
		FileInputStream inStream = new FileInputStream(src);
		FileOutputStream outStream = new FileOutputStream(dst);
		FileChannel inChannel = inStream.getChannel();
		FileChannel outChannel = outStream.getChannel();
		inChannel.transferTo(0, inChannel.size(), outChannel);
		inStream.close();
		outStream.close();
	}
}
