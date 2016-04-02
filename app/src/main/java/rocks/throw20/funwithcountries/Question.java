package rocks.throw20.funwithcountries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import rocks.throw20.funwithcountries.Data.Contract;

/**
 * Created by josel on 3/31/2016.
 */
public class Question {
    private static final String LOG_TAG = Question.class.getSimpleName();
    private final Context mContext;
    private Cursor mCursor;
    private ContentValues contentValues = new ContentValues();

    public Question(Context context){
        this.mContext = context;

    }

    /**
     * getQuestion
     * This method builds a question using the database
     * @return contentValues
     */
    public ContentValues getQuestion(String gameMode){
        Utilities util = new Utilities(mContext);
        // Get all the countries in a cursor
        mCursor = util.getAllCountries();
        int exclude[] = new int[]{0,0,0};

        if ( mCursor != null) {

            int cursorSize = mCursor.getCount();
            Log.e(LOG_TAG, "cursor size " + cursorSize);

            //--------------------------------------------------------------------------------------
            // Get the question and the correct answer
            //--------------------------------------------------------------------------------------

            mCursor.moveToFirst();
            int randomInt1 = util.getRandomInt(cursorSize,null);
            Log.e(LOG_TAG, "randomInt1 " + randomInt1);
            exclude[0] = randomInt1;
            Log.e(LOG_TAG, "exclude[0] " + exclude[0]);
            mCursor.move(randomInt1);

            String countryName = mCursor.getString(Contract.CountryEntry.indexCountryName);
            String countryCapital = mCursor.getString(Contract.CountryEntry.indexCountryCapital);



            String questionText = "What is the capital of " + countryName + "?";
            String answerText = "The the capital of " + countryName + " is " + countryCapital;

            contentValues.put("country_name", countryName);
            contentValues.put("question", questionText);
            contentValues.put("answer", answerText);

            // Build the multiple choices, include the correct answer
            contentValues.put("choice_1", countryCapital);

            int randomInt2 = util.getRandomInt(cursorSize,exclude);
            Log.e(LOG_TAG, "randomInt2 " + randomInt2);
            mCursor.moveToFirst();
            exclude[1] = randomInt2;
            Log.e(LOG_TAG, "exclude[1] " + exclude[1]);
            mCursor.move(randomInt2);
            contentValues.put("choice_2", mCursor.getString(Contract.CountryEntry.indexCountryCapital));

            int randomInt3 = util.getRandomInt(cursorSize,exclude);
            Log.e(LOG_TAG, "randomInt3 " + randomInt3);
            mCursor.moveToFirst();
            exclude[2] = randomInt3;
            mCursor.move(randomInt3);
            contentValues.put("choice_3", mCursor.getString(Contract.CountryEntry.indexCountryCapital));

            int randomInt4 = util.getRandomInt(cursorSize,exclude);
            mCursor.moveToFirst();
            Log.e(LOG_TAG, "randomInt4 " + randomInt4);
            mCursor.move(randomInt4);
            Log.e(LOG_TAG, "exclude[2] " + exclude[2]);
            contentValues.put("choice_4", mCursor.getString(Contract.CountryEntry.indexCountryCapital));



        }
        return contentValues;

    }

    /**
     * buildQuestions
     * This method will build the question and answers based on the game mode
     * @param gameMode the game mode (capitals, flags, subregions, randomize)
     * @return a set of ContentValues with the question/answer data
     */

    private ContentValues buildQuestion(String gameMode ){
        return null;
    }

}
