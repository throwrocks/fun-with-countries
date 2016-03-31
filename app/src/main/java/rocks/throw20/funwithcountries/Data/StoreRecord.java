package rocks.throw20.funwithcountries.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by josel on 3/10/2016.
 */
public class StoreRecord {

    private static final String LOG_TAG = StoreRecord.class.getSimpleName();
    Context mContext;
    ContentValues mCountryValues;

    public StoreRecord(
            Context context,
            ContentValues countryValues
    ) {
        this.mContext = context;
        this.mCountryValues = countryValues;
        // Get the name to see if it already exists
        String name = countryValues.getAsString(Contract.CountryEntry.countryName);
        Log.e(LOG_TAG, "name: " + name);
        // Query the database for the name
        Cursor countriesCursor = mContext.getContentResolver().query(
               Contract.CountryEntry.CONTENT_URI,
               new String[]{Contract.CountryEntry.countryName},
               Contract.CountryEntry.countryName + " = ?",
               new String[]{name},
               null);

        // If the record does not exist, add it to the database
        if ( !countriesCursor.moveToFirst()) {
             Log.e(LOG_TAG, "add record");
            mContext.getContentResolver().insert(
                    Contract.CountryEntry.CONTENT_URI,
                    mCountryValues
            );

        }else {
            countriesCursor.close();
           // Log.e(LOG_TAG, "update record");

            // Update the record
            mContext.getContentResolver().update(
                    Contract.CountryEntry.CONTENT_URI,
                    mCountryValues,
                    Contract.CountryEntry.countryName + " = ?",
                    new String[]{name}
            );
        }

    }
}
