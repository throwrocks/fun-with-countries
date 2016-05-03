package rocks.throw20.funwithcountries.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by josel on 3/10/2016.
 *
 */
class DBHelper extends SQLiteOpenHelper {

    // The database version
    private static final int DATABASE_VERSION = 27;

    private static final String DATABASE_NAME = "countries.db";

    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_COUNTRIES_TABLE  =
                "CREATE TABLE " +
                        Contract.CountryEntry.COUNTRIES_TABLE_NAME + " (" +
                        Contract.CountryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        Contract.CountryEntry.countryId + " INTEGER NOT NULL, " +
                        Contract.CountryEntry.countryName + " TEXT NOT NULL, " +
                        Contract.CountryEntry.countryCapital + " TEXT NULL, " +
                        Contract.CountryEntry.countryRegion + " TEXT NULL, " +
                        Contract.CountryEntry.countrySubRegion + " TEXT NULL, " +
                        Contract.CountryEntry.countryPopulation + " INTEGER NULL, " +
                        Contract.CountryEntry.countryAlpha2Code + " TEXT NULL" +
                        ")";
        sqLiteDatabase.execSQL(SQL_CREATE_COUNTRIES_TABLE);

        final String SQL_CREATE_SCORES_TABLE  =
                "CREATE TABLE " +
                        Contract.ScoreEntry.SCORES_TABLE_NAME + " (" +
                        Contract.ScoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        Contract.ScoreEntry.scoreDate + " TEXT NOT NULL, " +
                        Contract.ScoreEntry.scoreGameMode + " TEXT NOT NULL, " +
                        Contract.ScoreEntry.scoreQuestionsCount + " INTEGER NULL, " +
                        Contract.ScoreEntry.scoreCorrectAnswers + " INTEGER NULL, " +
                        Contract.ScoreEntry.scoreIncorrectAnswers + " INTEGER NULL, " +
                        Contract.ScoreEntry.scoreScorePercent + " TEXT NULL, " +
                        Contract.ScoreEntry.scoreGameDuration + " INTEGER NULL " +
                        ")";
        sqLiteDatabase.execSQL(SQL_CREATE_SCORES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.CountryEntry.COUNTRIES_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.ScoreEntry.SCORES_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
