package gtg.virus.gtpr;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Stack;

import gtg.virus.gtpr.entities.Audio;

import static gtg.virus.gtpr.service.AudioService.ABSOLUTE_PATH_SHELF;
import static gtg.virus.gtpr.service.AudioService.ABS_FILE_NAME;
import static gtg.virus.gtpr.service.AudioService.ACTION_MEDIA_PLAYER_SERVICE;
import static gtg.virus.gtpr.service.AudioService.ACTION_MEDIA_PLAYER_STOP_SERVICE;
import static gtg.virus.gtpr.service.AudioService.AUDIO_SERVICE_STATUS;
import static gtg.virus.gtpr.service.AudioService.ERROR_NOT_RUNNING;
import static gtg.virus.gtpr.service.AudioService.EXTRA_SERVICE_MESSAGE_STATUS;
import static gtg.virus.gtpr.service.AudioService.FILE_NAME;
import static gtg.virus.gtpr.service.AudioService.MAX_PLAYER_DURATION;
import static gtg.virus.gtpr.service.AudioService.PLAYER_DURATION;
import static gtg.virus.gtpr.service.AudioService.SEEK_SUCCESS;
import static gtg.virus.gtpr.service.AudioService.SERVICE_STATUS;
import static gtg.virus.gtpr.service.AudioService.SUCCESSFUL_PAUSE;
import static gtg.virus.gtpr.service.AudioService.SUCCESSFUL_RUNNING;
import static gtg.virus.gtpr.service.AudioService.SUCCESSFUL_STOP;
import static gtg.virus.gtpr.service.AudioService.TAG;

public class GTGAudioListener extends ActionBarActivity {

    public final static String PIN_EXTRA_PBOOK = "_pbook_extra";

    private SeekBar mSeekBar = null;

    private ImageButton mPlayStop = null;

    private ImageButton mPrev = null;

    private ImageButton mNext = null;

    private TextView mCurrent = null;

    private TextView mDuration = null;

    private TextView mTitle = null;

    private DecimalFormat format  = new DecimalFormat("00");

    private static final String configuration = "%s:%s:%s";

    private final Stack<Integer> pendingSeek = new Stack<Integer>();

    private volatile boolean isPlaying = false;

    private PlayerRunnable mPlayerRunnable = new PlayerRunnable();

    private int maxDuration = 0;

    private int currentDuration = 0;

    private volatile boolean pause = false;

    private int mCurrentHour = 0;

    private int mCurrentMin = 0;

    private int mCurrentSec = 0;

    private int mDurationHour = 0;

    private int mDurationMin = 0;

    private int mDurationSec = 0;

    private List<Audio> players = null;

    private int currPosition = 0;

    private int maxPosition = 0;

    private Handler mHandler = new Handler();

    protected StatusReceiver mReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        Bundle extras = getIntent().getExtras();

        players = new Gson().fromJson(extras.getString(PIN_EXTRA_PBOOK), new TypeToken<List<Audio>>() {
        }.getType());

        int ctr = 0;
        for(Audio a : players){
            if(a.getIsPlay()){
                currPosition = ctr;
            }
            ctr++;
        }

        mTitle = (TextView) findViewById(R.id.txt_title);

        maxPosition = players.size();

        mSeekBar = (SeekBar) findViewById(R.id.audio_player_seekbar);

        mPlayStop = (ImageButton) findViewById(R.id.audio_button_play_stop);

        mNext = (ImageButton) findViewById(R.id.audio_button_next);

        mPrev = (ImageButton) findViewById(R.id.audio_button_prev);

        mCurrent = (TextView) findViewById(R.id.txt_current_time);

        mDuration = (TextView) findViewById(R.id.txt_duration_time);

