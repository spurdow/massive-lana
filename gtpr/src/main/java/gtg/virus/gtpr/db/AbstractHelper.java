package gtg.virus.gtpr.db;


import android.content.Context;

public abstract class AbstractHelper {

    protected Context mContext;

    protected DatabaseHelper mInstance;

    public AbstractHelper(Context mContext) {
        this.mContext = mContext;
        this.mInstance = DatabaseHelper.getInstance(mContext);
    }
}
