package rocks.throw20.funwithcountries;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import rocks.throw20.funwithcountries.Data.FetchTask;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an auto-managed GoogleApiClient with acccess to App Invites.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .build();

        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        boolean autoLaunchDeepLink = true;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {
                                Log.e(LOG_TAG, "getInvitation:onResult:" + result.getStatus());
                                // Because autoLaunchDeepLink = true we don't have to do anything
                                // here, but we could set that to false and manually choose
                                // an Activity to launch to handle the deep link here.
                            }
                        });

        final Utilities util = new Utilities(this);

        // Stetho used for viewing the SQLite database values
        //Stetho.initializeWithDefaults(this);

        // Create a new util object to check if the countries exist
        Cursor mCursor = util.getAllCountries();
        // If the Cursor is null, or it doesn't contain 247 countries
        // create a DataFetch Async task and execute it
        if ( mCursor == null || mCursor.getCount() < 247) {
            FetchTask fetchData = new FetchTask(this);
            fetchData.execute();
        }

        Log.e(LOG_TAG,"start game");
        // Load the ad unit
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Setup the layout
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the ad
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(adRequest);

        // Get the GameMode buttons
        final Button buttonGameModeCapitals = (Button) findViewById(R.id.button_fun_with_capitals);
        final Button buttonGameModeFlags = (Button) findViewById(R.id.button_fun_with_flags);
        final Button buttonGameScores = (Button) findViewById(R.id.button_scores);
        final Button buttonShareApp = (Button) findViewById(R.id.button_share);
        // Create the GameActivity Intent
        final Intent gameIntent = new Intent(this, GameActivity.class);
        final Intent scoresIntent = new Intent(this, ScoresActivity.class);
        // Set the game settings in shared preferences
        SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPref.edit();
        // Default GameMode settings

        // Set the GameMode buttons onClickListeners
        buttonGameModeCapitals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                util.playSound("select");
                startActivity(gameIntent);
                editor.putInt("game_progress", 1);
                editor.putInt("game_progress_max", 10);
                editor.putInt("correct_answers", 0);
                editor.putInt("incorrect_answers", 0);
                editor.putString("used_countries", "");
                editor.putString("game_title", "Learn the Capitals");
                editor.putString("game_mode", "capitals");
                editor.apply();
            }
        });
        buttonGameModeFlags.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                util.playSound("select");
                startActivity(gameIntent);
                editor.putInt("game_progress", 1);
                editor.putInt("game_progress_max", 10);
                editor.putInt("correct_answers", 0);
                editor.putInt("incorrect_answers", 0);
                editor.putString("used_countries", "");
                editor.putString("game_title", "Learn the Flags");
                editor.putString("game_mode", "flags");
                editor.apply();
            }
        });

        buttonGameScores.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utilities media = new Utilities(getApplicationContext());
                media.playSound("select");
                startActivity(scoresIntent);
            }
        });

        buttonShareApp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             onInviteClicked();
            }
        });
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(LOG_TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Check how many invitations were sent and log a message
                // The ids array contains the unique invitation ids for each invitation sent
                // (one for each contact select by the user). You can use these for analytics
                // as the ID will be consistent on the sending and receiving devices.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                Log.e(LOG_TAG, getString(R.string.sent_invitations_fmt, ids.length));
            } else {
                // Sending failed or it was canceled, show failure message to the user
                //showMessage(getString(R.string.send_failed));
            }
        }
    }

}
