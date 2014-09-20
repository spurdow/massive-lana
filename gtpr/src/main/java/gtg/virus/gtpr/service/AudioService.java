package gtg.virus.gtpr.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static gtg.virus.gtpr.utils.Utilities.*;

import gtg.virus.gtpr.AudioBookMaker;
import gtg.virus.gtpr.R;
import gtg.virus.gtpr.entities.Audio;

public class AudioService extends Service implements MediaPlayer.OnPreparedListener , MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaRecorder.OnErrorListener, MediaRecorder.OnInfoListener {

    public final static String TAG = AudioService.class.getSimpleName();

    public final static String ACTION_MEDIA_PLAYER_SERVICE = TAG + ".MEDIA_PLAYER_SERVICE";

    public final static String ACTION_MEDIA_RECORDER_SERVICE = TAG + ".ACTION_MEDIA_RECORDER_SERVICE";

    public final static String ACTION_MEDIA_PLAYER_STOP_SERVICE = TAG + ".ACTION_MEDIA_PLAYER_STOP_SERVICE";

    public final static String ACTION_MEDIA_PLAYER_PAUSE_SERVICE = TAG + ".ACTION_MEDIA_PLAYER_PAUSE_SERVICE";

    public final static String ACTION_MEDIA_RECORDER_STOP_SERVICE = TAG + ".ACTION_MEDIA_RECORDER_STOP_SERVICE";

    public final static String ACTION_PLAY_STOP_PAUSE = TAG + ".ACTION_PLAY_STOP_PAUSE";

    public final static String ACTION_DATA_SOURCE = TAG + ".DATA_SOURCE";

    public final static String PLAY = TAG + ".PLAY";

    public final static String STOP = TAG + ".STOP";

    public final static String PAUSE = TAG + ".PAUSE";

    public final static String RECORD = TAG + ".RECORD";

    public final static String FILE_NAME = TAG + ".FILE_NAME" ;

    private static final int NOTIFICATION_ID = R.drawable.ic_audio;

    private boolean mPlay = false;

    private boolean mStop = false;

    private boolean mPause = false;

    private boolean mRecord = false;

    protected MediaPlayer mPlayer = null;

    protected MediaRecorder mRecorder = null;


    // integer status
    public final static int SUCCESSFUL_RUNNING = 0x001;

    public final static int SUCCESSFUL_STOP = 0x002;

    public final static int ERROR_NOT_RUNNING = 0x003;


    public final static String SUFFIX = ".3gp";

    // key status
    public final static String SERVICE_STATUS = TAG+ ".SERVICE_KEY_STATUS";

    public final static String EXTRA_SERVICE_MESSAGE_STATUS  = TAG + ".EXTRA_SERVICE_MSG_STATUS";

    public final static String EXTRA_SERVICE_PATH_STATUS = TAG+  ".EXTRA_SERVICE_PATH_STATUS";

    public final static String AUDIO_SERVICE_STATUS = TAG +  "AUDIO_SERVICE_STATUS";


    private String mRecorderAbsPath = "";

    private String mPlayerAbsPath = "";

    private String mPrevFileName = "";

    /**
     * BroadcastReceivers Instance
     */
    protected RecordPlaybackReceiver mRecorderReciever = null;

    protected RecordStopReceiver mStopRecorderReciever = null;

    protected PlayBackReceiver mPlayReceiver = null;

    protected StopPlaybackReceiver mStopReceiver = null;

    protected PausePlaybackReceiver mPauseReceiver = null;

    public final static String ABSOLUTE_PATH = Environment.getExternalStorageDirectory() + "/" + AUDIO_STORAGE_SUFFIX;

    private Notification mNotification;

