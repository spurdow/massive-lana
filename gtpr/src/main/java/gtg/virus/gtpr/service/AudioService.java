package gtg.virus.gtpr.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

import gtg.virus.gtpr.entities.Audio;

public class AudioService extends Service implements MediaPlayer.OnPreparedListener , MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener{

    public final static String TAG = AudioService.class.getSimpleName();

    public final static String ACTION_PLAY_STOP_PAUSE = TAG + ".ACTION_PLAY_STOP_PAUSE";

    public final static String ACTION_DATA_SOURCE = TAG + ".DATA_SOURCE";

    public final static String PLAY = TAG + ".PLAY";

    public final static String STOP = TAG + ".STOP";

    public final static String PAUSE = TAG + ".PAUSE";

    private boolean mPlay = false;

    private boolean mStop = false;

    private boolean mPause = false;

    protected MediaPlayer mPlayer = null;


    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleOnStart(intent , startId);
        return START_NOT_STICKY;
    }

    private void handleOnStart(final Intent intent, final int startId){
        new Thread(new Runnable(){

            /**
             * Starts executing the active part of the class' code. This method is
             * called when a thread is started that has been created with a class which
             * implements {@code Runnable}.
             */
            @Override
            public void run() {

            }
        }).start();
    }

    /**
     * Called when the media file is ready for playback.
     *
     * @param mp the MediaPlayer that is ready for playback
     */
    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    /**
     * Called when the end of a media source is reached during playback.
     *
     * @param mp the MediaPlayer that reached the end of the file
     */
    @Override
    public void onCompletion(MediaPlayer mp) {

    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    private final List<Audio> store = new ArrayList<Audio>();
    protected boolean addToStore(Audio audio){
        // implement 
        //if()
        return false;
    }

    public class PlayBackReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();

            mPlay = extras.getBoolean(PLAY , false);

            mStop = extras.getBoolean(STOP , false);

            mPause = extras.getBoolean(PAUSE , false);


            if(mPlay && (!mStop && !mPause)){

                if(!mPlayer.isPlaying()){
                    mPlayer.setOnPreparedListener(null);
                    mPlayer.setOnPreparedListener(AudioService.this);
                    mPlayer.setOnErrorListener(AudioService.this);
                    //mPlayer.setDataSource();
                }
            }else if(mStop && (!mPlay && !mPause)){

            }else if(mPause && (!mPlay && !mStop)){

            }
        }
    }
}
