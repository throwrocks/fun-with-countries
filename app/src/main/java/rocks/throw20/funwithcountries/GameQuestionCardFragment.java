package rocks.throw20.funwithcountries;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rocks.throw20.funwithcountries.Data.Contract;

/**
 * Created by joselopez on 7/21/16.
 */
public class GameQuestionCardFragment extends android.app.Fragment {
    public GameQuestionCardFragment() {
    }

    private static final String LOG_TAG = GameQuestionCardFragment.class.getSimpleName();
    private static CountDownTimer questionTimer;
    private View rootView;
    private SharedPreferences sharedPref;
    private boolean questionTimerIsRunning = false;
    private boolean timeUp;

    private TextView questionView;
    private TextView questionCountryView;
    private TextView gameProgressView;

    private LinearLayout confirmTextAnswerView;
    private LinearLayout confirmImageAnswerView;
    private TextView confirmAnswerTextQuestionTextView;
    private TextView confirmAnswerImageQuestionTextView;
    private TextView confirmAnswerTextView;
    private ImageView confirmAnswerImageView;
    private Button confirmAnswerButtonView;

    private String countryName;
    private String countryCapital;
    private String countryAlpha2Code;
    private String question;
    private String answer;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private LinearLayout gameContentText;
    private Button choice1View;
    private Button choice2View;
    private Button choice3View;
    private Button choice4View;
    private LinearLayout gameContentImage;
    private ImageButton choice1ImageButtonView;
    private ImageButton choice2ImageButtonView;
    private ImageButton choice3ImageButtonView;
    private ImageButton choice4ImageButtonView;
    // Constants for Shared Preferences
    private String PREF_GAME_PROGRESS = "game_progress";
    private String PREF_GAME_PROGRESS_MAX = "game_progress_max";
    private String PREF_USED_COUNTRIES = "used_countries";
    private String PREF_GAME_MODE = "game_mode";
    private String PREF_GAME_MODE_CAPITALS = "capitals";
    private String PREF_GAME_MODE_FLAGS = "flags";
    private String PREF_CORRECT_ANSWERS = "correct_answers";
    private String PREF_INCORRECT_ANSWERS = "incorrect_answers";
    // Constants for Bundle
    private String FIELD_COUNTRY_NAME = "country_name";
    private String FIELD_COUNTRY_CAPITAL = "country_capital";
    private String FIELD_COUNTRY_ALPHA2CODE = "country_alpha2Code";
    private String FIELD_QUESTION = "question";
    private String FIELD_ANSWER = "answer";
    private String FIELD_CHOICE1 = "choice1";
    private String FIELD_CHOICE2 = "choice2";
    private String FIELD_CHOICE3 = "choice3";
    private String FIELD_CHOICE4 = "choice4";
    private String FIELD_USED_COUNTRIES = "used_countries";
    private String FIELD_CURRENT_ANSWER = "current_answer";
    private String FIELD_EVALUATED_ANSWER = "evaluated_answer";
    private String FIELD_SELECTED_ANSWER = "selected_answer";
    private String FIELD_TIMER_IS_RUNNING = "timer_is_running";
    private String FIELD_TIMER_PROGRESS = "timer_progress";



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (savedInstanceState == null) {
            getArguments().putString("savedInstanceState", null);
            getQuestion(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_card_front, container, false);
        // Header views
        questionView = (TextView) rootView.findViewById(R.id.question);
        questionCountryView = (TextView) rootView.findViewById(R.id.question_country);
        gameProgressView = (TextView) rootView.findViewById(R.id.game_progress);
        // Game button views
        // for text answers (CAPITALS)
        gameContentText = (LinearLayout) rootView.findViewById(R.id.game_content_text);
        choice1View = (Button) rootView.findViewById(R.id.game_button_text1);
        choice2View = (Button) rootView.findViewById(R.id.game_button_text2);
        choice3View = (Button) rootView.findViewById(R.id.game_button_text3);
        choice4View = (Button) rootView.findViewById(R.id.game_button_text4);
        // for image answers (FLAGS)
        gameContentImage = (LinearLayout) rootView.findViewById(R.id.game_content_image);
        choice1ImageButtonView = (ImageButton) rootView.findViewById(R.id.game_button_image1);
        choice2ImageButtonView = (ImageButton) rootView.findViewById(R.id.game_button_image2);
        choice3ImageButtonView = (ImageButton) rootView.findViewById(R.id.game_button_image3);
        choice4ImageButtonView = (ImageButton) rootView.findViewById(R.id.game_button_image4);
        // Answer selection confirmation views
        confirmTextAnswerView = (LinearLayout) rootView.findViewById(R.id.game_answer_text_confirmation_view);
        confirmImageAnswerView = (LinearLayout) rootView.findViewById(R.id.game_answer_image_confirmation_view);
        confirmAnswerTextQuestionTextView = (TextView) rootView.findViewById(R.id.game_answer_confirmation_question_text);
        confirmAnswerImageQuestionTextView = (TextView) rootView.findViewById(R.id.game_answer_confirmation_question_flag);
        confirmAnswerTextView = (TextView) rootView.findViewById(R.id.game_answer_confirmation_country);
        confirmAnswerImageView = (ImageView) rootView.findViewById(R.id.game_answer_confirmation_flag);
        confirmAnswerButtonView = (Button) rootView.findViewById(R.id.game_answer_confirmation_submit);
        // Set the header and the questions
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
        int gameProgress = sharedPref.getInt(PREF_GAME_PROGRESS, 0);
        int gameProgressMax = sharedPref.getInt(PREF_GAME_PROGRESS_MAX, 0);
        //------------------------------------------------------------------------------------------
        // Build the header data
        //------------------------------------------------------------------------------------------
        String gameProgressText = "Question " + gameProgress + " of " + gameProgressMax;
        question = getArguments().getString(FIELD_QUESTION);
        countryName = getArguments().getString(FIELD_COUNTRY_NAME);
        //------------------------------------------------------------------------------------------
        // Set the header views
        //------------------------------------------------------------------------------------------
        gameProgressView.setText(gameProgressText);
        questionView.setText(question);
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
        String gameMode = sharedPref.getString(PREF_GAME_MODE, "");
        if (isnew) {
            SharedPreferences.Editor editor = sharedPref.edit();
            ContentValues contentValues = newQuestion(gameMode);
            // Set the question variables
            countryName = contentValues.getAsString(FIELD_COUNTRY_NAME);
            countryCapital = contentValues.getAsString(FIELD_COUNTRY_CAPITAL);
            countryAlpha2Code = contentValues.getAsString(FIELD_COUNTRY_ALPHA2CODE);
            question = contentValues.getAsString(FIELD_QUESTION);
            answer = contentValues.getAsString(FIELD_ANSWER);
            choice1 = contentValues.getAsString(FIELD_CHOICE1);
            choice2 = contentValues.getAsString(FIELD_CHOICE2);
            choice3 = contentValues.getAsString(FIELD_CHOICE3);
            choice4 = contentValues.getAsString(FIELD_CHOICE4);
            // Keep track of countries used during the game session
            String usedCountries = sharedPref.getString(PREF_USED_COUNTRIES, "");
            String usedCountriesSelection;
            if (usedCountries.isEmpty()) {
                usedCountriesSelection = "'" + countryName + "'";
            } else {
                usedCountriesSelection = usedCountries + " OR '" + countryName + "'";
            }
            // Store them in the bundle
            b.putString(FIELD_COUNTRY_NAME, countryName);
            b.putString(FIELD_COUNTRY_CAPITAL, countryCapital);
            if (gameMode.equals(PREF_GAME_MODE_CAPITALS)) {
                b.putString(FIELD_CURRENT_ANSWER, countryCapital);
            } else if (gameMode.equals(PREF_GAME_MODE_FLAGS)) {
                b.putString(FIELD_CURRENT_ANSWER, countryAlpha2Code.toLowerCase());
            }
            b.putString(FIELD_QUESTION, question);
            b.putString(FIELD_ANSWER, answer);
            b.putString(FIELD_CHOICE1, choice1);
            b.putString(FIELD_CHOICE2, choice2);
            b.putString(FIELD_CHOICE3, choice3);
            b.putString(FIELD_CHOICE4, choice4);
            editor.putString(FIELD_USED_COUNTRIES, usedCountriesSelection);
            editor.apply();;
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
        String usedCountries = sharedPref.getString(FIELD_USED_COUNTRIES, "");
        Question questionObj = new Question(this.getContext());
        ContentValues contentValues = questionObj.getQuestion(gameMode, new String[]{usedCountries});
        // If a new question is requested and there is a timer running, cancel it first
        if (questionTimerIsRunning) {
            questionTimer.cancel();
            questionTimerIsRunning = false;
            getArguments().putBoolean(FIELD_TIMER_IS_RUNNING, false);
        }
        return contentValues;
    }

    /**
     * setQuestionVies
     * This method sets the Views when starting the game and when getting new questions
     */
    private void setQuestionViews() {
        setLayoutHeader();
        Bundle b = getArguments();
        String gameMode = sharedPref.getString(PREF_GAME_MODE, "");
        //------------------------------------------------------------------------------------------
        // Get all the variables from the shared prefs and from the bundle
        //------------------------------------------------------------------------------------------
        countryCapital = b.getString(FIELD_COUNTRY_CAPITAL);
        answer = b.getString(FIELD_ANSWER);
        choice1 = b.getString(FIELD_CHOICE1);
        choice2 = b.getString(FIELD_CHOICE2);
        choice3 = b.getString(FIELD_CHOICE3);
        choice4 = b.getString(FIELD_CHOICE4);
        questionTimerIsRunning = b.getBoolean(FIELD_TIMER_IS_RUNNING);
        int questionTimerProgress = b.getInt(FIELD_TIMER_PROGRESS);
        if (gameMode.equals(PREF_GAME_MODE_CAPITALS)) {
            gameContentText.setVisibility(View.VISIBLE);
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
        } else if (gameMode.equals(PREF_GAME_MODE_FLAGS)) {
            gameContentImage.setVisibility(View.VISIBLE);
            int flagDrawable;
            int flagWidth = getContext().getResources().getInteger(R.integer.button_flag_width);
            int flagHeight = getContext().getResources().getInteger(R.integer.button_flag_height);
            Utilities util = new Utilities(getContext());
            //--------------------------------------------------------------------------------------
            // Flags: Choice 1
            //--------------------------------------------------------------------------------------
            final String choice1alpha2Code = choice1.toLowerCase();
            flagDrawable = util.getDrawable(getContext(), "flag_" + choice1alpha2Code);
            Picasso.with(getContext()).load(flagDrawable)
                    .resize(flagWidth, flagHeight)
                    .onlyScaleDown()
                    .into(choice1ImageButtonView);
            choice1ImageButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choice1ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.bright_green));
                    choice2ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.buttonTint));
                    choice3ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.buttonTint));
                    choice4ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.buttonTint));
                    selectAnswer(choice1alpha2Code);
                }
            });
            //--------------------------------------------------------------------------------------
            // Flags: Choice 2
            //--------------------------------------------------------------------------------------
            final String choice2alpha2Code = choice2.toLowerCase();
            flagDrawable = util.getDrawable(getContext(), "flag_" + choice2alpha2Code);
            Picasso.with(getContext()).load(flagDrawable)
                    .resize(flagWidth, flagHeight)
                    .onlyScaleDown()
                    .into(choice2ImageButtonView);
            choice2ImageButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choice1ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.buttonTint));
                    choice2ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.bright_green));
                    choice3ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.buttonTint));
                    choice4ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.buttonTint));
                    selectAnswer(choice2alpha2Code);
                }
            });
            //--------------------------------------------------------------------------------------
            // Flags: Choice 3
            //--------------------------------------------------------------------------------------
            final String choice3alpha2Code = choice3.toLowerCase();
            flagDrawable = util.getDrawable(getContext(), "flag_" + choice3alpha2Code);
            Picasso.with(getContext()).load(flagDrawable)
                    .resize(flagWidth, flagHeight)
                    .onlyScaleDown()
                    .into(choice3ImageButtonView);
            choice3ImageButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choice1ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.buttonTint));
                    choice2ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.buttonTint));
                    choice3ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.bright_green));
                    choice4ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.buttonTint));
                    selectAnswer(choice3alpha2Code);
                }
            });
            //--------------------------------------------------------------------------------------
            // Flags: Choice 4
            //--------------------------------------------------------------------------------------
            final String choice4alpha2Code = choice4.toLowerCase();
            flagDrawable = util.getDrawable(getContext(), "flag_" + choice4alpha2Code);
            Picasso.with(getContext()).load(flagDrawable)
                    .resize(flagWidth, flagHeight)
                    .onlyScaleDown()
                    .into(choice4ImageButtonView);
            choice4ImageButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choice1ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.buttonTint));
                    choice2ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.buttonTint));
                    choice3ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.buttonTint));
                    choice4ImageButtonView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.bright_green));
                    selectAnswer(choice4alpha2Code);
                }
            });
        }

        final DonutProgress gameTimerView = (DonutProgress) rootView.findViewById(R.id.game_timer);
        //------------------------------------------------------------------------------------------
        // Create a new timer for this question
        //------------------------------------------------------------------------------------------
        //int startTimer = 1111111000;
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
                getArguments().putBoolean(FIELD_TIMER_IS_RUNNING, true);
                int progress = (int) (long) (millisUntilFinished / 1000);
                if (progress <= 10) {
                    util.playSound("tick_normal");
                    getArguments().putInt(FIELD_TIMER_PROGRESS, progress);
                    //Log.e(LOG_TAG, "progress " + progress);
                    gameTimerView.setProgress(progress);
                }
            }

            // When the timer finishes, mark the question as wrong and end the question
            public void onFinish() {
                questionTimerIsRunning = false;
                timeUp = true;
                getArguments().putBoolean(FIELD_TIMER_IS_RUNNING, false);
                getArguments().putInt(FIELD_TIMER_PROGRESS, 0);
                gameTimerView.setProgress(0);
                gameTimerView.setInnerBottomTextSize(36);
                // Time is up, clear any selected answers and answer the question (incorrect)
                getArguments().putString(FIELD_SELECTED_ANSWER, "");
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
        getArguments().putString(FIELD_SELECTED_ANSWER, answer);
        getArguments().putString("sequence", "selectAnswer");
        selectedAnswerView();
    }

    /**
     * selectedAnwswerView
     * This method builds the confirmation answer text, and button to submit the asnwer
     */
    private void selectedAnswerView() {
        String gameMode = sharedPref.getString(PREF_GAME_MODE, "");
        String answerQuestion;
        String answer = getArguments().getString(FIELD_SELECTED_ANSWER);
        confirmAnswerButtonView.setVisibility(View.VISIBLE);
        // Display the confirmation text
        if (answer != null && !answer.equals("")) {
            if (gameMode.equals(PREF_GAME_MODE_CAPITALS)) {
                confirmTextAnswerView.setVisibility(View.VISIBLE);
                answerQuestion = "The capital is";
                confirmAnswerTextQuestionTextView.setText(answerQuestion);
                confirmAnswerTextView.setText(answer);
            } else if (gameMode.equals(PREF_GAME_MODE_FLAGS)) {
                Utilities util = new Utilities(getContext());
                int flagWidth = getContext().getResources().getInteger(R.integer.confirmation_flag_width);
                int flagHeight = getContext().getResources().getInteger(R.integer.confirmation_flag_height);
                confirmImageAnswerView.setVisibility(View.VISIBLE);
                answerQuestion = "The flag is";
                confirmAnswerImageQuestionTextView.setText(answerQuestion);
                int flagDrawable;
                flagDrawable = util.getDrawable(getContext(), "flag_" + answer.toLowerCase());
                Picasso.with(getContext()).load(flagDrawable)
                        .resize(flagWidth, flagHeight)
                        .onlyScaleDown()
                        .into(confirmAnswerImageView);
            }
            confirmAnswerButtonView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    answerQuestion();
                }
            });
        }
    }

    /**
     * answerQuestions
     * This method is called when the choice to a question is confirmed
     */
    private void answerQuestion() {
        Utilities util = new Utilities(getContext());

        getArguments().putString("sequence", "answerQuestion");
        SharedPreferences.Editor editor = sharedPref.edit();
        // Cancel the timer
        questionTimer.cancel();
        questionTimerIsRunning = false;
        getArguments().putBoolean(FIELD_TIMER_IS_RUNNING, false);
        // Get the current and selected answers to see if they match (evaluated answer)
        String selectedAnswer = getArguments().getString(FIELD_SELECTED_ANSWER, "");
        String currentAnswer = getArguments().getString(FIELD_CURRENT_ANSWER, "");
        // Evaluate the answer
        Boolean test = selectedAnswer.equals(currentAnswer);
        getArguments().putBoolean(FIELD_EVALUATED_ANSWER, test);
        // Evaluated answer text for keeping track of the score and displaying the result
        int gameCorrectAnswers = sharedPref.getInt(PREF_CORRECT_ANSWERS, 0);
        int gameIncorrectAnswers = sharedPref.getInt(PREF_INCORRECT_ANSWERS, 0);
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
        editor.putInt(PREF_CORRECT_ANSWERS, gameCorrectAnswers);
        editor.putInt(PREF_INCORRECT_ANSWERS, gameIncorrectAnswers);
        // Save the game's progress
        int gameProgressMax = sharedPref.getInt(PREF_GAME_PROGRESS_MAX, 0);
        int gameProgress = sharedPref.getInt(PREF_GAME_PROGRESS, 0);
        int gameProgressCalc = gameProgress + 1;

        editor.putInt(PREF_GAME_PROGRESS, gameProgressCalc);
        editor.apply();

        getArguments().putString("answer_result", questionResultText.toString());
        getArguments().putString("answer_result_display", answer);

        //------------------------------------------------------------------------------------------
        //The game is over !!!! Submit the score
        //------------------------------------------------------------------------------------------
        gameProgress = sharedPref.getInt(PREF_GAME_PROGRESS, 0);
        if (gameProgressMax == gameProgress - 1) {
            submitScore();
        }

        Activity act = getActivity();
        if (act instanceof GameActivity) {
            ((GameActivity) act).flipCard(getArguments());
        }
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
        String scoreGameMode = sharedPref.getString(PREF_GAME_MODE, "");
        int scoreQuestionsCount = sharedPref.getInt(PREF_GAME_PROGRESS_MAX, 0);
        int scoreCorrectAnswers = sharedPref.getInt(PREF_CORRECT_ANSWERS, 0);
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
}