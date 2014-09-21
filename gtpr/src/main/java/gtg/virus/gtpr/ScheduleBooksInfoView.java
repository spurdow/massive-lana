package gtg.virus.gtpr;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ScheduleBooksInfoView extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        addPreferencesFromResource(R.xml.alarm_xml);
    }
}
