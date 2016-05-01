package rocks.throw20.funwithcountries.Data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by josel on 3/10/2016.
 */
public class Provider extends ContentProvider {
    private static final String LOG_TAG = "ContentProvider";
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper mOpenHelper;

    private static final int COUNTRIES = 100;
    private static final int COUNTRY_NAME = 101;
    private static final int SCORES = 102;


    // Declare the query builders
    private static final SQLiteQueryBuilder sCountriesQueryBuilder;
    private static final SQLiteQueryBuilder sScoresQueryBuilder;

    // Set the tables for each query builder
    static {
        sCountriesQueryBuilder = new SQLiteQueryBuilder();
        sCountriesQueryBuilder.setTables(
                Contract.CountryEntry.COUNTRIES_TABLE_NAME
        );
        sScoresQueryBuilder = new SQLiteQueryBuilder();
        sScoresQueryBuilder.setTables(
                Contract.ScoreEntry.SCORES_TABLE_NAME
        );

    }

    //sCountryByID build the uri to get a country by id
    private static final String sCountryByName =
            Contract.CountryEntry.COUNTRIES_TABLE_NAME +
                    "." + Contract.CountryEntry.countryName + " = ? ";


    /**
     * getCountriesByID
     * @param uri the country uri
     * @param projection the fields requested
     * @param sortOrder the order by parameter
     * @return the countries cursor
     */
    private Cursor getCountryByName(Uri uri, String[] projection, String sortOrder) {
        Cursor returnCursor;

        String countrySelection = sCountryByName;
        //---------------------------------------------------------
        // Countries Cursor
        //---------------------------------------------------------
        String name = Contract.CountryEntry.getCountryNameFromUri(uri);
        String[] countriesSelectionArgs = {
                name
        };
        Cursor countriesCursor = sCountriesQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                countrySelection,
                countriesSelectionArgs,
                null,
                null,
                sortOrder + " DESC"
        );
        countriesCursor.moveToFirst();

        returnCursor = countriesCursor;
        return returnCursor;
    }


    /**
     * buildUriMatcher
     * @return the uri matching the request
     */
    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, Contract.PATH_COUNTRIES, COUNTRIES);
        matcher.addURI(authority, Contract.PATH_COUNTRY_NAME + "/country/*", COUNTRY_NAME);
        matcher.addURI(authority, Contract.PATH_SCORES, SCORES);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        //Log.e(LOG_TAG, "onCreate -> " + true);
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        //Log.e(LOG_TAG, "getType -> " + uri);
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);
        //Log.e(LOG_TAG, "getType -> " + true);
        switch (match) {
            // Student: Uncomment and fill out these two cases
            case COUNTRIES:
                return Contract.CountryEntry.CONTENT_TYPE;
            case COUNTRY_NAME:
                return Contract.CountryEntry.CONTENT_ITEM_TYPE;
            case SCORES:
                return Contract.ScoreEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        //Log.e(LOG_TAG, "query -> " + uri);

        switch (sUriMatcher.match(uri)) {

            case COUNTRIES:
            {
                //Log.e(LOG_TAG, "COUNTRIES -> " + true);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        Contract.CountryEntry.COUNTRIES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                //Log.e(LOG_TAG, "retCursor.getCount() -> " + retCursor.getCount());
                break;
            }
            case COUNTRY_NAME:
            {
                //Log.e(LOG_TAG, "query -> " + uri);
                retCursor = getCountryByName(uri, projection, sortOrder);
                break;
            }
            case SCORES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        Contract.ScoreEntry.SCORES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            default:
                //Log.e(LOG_TAG, "unknown uri -> " + uri);
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // This causes the cursor to register a content observer
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case COUNTRIES:
            {
                //Log.e(LOG_TAG, "Insert -> " + values.getAsString("id"));
                long _id = db.insert(Contract.CountryEntry.COUNTRIES_TABLE_NAME, null, values);
                if (_id > 0) returnUri = Contract.CountryEntry.buildCountriesUri(_id);
                else throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SCORES:
                long _id = db.insert(Contract.ScoreEntry.SCORES_TABLE_NAME, null, values);
                if (_id > 0) returnUri = Contract.ScoreEntry.buildScoresUri(_id);
                else throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case COUNTRIES:
                rowsDeleted = db.delete(
                        Contract.CountryEntry.COUNTRIES_TABLE_NAME, selection, selectionArgs);
                break;
            case SCORES:
                rowsDeleted = db.delete(
                        Contract.ScoreEntry.SCORES_TABLE_NAME, selection, selectionArgs);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }



    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case COUNTRIES:
                //Log.e(LOG_TAG, "updateCountries " + true);
                rowsUpdated = db.update(Contract.CountryEntry.COUNTRIES_TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case SCORES:
                rowsUpdated = db.update(Contract.ScoreEntry.SCORES_TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COUNTRIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value: values) {
                        Log.e(LOG_TAG, "Lets bulk!");
                        long _id = db.insert(Contract.CountryEntry.COUNTRIES_TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}