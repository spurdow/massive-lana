package gtg.virus.gtpr.application;


import android.app.Application;

import com.activeandroid.ActiveAndroid;

public class PinBookApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
