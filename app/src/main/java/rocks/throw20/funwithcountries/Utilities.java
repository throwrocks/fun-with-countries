package rocks.throw20.funwithcountries;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import rocks.throw20.funwithcountries.Data.Contract;
import rocks.throw20.funwithcountries.Data.FetchTask;

/**
 * Created by josel on 3/31/2016.
 */
public class Utilities {
    private static final String LOG_TAG = Utilities.class.getSimpleName();
    private Context mContext;

    public Utilities(Context context){
        this.mContext = context;
    }

    /**
     * getAllCountries
     * A simple method to get all countries from the database
     * @return
     */
    public Cursor getAllCountries(){
        Cursor mCursor;
        // Get all the countries in a cursor
        mCursor = mContext.getContentResolver().query(
                Contract.CountryEntry.buildCountries(),
                null,
                null,
                null,
                null);
        return mCursor;
    }


    public Cursor getAllCountriesWithCapitals(){
        Cursor mCursor;
        // Get all the countries in a cursor
        mCursor = mContext.getContentResolver().query(
                Contract.CountryEntry.buildCountries(),
                null,
                "LENGTH(" + Contract.CountryEntry.countryCapital + ") >0",
                null,
                null);
        return mCursor;
    }


    /**
     * getRandomInt
     * @param max the max int
     * @param exclude an array of ints to exclude so to avoid repetitions
     * @return the random int
     */
    public int getRandomInt(int max, int[] exclude) {
        Random rand = new Random();

        int n = rand.nextInt(max);
        // If the int = max substract 1 so it doesn't go out of bounds
        if ( n == max ){ n = n - 1;}

        if ( exclude != null ){
            //Log.e(LOG_TAG, "exclude length " + exclude.length);
            for (int i = 0; i < exclude.length; i++) {
                int e = exclude[i];
                boolean test = e == n;
                //Log.e(LOG_TAG, "test " + test);
                if ( test ){
                    getRandomInt(max, exclude);
                }
            }
    }
        return n;
    }


    /**
     * shuffleArray
     * Method to shuffle the Strings in an array
     * @param ar the array to be suffled
     */
    // Implementing Fisher–Yates shuffle
    // http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
    public String[] shuffleArray(String[] ar)
    {
        Random rand = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rand.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }
}
