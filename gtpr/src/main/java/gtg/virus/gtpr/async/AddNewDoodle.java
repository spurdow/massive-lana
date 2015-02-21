package gtg.virus.gtpr.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.activeandroid.ActiveAndroid;

import java.util.List;

import gtg.virus.gtpr.R;
import gtg.virus.gtpr.aaentity.AABook;
import gtg.virus.gtpr.aaentity.AADoodle;

/**
 * Created by DavidLuvelleJoseph on 2/22/2015.
 */
public class AddNewDoodle extends AsyncTask<Void , Void , Boolean> {

    private List<Float> x;
    private List<Float> y;
    private int size;
    private int book_page;
    private AABook book;
    private Context mContext;
    private int color;
    private ProgressDialog mDialog = null;

    public AddNewDoodle(Context mContext , List<Float> x, List<Float> y,  AABook book , int book_page , int color) {
        this.x = x;
        this.y = y;
        this.size = Math.min(x.size() , y.size());
        this.book = book;
        this.book_page = book_page;
        this.color = color;
        this.mContext = mContext;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(mDialog != null){
            mDialog.dismiss();
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage(mContext.getString(R.string.please_wait));
        mDialog.setIndeterminate(true);
        mDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        if(size <= 0){
            return false;
        }

        ActiveAndroid.beginTransaction();
        try {
            for(int i = 0 ; i < size ; i++){
                AADoodle doodle = new AADoodle();
                doodle.book = book;
                doodle.color = color;
                doodle.book_page = book_page;
                doodle.sx = x.get(i);
                doodle.sy = y.get(i);
                doodle.save();


            }
            ActiveAndroid.setTransactionSuccessful();
            return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }finally {
            ActiveAndroid.endTransaction();
        }

    }
}
