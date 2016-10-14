package com.mac.training.mixpanelappsample;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String MIXPANEL_DISTINCT_ID_NAME = "Mixpanel Example $distinctid";

    private MixpanelAPI mMixpanelAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add Mixpanel to your main activity
        mMixpanelAPI = MixpanelAPI.getInstance(this, "a7d93621c87071e0cfe363b5a44fbfef");

        initPushNotifications();
        //trackEvent();
    }

    private void trackEvent() {
        // Add code to track your first event
        try {
            JSONObject props = new JSONObject();
            props.put("Gender", "Female");
            props.put("Logged in", false);
            //mMixpanelAPI.track("MainActivity - onCreate called", props);
            mMixpanelAPI.track("Video play");
        } catch (JSONException jsonE) {
            Log.e(TAG, "Unable to add properties to JSONObject", jsonE);
        }
    }

    private void initPushNotifications() {
        /*final String trackingDistinctId = getTrackingDistinctId();
        mMixpanelAPI.identify(trackingDistinctId); //this is the distinct_id value that
        // will be sent with events. If you choose not to set this,
        // the SDK will generate one for you

        mMixpanelAPI.getPeople().identify(trackingDistinctId); //this is the distinct_id
        // that will be used for people analytics. You must set this explicitly in order
        // to dispatch people data.

        // People analytics must be identified separately from event analytics.
        // The data-sets are separate, and may have different unique keys (distinct_id).
        // We recommend using the same distinct_id value for a given user in both,
        // and identifying the user with that id as early as possible.

        mMixpanelAPI.getPeople().initPushHandling("144175179973");*/


        MixpanelAPI.People people = mMixpanelAPI.getPeople();
        // Always identify before initializing push notifications
        people.identify("13793");

        // Sets up GCM registration and handling of GCM messages
        // for Google API project number 717871474771
        people.initPushHandling("144175179973");

    }

    // To preserve battery life and customer bandwidth
    @Override
    protected void onDestroy() {
        mMixpanelAPI.flush();
        super.onDestroy();
    }


    private String getTrackingDistinctId() {
        final SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        String ret = prefs.getString(MIXPANEL_DISTINCT_ID_NAME, null);
        if (ret == null) {
            ret = generateDistinctId();
            final SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putString(MIXPANEL_DISTINCT_ID_NAME, ret);
            prefsEditor.commit();
        }

        return ret;
    }

    // These disinct ids are here for the purposes of illustration.
    // In practice, there are great advantages to using distinct ids that
    // are easily associated with user identity, either from server-side
    // sources, or user logins. A common best practice is to maintain a field
    // in your users table to store mixpanel distinct_id, so it is easily
    // accesible for use in attributing cross platform or server side events.
    private String generateDistinctId() {
        final Random random = new Random();
        final byte[] randomBytes = new byte[32];
        random.nextBytes(randomBytes);
        return Base64.encodeToString(randomBytes, Base64.NO_WRAP | Base64.NO_PADDING);
    }
}
