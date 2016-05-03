package rocks.throw20.funwithcountries;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.stetho.Stetho;

import rocks.throw20.funwithcountries.Data.FetchTask;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Cursor mCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.);
        //mediaPlayer.start(); // no need to call prepare(); create() does that for you
        // Stetho used for viewing the SQLite database values
        Stetho.initializeWithDefaults(this);

        // Create a new util object to check if the countries exist
        Utilities util = new Utilities(this);
        mCursor = util.getAllCountries();
        // If the Cursor is null, or it doesn't contain 247 countries
        // create a DataFetch Async task and execute it
        if ( mCursor == null || mCursor.getCount() < 247) {
            FetchTask fetchData = new FetchTask(this);
            fetchData.execute();
        }

        Log.e(LOG_TAG,"start game");
        // Setup the layout
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Get the GameMode buttons
        final Button buttonGameModeCapitals = (Button) findViewById(R.id.button_fun_with_capitals);
        final Button buttonGameModeFlags = (Button) findViewById(R.id.button_fun_with_flags);
        final Button buttonGameScores = (Button) findViewById(R.id.button_scores);
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
                startActivity(scoresIntent);
            }
        });

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    public void showQuestion(){
        Log.e(LOG_TAG, "showQuestion " + true);
        Question newQuestions = new Question(this);
        newQuestions.getQuestion("",new String[]{});
    }

}
