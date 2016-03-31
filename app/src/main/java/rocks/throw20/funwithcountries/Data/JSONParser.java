package rocks.throw20.funwithcountries.Data;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joselopez on 3/10/16.
 *
 */
class JSONParser {

    private static final String LOG_TAG = API.class.getSimpleName();

    ContentValues[] mContentValues;

    private final Context mContext;


    // Constructor
    public JSONParser(Context context){

        this.mContext = context;

    }

    public ContentValues[] getCountriesFromJSON(String countriesJSONString ){
        try {

            // Create the countriesJSON object with the countriesJSONString parameter
            JSONArray countriesArray = new JSONArray(countriesJSONString);

            // These are the individual fields
            final String fieldCountryId = Contract.CountryEntry.countryId;
            final String fieldCountryName = Contract.CountryEntry.countryName;
            final String fieldCountryCapital = Contract.CountryEntry.countryCapital;
            final String fieldCountryRegion = Contract.CountryEntry.countryRegion;
            final String fieldCountrySubRegion = Contract.CountryEntry.countrySubRegion;
            final String fieldCountryPopulation = Contract.CountryEntry.countryPopulation;

            int countriesQty = countriesArray.length();
            //Log.e(LOG_TAG, "CountriesQty: " + countriesQty);


            mContentValues = new ContentValues[countriesQty];

            // Loop through the countriesArray to parse each Json object needed
            for (int i = 0; i < countriesQty; i++) {
                JSONObject countryRecord = countriesArray.getJSONObject(i);
                //Log.e(LOG_TAG, "countryRecord: " + countryRecord);

                // Parse the individual data elements needed
                String countryName = countryRecord.getString(fieldCountryName);
                String countryCapital = countryRecord.getString(fieldCountryCapital);
                String countryRegion = countryRecord.getString(fieldCountryRegion);
                String countrySubRegion = countryRecord.getString(fieldCountrySubRegion);
                Long countryPopulation = countryRecord.getLong(fieldCountryPopulation);
                //Log.e(LOG_TAG, "Parsing string: " + countryName + " " + countryCapital + " " + countryRegion);

                // Create a content values object
                ContentValues countryValues = new ContentValues();
                countryValues.put(fieldCountryId, i);
                countryValues.put(fieldCountryName, countryName);
                countryValues.put(fieldCountryCapital, countryCapital);
                countryValues.put(fieldCountryRegion, countryRegion);
                countryValues.put(fieldCountrySubRegion, countrySubRegion);
                countryValues.put(fieldCountryPopulation, countryPopulation);

                //----------------------------------------------------------------------------------
                // Build an array of content values to be returned and processed outside this method
                //----------------------------------------------------------------------------------
                mContentValues[i] = countryValues;
            }
        }
        catch (JSONException e) {
        e.printStackTrace();
        }

        return mContentValues;
    }

}

