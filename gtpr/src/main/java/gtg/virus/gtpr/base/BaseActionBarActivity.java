package gtg.virus.gtpr.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;

/**
 * Created by DavidLuvelleJoseph on 2/15/2015.
 */
public abstract class BaseActionBarActivity extends ActionBarActivity {

    protected final Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(fullScreen()){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(resLayoutId());

        ButterKnife.inject(this);

        if(!fullScreen()) {
            ActionBar mActionBar = getSupportActionBar();
            if (mActionBar != null) {
                mActionBar.setDisplayShowTitleEnabled(displayTitle());
                mActionBar.setDisplayHomeAsUpEnabled(displayHomeUp());
                mActionBar.setHomeButtonEnabled(homeButton());
            }
        }

        provideOnCreate(savedInstanceState);
    }

    protected abstract boolean fullScreen();

    protected abstract boolean homeButton();

    protected abstract boolean displayHomeUp();

    protected abstract boolean displayTitle();

    protected abstract int resLayoutId();

    protected abstract void provideOnCreate(Bundle savedInstanceState);


}
