package rocks.throw20.funwithcountries;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.w3c.dom.Text;


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
    private Button nextQuestionView;

    private TextView actionConfirmationView;
    private Button actionAnswerView;
    private Button choice1View;
    private Button choice2View;
    private Button choice3View;
    private Button choice4View;

    private String countryName;
    private String countryCapital;
    private String question;

    private String answer;
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
        //Log.e(LOG_TAG, "onCreate " + savedInstanceState);
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
        setViews();
        return rootView;
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
    private void setViews(){

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
        String selectedAnswer = b.getString("selected_answer");
        String evaluatedAnswer = b.getString("evaluated_answer");
        //------------------------------------------------------------------------------------------
        // Get all the views
        TextView questionView = (TextView) rootView.findViewById(R.id.question);
        TextView gameScoreView = (TextView) rootView.findViewById(R.id.game_score);
        TextView questionCountryView = (TextView) rootView.findViewById(R.id.question_country);
        TextView gameProgressView = (TextView) rootView.findViewById(R.id.game_progress);
        TextView gameQuestionResultView = (TextView) rootView.findViewById(R.id.question_result);
        gameTimerView = (DonutProgress) rootView.findViewById(R.id.game_timer);
        choice1View = (Button) rootView.findViewById(R.id.choice1);
        choice2View = (Button) rootView.findViewById(R.id.choice2);
        choice3View = (Button) rootView.findViewById(R.id.choice3);
        choice4View = (Button) rootView.findViewById(R.id.choice4);
        actionConfirmationView = (TextView) rootView.findViewById(R.id.confirm_text);
        actionAnswerView = (Button) rootView.findViewById(R.id.action_answer);
        nextQuestionView = (Button) rootView.findViewById(R.id.action_next_question);
        //------------------------------------------------------------------------------------------
        // Set the visibility
        // Of the answer confirmation and answer actions views
        if ( selectedAnswer != null ) {
            actionConfirmationView.setVisibility(View.VISIBLE);
            actionAnswerView.setVisibility(View.VISIBLE);
        }else{
            actionConfirmationView.setVisibility(View.GONE);
            actionAnswerView.setVisibility(View.GONE);
        }
        // Of the next question button
        if ( evaluatedAnswer != null ) {
            gameQuestionResultView.setVisibility(View.VISIBLE);
            nextQuestionView.setVisibility(View.VISIBLE);
        } else{
            gameQuestionResultView.setVisibility(View.GONE);
            nextQuestionView.setVisibility(View.GONE);
        }
        //------------------------------------------------------------------------------------------
        // Set on ClickListeners
        // For the answer and next question buttons
        actionAnswerView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                answerQuestion();
            }
        });
        nextQuestionView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextQuestionOnClickListener();
            }
        });
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

        choice1View.setEnabled(true);
        choice2View.setEnabled(true);
        choice3View.setEnabled(true);
        choice4View.setEnabled(true);

        choice1View.setText(choice1);
        choice2View.setText(choice2);
        choice3View.setText(choice3);
        choice4View.setText(choice4);

    }

    /**
     * answerQuestions
     * This method is called when the choice to a question is confirmed
     */
    private void answerQuestion(){
        SharedPreferences.Editor editor = sharedPref.edit();
        choice1View = (Button) rootView.findViewById(R.id.choice1);
        choice2View = (Button) rootView.findViewById(R.id.choice2);
        choice3View = (Button) rootView.findViewById(R.id.choice3);
        choice4View = (Button) rootView.findViewById(R.id.choice4);

        choice1View.setEnabled(false);
        choice2View.setEnabled(false);
        choice3View.setEnabled(false);
        choice4View.setEnabled(false);

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


        nextQuestionView = (Button) rootView.findViewById(R.id.action_next_question);
        actionAnswerView = (Button) rootView.findViewById(R.id.action_answer);
        TextView confirmTextView = (TextView) rootView.findViewById(R.id.confirm_text);
        TextView questionResultView = (TextView) rootView.findViewById(R.id.question_result);

        questionResultView.setVisibility(View.VISIBLE);
        nextQuestionView.setVisibility(View.VISIBLE);
        confirmTextView.setVisibility(View.GONE);
        actionAnswerView.setVisibility(View.GONE);

        // Set the evaluation result (Correct vs Incorrect)
        questionResultView.setText(questionResultText);



        // Clear the Bundle
        getArguments().clear();
    }

    /**
     * nextQuestionOnClickListener
     * Attached to the "Next Question" button
     * Clears the Bundle, gets a new question, and sets the views
     */
    private void nextQuestionOnClickListener(){
        getArguments().clear();
        getQuestion(true);
        setViews();
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
        // Create a new timer for this question
        questionTimer = new CountDownTimer(10000, 1000) {
            // Count down the timer on every tick
            public void onTick(long millisUntilFinished) {
                Log.e(LOG_TAG, "onTick " + questionTimerIsRunning);
                gameTimerView.setInnerBottomText("");
                questionTimerIsRunning = true;
                int progress = (int) (long) (millisUntilFinished / 1000);
                gameTimerView.setProgress(progress);
            }
            // When the timer finishes, mark the question as wrong and end the question
            public void onFinish() {
                Log.e(LOG_TAG, "onFinish " + questionTimerIsRunning);
                questionTimerIsRunning= false;
                gameTimerView.setProgress(0);
                gameTimerView.setInnerBottomTextSize(36);
                gameTimerView.setInnerBottomText("Time up!");
                // Time is up, clear any selected answers and answer the question (incorrect)
                Bundle b = getArguments();
                b.putString("selected_answer","");
                answerQuestion();
            }
        }.start();

        return  contentValues;
    }

    /**
     * selectAnswer
     * This method is called when clicking on a choice button
     * It sets the views that confirm your answer and allow you to submit
     * @param answer the answer text
     */
    private void selectAnswer(String answer){
        actionAnswerView.setVisibility(View.VISIBLE);
        actionConfirmationView.setVisibility(View.VISIBLE);
        actionConfirmationView.setText("The capital is " + answer);
        getArguments().putString("selected_answer", answer);

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

    }

}
