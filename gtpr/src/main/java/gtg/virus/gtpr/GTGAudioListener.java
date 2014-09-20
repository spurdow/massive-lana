package gtg.virus.gtpr;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import static gtg.virus.gtpr.service.AudioService.*;

public class GTGAudioListener extends ActionBarActivity {

    public final static String PIN_EXTRA_PBOOK = "_pbook_extra";

    private SeekBar mSeekBar = null;

    private ImageButton mPlayStop = null;

    private ImageButton mPrev = null;

    private ImageButton mNext = null;

    private TextView mCurrent = null;

    private TextView mDuration = null;


    private DecimalFormat format  = new DecimalFormat("00");

    private static final String configuration = "%s:%s:%s";

    private final Stack<Integer> pendingSeek = new Stack<Integer>();

    private boolean isPlaying = false;

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

        maxPosition = players.size();

        mSeekBar = (SeekBar) findViewById(R.id.audio_player_seekbar);

        mPlayStop = (ImageButton) findViewById(R.id.audio_button_play_stop);


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

        if(mReceiver == null){
            registerReceiver(mReceiver, new IntentFilter(AUDIO_SERVICE_STATUS));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void reset(){
        mDurationHour = mDurationMin = mDurationSec = mCurrentHour = mCurrentMin = mCurrentSec = 0;

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
            i.putExtra(FILE_NAME , players.get(currPosition).getTitle());
            i.putExtra(ABS_FILE_NAME , ABSOLUTE_PATH_SHELF);
            Log.w(TAG , players.get(currPosition).getTitle());
            sendBroadcast(i);


        }else{
            ((ImageButton)v).setImageResource(R.drawable.ic_audio_stop);
            Intent i = new Intent();
            i.setAction(ACTION_MEDIA_PLAYER_SERVICE);
            i.putExtra(FILE_NAME , players.get(currPosition).getTitle());
            i.putExtra(ABS_FILE_NAME , ABSOLUTE_PATH_SHELF);
            Log.w(TAG , players.get(currPosition).getTitle());
            sendBroadcast(i);

        }
        isPlaying = !isPlaying;


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

            String service = extras.getString(SERVICE_STATUS);

            if(status == SEEK_SUCCESS){

            }else if(status == PLAYER_DURATION){
                currentDuration = Integer.valueOf(service) * 60;

            }else if(status == MAX_PLAYER_DURATION){
                maxDuration = Integer.valueOf(service) * 60;
                mSeekBar.setMax(maxDuration);

            }else if(status == SUCCESSFUL_RUNNING ){
                if(!pause){
                    mSeekBar.post(mPlayerRunnable);
                }
            }else if(status == SUCCESSFUL_STOP){
                mSeekBar.removeCallbacks(mPlayerRunnable);
                reset();
            }else if(status == SUCCESSFUL_PAUSE){
                pause = true;
            }else if(status == ERROR_NOT_RUNNING){
                mSeekBar.removeCallbacks(mPlayerRunnable);
                reset();

            }
        }
    }


    private void display(){

        mCurrent.setText(String.format(configuration , format.format(mCurrentHour) , format.format(mCurrentMin) , format.format(mCurrentSec)));

        mDuration.setText(String.format(configuration, format.format(mDurationHour), format.format(mDurationMin), format.format(mDurationSec)));
    }

    private class PlayerRunnable implements Runnable{

        @Override
        public void run() {

            mSeekBar.setProgress(currentDuration);

            display();

            while(pause){
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            mSeekBar.postDelayed(this, 1000L);
        }
    }


}
