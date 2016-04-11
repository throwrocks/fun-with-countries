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
        // Create a Utilities Object so we can run the randomInt and shuffleArray utility methods.
        Utilities util = new Utilities(mContext);
        // Get all the countries in a cursor
        mCursor = util.getAllCountriesWithCapitals();
        // The exclude array is used to store the position of the country records that have already
        // been used. It's passed to the getRandomInt method to make sure it doesn't return a
        // repeated int, and therefore we end up repeating countries for the choices
        int exclude[] = new int[3];
        // The choices variable is used to store the multiple choices. It's then passed to the
        // shuffleArray function so the choices are displayed in random order on our views.
        String choices[] = new String[4];
        // Only run if the cursor is not null
        if ( mCursor != null) {
            int cursorSize = mCursor.getCount();
            //Log.e(LOG_TAG, "cursor size " + cursorSize);

            // Get the first country. Our question and answer will be based on it.
            mCursor.moveToFirst();
            int randomInt1 = util.getRandomInt(cursorSize, null);
            mCursor.move(randomInt1);
            String countryName = mCursor.getString(Contract.CountryEntry.indexCountryName);
            String countryCapital = mCursor.getString(Contract.CountryEntry.indexCountryCapital);

            String questionText = "What is the capital of ";
            String answerText = "The capital of " + countryName + " is " + countryCapital;

            exclude[0] = randomInt1;

            // Build the multiple choices
            // Choice1
            choices[0] = mCursor.getString(Contract.CountryEntry.indexCountryCapital);

            // Choice2
            int randomInt2 = util.getRandomInt(cursorSize, exclude);
            mCursor.moveToFirst();
            mCursor.move(randomInt2);
            choices[1]= mCursor.getString(Contract.CountryEntry.indexCountryCapital);
            exclude[1] = randomInt2;

            // Choice3
            int randomInt3 = util.getRandomInt(cursorSize, exclude);
            mCursor.moveToFirst();
            mCursor.move(randomInt3);
            choices[2] = mCursor.getString(Contract.CountryEntry.indexCountryCapital);
            exclude[2] = randomInt3;

            // Choice4
            int randomInt4 = util.getRandomInt(cursorSize, exclude);
            mCursor.moveToFirst();
            mCursor.move(randomInt4);
            choices[3] = mCursor.getString(Contract.CountryEntry.indexCountryCapital);


            // Shuffle the choices
            util.shuffleArray(choices);

            // Store the values and return them
            contentValues.put("country_name", countryName);
            contentValues.put("country_capital", countryCapital);
            contentValues.put("question", questionText);
            contentValues.put("answer", answerText);

            contentValues.put("choice1", choices[0]);
            contentValues.put("choice2", choices[1]);
            contentValues.put("choice4", choices[2]);
            contentValues.put("choice3", choices[3]);

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
