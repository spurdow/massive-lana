package gtg.virus.gtpr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import gtg.virus.gtpr.AbstractViewer;
import gtg.virus.gtpr.entities.PBook;

import static gtg.virus.gtpr.utils.Utilities.PIN_EXTRA_PBOOK;


public class GTGTxtViewer extends AbstractViewer {
    /**
     * @return the layout from R.
     */

    private TextView mTextView;
    @Override
    protected int getContentViewResId() {
        return R.layout.txt_main_layout;
    }

    @Override
    protected void initializeResources(Bundle saveInstanceState) {
        mTextView = (TextView) findViewById(R.id.txt_main_layout_view);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String gsonExtra =  extras.getString(PIN_EXTRA_PBOOK);

            mBook = new Gson().fromJson(gsonExtra, PBook.class);
        }

         new AsyncTask<Void, Void, String>() {

            /**
             * Override this method to perform a computation on a background thread. The
             * specified parameters are the parameters passed to {@link #execute}
             * by the caller of this task.
             * <p/>
             * This method can call {@link #publishProgress} to publish updates
             * on the UI thread.
             *
             * @param params The parameters of the task.
             * @return A result, defined by the subclass of this task.
             * @see #onPreExecute()
             * @see #onPostExecute
             * @see #publishProgress
             */
            @Override
            protected String doInBackground(Void... params) {
                final StringBuilder text = new StringBuilder();
                File f = new File(mBook.getPath());
                FileReader fReader = null;
                try {
                    fReader = new FileReader(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                BufferedReader br = new BufferedReader(fReader);

                String line;

                try {
                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return text.toString();
            }

             /**
              * <p>Runs on the UI thread after {@link #doInBackground}. The
              * specified result is the value returned by {@link #doInBackground}.</p>
              * <p/>
              * <p>This method won't be invoked if the task was cancelled.</p>
              *
              * @param s The result of the operation computed by {@link #doInBackground}.
              * @see #onPreExecute
              * @see #doInBackground
              * @see #onCancelled(Object)
              */
             @Override
             protected void onPostExecute(String s) {
                 super.onPostExecute(s);
                 if(s!=null && !s.isEmpty()){
                     mTextView.setText(s);
                 }
             }
         }.execute(null, null, null);

    }


}
