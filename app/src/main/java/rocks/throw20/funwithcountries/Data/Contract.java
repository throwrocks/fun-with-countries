package rocks.throw20.funwithcountries.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by josel on 3/10/2016.
 *
 */
public class Contract {

    // The content authority
    public static final String CONTENT_AUTHORITY = "rocks.throw20.funwithcountries";
    //The content uri for the top level authority
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // The path to the countries database
    public static final String PATH_COUNTRIES = "countries";
    // The path to the country table
    public static final String PATH_COUNTRY_NAME = "country/";

    // The path to the scores database
    public static final String PATH_SCORES = "scores";
    // The path to the country table
    public static final String PATH_SCORE_RECORD = "score/";



    /**
     * Country Entry
     * The inner class that defines the contents of the countries table
     */

    public static final class CountryEntry implements BaseColumns{
        // This is the complete path to the countries database
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_COUNTRIES)
                .build();
        // Returns multiple records
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRIES;
        // Returns a single record
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY_NAME;

        // The internal id is used by all tables
        public static final String COUNTRIES_TABLE_NAME = "countries";

        //The countries table fields
        public static final String countryId = "id";
        public static final String countryName = "name";
        public static final String countryCapital = "capital";
        public static final String countryRegion = "region";
        public static final String countrySubRegion = "subregion";
        public static final String countryPopulation = "population";
        public static final String countryAlpha2Code = "alpha2Code";

        //The countries table index
        public static final int indexCountryId = 1;
        public static final int indexCountryName = 2;
        public static final int indexCountryCapital = 3;
        public static final int indexRegion = 4;
        public static final int indexSubRegion = 5;
        public static final int indexPopulation = 6;
        public static final int indexAlpha2code = 7;

        public static Uri buildCountriesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        // Build the countries Uri
        public static Uri buildCountries() {
            return CONTENT_URI.buildUpon().build();
        }

        // Get the country name from the uri
        public static String getCountryNameFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    /**
     * ScoreEntry
     *
     */
    public static final class ScoreEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SCORES)
                .build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCORES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCORE_RECORD;

        public static final String SCORES_TABLE_NAME = "scores";

        //The countries table fields
        public static final String scoreId = "id";
        public static final String scoreDate = "date";
        public static final String scoreGameMode = "game_mode";
        public static final String scoreQuestionsCount = "questions_count";
        public static final String scoreCorrectAnswers = "correct_answers";
        public static final String scoreIncorrectAnswers = "incorrect_answers";
        public static final String scoreGameDuration = "game_duration";

        //The countries table index
        public static final int indexScoreId = 1;
        public static final int indexScoreDate = 2;
        public static final int indexGameMode = 3;
        public static final int indexQuestionsCount = 4;
        public static final int indexCorrectAnswers = 5;
        public static final int indexIncorrectAnswers = 6;
        public static final int indexGameDuration = 7;

        public static Uri buildScoresUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        // Build the countries Uri
        public static Uri buildScores() {
            return CONTENT_URI.buildUpon().build();
        }

    }
}
