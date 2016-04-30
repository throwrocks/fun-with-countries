package rocks.throw20.funwithcountries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

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
    public ContentValues getQuestion(String gameMode, String[] usedCountries){
        ContentValues contentValues;
        if ( gameMode != null ) {
            contentValues = buildQuestion(gameMode,usedCountries);
        }
        else {
            contentValues = null;
        }
        return contentValues;

    }

    /**
     * buildQuestion
     * This method returns the ContentValues used in the capitals game mode
     * @return a set of ContentValues with the question/answer data
     */

    private ContentValues buildQuestion (String gameMode, String[] usedCountries){
        // Create a Utilities Object so we can run the randomInt and shuffleArray utility methods.
        Utilities util = new Utilities(mContext);
        // Get all the countries in a cursor
        Log.e(LOG_TAG, "usedCountries " + usedCountries[0]);
        if ( usedCountries[0] == null ){
            mCursor = util.getAllCountriesWithCapitals();
        } else {
            mCursor = util.getAllCountriesWithCapitalsExcept(usedCountries);
        }
        // The exclude array is used to store the position of the country records that have already
        // been used. It's passed to the getRandomInt method to make sure it doesn't return a
        // repeated int, and therefore we end up repeating countries for the choices
        int exclude[] = new int[4];
        // The choices variable is used to store the multiple choices. It's then passed to the
        // shuffleArray function so the choices are displayed in random order on our views.
        String choices[] = new String[4];
        // Only run if the cursor is not null
        if ( mCursor != null) {
            int cursorSize = mCursor.getCount();
            //Log.e(LOG_TAG, "cursor size " + cursorSize);

            // Get the first country. Our question and answer will be based on it.



            String countryName = "" ;
            String countryCapital = "" ;
            String countryAlpha2Code = "" ;
            String questionText = "";
            String answerText = "";

            // Build the multiple choices
            // Choice1
            if ( gameMode.equals("capitals")) {
                mCursor.moveToFirst();
                int randomInt1 = util.getRandomInt(cursorSize, null);
                mCursor.move(randomInt1);
                countryName = mCursor.getString(Contract.CountryEntry.indexCountryName);
                countryCapital = mCursor.getString(Contract.CountryEntry.indexCountryCapital);
                countryAlpha2Code = mCursor.getString(Contract.CountryEntry.indexAlpha2code);
                choices[0]= mCursor.getString(Contract.CountryEntry.indexCountryCapital);
                exclude[0] = randomInt1;
            }
            else if ( gameMode.equals("flags")){
                int drawableId ;
                do {   mCursor.moveToFirst();
                    int randomInt1 = util.getRandomInt(cursorSize, exclude);
                    mCursor.move(randomInt1);
                    countryName = mCursor.getString(Contract.CountryEntry.indexCountryName);
                    countryCapital = mCursor.getString(Contract.CountryEntry.indexCountryCapital);
                    countryAlpha2Code = mCursor.getString(Contract.CountryEntry.indexAlpha2code);
                    Log.e(LOG_TAG, "countryAlpha2Code " + countryAlpha2Code);
                    drawableId = util.getDrawable(mContext,"flag_" + countryAlpha2Code.toLowerCase());
                    exclude[0] = randomInt1; }
                while ( drawableId == 0 ) ;

                choices[0]= mCursor.getString(Contract.CountryEntry.indexAlpha2code);
            }


            if ( gameMode.equals("capitals")) {
                questionText = "What is the capital of ";
                answerText = "The capital of " + countryName + " is " + countryCapital;
            }
            else if ( gameMode.equals("flags")){
                questionText = "What is the flag of ";
                answerText = "The flag of " + countryName + " is ";
            }



            // Choice2
            if ( gameMode.equals("capitals")) {
                mCursor.moveToFirst();
                int randomInt2 = util.getRandomInt(cursorSize, exclude);
                mCursor.move(randomInt2);
                countryCapital = mCursor.getString(Contract.CountryEntry.indexCountryCapital);
                choices[1]= mCursor.getString(Contract.CountryEntry.indexCountryCapital);
                exclude[1] = randomInt2;
            }
            else if ( gameMode.equals("flags")){
                int drawableId ;
                do {   mCursor.moveToFirst();
                    int randomInt2 = util.getRandomInt(cursorSize, exclude);
                    mCursor.move(randomInt2);
                    countryAlpha2Code = mCursor.getString(Contract.CountryEntry.indexAlpha2code);
                    Log.e(LOG_TAG, "countryAlpha2Code " + countryAlpha2Code);
                    drawableId = util.getDrawable(mContext,"flag_" + countryAlpha2Code.toLowerCase());
                    exclude[1] = randomInt2; }
                while ( drawableId == 0 ) ;

                choices[1]= mCursor.getString(Contract.CountryEntry.indexAlpha2code);
            }


            // Choice3
            if ( gameMode.equals("capitals")) {
                mCursor.moveToFirst();
                int randomInt3 = util.getRandomInt(cursorSize, exclude);
                mCursor.move(randomInt3);
                countryCapital = mCursor.getString(Contract.CountryEntry.indexCountryCapital);
                choices[2]= mCursor.getString(Contract.CountryEntry.indexCountryCapital);
                exclude[2] = randomInt3;
            }
            else if ( gameMode.equals("flags")){
                int drawableId ;
                do {   mCursor.moveToFirst();
                    int randomInt3 = util.getRandomInt(cursorSize, exclude);
                    mCursor.move(randomInt3);
                    countryAlpha2Code = mCursor.getString(Contract.CountryEntry.indexAlpha2code);
                    Log.e(LOG_TAG, "countryAlpha2Code " + countryAlpha2Code);
                    drawableId = util.getDrawable(mContext,"flag_" + countryAlpha2Code.toLowerCase());
                    exclude[2] = randomInt3; }
                while ( drawableId == 0 ) ;

                choices[2]= mCursor.getString(Contract.CountryEntry.indexAlpha2code);
            }


            // Choice4
            if ( gameMode.equals("capitals")) {
                mCursor.moveToFirst();
                int randomInt4 = util.getRandomInt(cursorSize, exclude);
                mCursor.move(randomInt4);
                countryCapital = mCursor.getString(Contract.CountryEntry.indexCountryCapital);
                choices[3]= mCursor.getString(Contract.CountryEntry.indexCountryCapital);
                exclude[3] = randomInt4;
            }
            else if ( gameMode.equals("flags")){
                int drawableId ;
                do {   mCursor.moveToFirst();
                    int randomInt4 = util.getRandomInt(cursorSize, exclude);
                    mCursor.move(randomInt4);
                    countryAlpha2Code = mCursor.getString(Contract.CountryEntry.indexAlpha2code);
                    Log.e(LOG_TAG, "countryAlpha2Code " + countryAlpha2Code);
                    drawableId = util.getDrawable(mContext,"flag_" + countryAlpha2Code.toLowerCase());
                    exclude[3] = randomInt4; }
                while ( drawableId == 0 ) ;

                choices[3]= mCursor.getString(Contract.CountryEntry.indexAlpha2code);
            }







            // Shuffle the choices
            util.shuffleArray(choices);

            // Store the values and return them
            contentValues.put("country_name", countryName);
            contentValues.put("country_capital", countryCapital);
            contentValues.put("question", questionText);
            contentValues.put("answer", answerText);
            contentValues.put("alpha2Code",countryAlpha2Code);

            contentValues.put("choice1", choices[0]);
            contentValues.put("choice2", choices[1]);
            contentValues.put("choice4", choices[2]);
            contentValues.put("choice3", choices[3]);

        }
        return contentValues;
    }

}