    private void initializeMediaPlayer(){
        mPlayer = new MediaPlayer();

        mPlayer.setWakeMode(getApplicationContext() , PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setOnErrorListener(this);
    }

    private void initializeMediaRecorder(){
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);


        mRecorder.setOutputFile(ABSOLUTE_PATH);
        mRecorder.setOnInfoListener(this);

        File dir = new File(ABSOLUTE_PATH);
        if(!dir.exists())
            dir.mkdirs();

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
                synchronized (AudioService.this){

                    // init recorder
                    mRecorderReciever = new RecordPlaybackReceiver();
                    registerReceiver(mRecorderReciever  , new IntentFilter(ACTION_MEDIA_RECORDER_SERVICE));

                    // init recorder stopper
                    mStopRecorderReciever = new RecordStopReceiver();
                    registerReceiver(mStopRecorderReciever , new IntentFilter(ACTION_MEDIA_RECORDER_STOP_SERVICE));


                    mPlayReceiver = new PlayBackReceiver();
                    registerReceiver(mPlayReceiver , new IntentFilter(ACTION_MEDIA_PLAYER_SERVICE));

                    mStopReceiver = new StopPlaybackReceiver();
                    registerReceiver(mStopReceiver , new IntentFilter(ACTION_MEDIA_PLAYER_STOP_SERVICE));

                    mPauseReceiver = new PausePlaybackReceiver();
                    registerReceiver(mPauseReceiver , new IntentFilter(ACTION_MEDIA_PLAYER_PAUSE_SERVICE));
                }
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
        mp.start();
        setUpAsForeground("Playing..." , R.drawable.ic_audio_play);

    }

