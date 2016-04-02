package rocks.throw20.funwithcountries;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
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
        Intent intent = getIntent();
        gameMode = intent.getStringExtra("gameMode");
        gameTitle = intent.getStringExtra("gameTitle");

        Bundle args = new Bundle();
        args.putString("gameMode", gameMode);
        args.putString("gameTitle", gameTitle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(gameTitle);
        setSupportActionBar(toolbar);

        if ( savedInstanceState == null ) {
            Log.e(LOG_TAG, "args " + args);
            GameActivityFragment gameActivityFragment = new GameActivityFragment();
            gameActivityFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.game_frame, gameActivityFragment)
                    .commit();

        }

    }


}
