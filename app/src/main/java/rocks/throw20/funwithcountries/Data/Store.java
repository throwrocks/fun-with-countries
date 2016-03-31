package rocks.throw20.funwithcountries.Data;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by josel on 3/10/2016.
 */
public class Store {

    private static final String LOG_TAG = Store.class.getSimpleName();
    Context mContext;

    /**
     * Constructor
     * @param context the context
     * @param countryValues the ContentValues[]
     * @param method the method of inserting
     *               bulk = bulk insert, use this when populating for the first time
     *               insert/update = loops through, created if it doesn't exist, updates if it does
     */
    public Store(
            Context context,
            ContentValues[] countryValues,
            String method
    ){

        this.mContext = context;

        if (method.equals("bulk")){
            //Log.e(LOG_TAG, "Lets bulk");
            mContext.getContentResolver().bulkInsert(
                    Contract.CountryEntry.CONTENT_URI,
                    countryValues
            );
        }else if(method.equals("insert/update")){
            //Log.e(LOG_TAG, "Lets insert/update!");
            for (ContentValues value: countryValues) {
                new StoreRecord( mContext, value );
            }
        }
    }
}
