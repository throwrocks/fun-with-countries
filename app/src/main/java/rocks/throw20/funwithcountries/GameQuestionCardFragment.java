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
        int gameProgress = sharedPref.getInt("game_progress", 0);
        int gameProgressMax = sharedPref.getInt("game_progress_max", 0);
        //------------------------------------------------------------------------------------------
        // Build the header data
        //------------------------------------------------------------------------------------------
        String gameProgressText = "Question " + gameProgress + " of " + gameProgressMax;
        question = getArguments().getString("question");
        countryName = getArguments().getString("country_name");
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
                b.putString("current_answer", countryAlpha2Code.toLowerCase());
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
        } else if (gameMode.equals("flags")) {
            gameContentImage.setVisibility(View.VISIBLE);
            String alpha2Code;
            int flagDrawable;
            Utilities util = new Utilities(getContext());
            //--------------------------------------------------------------------------------------
            // Flags: Choice 1
            //--------------------------------------------------------------------------------------
            final String choice1alpha2Code = choice1.toLowerCase();
            flagDrawable = util.getDrawable(getContext(), "flag_" + choice1alpha2Code);
            Picasso.with(getContext()).load(flagDrawable)
                    .resize(275, 165)
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
                    .resize(275, 165)
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
                    .resize(275, 165)
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
                    .resize(275, 165)
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
        Log.i("answer", answer);
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
        String answerQuestion;
        String answer = getArguments().getString("selected_answer");
        confirmAnswerButtonView.setVisibility(View.VISIBLE);
        // Display the confirmation text
        if (answer != null && !answer.equals("")) {

            if (gameMode.equals("capitals")) {
                confirmTextAnswerView.setVisibility(View.VISIBLE);
                answerQuestion = "The capital is";
                confirmAnswerTextQuestionTextView.setText(answerQuestion);
                confirmAnswerTextView.setText(answer);
            } else if (gameMode.equals("flags")) {
                Utilities util = new Utilities(getContext());
                confirmImageAnswerView.setVisibility(View.VISIBLE);
                answerQuestion = "The flag is";
                confirmAnswerImageQuestionTextView.setText(answerQuestion);
                int flagDrawable;
                flagDrawable = util.getDrawable(getContext(), "flag_" + answer.toLowerCase());
                Picasso.with(getContext()).load(flagDrawable)
                        .resize(91, 55)
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
}