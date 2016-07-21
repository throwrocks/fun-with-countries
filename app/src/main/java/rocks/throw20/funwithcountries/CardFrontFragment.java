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

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rocks.throw20.funwithcountries.Data.Contract;

/**
 * Created by joselopez on 7/21/16.
 */
public class CardFrontFragment extends Fragment {
    public CardFrontFragment() {
    }

    private static final String LOG_TAG = GameActivityFragment.class.getSimpleName();
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
            getArguments().putString("savedInstanceState", null);
            getQuestion(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "onCreateView " + true);
        rootView = inflater.inflate(R.layout.fragment_card_front, container, false);

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

        viewScoresView.setVisibility(View.GONE);

        setLayoutHeader();
        setQuestionViews();
        return rootView;
    }


    /**
     * setLayoutHeader
     * Set the top views that display the answer, the timer, and the score
     */
    private void setLayoutHeader() {

        //------------------------------------------------------------------------------------------
        // Get header data from the shared preferences
        //------------------------------------------------------------------------------------------
        int gameProgress = sharedPref.getInt("game_progress", 0);
        int gameProgressMax = sharedPref.getInt("game_progress_max", 0);
        int correctAnswers = sharedPref.getInt("correct_answers", 0);
        //------------------------------------------------------------------------------------------
        // Build the header data
        //------------------------------------------------------------------------------------------
        String gameProgressText = "Question " + gameProgress + " of " + gameProgressMax;
        String gameScoreText = "Score: " + correctAnswers;
        question = getArguments().getString("question");
        countryName = getArguments().getString("country_name");
        //------------------------------------------------------------------------------------------
        // Set the header views
        //------------------------------------------------------------------------------------------
        gameProgressView.setText(gameProgressText);
        questionView.setText(question);
        gameScoreView.setText(gameScoreText);
        String countryNameDisplay = countryName + "?";
        questionCountryView.setText(countryNameDisplay);
    }

    /**
     * getQuestion
     *
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
            countryAlpha2Code = contentValues.getAsString("country_alpha2Code");
            question = contentValues.getAsString("question");
            answer = contentValues.getAsString("answer");
            choice1 = contentValues.getAsString("choice1");
            choice2 = contentValues.getAsString("choice2");
            choice3 = contentValues.getAsString("choice3");
            choice4 = contentValues.getAsString("choice4");

            // Keep track of countries used during the game session
            String usedCountries = sharedPref.getString("used_countries", "");
            String usedCountriesSelection;
            if (usedCountries.isEmpty()) {
                usedCountriesSelection = "'" + countryName + "'";
            } else {
                usedCountriesSelection = usedCountries + " OR '" + countryName + "'";
            }

            // Store them in the bundle
            b.putString("country_name", countryName);
            b.putString("country_capital", countryCapital);
            if (gameMode.equals("capitals")) {
                b.putString("current_answer", countryCapital);
            } else if (gameMode.equals("flags")) {
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
     *
     * @return a ContentValues Object with the question, answer, and choices.
     */
    private ContentValues newQuestion(String gameMode) {
        String usedCountries = sharedPref.getString("used_countries", "");
        Question questionObj = new Question(this.getContext());
        //Log.e(LOG_TAG,"gameMode: " + gameMode);
        ContentValues contentValues = questionObj.getQuestion(gameMode, new String[]{usedCountries});
        // If a new question is requested and there is a timer running, cancel it first
        if (questionTimerIsRunning) {
            questionTimer.cancel();
            questionTimerIsRunning = false;
            getArguments().putBoolean("timer_is_running", false);
        }
        return contentValues;
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
        //------------------------------------------------------------------------------------------
        countryCapital = b.getString("country_capital");
        answer = b.getString("answer");
        choice1 = b.getString("choice1");
        choice2 = b.getString("choice2");
        choice3 = b.getString("choice3");
        choice4 = b.getString("choice4");
        questionTimerIsRunning = b.getBoolean("timer_is_running");
        int questionTimerProgress = b.getInt("timer_progress");

        if (gameMode.equals("capitals")) {
            choice1View.setText(choice1);
            choice1View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence countryCapital = choice1View.getText();
                    selectAnswer(countryCapital.toString());
                }
            });
            choice2View.setText(choice2);
            choice2View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence countryCapital = choice2View.getText();
                    selectAnswer(countryCapital.toString());
                }
            });
            choice3View.setText(choice3);
            choice3View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence countryCapital = choice3View.getText();
                    selectAnswer(countryCapital.toString());
                }
            });
            choice4View.setText(choice4);
            choice4View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence countryCapital = choice4View.getText();
                    selectAnswer(countryCapital.toString());
                }
            });
        }

        // Enable the buttons
        choice1View.setEnabled(true);
        choice2View.setEnabled(true);
        choice3View.setEnabled(true);
        choice4View.setEnabled(true);

        // Confirm answer view
        confirmAnswerView.setVisibility(View.GONE);

        // Answer views (correct or incorrect)
        answerResultView.setVisibility(View.GONE);

        // Next question view
        nextQuestionView.setVisibility(View.GONE);

        final DonutProgress gameTimerView = (DonutProgress) rootView.findViewById(R.id.game_timer);
        //------------------------------------------------------------------------------------------
        // Create a new timer for this question
        //------------------------------------------------------------------------------------------
        int startTimer = 11000;
        // If a timer is running, resume it
        if (questionTimerIsRunning) {
            startTimer = questionTimerProgress * 1000;
        }
        questionTimer = new CountDownTimer(startTimer, 1000) {
            // Count down the timer on every tick
            public void onTick(long millisUntilFinished) {
                Utilities util = new Utilities(getContext());
                gameTimerView.setInnerBottomText("");
                questionTimerIsRunning = true;
                timeUp = false;
                getArguments().putBoolean("timer_is_running", true);
                int progress = (int) (long) (millisUntilFinished / 1000);
                if (progress <= 10) {

                    util.playSound("tick_normal");
                    getArguments().putInt("timer_progress", progress);
                    //Log.e(LOG_TAG, "progress " + progress);
                    gameTimerView.setProgress(progress);
                }
            }

            // When the timer finishes, mark the question as wrong and end the question
            public void onFinish() {
                questionTimerIsRunning = false;
                timeUp = true;
                getArguments().putBoolean("timer_is_running", false);
                getArguments().putInt("timer_progress", 0);
                gameTimerView.setProgress(0);
                gameTimerView.setInnerBottomTextSize(36);
                // Time is up, clear any selected answers and answer the question (incorrect)
                getArguments().putString("selected_answer", "");
                // Select and answer
                selectAnswer("");
                answerQuestion();
            }
        }.start();

    }

    /**
     * selectAnswer
     * This method is called when clicking on a choice button
     * It sets the views that confirm your answer and allow you to submit
     *
     * @param answer the answer text
     */
    private void selectAnswer(String answer) {
        getArguments().putString("selected_answer", answer);
        getArguments().putString("sequence", "selectAnswer");
        selectedAnswerView();
    }

    /**
     * selectedAnwswerView
     * This method builds the confirmation answer text, and button to submit the asnwer
     */
    private void selectedAnswerView() {

        String gameMode = sharedPref.getString("game_mode", "");
        String answer = getArguments().getString("selected_answer");

        // Display the confirmation text
        confirmAnswerView.setVisibility(View.VISIBLE);

        String answerDisplay;
        if (gameMode.equals("capitals")) {
            answerDisplay = "The capital of " + countryName + " is " + answer + "?";
            confirmAnswerTextView.setText(answerDisplay);
        }

        confirmAnswerButtonView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                answerQuestion();
            }
        });
    }

    /**
     * answerQuestions
     * This method is called when the choice to a question is confirmed
     */
    private void answerQuestion() {
        Utilities util = new Utilities(getContext());



        String gameMode = sharedPref.getString("game_mode", "");
        getArguments().putString("sequence", "answerQuestion");
        SharedPreferences.Editor editor = sharedPref.edit();
        // Cancel the timer
        questionTimer.cancel();
        questionTimerIsRunning = false;
        getArguments().putBoolean("timer_is_running", false);
        // Get the current and selected answers to see if they match (evaluated answer)
        String selectedAnswer = getArguments().getString("selected_answer", "");
        String currentAnswer = getArguments().getString("current_answer", "");
        // Evaluate the answer
        Boolean test = selectedAnswer.equals(currentAnswer);
        getArguments().putBoolean("evaluated_answer", test);
        // Evaluated answer text for keeping track of the score and displaying the result
        int gameCorrectAnswers = sharedPref.getInt("correct_answers", 0);
        int gameIncorrectAnswers = sharedPref.getInt("incorrect_answers", 0);
        CharSequence questionResultText;
        // The answer was correct
        if (test) {
            gameCorrectAnswers = gameCorrectAnswers + 1;
            questionResultText = "Correct";
            util.playSound("success");
        }
        // The time is up
        else if (timeUp) {
            gameIncorrectAnswers = gameIncorrectAnswers + 1;
            questionResultText = "Time up!";
        }
        // The answer was incorrect
        else {
            gameIncorrectAnswers = gameIncorrectAnswers + 1;
            questionResultText = "Incorrect";
            util.playSound("failure");
        }
        // Save the score
        editor.putInt("correct_answers", gameCorrectAnswers);
        editor.putInt("incorrect_answers", gameIncorrectAnswers);
        // Save the game's progress
        int gameProgressMax = sharedPref.getInt("game_progress_max", 0);
        int gameProgress = sharedPref.getInt("game_progress", 0);
        int gameProgressCalc = gameProgress + 1;


        editor.putInt("game_progress", gameProgressCalc);
        editor.apply();

        getArguments().putString("answer_result", questionResultText.toString());
        getArguments().putString("answer_result_display", answer);

        //------------------------------------------------------------------------------------------
        //The game is over !!!! Submit the score
        //------------------------------------------------------------------------------------------
        gameProgress = sharedPref.getInt("game_progress", 0);
        if (gameProgressMax == gameProgress - 1) {
            Log.e(LOG_TAG, "submitScore " + true);
            submitScore();
        }

        //Log.e(LOG_TAG, "question result 1 " + questionResultText);
        //Log.e(LOG_TAG, "question result 2 " + answer);
        //Log.e(LOG_TAG, "gameMode " + gameMode);
        // Disable, slide out and remove the choice buttons
        if (gameMode.equals("capitals")) {

            choice1View.setEnabled(false);
            choice2View.setEnabled(false);
            choice3View.setEnabled(false);
            choice4View.setEnabled(false);

        }/*else if ( gameMode.equals("flags")){
            choice1ImageButtonView = (ImageButton) rootView.findViewById(R.id.choice1);
            choice2ImageButtonView = (ImageButton) rootView.findViewById(R.id.choice2);
            choice3ImageButtonView = (ImageButton) rootView.findViewById(R.id.choice3);
            choice4ImageButtonView = (ImageButton) rootView.findViewById(R.id.choice4);
            choice1ImageButtonView.setEnabled(false);
            choice2ImageButtonView.setEnabled(false);
            choice3ImageButtonView.setEnabled(false);
            choice4ImageButtonView.setEnabled(false);
            slideOutView("gameContent", choice1ImageButtonView, 360);
            slideOutView("gameContent", choice2ImageButtonView, 340);
            slideOutView("gameContent", choice3ImageButtonView, 320);
            slideOutView("gameContent", choice4ImageButtonView, 300);
        }*/


        answerQuestionView();
    }



    /**
     * selectedAnswerView
     * This method builds the confirmation answer text, and button to submit the asnwer
     */
    private void answerQuestionView() {

        // Remove the confirmation text and the confirmation button
        confirmAnswerView.setVisibility(View.GONE);
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
        answerResultView.setVisibility(View.GONE);
        nextQuestionView.setVisibility(View.GONE);
        getArguments().clear();
        getQuestion(true);
        // Set the question views
        setQuestionViews();
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