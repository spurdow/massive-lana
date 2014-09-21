package gtg.virus.gtpr;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Calendar;

import gtg.virus.gtpr.db.Book;
import gtg.virus.gtpr.db.ScheduleHelper;
import gtg.virus.gtpr.db.ScheduledBooks;
import gtg.virus.gtpr.utils.Utilities;

public class ScheduleBooksInfoView extends ActionBarActivity {

    public final static String TAG = ScheduleBooksInfoView.class.getSimpleName();

    public final static String EXTRA_TAG = TAG + "._EXTRA";

    private final DecimalFormat format = new DecimalFormat("00");

    private EditText mInfo;

    private EditText mTime;

    private CheckBox mSunday;

    private CheckBox mMonday;

    private CheckBox mTuesday;

    private CheckBox mWednesday;

    private CheckBox mThursday;

    private CheckBox mFriday;

    private CheckBox mSaturday;

    private Book book;

    private ScheduledBooks sb;

    private int[] index = new int[7];

    private ScheduleHelper helper;

    private int position ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_layout);

        Bundle extras = getIntent().getExtras();

        final String g = extras.getString(EXTRA_TAG);
        position = extras.getInt("position");
        book = new Gson().fromJson(g , Book.class);


        helper = new ScheduleHelper(this);
        sb = helper.getSchedBook(book.getId());
        if(sb == null){
            sb = new ScheduledBooks();
            sb.setBook_id(book.getId());
            sb.setPath(book.getPath());
            sb.setBitWeek("0000000");
            Log.w(TAG , "Sb is null");
        }



        mInfo = (EditText) findViewById(R.id.schedule_txt_info);
        mTime = (EditText) findViewById(R.id.schedule_txt_time);

        mInfo.setText(sb.getInfo());
        mTime.setText(sb.getTimeToAlarm());


        mSunday = (CheckBox) findViewById(R.id.ck_sunday);
        mMonday = (CheckBox) findViewById(R.id.ck_monday);
        mTuesday = (CheckBox) findViewById(R.id.ck_tuesday);
        mWednesday = (CheckBox) findViewById(R.id.ck_wednesday);
        mThursday = (CheckBox) findViewById(R.id.ck_thursday);
        mFriday = (CheckBox) findViewById(R.id.ck_friday);
        mSaturday = (CheckBox) findViewById(R.id.ck_saturday);


        if(sb != null && sb.getBitWeek() != null && sb.getBitWeek().length()==7){
            index = Utilities.bitWeekAsInt(sb.getBitWeek());
        }

        mSunday.setChecked((index[0]==1)?true:false);
        mMonday.setChecked((index[1] == 1) ? true : false);
        mTuesday.setChecked((index[2] == 1) ? true : false);
        mWednesday.setChecked((index[3] == 1) ? true : false);
        mThursday.setChecked((index[4] == 1) ? true : false);
        mFriday.setChecked((index[5] == 1) ? true : false);
        mSaturday.setChecked((index[6] == 1) ? true : false);

        mSunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                index[0] = isChecked?1:0;
            }
        });

        mMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                index[1] = isChecked?1:0;
            }
        });

        mTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                index[2] = isChecked?1:0;
            }
        });

        mWednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                index[3] = isChecked?1:0;
            }
        });

        mThursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                index[4] = isChecked?1:0;
            }
        });

        mFriday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                index[5] = isChecked?1:0;
            }
        });

        mSaturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                index[6] = isChecked?1:0;
            }
        });

        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ScheduleBooksInfoView.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mTime.setText(format.format(selectedHour) + ":" + format.format(selectedMinute) + ":00");
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

/*        mBegDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int y = mcurrentTime.get(Calendar.YEAR);
                int m = mcurrentTime.get(Calendar.MONTH);
                int d = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(ScheduleBooksInfoView.this,new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mBegDate.setText(monthOfYear +"/"+dayOfMonth+"/"+year);
                    }
                } , y , m , d);
                datePicker.setTitle("Select Date");
                datePicker.show();
            }
        });



        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int y = mcurrentTime.get(Calendar.YEAR);
                int m = mcurrentTime.get(Calendar.MONTH);
                int d = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(ScheduleBooksInfoView.this,new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mEndDate.setText(monthOfYear +"/"+dayOfMonth+"/"+year);
                    }
                } , y , m , d);
                datePicker.setTitle("Select Date");
                datePicker.show();
            }
        });*/


        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scheduled_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String info = mInfo.getText().toString()+"";
        String time = mTime.getText().toString()+"";
        String bitWeek  = Utilities.bitWeekAsString(index)+"";
        switch(item.getItemId()){
            case android.R.id.home:

                setResult(result);
                finish();
                break;

            case R.id.menu_add:

                // perform saving here...



                sb.setInfo(info);
                sb.setTimeToAlarm(time);
                sb.setBitWeek(bitWeek);
                sb.setBook_id(book.getId());
                sb.setPath(book.getPath());
                sb.setStatus(1);


                long id = helper.add(sb);
                result = RESULT_OK;
                setResult(result);
                Toast.makeText(this, "Added " + id + " " + bitWeek, Toast.LENGTH_SHORT).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(result);
    }

    private int result = RESULT_CANCELED;
}
