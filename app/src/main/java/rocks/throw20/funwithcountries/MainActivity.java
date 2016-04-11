package rocks.throw20.funwithcountries;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.stetho.Stetho;

import rocks.throw20.funwithcountries.Data.Contract;
import rocks.throw20.funwithcountries.Data.FetchTask;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Cursor mCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(this);

        Utilities util = new Utilities(this);
        mCursor = util.getAllCountries();

        // If the Cursor is null, or it doesn't contain 247 records
        // create a DataFetch Async task and execute it
        if ( mCursor == null || mCursor.getCount() != 247) {
            FetchTask fetchData = new FetchTask(this);
            fetchData.execute();
        }

        // Setup the layout
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Set the buttons OnClickListeners
        final Button button = (Button) findViewById(R.id.button_fun_with_capitals);
        final Intent intent = new Intent(this, GameActivity.class);

        SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPref.edit();
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                editor.putInt("game_progress", 1);
                editor.putInt("game_progress_max", 10);
                editor.putString("game_mode", "capitals");
                editor.putString("game_title", "Learn the Capitals");
                editor.putInt("correct_answers", 0);
                editor.putInt("incorrect_answers",0);
                editor.putString("game_title", "Learn the Capitals");
                editor.putString("used_countries", "");
                editor.apply();

                startActivity(intent);


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
