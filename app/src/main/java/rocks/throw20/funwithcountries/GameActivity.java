package rocks.throw20.funwithcountries;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;


public class GameActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    GameAnswerCardFragment gameAnswerCardFragment;
    GameQuestionCardFragment gameQuestionCardFragment;
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
            GameQuestionCardFragment gameActivityFragment = new GameQuestionCardFragment();
            gameActivityFragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .add(R.id.game_frame, gameActivityFragment, "gameFragment")
                    .commit();
        } else {
            GameQuestionCardFragment gameActivityFragment = (GameQuestionCardFragment) getFragmentManager().findFragmentByTag("gameFragment");
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * exitByBackKey
     * This prompts the user to exit the game
     */
    protected void exitByBackKey() {
        new AlertDialog.Builder(this)
                .setMessage("Do you want to exit this game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                }).show();
    }

    /**
     * flipCard
     * This method is called to flip the card from question to answer and vice versa
     * @param args the bundle used to store the game's state
     */

    public void flipCard(Bundle args) {
        if (mShowingBack) {
            mShowingBack = false;
            gameQuestionCardFragment = new GameQuestionCardFragment();
            gameQuestionCardFragment.setArguments(args);
            getFragmentManager().popBackStack();
                    getFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.animator.card_flip_left_in, R.animator.card_flip_left_out,
                            R.animator.card_flip_right_in, R.animator.card_flip_right_out)
                    .replace(R.id.game_frame, gameQuestionCardFragment)
                    .commit();
        } else {
            mShowingBack = true;
            gameAnswerCardFragment = new GameAnswerCardFragment();
            gameAnswerCardFragment.setArguments(args);
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                            R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                    .replace(R.id.game_frame, gameAnswerCardFragment)
                    .commit();
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
        invalidateOptionsMenu();
    }

}
