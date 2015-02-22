package gtg.virus.gtpr.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ScheduledService extends Service{



    private ScheduledServiceBinder mBinder = new ScheduledServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    /**
     * Binder class
     */
    public class ScheduledServiceBinder extends Binder {
        ScheduledService getService(){
            return ScheduledService.this;
        }
    }
}
