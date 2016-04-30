package rocks.throw20.funwithcountries;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.squareup.picasso.Picasso;

import junit.framework.Assert;

import rocks.throw20.funwithcountries.Data.Contract;


/**
 * Created by josel on 4/2/2016.
 */
public class GameActivityFragment extends Fragment {
    private static final String LOG_TAG = GameActivityFragment.class.getSimpleName();
    private SharedPreferences sharedPref;
    private View rootView;

    private static CountDownTimer questionTimer;
    private boolean questionTimerIsRunning = false;
    private boolean timeUp;
    private LinearLayout gameContent;

    private TextView confirmAnswerTextView;
    private Button confirmAnswerButtonView;

    private TextView answerResultView;
    private TextView nextQuestionTextView;
    private Button nextQuestionButtonView;

    private String countryName;
    private String countryCapital;
    private String question;

    private String answer;

    private Button choice1View;
    private Button choice2View;
    private Button choice3View;
    private Button choice4View;

    private ImageButton choice1ImageButtonView;
    private ImageButton choice2ImageButtonView;
    private ImageButton choice3ImageButtonView;
    private ImageButton choice4ImageButtonView;

    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;

    private String usedCountries;
    private String usedCountriesSelection;
    private final int linearLayoutMatchParent = LinearLayout.LayoutParams.MATCH_PARENT;
    private final int linearLayoutWrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
    // private final int relativeBelow = LinearLayout.BELOW;
    // private final int relativeCenterHorizontal = LinearLayout.CENTER_HORIZONTAL;

