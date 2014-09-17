package gtg.virus.gtpr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormat;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import gtg.virus.gtpr.service.AudioService;

import static gtg.virus.gtpr.service.AudioService.*;


public class AudioBookMaker extends ActionBarActivity {


    private Switch mToggle;

    private TextView mTime;

    private ImageView mBackgroundLabel;

    private int audioStatus;

    private String audioMessage;

    private AudioServiceStatusReceiver serviceReceiver = null;

    private boolean hangingStart = false;

    private volatile boolean hangingStop = false;

    private Handler mHandler = new Handler();

    private long time = 0;

    private long timeStarted = 0;

    public final static long MAX_TIME = 60 * 60;

    private final DecimalFormat formatter = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);


        mToggle = (Switch) findViewById(R.id.audio_toggle_play_stop);
        mTime = (TextView) findViewById(R.id.audio_time_elapse);
        mBackgroundLabel = (ImageView) findViewById(R.id.audio_background_image_view);

        mToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    start();
                }else{
                    stop();
                }
            }
        });

        serviceReceiver = new AudioServiceStatusReceiver();
        registerReceiver(serviceReceiver , new IntentFilter(AUDIO_SERVICE_STATUS));

        Intent service = new Intent(this , AudioService.class);
        startService(service);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void start(){
        if(!hangingStart){

            hangingStart = true;
            Intent i = new Intent();
            i.setAction(ACTION_MEDIA_RECORDER_SERVICE);
            i.putExtra(FILE_NAME, "Book_Recording_" +System.currentTimeMillis());
            sendBroadcast(i);
            timeStarted = System.currentTimeMillis();


        }
    }

    private void stop(){
        if(!hangingStop){
            min = 0;
            time = 0;
            hangingStop = true;
            Intent i = new Intent();
            i.setAction(ACTION_MEDIA_RECORDER_STOP_SERVICE);
            sendBroadcast(i);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home: finish(); break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *         Intent i = new Intent();
     i.setAction(AUDIO_SERVICE_STATUS);
     i.putExtra(SERVICE_STATUS , status);
     i.putExtra(EXTRA_SERVICE_MESSAGE_STATUS , message);
     sendBroadcast(i);
     */

    private class AudioServiceStatusReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();

            audioStatus =  extras.getInt(SERVICE_STATUS);
            audioMessage =  extras.getString(EXTRA_SERVICE_MESSAGE_STATUS);

            if(audioStatus == ERROR_NOT_RUNNING){
                Crouton.makeText(AudioBookMaker.this, audioMessage , Style.ALERT).show();
                if(hangingStart){
                    // disable recording ui's
                    hangingStart = false;
                }

                if(hangingStop){
                    // disable
                    hangingStop = false;
                }

                mHandler.removeCallbacks(runner);
                mBackgroundLabel.setImageResource(R.drawable.ic_device_access_mic_muted);
            }else if(audioStatus == SUCCESSFUL_RUNNING){
                Crouton.makeText(AudioBookMaker.this  , audioMessage, Style.INFO).show();
                if(hangingStart){
                    // run recording ui's
                    mHandler.post(runner);
                    mBackgroundLabel.setImageResource(R.drawable.ic_device_access_mic);
                }
            }else if(audioStatus == SUCCESSFUL_STOP){
                Crouton.makeText(AudioBookMaker.this , audioMessage, Style.INFO).show();
                if(hangingStop){
                    // stop all recordings running ui
                    Crouton.makeText(AudioBookMaker.this , "Saved", Style.INFO).show();
                    hangingStart = false;
                    hangingStop = false;
                    mHandler.removeCallbacks(runner);
                    mTime.setText(String.format(filter , min, formatter.format(time) ));
                }

                mBackgroundLabel.setImageResource(R.drawable.ic_device_access_mic_muted);
            }
        }
    }

    private final static String filter = "%s:%s";
    private int min = 0;
    protected TestRunner runner = new TestRunner();
    private class TestRunner implements Runnable{

        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {

            time+= 1;

            if(time >= 60){
                min+=1;
                time = 0;
            }

            mTime.setText(String.format(filter , min, formatter.format(time) ));

            if((time  )>= MAX_TIME){
                stop();
            }else{
                mHandler.postDelayed(this, 1000);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(serviceReceiver != null){
            unregisterReceiver(serviceReceiver);
            serviceReceiver = null;
        }

        stopService(new Intent(this, AudioService.class));
    }
}
