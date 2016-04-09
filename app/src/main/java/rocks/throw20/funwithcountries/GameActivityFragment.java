package rocks.throw20.funwithcountries;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;


/**
 * Created by josel on 4/2/2016.
 */
public class GameActivityFragment extends Fragment{
    private static final String LOG_TAG = GameActivityFragment.class.getSimpleName();
    SharedPreferences sharedPref;
    private View rootView;




    private DonutProgress gameTimerView;
    private static CountDownTimer questionTimer;
    private boolean questionTimerIsRunning = false;

    private RelativeLayout gameContent;

    private TextView confirmAnswerTextView;
    private Button confirmAnswerButtonView;

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

    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;

    private String currentAnswer;


    public GameActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref =  PreferenceManager.getDefaultSharedPreferences(getActivity());
        Log.e(LOG_TAG, "onCreate " + true);
        if (savedInstanceState == null) {
            getQuestion(true);
        }
        else{
            getQuestion(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_game, container, false);
        setQuestionViews();
        return rootView;
    }

    @Override
    public void onPause() {
        Log.e(LOG_TAG, "onPause " + true);
        // TODO pause the timer
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e(LOG_TAG, "onStop " + true);
        cancelGame();
        super.onStop();
    }

    @Override
    public void onResume() {
        Log.e(LOG_TAG, "onResume " + true);

        super.onResume();
    }

    /**
     * getQuestion
     * @param isnew wether the question is new, or it's being restored from savedInstanceState
     */
    private void getQuestion(Boolean isnew){
        Bundle b = getArguments();
        if ( isnew ) {
            ContentValues contentValues = newQuestion();
            // Set the question variables
            countryName = contentValues.getAsString("country_name");
            countryCapital = contentValues.getAsString("country_capital");
            question = contentValues.getAsString("question");
            answer = contentValues.getAsString("answer");
            choice1 = contentValues.getAsString("choice1");
            choice2 = contentValues.getAsString("choice2");
            choice3 = contentValues.getAsString("choice3");
            choice4 = contentValues.getAsString("choice4");
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
        }
    }

