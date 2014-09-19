package gtg.virus.gtpr;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
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

    private String mFileName = "AudioPinBook_" ;

    private String mPrevAbsPath = "";

    private String mFileName_suffix = System.currentTimeMillis()+"";

    private TextView mTextView = null;

    private LinearLayout mBtn_parent;

    private Button mBtn_discard;

    private Button mBtn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);



        mTime = (TextView) findViewById(R.id.audio_time_elapse);
        mBackgroundLabel = (ImageView) findViewById(R.id.audio_background_image_view);
        mTextView = (TextView) findViewById(R.id.audio_txt_file_name);

        mTextView.setText(mFileName + mFileName_suffix);

        mBtn_parent = (LinearLayout) findViewById(R.id.parent_btn_layout);
        mBtn_discard = (Button) findViewById(R.id.btn_audio_discard);
        mBtn_save = (Button) findViewById(R.id.btn_audio_save);


        mBtn_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>(){


                    @Override
                    protected Void doInBackground(Void... params) {

                        File f = new File(mPrevAbsPath);
                        while(!f.delete()){

                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        return null;
                    }
                }.execute(null , null, null);

                Crouton.makeText(AudioBookMaker.this , "Discarded!" , Style.INFO).show();
                mBtn_parent.setVisibility(View.INVISIBLE);
            }
        });


        mBtn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crouton.makeText(AudioBookMaker.this , "Saved!" , Style.INFO).show();
                mBtn_parent.setVisibility(View.INVISIBLE);
            }
        });
        serviceReceiver = new AudioServiceStatusReceiver();
        registerReceiver(serviceReceiver , new IntentFilter(AUDIO_SERVICE_STATUS));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void start(){
        if(!hangingStart){

            hangingStart = true;
            Intent i = new Intent();
            i.setAction(ACTION_MEDIA_RECORDER_SERVICE);
            i.putExtra(FILE_NAME, mFileName + mFileName_suffix);
            sendBroadcast(i);
            timeStarted = System.currentTimeMillis();


        }
    }

    private void stop(){
        if(!hangingStop){
            min = 0;
            time = 0;
            mHandler.removeCallbacks(runner);
            mTime.setText(String.format(filter , min, formatter.format(time) ));
            hangingStop = true;
            Intent i = new Intent();
            i.setAction(ACTION_MEDIA_RECORDER_STOP_SERVICE);
            sendBroadcast(i);
            mPrevAbsPath = ABSOLUTE_PATH + "/" + mFileName+mFileName_suffix + SUFFIX;
            mFileName_suffix = System.currentTimeMillis() +"";

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.audio_creator_menu , menu);

        View view =  menu.findItem(R.id.opt_menu_switch).getActionView();

        mToggle = (Switch)view.findViewById(R.id.audio_toggle_play_stop);

        mToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    start();
                    mBtn_parent.setVisibility(View.INVISIBLE);
                }else{
                    stop();
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home: finish(); break;
            case R.id.opt_menu_overwrite:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EditText editText = new EditText(this);
                editText.setText(mFileName+mFileName_suffix);
                AlertDialog alert = builder.setTitle("Edit Filename")
                        .setView(editText)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String editTextString = editText.getText().toString();
                                if (editTextString != null && !editTextString.isEmpty()) {
                                    mFileName = editTextString;
                                    mTextView.setText(mFileName);


                                } else {
                                    Crouton.makeText(AudioBookMaker.this, "File name is empty", Style.ALERT).show();
                                }

                            }
                        }).create();

                alert.show();
                break;
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
                    hangingStart = false;
                    hangingStop = false;
                    mTextView.setText(mFileName+mFileName_suffix);
                    mBtn_parent.setVisibility(View.VISIBLE);
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

            mTime.setText(String.format(filter , formatter.format(min), formatter.format(time) ));

            if((time  )>= MAX_TIME){
                stop();
                Crouton.makeText(AudioBookMaker.this , "Maximum time reached! Saving..." , Style.ALERT).show();
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


        Crouton.cancelAllCroutons();
    }
}