        reset();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.w(TAG, "fromUser " + fromUser);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mPlayStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayStopClick(v);
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prev();
            }
        });

        if(mReceiver == null){
            mReceiver = new StatusReceiver();
            registerReceiver(mReceiver, new IntentFilter(AUDIO_SERVICE_STATUS));
        }

        setUp();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUp(){
        mTitle.setText(players.get(currPosition).getTitle());
    }

    private void next(){
        currPosition = ( currPosition + 1 ) % players.size() ;
/*        reset();
        onPlayStopClick(mPlayStop);*/
        onPlayStopClick(mPlayStop);
        setUp();
        Log.w(TAG , "Currposition " + currPosition);
    }

    private void prev(){
        currPosition = ( currPosition + players.size() - 1) % players.size();
/*        reset();
        onPlayStopClick(mPlayStop);*/
        onPlayStopClick(mPlayStop);
        setUp();
        Log.w(TAG , "Currposition " + currPosition);
    }

    private void reset(){
        mDurationHour = mDurationMin = mDurationSec = mCurrentHour = mCurrentMin = mCurrentSec = currentDuration = maxDuration= 0;

        mSeekBar.setProgress(0);
        mSeekBar.setMax(0);

        isPlaying = false;

        mCurrent.setText(String.format(configuration , format.format(mCurrentHour) , format.format(mCurrentMin) , format.format(mCurrentSec)));

        mDuration.setText(String.format(configuration , format.format(mCurrentHour) , format.format(mCurrentMin) , format.format(mCurrentSec)));

    }

    private void onPlayStopClick(View v){

        if(isPlaying){
            ((ImageButton)v).setImageResource(R.drawable.ic_audio_play);
            mSeekBar.removeCallbacks(mPlayerRunnable);
            mSeekBar.setProgress(0);

            Intent i = new Intent();
            i.setAction(ACTION_MEDIA_PLAYER_STOP_SERVICE);
            i.putExtra(FILE_NAME, players.get(currPosition).getTitle());
            i.putExtra(ABS_FILE_NAME , ABSOLUTE_PATH_SHELF);
            Log.w(TAG , players.get(currPosition).getTitle());
            mHandler.removeCallbacks(mPlayerRunnable);
            reset();
            sendBroadcast(i);
            isPlaying = false;

        }else{

            ((ImageButton)v).setImageResource(R.drawable.ic_audio_stop);
            Intent i = new Intent();
            i.setAction(ACTION_MEDIA_PLAYER_SERVICE);
            i.putExtra(FILE_NAME , players.get(currPosition).getTitle());
            i.putExtra(ABS_FILE_NAME , ABSOLUTE_PATH_SHELF);
            Log.w(TAG , players.get(currPosition).getTitle());
            sendBroadcast(i);

            isPlaying = true;

        }

        Log.w(TAG ,"Is Playing : " + isPlaying);






     }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: finish();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mReceiver != null){
            unregisterReceiver(mReceiver);
            mReceiver =null;
        }
    }

    private class StatusReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();

            int status = extras.getInt(SERVICE_STATUS);

            String service = extras.getString(EXTRA_SERVICE_MESSAGE_STATUS);

            if(status == SEEK_SUCCESS){

            }else if(status == PLAYER_DURATION){
                currentDuration = Integer.valueOf(service) / 1000;

            }else if(status == MAX_PLAYER_DURATION){
                maxDuration = Integer.valueOf(service) / 1000 ;
                Log.w(TAG , "MAx Duration : " + maxDuration);
                mSeekBar.setMax(maxDuration );
                calculateDuration();

            }else if(status == SUCCESSFUL_RUNNING ){
                if(!pause){
                    mHandler.post(mPlayerRunnable);
                }
            }else if(status == SUCCESSFUL_STOP){
                mHandler.removeCallbacks(mPlayerRunnable);
                reset();
            }else if(status == SUCCESSFUL_PAUSE){
                pause = true;
            }else if(status == ERROR_NOT_RUNNING){
                mHandler.removeCallbacks(mPlayerRunnable);
                reset();

            }
        }
    }


    private void calculateDuration(){

        mDurationHour = (int) (maxDuration / 3600);
        mDurationMin = ((int) (maxDuration / 60)) % 60;
        mDurationSec = maxDuration % 60;

        mDuration.setText(String.format(configuration, format.format(mDurationHour), format.format(mDurationMin), format.format(mDurationSec)));
    }

    private void display(){

        mCurrent.setText(String.format(configuration , format.format(mCurrentHour) , format.format(mCurrentMin) , format.format(mCurrentSec)));

       // mDuration.setText(String.format(configuration, format.format(mDurationHour), format.format(mDurationMin), format.format(mDurationSec)));
    }

    private class PlayerRunnable implements Runnable{

        private void calculateCurrent(){
            currentDuration = currentDuration + 1;

            mCurrentSec = mCurrentSec + 1;

            if(mCurrentSec > 59){
                mCurrentMin = mCurrentMin + 1;
                mCurrentSec = 0;
            }

            if(mCurrentMin > 59){
                mCurrentHour= (mCurrentHour + 1) % 24;
                mCurrentMin = 0;
            }


            // days
        }


        @Override
        public void run() {

            calculateCurrent();

            //calculateDuration();

            mSeekBar.setProgress(currentDuration);

            display();

            while(pause){
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            mHandler.postDelayed(this, 1000L);
        }
    }



}