    /**
     * setViews
     * This method sets the Views when starting the game and when getting new questions
     */
    private void setQuestionViews(){
        Log.e(LOG_TAG, "setQuestionViews " + true);
        Bundle b = getArguments();
        //------------------------------------------------------------------------------------------
        // Get all the variables from the shared prefs and from the bundle
        int gameProgress = sharedPref.getInt("game_progress", 0);
        int gameProgressMax = sharedPref.getInt("game_progress_max", 0);
        int correctAnswers = sharedPref.getInt("correct_answers", 0);
        String gameProgressText = "Question " + gameProgress + " of " + gameProgressMax;
        String gameScoreText = "Score: " + correctAnswers;
        countryName = b.getString("country_name");
        countryCapital = b.getString("country_capital");
        question = b.getString("question");
        answer = b.getString("answer");
        choice1 = b.getString("choice1");
        choice2 = b.getString("choice2");
        choice3 = b.getString("choice3");
        choice4 = b.getString("choice4");


        gameContent =  (RelativeLayout) rootView.findViewById(R.id.game_content);
        //------------------------------------------------------------------------------------------
        // Choice 1
        if ( choice1View == null ){
            Log.e(LOG_TAG, "choice1View null " + true);
        choice1View = new Button(getActivity());
        RelativeLayout.LayoutParams choice1ViewParams =  new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        choice1ViewParams.addRule(RelativeLayout.BELOW, R.id.question_country);
        choice1View.setId(R.id.choice1);
        choice1View.setLayoutParams(choice1ViewParams);
        }
        if ( rootView.findViewById(R.id.choice1) == null ){
            Log.e(LOG_TAG, "choice1View on Layout " + false);
            gameContent.addView(choice1View);
        }
        //------------------------------------------------------------------------------------------
        // Choice 2
        if ( choice2View == null ) {
            Log.e(LOG_TAG, "choice2View null " + true);
            choice2View = new Button(getActivity());
            RelativeLayout.LayoutParams choice2ViewParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            choice2ViewParams.addRule(RelativeLayout.BELOW, R.id.choice1);
            choice2View.setId(R.id.choice2);
            choice2View.setLayoutParams(choice2ViewParams);
        }
        if ( rootView.findViewById(R.id.choice2) == null ){
            Log.e(LOG_TAG, "choice2View on Layout " + false);
            gameContent.addView(choice2View);
        }
        //------------------------------------------------------------------------------------------
        // Choice 3
        if ( choice3View == null ) {
            Log.e(LOG_TAG, "choice3View null " + true);
            choice3View = new Button(getActivity());
            RelativeLayout.LayoutParams choice3ViewParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            choice3ViewParams.addRule(RelativeLayout.BELOW, R.id.choice2);
            choice3View.setId(R.id.choice3);
            choice3View.setLayoutParams(choice3ViewParams);
        }
        if ( rootView.findViewById(R.id.choice3) == null ){
            Log.e(LOG_TAG, "choice3View on Layout " + false);
            gameContent.addView(choice3View);
        }
        //------------------------------------------------------------------------------------------
        // Choice 4
        if ( choice4View == null ) {
            Log.e(LOG_TAG, "choice4View null " + true);
            choice4View = new Button(getActivity());
            RelativeLayout.LayoutParams choice4ViewParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            choice4ViewParams.addRule(RelativeLayout.BELOW, R.id.choice3);
            choice4View.setId(R.id.choice4);
            choice4View.setLayoutParams(choice4ViewParams);
        }
        if ( rootView.findViewById(R.id.choice4) == null ){
            Log.e(LOG_TAG, "choice4View on Layout " + false);
            gameContent.addView(choice4View);
        }


        choice1View.setText(choice1);
        choice2View.setText(choice2);
        choice3View.setText(choice3);
        choice4View.setText(choice4);


        slideInView(choice1View, 360);
        slideInView(choice2View, 340);
        slideInView(choice3View, 320);
        slideInView(choice4View, 300);

        choice1View.setEnabled(true);
        choice2View.setEnabled(true);
        choice3View.setEnabled(true);
        choice4View.setEnabled(true);
        //------------------------------------------------------------------------------------------
        // Set the top views
        TextView questionView = (TextView) rootView.findViewById(R.id.question);
        TextView gameScoreView = (TextView) rootView.findViewById(R.id.game_score);
        TextView questionCountryView = (TextView) rootView.findViewById(R.id.question_country);
        TextView gameProgressView = (TextView) rootView.findViewById(R.id.game_progress);
        gameTimerView = (DonutProgress) rootView.findViewById(R.id.game_timer);


        // For the choice buttons
        choice1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence countryCapital = choice1View.getText();
                selectAnswer(countryCapital.toString());
            }
        });

        choice2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence countryCapital = choice2View.getText();
                selectAnswer(countryCapital.toString());
            }
        });
        choice3View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence countryCapital = choice3View.getText();
                selectAnswer(countryCapital.toString());
            }
        });
        choice4View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence countryCapital = choice4View.getText();
                selectAnswer(countryCapital.toString());
            }
        });
        //------------------------------------------------------------------------------------------
        // Set the views
        gameProgressView.setText(gameProgressText);
        questionView.setText(question);
        gameScoreView.setText(gameScoreText);
        questionCountryView.setText(countryName + "?");





        // Create a new timer for this question
        questionTimer = new CountDownTimer(11000, 1000) {
            // Count down the timer on every tick
            public void onTick(long millisUntilFinished) {

                gameTimerView.setInnerBottomText("");
                questionTimerIsRunning = true;
                int progress = (int) (long) ( millisUntilFinished / 1000);
                //Log.e(LOG_TAG, "progress " + progress);
                if ( progress <= 10 ){

                    gameTimerView.setProgress(progress);
                }
            }
            // When the timer finishes, mark the question as wrong and end the question
            public void onFinish() {
                Log.e(LOG_TAG, "onFinish " + true);
                questionTimerIsRunning= false;
                Log.e(LOG_TAG, "progress " + 0);
                gameTimerView.setProgress(0);
                gameTimerView.setInnerBottomTextSize(36);
                gameTimerView.setInnerBottomText("Time up!");
                // Time is up, clear any selected answers and answer the question (incorrect)
                Bundle b = getArguments();
                b.putString("selected_answer","");
                answerQuestion();
            }
        }.start();



    }

    private void slideInView(View view, int duration){
        TranslateAnimation animate = new TranslateAnimation(-view.getWidth(),0,0,0);
        animate.setDuration(duration);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    private void slideOutView(View view, int duration){
        TranslateAnimation animate = new TranslateAnimation(0,+view.getWidth(),0,0);
        animate.setDuration(duration);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.INVISIBLE);
    }

    public void slideToTop(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,view.getHeight(),344);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        //view.setVisibility(View.VISIBLE);
    }


    /**
     * selectAnswer
     * This method is called when clicking on a choice button
     * It sets the views that confirm your answer and allow you to submit
     * @param answer the answer text
     */
    private void selectAnswer(String answer){
        selectedAnswerView(answer);
        getArguments().putString("selected_answer", answer);

    }

    /**
     * selectedAnwswerView
     * This method builds the confirmation answer text, and button to submit the asnwer
     * @param answer pass the answer string to be displayed
     */
    private void selectedAnswerView(String answer){
        Log.e(LOG_TAG, "selectedAnswerView " + confirmAnswerTextView);
        RelativeLayout gameContent = (RelativeLayout) rootView.findViewById(R.id.game_content);
        //------------------------------------------------------------------------------------------
        if ( confirmAnswerTextView == null ) {
            Log.e(LOG_TAG, "confirmAnswerTextView " + true);
            // confirmAnswerTextView
            confirmAnswerTextView = new TextView(getActivity());
            RelativeLayout.LayoutParams confirmTextParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            confirmTextParams.addRule(RelativeLayout.BELOW, R.id.choice4);
            confirmAnswerTextView.setId(R.id.confirm_text);
            confirmAnswerTextView.setLayoutParams(confirmTextParams);

        }
        if ( rootView.findViewById(R.id.confirm_text) == null ){
            gameContent.addView(confirmAnswerTextView);
        }
        confirmAnswerTextView.setText("The capital is " + answer);
        //------------------------------------------------------------------------------------------
        // confirmAnswerButtonView
        if ( confirmAnswerButtonView == null ) {
            confirmAnswerButtonView = new Button(getActivity());
            RelativeLayout.LayoutParams actionAnswerParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            actionAnswerParams.addRule(RelativeLayout.BELOW, R.id.confirm_text);
            confirmAnswerButtonView.setId(R.id.action_answer);
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
        if ( rootView.findViewById(R.id.action_answer) == null ){
            gameContent.addView(confirmAnswerButtonView);
        }

    }


    /**
     * answerQuestions
     * This method is called when the choice to a question is confirmed
     */
    private void answerQuestion(){
        SharedPreferences.Editor editor = sharedPref.edit();


        // Cancel the timer
        Log.e(LOG_TAG, "questioNTimer " + questionTimer);
        questionTimer.cancel();

        // Get the current and selected answers to see if they match (evaluated answer)
        String selectedAnswer = getArguments().getString("selected_answer", "");
        String currentAnswer = getArguments().getString("current_answer", "");

        // Evaluate the answer
        Boolean test  = selectedAnswer.equals(currentAnswer);
        getArguments().putBoolean("evaluated_answer",test);

        // Evaluated answer text for keeping track of the score and displaying the result
        int gameCorrectAnswers = sharedPref.getInt("correct_answers",0);
        int gameIncorrectAnswers = sharedPref.getInt("incorrect_answers",0);
        CharSequence questionResultText;

        // The answer was correct
        if ( test ){gameCorrectAnswers = gameCorrectAnswers + 1 ; questionResultText = "Correct";}
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
        if ( gameProgress == gameProgressMax ) {
            endGame();
        }
        // Track game progress
        else {
            editor.putInt("game_progress", gameProgressCalc);
            editor.apply();
        }

        answerQuestionView(questionResultText.toString());;
        getArguments().clear();
    }

    /**
     * selectedAnwswerView
     * This method builds the confirmation answer text, and button to submit the asnwer
     */
    private void answerQuestionView(String resultText){
        final RelativeLayout gameContent = (RelativeLayout) rootView.findViewById(R.id.game_content);

        Button choice1View = (Button) rootView.findViewById(R.id.choice1);
        Button choice2View = (Button) rootView.findViewById(R.id.choice2);
        Button choice3View = (Button) rootView.findViewById(R.id.choice3);
        Button choice4View = (Button) rootView.findViewById(R.id.choice4);

        choice1View.setEnabled(false);
        choice2View.setEnabled(false);
        choice3View.setEnabled(false);
        choice4View.setEnabled(false);

        slideOutView(choice1View, 360);
        slideOutView(choice2View, 340);
        slideOutView(choice3View, 320);
        slideOutView(choice4View, 300);
        // Remove confirmation view
        gameContent.removeView(confirmAnswerTextView);
        gameContent.removeView(confirmAnswerButtonView);

        Log.e(LOG_TAG, "answerQuestionView " + true);

        //------------------------------------------------------------------------------------------
        if ( nextQuestionTextView == null ) {
            Log.e(LOG_TAG, "nextQuestionTextView null " + true);

            nextQuestionTextView = new TextView(getActivity());
            RelativeLayout.LayoutParams questionResultParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            questionResultParams.addRule(RelativeLayout.BELOW, R.id.question_country);
            nextQuestionTextView.setId(R.id.question_result);
            nextQuestionTextView.setLayoutParams(questionResultParams);

        }
        if ( rootView.findViewById(R.id.question_result) == null ){
            Log.e(LOG_TAG, "nextQuestionTextView on layout " + false);
            gameContent.addView(nextQuestionTextView);
        }
        nextQuestionTextView.setText(resultText);
        nextQuestionTextView.setText("The capital is " + answer);
        //------------------------------------------------------------------------------------------
        // nextQuestionButtonView
        if ( nextQuestionButtonView == null ) {
            Log.e(LOG_TAG, "nextQuestionButtonView null " + true);
            nextQuestionButtonView = new Button(getActivity());
            RelativeLayout.LayoutParams nextQuestionParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            nextQuestionParams.addRule(RelativeLayout.BELOW, R.id.question_result);
            nextQuestionButtonView.setId(R.id.action_next_question);
            nextQuestionButtonView.setLayoutParams(nextQuestionParams);
            nextQuestionButtonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0);

            //------------------------------------------------------------------------------------------
            // nextQuestionButtonView: Set on ClickListeners
            nextQuestionButtonView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    nextQuestionOnClickListener();
                }
            });
        }
        if ( rootView.findViewById(R.id.action_next_question) == null ){
            Log.e(LOG_TAG, "nextQuestionButtonView on layout " + false);
            gameContent.addView(nextQuestionButtonView);
        }

        nextQuestionButtonView.setText(R.string.action_next_question);
        slideInView(nextQuestionTextView, 360);
        slideInView(nextQuestionButtonView, 340);
    }

    /**
     * nextQuestionOnClickListener
     * Attached to the "Next Question" button
     * Clears the Bundle, gets a new question, and sets the views
     */
    private void nextQuestionOnClickListener(){
        slideOutView(nextQuestionTextView, 360);
        slideOutView(nextQuestionButtonView, 360);
        gameContent.removeView(nextQuestionTextView);
        gameContent.removeView(nextQuestionButtonView);
        getArguments().clear();
        getQuestion(true);
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

    }

    /**
     * newQuestion
     * This method creates a new Question object and returns the ContentValues from it
     * It also creates a CountDownTimer to drive the timer and its display on the question
     * @return a ContentValues Object with the question, answer, and choices.
     */
    private ContentValues newQuestion(){
        String gameMode = "";
        Question questionObj = new Question(this.getContext());
        ContentValues contentValues = questionObj.getQuestion(gameMode);
        Log.e(LOG_TAG, "questionTimerIsRunning " + questionTimerIsRunning);
        // If a new question is requested and there is a timer running, cancel it first
        if ( questionTimerIsRunning ) {
            questionTimerIsRunning= false;
            questionTimer.cancel();
        }

        return  contentValues;
    }

}
