package gtg.virus.gtpr.utils.extensions;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DavidLuvelleJoseph on 2/10/2015.
 */
public class PacificoTextView extends TextView {
    public PacificoTextView(Context context) {
        super(context);
        init(context , null);
    }

    public PacificoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PacificoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context , attrs);
    }


    private void init(Context context , AttributeSet attrs){
        Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/Pacifico.ttf");
        setTypeface(tf,Typeface.BOLD);
    }
}
