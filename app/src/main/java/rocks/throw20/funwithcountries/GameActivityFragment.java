package rocks.throw20.funwithcountries;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * GameActivityFragment
 * Where the game runs under the various modes
 */
public class GameActivityFragment extends Fragment {
    private static final String LOG_TAG = GameActivityFragment.class.getSimpleName();
    private View rootView;
    private SharedPreferences sharedPref;
    private LinearLayout gameHeader;

    private String countryName;
    private String countryCapital;
    private String question;
    private String answer;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private Button choice1View;
    private Button choice2View;
    private Button choice3View;
    private Button choice4View;

    public GameActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Log.e(LOG_TAG, "onCreate " + true);
        if (savedInstanceState == null) {
            //Log.e(LOG_TAG, "onCreate " + true);
            getArguments().putString("savedInstanceState", null);
            getQuestion(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "onCreateView " + true);
        rootView = inflater.inflate(R.layout.fragment_game_question, container, false);
        choice1View = (Button) rootView.findViewById(R.id.game_button_text1);
        choice2View = (Button) rootView.findViewById(R.id.game_button_text2);
        choice3View = (Button) rootView.findViewById(R.id.game_button_text3);
        choice4View = (Button) rootView.findViewById(R.id.game_button_text4);
        setLayoutHeader();
        setQuestionViews();
        return rootView;
    }

    /**
     * setLayoutHeader
     * Set the top views that display the answer, the timer, and the score
     */
    private void setLayoutHeader() {
        gameHeader = (LinearLayout) rootView.findViewById(R.id.game_header);

        int gameProgress = sharedPref.getInt("game_progress", 0);
        int gameProgressMax = sharedPref.getInt("game_progress_max", 0);
        int correctAnswers = sharedPref.getInt("correct_answers", 0);

        String gameProgressText = "Question " + gameProgress + " of " + gameProgressMax;
        String gameScoreText = "Score: " + correctAnswers;
        //question = getArguments().getString("question");
        //countryName = getArguments().getString("country_name");
        //------------------------------------------------------------------------------------------
        // Set the header views
        //------------------------------------------------------------------------------------------
        TextView questionView = (TextView) rootView.findViewById(R.id.question);
        TextView gameScoreView = (TextView) rootView.findViewById(R.id.game_score);
        TextView questionCountryView = (TextView) rootView.findViewById(R.id.question_country);
        TextView gameProgressView = (TextView) rootView.findViewById(R.id.game_progress);

        gameProgressView.setText(gameProgressText);
        //questionView.setText(question);
        gameScoreView.setText(gameScoreText);
        //String countryNameDisplay = countryName + "?";
        //questionCountryView.setText(countryNameDisplay);
    }

    /**
     * getQuestion
     * @param isnew wether the question is new, or it's being restored from savedInstanceState
     */
    private void getQuestion(Boolean isnew) {
        Bundle b = getArguments();
        b.putString("sequence", "getQuestion");
        String gameMode = sharedPref.getString("game_mode", "");
        if (isnew) {
            SharedPreferences.Editor editor = sharedPref.edit();
            ContentValues contentValues = newQuestion(gameMode);
            // Set the question variables
            countryName = contentValues.getAsString("country_name");
            countryCapital = contentValues.getAsString("country_capital");
            String countryAlpha2Code = contentValues.getAsString("country_alpha2Code");
            //Log.e(LOG_TAG,"alphaCode " + countryAlpha2Code);
            question = contentValues.getAsString("question");
            answer = contentValues.getAsString("answer");
            choice1 = contentValues.getAsString("choice1");
            choice2 = contentValues.getAsString("choice2");
            choice3 = contentValues.getAsString("choice3");
            choice4 = contentValues.getAsString("choice4");

            // Keep track of countries used during the game session
            String usedCountries = sharedPref.getString("used_countries", "");
            //Log.e(LOG_TAG, "usedCountries: " + usedCountries);
            String usedCountriesSelection;
            if (usedCountries.isEmpty()) {
                usedCountriesSelection = "'" + countryName + "'";
            } else {
                usedCountriesSelection = usedCountries + " OR '" + countryName + "'";
            }

            // Store them in the bundle
            b.putString("country_name", countryName);
            b.putString("country_capital", countryCapital);
            if ( gameMode.equals("capitals")){
                b.putString("current_answer", countryCapital);
            }else if ( gameMode.equals("flags")){
                b.putString("current_answer", countryAlpha2Code);
            }
            b.putString("question", question);
            b.putString("answer", answer);
            b.putString("choice1", choice1);
            b.putString("choice2", choice2);
            b.putString("choice3", choice3);
            b.putString("choice4", choice4);
            editor.putString("used_countries", usedCountriesSelection);
            editor.apply();
            //Log.e(LOG_TAG, "used_countries: " + usedCountriesSelection);
        }
    }

    /**
     * newQuestion
     * This method creates a new Question object and returns the ContentValues from it
     * It also creates a CountDownTimer to drive the timer and its display on the question
     * @return a ContentValues Object with the question, answer, and choices.
     */
    private ContentValues newQuestion(String gameMode){
        String usedCountries = sharedPref.getString("used_countries", "");
        Question questionObj = new Question(this.getContext());
        //Log.e(LOG_TAG,"gameMode: " + gameMode);
        ContentValues contentValues = questionObj.getQuestion(gameMode,new String[]{usedCountries});
        // If a new question is requested and there is a timer running, cancel it first
        /*if ( questionTimerIsRunning ) {
            questionTimer.cancel();
            questionTimerIsRunning= false;
            getArguments().putBoolean("timer_is_running",false);
        }*/
        return  contentValues;
    }

    /**
     * setQuestionVies
     * This method sets the Views when starting the game and when getting new questions
     */
    private void setQuestionViews() {
        Utilities util = new Utilities(getContext());
        setLayoutHeader();
        Bundle b = getArguments();
        String gameMode = sharedPref.getString("game_mode", "");
        //------------------------------------------------------------------------------------------
        // Get all the variables from the shared prefs and from the bundle
        countryCapital = b.getString("country_capital");
        answer = b.getString("answer");
        choice1 = b.getString("choice1");
        choice2 = b.getString("choice2");
        choice3 = b.getString("choice3");
        choice4 = b.getString("choice4");
        //questionTimerIsRunning = b.getBoolean("timer_is_running");
        int questionTimerProgress = b.getInt("timer_progress");

        if (gameMode.equals("capitals")) {
            choice1View.setText(choice1);
            choice1View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence countryCapital = choice3View.getText();
                    //selectAnswer(countryCapital.toString());
                }
            });
            choice2View.setText(choice2);
            choice2View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence countryCapital = choice3View.getText();
                    //selectAnswer(countryCapital.toString());
                }
            });
            choice3View.setText(choice3);
            choice3View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence countryCapital = choice3View.getText();
                    //selectAnswer(countryCapital.toString());
                }
            });
            choice4View.setText(choice4);
            choice4View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence countryCapital = choice4View.getText();
                    //selectAnswer(countryCapital.toString());
                }
            });
        }
    }

}
