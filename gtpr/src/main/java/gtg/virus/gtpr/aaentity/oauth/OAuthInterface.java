package gtg.virus.gtpr.aaentity.oauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Android on 11/20/2014.
 */
public interface OAuthInterface {

    void onCreate(Bundle instanceState);

    void onPause();
    void onResume();

    void onStop();
    void onStart();

    void onActivityResult(Activity currentActivity, int requestCode, int resultCode, Intent data);
}