    void setUpAsForeground(String text , int resId) {
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), AudioBookMaker.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification = new Notification();
        mNotification.tickerText = text;
        mNotification.icon = resId;
        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
        mNotification.setLatestEventInfo(getApplicationContext(), "AudioBook Player",
                text, pi);
        startForeground(NOTIFICATION_ID, mNotification);
    }

    /**
     * Called when the end of a media source is reached during playback.
     *
     * @param mp the MediaPlayer that reached the end of the file
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        setUpAsForeground("AudioBook has finished." , R.drawable.ic_audio_stop);
        store.clear();
        mpStack.pop();

        if(!mpStack.isEmpty() && mPlayer != null){
            Log.w(TAG , "We have remaining 3gp to play");
            String mFileName = mpStack.pop().getKey();
            mPlayerAbsPath = ABSOLUTE_PATH +"/"+mFileName;
            try {
                mPlayer.stop();
                mPlayer.reset();
                mPlayer.setDataSource(mPlayerAbsPath);
                mPlayer.setOnPreparedListener(AudioService.this);
                mPlayer.setOnCompletionListener(AudioService.this);
                mPlayer.setScreenOnWhilePlaying(true);
                mPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
                // alarm user error occured
                broadcastServiceStatus(ERROR_NOT_RUNNING, "MediaPlayer not running...");

            }

        }else if(mPlayer == null){
            mpStack.clear();
        }
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        if(mp.isPlaying())
            mp.stop();

        broadcastServiceStatus(ERROR_NOT_RUNNING , "MediaPlayer has stopped...");

        store.clear();

        mpStack.clear();
        return true;
    }

    private final Map<String , String> store = new HashMap<String ,String>();

    /**
     *
     * @return true if not exists in store, else false
     */
    protected boolean addToStore(String fileName , String absPath){
        // TODO: check if audio is already in the cache store
        if(store.containsKey(fileName) || store.containsValue(absPath)){
            return false;
        }else{
            store.put(fileName , absPath);
            return true;
        }

    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        Log.i(TAG , "MediaRecorder " + mr.toString());
    }


    protected Stack<Map.Entry<String,String>> mpStack = new Stack<Map.Entry<String,String>>();
    public class PlayBackReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {

                Bundle extras = intent.getExtras();

                final String mFileName = extras.getString(FILE_NAME);

                mPlayerAbsPath = ABSOLUTE_PATH + "/" + mFileName;
                Log.w(TAG , mPlayerAbsPath);
                if(addToStore(mFileName , mPlayerAbsPath)) {
                    if (mPlayer == null) {

                        initializeMediaPlayer();
                        try {
                            mPlayer.setDataSource(mPlayerAbsPath);
                            mPlayer.setOnPreparedListener(AudioService.this);
                            mPlayer.setOnCompletionListener(AudioService.this);
                            mPlayer.setScreenOnWhilePlaying(true);
                            mPlayer.prepareAsync();
                            KVEntry e = new KVEntry(mFileName , mPlayerAbsPath);
                            mpStack.push(e);
                        } catch (IOException e) {
                            e.printStackTrace();


                            // alarm user error occured
                            broadcastServiceStatus(ERROR_NOT_RUNNING, "MediaPlayer not running...");

                        }

                    } else {



                        mPlayer.reset();
                        try {
                            mPlayer.setDataSource(mPlayerAbsPath);
                            mPlayer.setOnPreparedListener(AudioService.this);
                            mPlayer.setOnCompletionListener(AudioService.this);
                            mPlayer.setScreenOnWhilePlaying(true);
                            mPlayer.prepareAsync();
                            KVEntry e = new KVEntry(mFileName , mPlayerAbsPath);
                            mpStack.push(e);
                        } catch (IOException e) {
                            e.printStackTrace();

                            // alarm user error occured
                            broadcastServiceStatus(ERROR_NOT_RUNNING, "MediaPlayer not running...");

                        }
                    }
                // else filename or path already existed
                // meaning it is playing which is stored in store
                }else{
                    if(mPlayer == null){
                        initializeMediaPlayer();
                    }
                }


        }
    }


    public class StopPlaybackReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extra = intent.getExtras();
            String fileName = extra.getString(FILE_NAME);
            mPlayerAbsPath = ABSOLUTE_PATH + "/" + fileName;

            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();

                }

            }
            if(!mpStack.isEmpty() && mPlayer != null){
                Log.w(TAG , "We have remaining 3gp to play");
                String mFileName = mpStack.pop().getKey();
                mPlayerAbsPath = ABSOLUTE_PATH +"/"+mFileName;
                try {
                    mPlayer.stop();
                    mPlayer.reset();
                    mPlayer.setDataSource(mPlayerAbsPath);
                    mPlayer.setOnPreparedListener(AudioService.this);
                    mPlayer.setOnCompletionListener(AudioService.this);
                    mPlayer.setScreenOnWhilePlaying(true);
                    mPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                    // alarm user error occured
                    broadcastServiceStatus(ERROR_NOT_RUNNING, "MediaPlayer not running...");

                }

            }else if(mPlayer == null){
                mpStack.clear();
            }
            store.clear();


            setUpAsForeground("Player stopped..." , R.drawable.ic_audio_stop);

        }
    }

    public class PausePlaybackReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
                if(mPlayer!=null){
                    if(mPlayer.isPlaying()) {
                        mPlayer.pause();
                    }
                }

                setUpAsForeground("Player Paused..." , R.drawable.ic_audio_pause);

        }
    }

    /**
     * Fancy over here, first is getting us
     * the boolean
     *
     * 1. if mRecorder has not been init
     * then init it here and if mRecord is false
     * well prepare the recorder and start
     * if something happens bad, well message the UI
     * that recording failed , so users will try again.
     * hit mRecord to true.
     *
     * 2. if mRecord is false then
     * most likely well reset and listen for audio input again.
     * hit mRecord to true.
     */
    public class RecordPlaybackReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK , ACTION_MEDIA_RECORDER_SERVICE);
            wl.acquire();

            try{
                Bundle extras = intent.getExtras();
                final String fileName = extras.getString(FILE_NAME) + SUFFIX;
                mRecorderAbsPath = ABSOLUTE_PATH + "/" + fileName;
                if(mRecorder == null){
                    initializeMediaRecorder();

                    mRecorder.setOutputFile(mRecorderAbsPath);

                    if(!mRecord){

                        try {
                            mRecorder.prepare();

                        } catch (IOException e) {
                            e.printStackTrace();

                            // message the app that media recording
                            // has failed ERROR MESSAGE
                            broadcastServiceStatus(ERROR_NOT_RUNNING , "MediaRecorder Error!");
                            wl.release();
                            return;
                        }

                        mRecorder.start();
                        // media recording has started
                        // message the app UI to work with recording
                        broadcastServiceStatus(SUCCESSFUL_RUNNING , "MediaRecorder has started!");
                        mRecord = true;
                    }
                }

                if(!mRecord){
                    mRecorder.reset();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
                    mRecorder.setOutputFile(mRecorderAbsPath);
                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();

                        // message the app that media recording
                        // has failed ERROR MESSAGE
                        broadcastServiceStatus(ERROR_NOT_RUNNING , "MediaRecorder Error!");
                        wl.release();
                        return;
                    }

                    mRecorder.start();
                    // media recording has started
                    // message the app UI to work with recording
                    broadcastServiceStatus(SUCCESSFUL_RUNNING , "MediaRecorder has started!");

                    mRecord = true;
                }


            }finally{
                wl.release();
            }
        }
    }

    /**
     * Nothing fancy here only stopping a running recorder
     */
    public class RecordStopReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            PowerManager pm  = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK , ACTION_MEDIA_RECORDER_STOP_SERVICE);
            wl.acquire();
            try{
                if(mRecord){
                    mRecorder.stop();
                    mRecord = false;
                    // message the hungry UI
                    // that recording has stopped
                    broadcastServiceStatus(SUCCESSFUL_STOP , "MediaRecorder stopped successfully!");
                }
            }finally{
                wl.release();
            }
        }
    }

    /**
     * Send status and message to the hungry UI and USER ;)
     * @param status
     * @param message
     */
    public void broadcastServiceStatus(int status, String message){
        Intent i = new Intent();
        i.setAction(AUDIO_SERVICE_STATUS);
        i.putExtra(SERVICE_STATUS , status);
        i.putExtra(EXTRA_SERVICE_MESSAGE_STATUS , message);
        sendBroadcast(i);

    }


    public void broadcastExtraStatus(String message){

    }


    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        Log.e(TAG , "MediaRecorder Error " + mr.toString());
        broadcastServiceStatus(ERROR_NOT_RUNNING , "MediaRecorder Error");
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////


    private class KVEntry implements Map.Entry<String, String> {

        private String key;
        private String val;
        public KVEntry(String k, String v){
            this.key = k;
            this.val = v;
        }
        /**
         * Returns the key.
         *
         * @return the key
         */
        @Override
        public String getKey() {
            return key;
        }

        /**
         * Returns the value.
         *
         * @return the value
         */
        @Override
        public String getValue() {
            return val;
        }

        public void setKey(String key){
            this.key = key;
        }

        public void OverridesetValue(String val){
            this.val = val;
        }

        /**
         * Sets the value of this entry to the specified value, replacing any
         * existing value.
         *
         * @param object the new value to set.
         * @return object the replaced value of this entry.
         */
        @Override
        public String setValue(String object) {
            val = object;
            return object;
        }
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mPlayer != null){
            mPlayer.release();
            mPlayer = null;
        }

        if(mRecorder != null){
            mRecorder.release();
            mRecorder = null;
        }

        // avoid memory leaks
        // be a good citizen
        // follow clean up rules
        if(mRecorderReciever != null){
            unregisterReceiver(mRecorderReciever);
            mRecorderReciever = null;
        }

        // avoid memory leaks
        // be a good citizen
        // follow clean up rules
        if(mStopRecorderReciever != null){
            unregisterReceiver(mStopRecorderReciever);
            mStopRecorderReciever = null;
        }

        if(mPlayReceiver != null){
            unregisterReceiver(mPlayReceiver);
            mPlayReceiver = null;
        }

        if(mStopReceiver != null){
            unregisterReceiver(mStopReceiver);
            mStopReceiver = null;
        }

        if(mPauseReceiver != null){
            unregisterReceiver(mPauseReceiver);
            mPauseReceiver = null;
        }
    }
}
