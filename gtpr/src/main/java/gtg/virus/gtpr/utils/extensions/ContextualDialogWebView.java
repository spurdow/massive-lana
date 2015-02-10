package gtg.virus.gtpr.utils.extensions;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.webkit.WebView;

/**
 * Created by DavidLuvelleJoseph on 2/5/2015.
 */
public class ContextualDialogWebView extends WebView {
    private static final String TAG = ContextualDialogWebView.class.getSimpleName();

    public ContextualDialogWebView(Context context) {
        super(context);
        init(context , null);
    }

    public ContextualDialogWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context , null);
    }

    public ContextualDialogWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context , null);
    }

    public ContextualDialogWebView(Context context, AttributeSet attrs, int defStyle, boolean privateBrowsing) {
        super(context, attrs, defStyle, privateBrowsing);
        init(context , null);
    }

    void init(Context context, AttributeSet attrs){

    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {

        Log.w(TAG , "ActionMode activated!");

        return super.startActionMode(callback);
    }
}