    private String sequence;

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
            Log.e(LOG_TAG, "onCreate " + true);
            getArguments().putString("savedInstanceState", null);
            getQuestion(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "onCreateView " + true);
        rootView = inflater.inflate(R.layout.fragment_game_question, container, false);
        return rootView;
    }

    @Override
    public void onPause() {
        Log.e(LOG_TAG, "onPause " + true);
        // Cancel the timer, it will be recreated and started from where it left off
        questionTimer.cancel();
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e(LOG_TAG, "onStop " + true);
        //cancelGame();
        super.onStop();
    }

    @Override
    public void onResume() {
        Log.e(LOG_TAG, "onResume " + true);
        sequence = getArguments().getString("sequence");
        Log.e(LOG_TAG, "sequence " + sequence);
        if (sequence != null) {
            switch (sequence) {
                case "getQuestion":
                    setQuestionViews();
                    break;
                case "selectAnswer":
                    setQuestionViews();
                    selectedAnswerView();
                    break;
                case "answerQuestion":
                    setLayourHeader();
                    answerQuestionView();
                    break;
            }
        }

        super.onResume();
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
            question = contentValues.getAsString("question");
            answer = contentValues.getAsString("answer");
            choice1 = contentValues.getAsString("choice1");
            choice2 = contentValues.getAsString("choice2");
            choice3 = contentValues.getAsString("choice3");
            choice4 = contentValues.getAsString("choice4");

            // Keep track of countries used during the game session
            usedCountries = sharedPref.getString("used_countries", "");
            Log.e(LOG_TAG, "usedCountries: " + usedCountries);
            if (usedCountries.isEmpty()) {
                usedCountriesSelection = "'" + countryName + "'";
            } else {
                usedCountriesSelection = usedCountries + " OR '" + countryName + "'";
            }

            // Store them in the bundle
            b.putString("country_name", countryName);
            b.putString("country_capital", countryCapital);
            b.putString("current_answer", countryCapital);
            b.putString("question", question);
            b.putString("answer", answer);
            b.putString("choice1", choice1);
            b.putString("choice2", choice2);
            b.putString("choice3", choice3);
            b.putString("choice4", choice4);
            editor.putString("used_countries", usedCountriesSelection);
            editor.apply();
            Log.e(LOG_TAG, "used_countries: " + usedCountriesSelection);
        }
    }

    private void setLayourHeader() {
        int gameProgress = sharedPref.getInt("game_progress", 0);
        int gameProgressMax = sharedPref.getInt("game_progress_max", 0);
        int correctAnswers = sharedPref.getInt("correct_answers", 0);

        String gameProgressText = "Question " + gameProgress + " of " + gameProgressMax;
        String gameScoreText = "Score: " + correctAnswers;
        question = getArguments().getString("question");
        countryName = getArguments().getString("country_name");
        //------------------------------------------------------------------------------------------
        // Set the top views
        //------------------------------------------------------------------------------------------
        TextView questionView = (TextView) rootView.findViewById(R.id.question);
        TextView gameScoreView = (TextView) rootView.findViewById(R.id.game_score);
        TextView questionCountryView = (TextView) rootView.findViewById(R.id.question_country);
        TextView gameProgressView = (TextView) rootView.findViewById(R.id.game_progress);


        gameProgressView.setText(gameProgressText);
        questionView.setText(question);
        gameScoreView.setText(gameScoreText);
        String countryNameDisplay = countryName + "?";
        questionCountryView.setText(countryNameDisplay);
    }

    /**
     * setViews
     * This method sets the Views when starting the game and when getting new questions
     */
    private void setQuestionViews() {
        setLayourHeader();
        Log.e(LOG_TAG, "setQuestionViews " + true);
        Bundle b = getArguments();
        String gameMode = sharedPref.getString("game_mode", "");
        gameContent = (LinearLayout) rootView.findViewById(R.id.game_content);
        final DonutProgress gameTimerView = (DonutProgress) rootView.findViewById(R.id.game_timer);
        //------------------------------------------------------------------------------------------
        // Get all the variables from the shared prefs and from the bundle
        countryCapital = b.getString("country_capital");
        answer = b.getString("answer");
        choice1 = b.getString("choice1");
        choice2 = b.getString("choice2");
        choice3 = b.getString("choice3");
        choice4 = b.getString("choice4");
        questionTimerIsRunning = b.getBoolean("timer_is_running");
        int questionTimerProgress = b.getInt("timer_progress");

        // Set the choices based on the Game Mode
        //------------------------------------------------------------------------------------------
        // Game Mode: Capitals
        //------------------------------------------------------------------------------------------
        if (gameMode.equals("capitals")) {
            //--------------------------------------------------------------------------------------
            // Capitals: Choice 1
            //--------------------------------------------------------------------------------------
            if (choice1View == null) {
                choice1View = new Button(getActivity());
                LinearLayout.LayoutParams choiceView1params =
                        new LinearLayout.LayoutParams(linearLayoutMatchParent, linearLayoutWrapContent);
                choice1View.setId(R.id.choice1);
                choice1View.setLayoutParams(choiceView1params);
            }
            if (rootView.findViewById(R.id.choice1) == null) {
                choice1View.setText(choice1);
                choice1View.setEnabled(true);
                choice1View.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence countryCapital = choice1View.getText();
                        selectAnswer(countryCapital.toString());
                    }
                });
                gameContent.addView(choice1View);
            }
            //--------------------------------------------------------------------------------------
            // Capitals: Choice 2
            //--------------------------------------------------------------------------------------
            if (choice2View == null) {
                choice2View = new Button(getActivity());
                LinearLayout.LayoutParams choiceView2params =
                        new LinearLayout.LayoutParams(linearLayoutMatchParent, linearLayoutWrapContent);
                //choiceView2params.addRule(relativeBelow, R.id.choice1);
                choice2View.setId(R.id.choice2);
                choice2View.setLayoutParams(choiceView2params);
            }
            if (rootView.findViewById(R.id.choice2) == null) {
                choice2View.setText(choice2);
                choice2View.setEnabled(true);
                choice2View.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence countryCapital = choice2View.getText();
                        selectAnswer(countryCapital.toString());
                    }
                });
                gameContent.addView(choice2View);
            }
            //--------------------------------------------------------------------------------------
            // Capitals: Choice 3
            //--------------------------------------------------------------------------------------
            if (choice3View == null) {
                choice3View = new Button(getActivity());
                LinearLayout.LayoutParams choiceView3params =
                        new LinearLayout.LayoutParams(linearLayoutMatchParent, linearLayoutWrapContent);
                //choiceView3params.addRule(relativeBelow, R.id.choice2);
                choice3View.setId(R.id.choice3);
                choice3View.setLayoutParams(choiceView3params);
            }
            if (rootView.findViewById(R.id.choice3) == null) {
                choice3View.setText(choice3);
                choice3View.setEnabled(true);
                choice3View.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence countryCapital = choice3View.getText();
                        selectAnswer(countryCapital.toString());
                    }
                });
                gameContent.addView(choice3View);
            }
            //--------------------------------------------------------------------------------------
            // Capitals: Choice 4
            //--------------------------------------------------------------------------------------
            if (choice4View == null) {
                choice4View = new Button(getActivity());
                LinearLayout.LayoutParams choiceView4params =
                        new LinearLayout.LayoutParams(linearLayoutMatchParent, linearLayoutWrapContent);
                //choiceView4params.addRule(relativeBelow, R.id.choice3);
                choice4View.setId(R.id.choice4);
                choice4View.setLayoutParams(choiceView4params);
            }
            if (rootView.findViewById(R.id.choice4) == null) {
                choice4View.setText(choice4);
                choice4View.setEnabled(true);
                choice4View.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence countryCapital = choice4View.getText();
                        selectAnswer(countryCapital.toString());
                    }
                });
                gameContent.addView(choice4View);
            }
        }
        //------------------------------------------------------------------------------------------
        // Game Mode: flags
        //------------------------------------------------------------------------------------------
        else if (gameMode.equals("flags")) {
            //--------------------------------------------------------------------------------------
            // Flags: Choice 1
            //--------------------------------------------------------------------------------------
            if (choice1ImageButtonView == null) {
                choice1ImageButtonView = new ImageButton(getActivity());
                LinearLayout.LayoutParams choiceView1params =
                        new LinearLayout.LayoutParams(linearLayoutWrapContent, linearLayoutWrapContent);
                choice1ImageButtonView.setId(R.id.choice1);
                choice1ImageButtonView.setLayoutParams(choiceView1params);
            }
            if (rootView.findViewById(R.id.choice1) == null) {
                Log.e(LOG_TAG, "choice1 " + choice1.toLowerCase());
                int flagDrawable = getDrawable(getContext(), "flag_" + choice1.toLowerCase());
                Picasso.with(getContext()).load(flagDrawable)
                        .resize(250, 250)
                        .onlyScaleDown()
                        .into(choice1ImageButtonView);

                //choice1View.setText(choice1);
                choice1ImageButtonView.setEnabled(true);
                choice1ImageButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence countryCapital = choice1View.getText();
                        selectAnswer(countryCapital.toString());
                    }
                });
                gameContent.addView(choice1ImageButtonView);
            }
            //--------------------------------------------------------------------------------------
            // Flags: Choice 2
            //--------------------------------------------------------------------------------------
            if (choice2ImageButtonView == null) {
                choice2ImageButtonView = new ImageButton(getActivity());
                LinearLayout.LayoutParams choiceView2params =
                        new LinearLayout.LayoutParams(linearLayoutWrapContent, linearLayoutWrapContent);
                choice2ImageButtonView.setId(R.id.choice2);
                choice2ImageButtonView.setLayoutParams(choiceView2params);
            }
            if (rootView.findViewById(R.id.choice2) == null) {
                Log.e(LOG_TAG, "choice2 " + choice2.toLowerCase());
                int flagDrawable = getDrawable(getContext(), "flag_" + choice2.toLowerCase());

                Picasso.with(getContext()).load(flagDrawable)
                        .resize(250, 250)
                        .onlyScaleDown()
                        .into(choice2ImageButtonView);

                //choice1View.setText(choice1);
                choice2ImageButtonView.setEnabled(true);
                choice2ImageButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence countryCapital = choice2View.getText();
                        selectAnswer(countryCapital.toString());
                    }
                });
                gameContent.addView(choice2ImageButtonView);
            }
            //--------------------------------------------------------------------------------------
            // Flags: Choice 3
            //--------------------------------------------------------------------------------------
            if (choice3ImageButtonView == null) {
                choice3ImageButtonView = new ImageButton(getActivity());
                LinearLayout.LayoutParams choiceView3params =
                        new LinearLayout.LayoutParams(linearLayoutWrapContent, linearLayoutWrapContent);
                choice3ImageButtonView.setId(R.id.choice3);
                choice3ImageButtonView.setLayoutParams(choiceView3params);
            }
            if (rootView.findViewById(R.id.choice3) == null) {
                Log.e(LOG_TAG, "choice3 " + choice3.toLowerCase());
                int flagDrawable = getDrawable(getContext(), "flag_" + choice3.toLowerCase());

                Picasso.with(getContext()).load(flagDrawable)
                        .resize(250, 250)
                        .onlyScaleDown()
                        .into(choice3ImageButtonView);

                //choice1View.setText(choice1);
                choice3ImageButtonView.setEnabled(true);
                choice3ImageButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence countryCapital = choice3View.getText();
                        selectAnswer(countryCapital.toString());
                    }
                });
                gameContent.addView(choice3ImageButtonView);
            }
            //--------------------------------------------------------------------------------------
            // Flags: Choice 4
            //--------------------------------------------------------------------------------------
            if (choice4ImageButtonView == null) {
                choice4ImageButtonView = new ImageButton(getActivity());
                LinearLayout.LayoutParams choiceView1params = new LinearLayout.LayoutParams(linearLayoutWrapContent, linearLayoutWrapContent);
                choice4ImageButtonView.setId(R.id.choice4);
                choice4ImageButtonView.setLayoutParams(choiceView1params);
            }
            if (rootView.findViewById(R.id.choice4) == null) {
                Log.e(LOG_TAG, "choice4 " + choice4.toLowerCase());
                int flagDrawable = getDrawable(getContext(), "flag_" + choice4.toLowerCase());

                Picasso.with(getContext()).load(flagDrawable)
                        .resize(250, 250)
                        .onlyScaleDown()
                        .into(choice4ImageButtonView);

                //choice4View.setText(choice4);
                choice4ImageButtonView.setEnabled(true);
                choice4ImageButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence countryCapital = choice4View.getText();
                        selectAnswer(countryCapital.toString());
                    }
                });
                gameContent.addView(choice4ImageButtonView);
            }
        }




        //------------------------------------------------------------------------------------------
        // Slide in the choice views
        //------------------------------------------------------------------------------------------
        if ( gameMode.equals("capitals")) {
            slideInView(choice1View, 360);
            slideInView(choice2View, 340);
            slideInView(choice3View, 320);
            slideInView(choice4View, 300);
        }else if ( gameMode.equals("flags")){
            slideInView(choice1ImageButtonView, 360);
            slideInView(choice2ImageButtonView, 360);
            slideInView(choice3ImageButtonView, 360);
            slideInView(choice4ImageButtonView, 360);
        }


        //------------------------------------------------------------------------------------------
        // Create a new timer for this question
            //------------------------------------------------------------------------------------------
        Log.e(LOG_TAG, "timer is running " + questionTimerIsRunning);
        int startTimer = 11000;
        // If a timer is running, resume it
        if ( questionTimerIsRunning ){startTimer = questionTimerProgress * 1000;}
        Log.e(LOG_TAG, "create new timer " + true);

        questionTimer = new CountDownTimer(startTimer, 1000) {
            // Count down the timer on every tick
            public void onTick(long millisUntilFinished) {

                gameTimerView.setInnerBottomText("");
                questionTimerIsRunning = true;
                timeUp = false;
                getArguments().putBoolean("timer_is_running",true);
                int progress = (int) (long) ( millisUntilFinished / 1000);
                if ( progress <= 10 ){
                    getArguments().putInt("timer_progress",progress);
                    Log.e(LOG_TAG, "progress " + progress);
                    gameTimerView.setProgress(progress);
                }
            }
            // When the timer finishes, mark the question as wrong and end the question
            public void onFinish() {

                Log.e(LOG_TAG, "onFinish " + true);
                questionTimerIsRunning = false;
                timeUp = true;
                getArguments().putBoolean("timer_is_running", false);
                getArguments().putInt("timer_progress", 0);
                gameTimerView.setProgress(0);
                gameTimerView.setInnerBottomTextSize(36);
                //gameTimerView.setInnerBottomText("Time up!");
                // Time is up, clear any selected answers and answer the question (incorrect)
                getArguments().putString("selected_answer","");
                // TODO Create a timeout function
                // Select and answer
                selectAnswer("");
                answerQuestion();
            }
        }.start();
    }
    public static int getDrawable(Context context, String name)
    {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);

        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }
    /**
     * selectAnswer
     * This method is called when clicking on a choice button
     * It sets the views that confirm your answer and allow you to submit
     * @param answer the answer text
     */
    private void selectAnswer(String answer){
        Log.e(LOG_TAG, "selectAnswer answer: " + answer);
        getArguments().putString("selected_answer", answer);
        getArguments().putString("sequence", "selectAnswer");
        selectedAnswerView();
    }
    /**
     * selectedAnwswerView
     * This method builds the confirmation answer text, and button to submit the asnwer

     */
    private void selectedAnswerView(){
        String gameMode = sharedPref.getString("game_mode","");
        String answer = getArguments().getString("selected_answer");
        Log.e(LOG_TAG, "confirmAnswerTextView " + confirmAnswerTextView);
        gameContent = (LinearLayout) rootView.findViewById(R.id.game_content);
        //------------------------------------------------------------------------------------------
        if ( confirmAnswerTextView == null ) {
            Log.e(LOG_TAG, "confirmAnswerTextView " + true);
            confirmAnswerTextView = new TextView(getActivity());
            LinearLayout.LayoutParams confirmTextParams = new LinearLayout.LayoutParams(linearLayoutMatchParent, linearLayoutWrapContent);
            confirmAnswerTextView.setId(R.id.answer_confirmation_text);
            confirmAnswerTextView.setLayoutParams(confirmTextParams);
        }
        if ( rootView.findViewById(R.id.answer_confirmation_text) == null ){
            Log.e(LOG_TAG, "confirmAnswerTextView " + " found");
            gameContent.addView(confirmAnswerTextView);
        }
        String answerDisplay = "";
        if ( gameMode.equals("capitals")) { answerDisplay = "The capital is " + answer; }
        else if ( gameMode.equals("flags")){ answerDisplay = "The flag is " + "" ; }
        confirmAnswerTextView.setText(answerDisplay);
        //------------------------------------------------------------------------------------------
        // confirmAnswerButtonView
        if ( confirmAnswerButtonView == null ) {
            confirmAnswerButtonView = new Button(getActivity());
            LinearLayout.LayoutParams actionAnswerParams = new LinearLayout.LayoutParams(linearLayoutMatchParent, linearLayoutWrapContent);
            //actionAnswerParams.addRule(relativeBelow, R.id.answer_confirmation_text);
            confirmAnswerButtonView.setId(R.id.answer_confirmation_button);
            confirmAnswerButtonView.setLayoutParams(actionAnswerParams);
            confirmAnswerButtonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_black_24dp, 0);
            gameContent.addView(confirmAnswerButtonView);
            confirmAnswerButtonView.setText(R.string.action_submit);
            //------------------------------------------------------------------------------------------
            // confirmAnswerButtonView: Set on ClickListeners
            confirmAnswerButtonView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    answerQuestion();
                }
            });
        }
        if ( rootView.findViewById(R.id.answer_confirmation_button) == null ){
            gameContent.addView(confirmAnswerButtonView);
        }
    }

    /**
     * answerQuestions
     * This method is called when the choice to a question is confirmed
     */
    private void answerQuestion(){
        getArguments().putString("sequence", "answerQuestion");
        Log.e(LOG_TAG, "sequence " + getArguments().getString("sequence"));
        SharedPreferences.Editor editor = sharedPref.edit();
        // Cancel the timer
        questionTimer.cancel();
        questionTimerIsRunning = false;
        getArguments().putBoolean("timer_is_running",false);
        // Get the current and selected answers to see if they match (evaluated answer)
        String selectedAnswer = getArguments().getString("selected_answer", "");
        String currentAnswer = getArguments().getString("current_answer", "");
        // Evaluate the answer
        Boolean test  = selectedAnswer.equals(currentAnswer);
        getArguments().putBoolean("evaluated_answer",test);
        Log.e(LOG_TAG, "answerQuestions selectedAnswer: " + selectedAnswer);
        Log.e(LOG_TAG, "answerQuestions currentAnswer: " + currentAnswer);
        // Evaluated answer text for keeping track of the score and displaying the result
        int gameCorrectAnswers = sharedPref.getInt("correct_answers",0);
        int gameIncorrectAnswers = sharedPref.getInt("incorrect_answers",0);
        CharSequence questionResultText;
        // The answer was correct
        if ( test ){gameCorrectAnswers = gameCorrectAnswers + 1 ; questionResultText = "Correct";}
        // The time is up
        else if ( timeUp ){ gameIncorrectAnswers = gameIncorrectAnswers + 1 ; questionResultText = "Time up!"; }
        // The answer was incorrect
        else{ gameIncorrectAnswers = gameIncorrectAnswers + 1 ; questionResultText = "Incorrect";}
        // Save the score
        editor.putInt("correct_answers", gameCorrectAnswers);
        editor.putInt("incorrect_answers", gameIncorrectAnswers);
        // Save the game's progress
        int gameProgress = sharedPref.getInt("game_progress",0);
        int gameProgressMax = sharedPref.getInt("game_progress_max",0);
        int gameProgressCalc =  gameProgress + 1;
        // TODO  End the game
        if ( gameProgress == gameProgressMax ) {endGame();}
        // Track game progress
        else {
            editor.putInt("game_progress", gameProgressCalc);
            editor.apply();
        }

        getArguments().putString("answer_result",questionResultText.toString());
        getArguments().putString("answer_result_display",answer);

        Log.e(LOG_TAG, "question result 1 " + questionResultText);
        Log.e(LOG_TAG, "question result 2 " + answer);

        // Disable, slide out and remove the choice buttons

        choice1View = (Button) rootView.findViewById(R.id.choice1);
        choice2View = (Button) rootView.findViewById(R.id.choice2);
        choice3View = (Button) rootView.findViewById(R.id.choice3);
        choice4View = (Button) rootView.findViewById(R.id.choice4);
        choice1View.setEnabled(false);
        choice2View.setEnabled(false);
        choice3View.setEnabled(false);
        choice4View.setEnabled(false);
        slideOutView(choice1View, 360);
        slideOutView(choice2View, 340);
        slideOutView(choice3View, 320);
        slideOutView(choice4View, 300);
        // Remove confirmation view
        slideOutView(confirmAnswerTextView,360);
        slideOutView(confirmAnswerButtonView,340);

        answerQuestionView();
    }

    /**
     * selectedAnwswerView
     * This method builds the confirmation answer text, and button to submit the asnwer
     */
    private void answerQuestionView(){
        String resultText = getArguments().getString("answer_result");
        String resultTextDescription = getArguments().getString("answer_result_display");
        gameContent = (LinearLayout) rootView.findViewById(R.id.game_content);
        //Log.e(LOG_TAG, "answerResultView " + answerResultView);
        //------------------------------------------------------------------------------------------
        // answerResultView
        //------------------------------------------------------------------------------------------
        if ( answerResultView == null ) {
            answerResultView = new TextView(getActivity());
            //Log.e(LOG_TAG, "create answer result view " + true);
            LinearLayout.LayoutParams answerResultViewParams = new LinearLayout.LayoutParams(linearLayoutWrapContent, linearLayoutWrapContent);
            answerResultViewParams.gravity = Gravity.CENTER;
            answerResultView.setId(R.id.next_question_answer_result_text);
            answerResultView.setLayoutParams(answerResultViewParams);
            answerResultView.setTextSize(28);

        }
        if ( rootView.findViewById(R.id.next_question_answer_result_text) == null ){

            gameContent.addView(answerResultView);
            if ( resultText != null  && resultText.equals("Incorrect")) {
                Log.e(LOG_TAG, "result text " + resultText);
                answerResultView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.incorrectBackground));
                answerResultView.setTextColor(ContextCompat.getColor(getActivity(), R.color.incorrectText));
            } else if ( resultText != null  && resultText.equals("Time up!") ){
                Log.e(LOG_TAG, "result text " + resultText);
                answerResultView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.timeUpBackground));
                answerResultView.setTextColor(ContextCompat.getColor(getActivity(), R.color.timeUpText));
            } else if ( resultText != null && resultText.equals("Correct")) {
                Log.e(LOG_TAG, "result text " + resultText);
                answerResultView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.correctBackground));
                answerResultView.setTextColor(ContextCompat.getColor(getActivity(), R.color.correctText));
            }

            answerResultView.setText(resultText);}
        //------------------------------------------------------------------------------------------
        // nextQuestionTextView
        //------------------------------------------------------------------------------------------
        if ( nextQuestionTextView == null ) {
            nextQuestionTextView = new TextView(getActivity());
            LinearLayout.LayoutParams questionResultParams = new LinearLayout.LayoutParams(linearLayoutWrapContent, linearLayoutWrapContent);
            questionResultParams.gravity = Gravity.CENTER;
            nextQuestionTextView.setId(R.id.next_question_text);
            nextQuestionTextView.setLayoutParams(questionResultParams);}
        if ( rootView.findViewById(R.id.next_question_text) == null ){
            gameContent.addView(nextQuestionTextView);
            nextQuestionTextView.setText(resultTextDescription);}
        //------------------------------------------------------------------------------------------
        // nextQuestionButtonView
        //------------------------------------------------------------------------------------------
        if ( nextQuestionButtonView == null ) {
            nextQuestionButtonView = new Button(getActivity());
            LinearLayout.LayoutParams nextQuestionParams = new LinearLayout.LayoutParams(linearLayoutWrapContent, linearLayoutWrapContent);
            nextQuestionParams.gravity = Gravity.CENTER;
            nextQuestionButtonView.setId(R.id.next_question_button);
            nextQuestionButtonView.setLayoutParams(nextQuestionParams);
            nextQuestionButtonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0);
            //------------------------------------------------------------------------------------------
            // nextQuestionButtonView: Set on ClickListeners
            //------------------------------------------------------------------------------------------
            nextQuestionButtonView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    nextButtonOnClickListener();
                    }
            });
        }
        if ( rootView.findViewById(R.id.next_question_button) == null ){
            Log.e(LOG_TAG, "nextQuestionButtonView on layout " + false);
            gameContent.addView(nextQuestionButtonView);
            nextQuestionButtonView.setText(R.string.action_next_question);}
        // Slide in the next question views
        slideInView(answerResultView,360);
        slideInView(nextQuestionTextView,340);
        slideInView(nextQuestionButtonView,320);

        // Display the Score
        int correctAnswers = sharedPref.getInt("correct_answers", 0);
        String gameScoreText = "Score: " + correctAnswers;
        TextView gameScoreView = (TextView) rootView.findViewById(R.id.game_score);
        gameScoreView.setText(gameScoreText);

    }

    private void nextButtonOnClickListener(){
        answerResultView = (TextView) rootView.findViewById(R.id.next_question_answer_result_text);
        nextQuestionTextView = (TextView) rootView.findViewById(R.id.next_question_text);
        nextQuestionButtonView = (Button) rootView.findViewById(R.id.next_question_button);
        Log.e(LOG_TAG, "answerResultView on layout " + answerResultView);
        Log.e(LOG_TAG, "nextQuestionTextView on layout " + nextQuestionTextView);
        Log.e(LOG_TAG, "nextQuestionButtonView on layout " + nextQuestionButtonView);
        slideOutView(answerResultView,160);
        slideOutView(nextQuestionTextView, 140);
        slideOutView(nextQuestionButtonView, 120);
        getArguments().clear();
        getQuestion(true);
        // Set the question views
        setQuestionViews();
    }

    /**
     * endGame
     * Method to end the game, submit the scores, and start the scores activity
     */
    private void endGame(){

        // TODO Implement game end logic

        Context context = getActivity();
        CharSequence text = "Game Over!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        getActivity().finish();

    }

    /**
     * cancelGame
     * Method to cancel the game and return the MainActivity
     */
    private void cancelGame(){
        questionTimer.cancel();
        questionTimerIsRunning = false;
        getArguments().putBoolean("timer_is_running",false);

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
        Log.e(LOG_TAG,"gameMode: " + gameMode);
        ContentValues contentValues = questionObj.getQuestion(gameMode,new String[]{usedCountries});
        // If a new question is requested and there is a timer running, cancel it first
        if ( questionTimerIsRunning ) {
            questionTimer.cancel();
            questionTimerIsRunning= false;
            getArguments().putBoolean("timer_is_running",false);
        }
        return  contentValues;
    }

    /**
     * slideInView
     * Method to slide in a view
     * @param view the view to slide
     * @param duration the duration of the slide
     */
    private void slideInView(View view, int duration){
        TranslateAnimation animate = new TranslateAnimation(-view.getWidth(),0,0,0);
        animate.setDuration(duration);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    /**
     * slideOutView
     * Method to slide out and remove a view
     * @param view the view slide out and remove
     * @param duration the duration of the slide
     */
    private void slideOutView(View view, int duration){
        gameContent =  (LinearLayout) rootView.findViewById(R.id.game_content);
        TranslateAnimation animate = new TranslateAnimation(0,+view.getWidth(),0,0);
        animate.setDuration(duration);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        gameContent.removeView(view);
    }

    public void slideToTop(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,view.getHeight(),344);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        //view.setVisibility(View.VISIBLE);
    }
}
