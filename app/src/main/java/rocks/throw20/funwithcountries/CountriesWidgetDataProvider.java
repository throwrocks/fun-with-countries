package rocks.throw20.funwithcountries;

/**
 * Created by josel on 5/29/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import rocks.throw20.funwithcountries.Data.Contract;

/**
 * SoccerScoresWidgetDataProvider
 * Implements RemoteViewsFactory to build the home screen widget and populate it with data
 */
class CountriesWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory  {
    private static final String LOG_TAG = "WidgetDataProvider";

    private Context mContext = null;
    private final JSONArray resultsArray = new JSONArray();
    public CountriesWidgetDataProvider(Context context) {
        mContext = context;

    }

    private void initData() {

        // Get today's date and convert it to a string to query the database for today's games
        Utilities util = new Utilities(mContext);
        Cursor results = util.getAllCountriesWithCapitals();
        results.moveToFirst();
        int resultsCount = results.getCount();

        for (int i = 1; i < resultsCount; i++) {

            String countryName = results.getString(Contract.CountryEntry.indexCountryName);
            String countryCapital = results.getString(Contract.CountryEntry.indexCountryCapital);
            String countryAlpha2Code = results.getString(Contract.CountryEntry.indexAlpha2code);

            // Put results in a JSON object
            JSONObject record = new JSONObject();
            try {
                Log.e(LOG_TAG,countryName);
                record.put("countryName", countryName);
                record.put("countryCapital", countryCapital);
                record.put("countryAlpha2Code", countryAlpha2Code);
                resultsArray.put(record);
            }
            catch (JSONException e) {
                Log.e(LOG_TAG,e.getMessage());
            }

        }
    }


    /**
     * Called when your factory is first constructed. The same factory may be shared across
     * multiple RemoteViewAdapters depending on the intent passed.
     */
    @Override
    public void onCreate() {
        Log.e(LOG_TAG, "onCreate = " + true);

        initData();

    }



    @Override
    public void onDataSetChanged() {
        initData();
        Log.e(LOG_TAG, "onDateSetChanged = " + true);

    }


    /**
     * Called when the last RemoteViewsAdapter that is associated with this factory is
     * unbound.
     */
    @Override
    public void onDestroy() {

    }

    /**

     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return resultsArray.length() ;
    }

    /**

     * <p/>
     * Note: expensive tasks can be safely performed synchronously within this method, and a
     * loading view will be displayed in the interim. See {@link #getLoadingView()}.
     *
     * @param position The position of the item within the Factory's data set of the item whose
     *                 view we want.
     * @return A RemoteViews object corresponding to the data at the specified position.
     */
    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews mView = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);

        try {
            JSONObject record = (JSONObject) resultsArray.get(position);
            String countryName = record.get("countryName").toString();
            String countryCapital = record.get("countryCapital").toString();

            mView.setTextViewText(android.R.id.text1, countryName + " - " + countryCapital );
            mView.setTextColor(android.R.id.text1, Color.BLACK);

            // Set the list items to launch the soccer scores activity
            //Intent fillInIntent = new Intent();
            //mView.setOnClickFillInIntent(android.R.id.text1, fillInIntent);

        }
        catch (JSONException e) {
            Log.e(LOG_TAG,e.getMessage());
        }

        return mView ;
    }

    /**
     * This allows for the use of a custom loading view which appears between the time that
     * {@link #getViewAt(int)} is called and returns. If null is returned, a default loading
     * view will be used.
     *
     * @return The RemoteViews representing the desired loading view.
     */
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    /**

     *
     * @return The number of types of Views that will be returned by this factory.
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    /**

     *
     * @param position The position of the item within the data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position ;
    }


    /**

     *
     * @return True if the same id always refers to the same object.
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }
}