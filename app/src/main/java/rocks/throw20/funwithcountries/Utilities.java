package rocks.throw20.funwithcountries;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Switch;

import junit.framework.Assert;

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

    public Cursor getAllCountriesWithCapitalsExcept(String[] selectArgs){
        Cursor mCursor;
        // Get all the countries in a cursor
        // Except for countries with no capital,
        // and countries already used during the game session
        mCursor = mContext.getContentResolver().query(
                Contract.CountryEntry.buildCountries(),
                null,
                "LENGTH(" + Contract.CountryEntry.countryCapital + ") >0" + " AND " + Contract.CountryEntry.countryName + "!=?",
                selectArgs,
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

    /**
     * getDrawable
     * http://stackoverflow.com/questions/7948059/dynamic-loading-of-images-r-drawable-using-variable
     * @param context the context
     * @param name the name of the drawable
     * @return the int of the drawable
     */
    public int getDrawable(Context context, String name)
    {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);

        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }

    /**
     * playSound
     * @param sound the name of the sound to play
     */
    public void playSound(String sound){
        MediaPlayer mediaPlayer;
        switch (sound) {
            case "success" :
                mediaPlayer = MediaPlayer.create(mContext, R.raw.success);
                mediaPlayer.start();
                break;
            case "failure" :
                mediaPlayer = MediaPlayer.create(mContext, R.raw.failure);
                mediaPlayer.start();
                break;
            case "time_up" :
                mediaPlayer = MediaPlayer.create(mContext, R.raw.failure);
                mediaPlayer.start();
                break;
            case "select":
                mediaPlayer = MediaPlayer.create(mContext, R.raw.select);
                mediaPlayer.start();
                break;
            case "tick_normal":
                mediaPlayer = MediaPlayer.create(mContext, R.raw.tick_normal);
                mediaPlayer.start();
                break;
        }

    }
}
