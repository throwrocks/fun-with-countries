package rocks.throw20.funwithcountries;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import rocks.throw20.funwithcountries.Data.Contract;
import rocks.throw20.funwithcountries.Data.FetchTask;

public class CountriesListActivity extends AppCompatActivity implements CountriesListFragment.Callback{
    private final String LOG_TAG = CountriesListActivity.class.getSimpleName();

    private boolean mTwoPane;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Stetho.initializeWithDefaults(this);

        // Get all the countries in a cursor
        mCursor = this.getContentResolver().query(
                Contract.CountryEntry.buildCountries(),
                null,
                null,
                null,
                null);
        //Log.e(LOG_TAG, "getItemCount -> " + mCursor.getCount());

        // If the Cursor is null, or it doesn't contain 247 records
        // create a DataFetch Async task and execute it
        if ( mCursor == null || mCursor.getCount() != 247) {
            FetchTask fetchData = new FetchTask(this);
            fetchData.execute();
        }

        setContentView(R.layout.activity_countries_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri dateUri) {

    }
}
