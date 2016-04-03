package rocks.throw20.funwithcountries;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rocks.throw20.funwithcountries.R;

public class GameActivity extends AppCompatActivity {
    private static final String LOG_TAG = GameActivity.class.getSimpleName();
    String gameMode;
    String gameTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.e(LOG_TAG, "setContentView " + true);

        Bundle args = new Bundle();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        gameTitle = sharedPref.getString("game_title","");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(gameTitle);
        setSupportActionBar(toolbar);


        if (savedInstanceState == null) {
            GameActivityFragment gameActivityFragment = new GameActivityFragment();
            gameActivityFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.game_frame, gameActivityFragment, "gameFragment")
                    .commit();
        }
        else {
            GameActivityFragment gameActivityFragment = (GameActivityFragment) getSupportFragmentManager().findFragmentByTag("gameFragment");
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

}
