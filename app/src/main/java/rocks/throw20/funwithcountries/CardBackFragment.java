package rocks.throw20.funwithcountries;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rocks.throw20.funwithcountries.Data.Contract;

/**
 * Created by joselopez on 7/21/16.
 */
public class CardBackFragment extends android.app.Fragment {

    private static final String LOG_TAG = CardFrontFragment.class.getSimpleName();
    private static CountDownTimer questionTimer;
    private View rootView;
    private SharedPreferences sharedPref;
    private boolean questionTimerIsRunning = false;
    private boolean timeUp;


    private TextView questionView;
    private TextView gameScoreView;
    private TextView questionCountryView;
    private TextView gameProgressView;

    private LinearLayout confirmAnswerView;
    private TextView confirmAnswerTextView;
    private Button confirmAnswerButtonView;

    private LinearLayout answerResultView;
    private TextView answerResultDisplay;
    private TextView answerResultCorrectAnswer;

    private LinearLayout nextQuestionView;
    private Button nextQuestionButtonView;

    private LinearLayout viewScoresView;
    private Button viewScoresButtonView;

    private String countryName;
    private String countryCapital;
    private String countryAlpha2Code;
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


    public CardBackFragment() {
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
        Log.e(LOG_TAG, "onCreate " + true);
        if (savedInstanceState == null) {
            getArguments().putString("savedInstanceState", null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_card_back, container, false);

        // Header views
        questionView = (TextView) rootView.findViewById(R.id.question);
        gameScoreView = (TextView) rootView.findViewById(R.id.game_score);
        questionCountryView = (TextView) rootView.findViewById(R.id.question_country);
        gameProgressView = (TextView) rootView.findViewById(R.id.game_progress);

        // Game button views
        choice1View = (Button) rootView.findViewById(R.id.game_button_text1);
        choice2View = (Button) rootView.findViewById(R.id.game_button_text2);
        choice3View = (Button) rootView.findViewById(R.id.game_button_text3);
        choice4View = (Button) rootView.findViewById(R.id.game_button_text4);

        // Answer selection confirmation views
        confirmAnswerView = (LinearLayout) rootView.findViewById(R.id.game_answer_confirmation_view);
        confirmAnswerTextView = (TextView) rootView.findViewById(R.id.game_answer_confirmation_text);
        confirmAnswerButtonView = (Button) rootView.findViewById(R.id.game_answer_confirmation_submit);

        // Answer result views (correct or incorrect)
        answerResultView = (LinearLayout) rootView.findViewById(R.id.game_answer_result_view);
        answerResultDisplay = (TextView) rootView.findViewById(R.id.game_answer_result_display);
        answerResultCorrectAnswer = (TextView) rootView.findViewById(R.id.game_answer_result_correct_answer);

        // Next question view
        nextQuestionView = (LinearLayout) rootView.findViewById(R.id.game_next_question_view);
        nextQuestionButtonView = (Button) rootView.findViewById(R.id.game_next_question_button);

        viewScoresView = (LinearLayout) rootView.findViewById(R.id.game_view_scores_view);
        viewScoresButtonView = (Button) rootView.findViewById(R.id.game_view_scores_button);
        answerQuestionView();


        return rootView;

    }

    /**
     * selectedAnswerView
     * This method builds the confirmation answer text, and button to submit the asnwer
     */
    private void answerQuestionView() {

        // Remove the confirmation text and the confirmation button
        //confirmAnswerView.setVisibility(View.GONE);
        // Show the answer's result (correct or incorrect)
        answerResultView.setVisibility(View.VISIBLE);
        // Show the next question views
        nextQuestionView.setVisibility(View.VISIBLE);


        Utilities util = new Utilities(getContext());
        //Log.e(LOG_TAG, "answerQuestionView " + true);
        String resultText = getArguments().getString("answer_result");
        String resultTextDescription = getArguments().getString("answer_result_display");
        String currentAnswer = getArguments().getString("current_answer", "");
        String gameMode = sharedPref.getString("game_mode", "");
        int gameProgress = sharedPref.getInt("game_progress", 0);
        int gameProgressMax = sharedPref.getInt("game_progress_max", 0);
        Log.e(LOG_TAG, "game progress " + gameProgress);
        Log.e(LOG_TAG, "game max " + gameProgressMax);

        //Log.e(LOG_TAG, "answerResultView " + answerResultView);
        //------------------------------------------------------------------------------------------
        // answerResultView
        //------------------------------------------------------------------------------------------
        if (resultText != null && resultText.equals("Incorrect")) {
            //Log.e(LOG_TAG, "result text " + resultText);
            answerResultDisplay.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.incorrectBackground));
            answerResultDisplay.setTextColor(ContextCompat.getColor(getActivity(), R.color.incorrectText));
        } else if (resultText != null && resultText.equals("Time up!")) {
            //Log.e(LOG_TAG, "result text " + resultText);
            answerResultDisplay.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.timeUpBackground));
            answerResultDisplay.setTextColor(ContextCompat.getColor(getActivity(), R.color.timeUpText));
        } else if (resultText != null && resultText.equals("Correct")) {
            //Log.e(LOG_TAG, "result text " + resultText);
            answerResultDisplay.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.correctBackground));
            answerResultDisplay.setTextColor(ContextCompat.getColor(getActivity(), R.color.correctText));
        }
        answerResultDisplay.setText(resultText);
        //------------------------------------------------------------------------------------------
        // nextQuestionTextView
        //------------------------------------------------------------------------------------------

        if (gameMode.equals("capitals")) {
            answerResultCorrectAnswer.setText(resultTextDescription);
        } /* else if (gameMode.equals("flags")) {

                nextQuestionTextView.setText(resultTextDescription);
                answerFlag = new ImageView(getActivity());
                //Log.e(LOG_TAG,"Next/answer " + answer);
                // Display the correct flag
                int flagDrawable = util.getDrawable(getContext(), "flag_" + currentAnswer.toLowerCase());
                Picasso.with(getContext()).load(flagDrawable)
                        .resize(220, 140)
                        .onlyScaleDown()
                        .into(answerFlag);
                gameAnswer.addView(answerFlag);
            }
        }*/
        //------------------------------------------------------------------------------------------
        // The game is over, show the "View Score" button instead of the "Next Question" button
        //------------------------------------------------------------------------------------------
        if (gameProgressMax == gameProgress - 1) {
            nextQuestionView.setVisibility(View.GONE);
            viewScoresView.setVisibility(View.VISIBLE);
            viewScoresButtonView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e(LOG_TAG, "endGame " + "lel");
                    endGame();
                }
            });
        }

        //------------------------------------------------------------------------------------------
        // Game in progress, show the "Next Question" button
        //------------------------------------------------------------------------------------------
        else {
            //------------------------------------------------------------------------------------------
            // nextQuestionButtonView: Set on ClickListeners
            //------------------------------------------------------------------------------------------
            nextQuestionButtonView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    nextButtonOnClickListener();
                }
            });
            nextQuestionButtonView.setText(R.string.action_next_question);
            // Display the Score
            int correctAnswers = sharedPref.getInt("correct_answers", 0);
            String gameScoreText = "Score: " + correctAnswers;
            gameScoreView.setText(gameScoreText);
        }
    }

    private void nextButtonOnClickListener() {
        String gameMode = sharedPref.getString("game_mode", "");
        //answerResultView.setVisibility(View.GONE);
        //nextQuestionView.setVisibility(View.GONE);
        getArguments().clear();
        ((GameActivity)getActivity()).flipCard(getArguments());
        //getQuestion(true);
        // Set the question views
        //setQuestionViews();
    }

    /**
     * submitScore
     * This method stores the score to the database
     * It's called when the game ends
     */
    private void submitScore() {
        // Get the score data to be stored in the database
        DateFormat df = new SimpleDateFormat("M/d/yy", Locale.US);
        String scoreDate = df.format(new Date());
        String scoreGameMode = sharedPref.getString("game_mode", "");
        int scoreQuestionsCount = sharedPref.getInt("game_progress_max", 0);
        int scoreCorrectAnswers = sharedPref.getInt("correct_answers", 0);

        // Calculate the score percent
        double num = (double) scoreCorrectAnswers / scoreQuestionsCount;
        NumberFormat defaultFormat = NumberFormat.getPercentInstance();
        defaultFormat.setMinimumFractionDigits(0);
        String scorePercent = defaultFormat.format(num);

        // Calculate the final score
        int scoreFinalScore = (scoreQuestionsCount * 20) + (scoreCorrectAnswers * 5);


        String scoreGameDuration = "";
        // Create a content values object
        ContentValues scoreValues = new ContentValues();
        scoreValues.put(Contract.ScoreEntry.scoreDate, scoreDate);
        scoreValues.put(Contract.ScoreEntry.scoreGameMode, scoreGameMode);
        scoreValues.put(Contract.ScoreEntry.scoreQuestionsCount, scoreQuestionsCount);
        scoreValues.put(Contract.ScoreEntry.scoreCorrectAnswers, scoreCorrectAnswers);
        scoreValues.put(Contract.ScoreEntry.scoreScorePercent, scorePercent);
        scoreValues.put(Contract.ScoreEntry.scoreFinalScore, scoreFinalScore);
        scoreValues.put(Contract.ScoreEntry.scoreGameDuration, scoreGameDuration);
        // Insert the score record
        getContext().getContentResolver().insert(
                Contract.ScoreEntry.CONTENT_URI,
                scoreValues
        );
    }

    /**
     * endGame
     * Method to end the game, submit the scores, and start the scores activity
     */
    private void endGame(){
        // Launch the ScoresActivity and end the GameActivity
        final Intent intent = new Intent(getContext(), ScoresActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

}