package gtg.virus.gtpr;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Switch;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import gtg.virus.gtpr.adapters.BookAdapter;
import gtg.virus.gtpr.db.Book;
import gtg.virus.gtpr.db.BookHelper;

public class ScheduledBooksView extends ActionBarActivity implements BookAdapter.OnEventListener {


    private ListView mListView = null;

    private BookAdapter mAdapter = null;

    public final static int RESULT_CODE_SET_ALARM = 0xff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scheduled_books);

        mListView = (ListView) findViewById(R.id.list_books);


        mAdapter = new BookAdapter(this , new ArrayList<Book>());
        mAdapter.setmListener(this);



        mListView.setAdapter(mAdapter);


        load();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void load(){
        new AsyncTask<Void ,Void , List<Book>>(){

            @Override
            protected List<Book> doInBackground(Void... params) {
                BookHelper book = new BookHelper(ScheduledBooksView.this);

                List<Book> books = book.list();

                return books;
            }

            @Override
            protected void onPostExecute(List<Book> books) {
                super.onPostExecute(books);
                if(books.size() <= 0){
                    Crouton.makeText(ScheduledBooksView.this , "Empty EBooks" , Style.ALERT).show();
                }else{
                    for(Book b : books){
                        mAdapter.add(b);
                    }
                }

            }
        }.execute(null,null,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scheduled_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: finish();break;
            case R.id.menu_add:
                Intent i = new Intent();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(BookAdapter adapter, Book book, int position) {

    }
    private Switch t = null;
    @Override
    public void onToggle(BookAdapter adapter,final Book book, final int position, boolean toggle,final Switch t) {
        if(toggle) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            this.t = t;

            builder.setTitle("Setup Schedule.").setMessage("Are you sure you want to set a schedule to read/listen to this pinbook? By yes you will be provided with info to set the alarm, but still you can disable this later.");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(ScheduledBooksView.this, ScheduleBooksInfoView.class);
                    i.putExtra(ScheduleBooksInfoView.EXTRA_TAG, book.toString());
                    i.putExtra("position" , position);
                    startActivityForResult(i, RESULT_CODE_SET_ALARM);
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            t.setChecked(false);
                            mAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }).create().show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_CODE_SET_ALARM){
            if(resultCode == RESULT_OK){

                Crouton.makeText(this, "Saved!" , Style.INFO).show();
            }else{
                t.setChecked(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crouton.clearCroutonsForActivity(this);
    }
}
