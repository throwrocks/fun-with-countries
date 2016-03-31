package rocks.throw20.funwithcountries.Data;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by joselopez on 3/10/16.
 */
public class FetchTask extends AsyncTask<String, Void, Void> {

    private static final String LOG_TAG = FetchTask.class.getSimpleName();

    private final Context mContext;

        // Constructor
        public FetchTask(Context context){
        this.mContext = context;

        }

    @Override
    protected Void doInBackground(String... params) {

        String jsonResults;
        ContentValues[] parsedResults;

        // Create an API object
        API mAPI = new API(mContext);

        // Get the results from the API
        jsonResults = mAPI.callAPI();
        Log.e(LOG_TAG, "results " + jsonResults);

        // Parse the results if not null
        if ( jsonResults != null ) {
            JSONParser parseCountries = new JSONParser(mContext);

            // Get the parsed results
            parsedResults = parseCountries.getCountriesFromJSON(jsonResults);

            if ( parsedResults != null ){
                // Bulk insert
                Store bulkInsert = new Store(mContext, parsedResults, "insert/update");

            }
        }



        return null;
    }


}
