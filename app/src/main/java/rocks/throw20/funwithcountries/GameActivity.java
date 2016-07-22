package rocks.throw20.funwithcountries;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


public class GameActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    CardBackFragment cardBackFragment;
    CardFrontFragment cardFrontFragment;
    /**
     * A handler object, used for deferring UI operations.
     */
    private Handler mHandler = new Handler();

    /**
     * Whether or not we're showing the back of the card (otherwise showing the front).
     */
    private boolean mShowingBack = false;

    private static final String LOG_TAG = GameActivity.class.getSimpleName();
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.e(LOG_TAG, "setContentView " + true);

        Bundle args = new Bundle();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String gameTitle = sharedPref.getString("game_title", "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(gameTitle);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Log.e(LOG_TAG, "SavedInstanceState " + null);
            CardFrontFragment gameActivityFragment = new CardFrontFragment();
            gameActivityFragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .add(R.id.game_frame, gameActivityFragment, "gameFragment")
                    .commit();
        } else {
            CardFrontFragment gameActivityFragment = (CardFrontFragment) getFragmentManager().findFragmentByTag("gameFragment");
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            //moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit this game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                        //close();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }

    public void flipCard(Bundle args) {
        if (mShowingBack) {
            mShowingBack = false;
            getFragmentManager().beginTransaction().remove(cardBackFragment).commit();
            cardFrontFragment = new CardFrontFragment();
            cardFrontFragment.setArguments(args);
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                            R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                    .replace(R.id.game_frame, cardFrontFragment)
                    .commit();
        } else {
            mShowingBack = true;
            cardBackFragment = new CardBackFragment();
            cardBackFragment.setArguments(args);
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                            R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                    .replace(R.id.game_frame, cardBackFragment)
                    .commit();

            // Defer an invalidation of the options menu (on modern devices, the action bar). This
            // can't be done immediately because the transaction may not yet be committed. Commits
            // are asynchronous in that they are posted to the main thread's message loop.
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    invalidateOptionsMenu();
                }
            });
        }
    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        // When the back stack changes, invalidate the options menu (action bar).
        invalidateOptionsMenu();
    }

}
