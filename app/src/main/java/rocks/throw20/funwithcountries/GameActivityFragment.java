package rocks.throw20.funwithcountries;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.w3c.dom.Text;


/**
 * Created by josel on 4/2/2016.
 */
public class GameActivityFragment extends Fragment{
    private static final String LOG_TAG = GameActivityFragment.class.getSimpleName();

    View rootView;

    TextView gameProgress;
    DonutProgress gameIimer;

    Button nextQuestionView;

    TextView actionConfirmation;
    TextView questionView;
    TextView questionCountryView;
    Button actionAnswerView;
    TextView confirmTextView;
    Button choice1View;
    Button choice2View;
    Button choice3View;
    Button choice4View;

    String countryName;
    String countryCapital;
    String question;

    String answer;
    String choice1;
    String choice2;
    String choice3;
    String choice4;

    String selectedAnswer;
    String currentAnswer;
    String evaluatedAnswer;


    public GameActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG_TAG, "onCreate " + savedInstanceState);
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

    private void getQuestion(Boolean isnew){
        Bundle args = getArguments();
        if ( isnew ) {
            ContentValues contentValues = newQuestion();
            Log.e(LOG_TAG, "new content " + true);
            // Set the question variables
            countryName = contentValues.getAsString("country_name");
            countryCapital = contentValues.getAsString("country_capital");
            question = contentValues.getAsString("question");
            answer = contentValues.getAsString("answer");
            choice1 = contentValues.getAsString("choice1");
            choice2 = contentValues.getAsString("choice2");
            choice3 = contentValues.getAsString("choice3");
            choice4 = contentValues.getAsString("choice4");
            // Stored them in the bundle
            args.putString("country_name", countryName);
            args.putString("country_capital", countryCapital);
            args.putString("current_answer", countryCapital);
            args.putString("question", question);
            args.putString("answer", answer);
            args.putString("choice1", choice1);
            args.putString("choice2", choice2);
            args.putString("choice3", choice3);
            args.putString("choice4", choice4);

        }

    }


    private void setViews(){

        countryName = getArguments().getString("country_name");
        countryCapital = getArguments().getString("country_capital");
        question = getArguments().getString("question");
        answer = getArguments().getString("answer");
        choice1 = getArguments().getString("choice1");
        choice2 = getArguments().getString("choice2");
        choice3 = getArguments().getString("choice3");
        choice4 = getArguments().getString("choice4");
        selectedAnswer = getArguments().getString("selected_answer");
        evaluatedAnswer = getArguments().getString("evaluated_answer");
        Log.e(LOG_TAG, "evaluated answer " + evaluatedAnswer);

        questionView = (TextView) rootView.findViewById(R.id.question);
        questionCountryView = (TextView) rootView.findViewById(R.id.question_country);

        gameProgress = (TextView) rootView.findViewById(R.id.game_progress);
        gameIimer = (DonutProgress) rootView.findViewById(R.id.game_timer);

        choice1View = (Button) rootView.findViewById(R.id.choice1);
        choice2View = (Button) rootView.findViewById(R.id.choice2);
        choice3View = (Button) rootView.findViewById(R.id.choice3);
        choice4View = (Button) rootView.findViewById(R.id.choice4);


        actionConfirmation = (TextView) rootView.findViewById(R.id.confirm_text);
        actionAnswerView = (Button) rootView.findViewById(R.id.action_answer);
        nextQuestionView = (Button) rootView.findViewById(R.id.action_next_question);

        if ( selectedAnswer != null ) {
            actionConfirmation.setVisibility(View.VISIBLE);
            actionAnswerView.setVisibility(View.VISIBLE);
        }else{
            actionConfirmation.setVisibility(View.GONE);
            actionAnswerView.setVisibility(View.GONE);
        }

        if ( evaluatedAnswer != null ){
            nextQuestionView.setVisibility(View.VISIBLE);
        } else{
            nextQuestionView.setVisibility(View.GONE);
        }


        actionAnswerView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                answerQuestion();
            }
        });


        nextQuestionView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {nextQuestionOnClickListener();}
        });


        questionView.setText(question);
        questionCountryView.setText(countryName + "?");

        choice1View.setEnabled(true);
        choice2View.setEnabled(true);
        choice3View.setEnabled(true);
        choice4View.setEnabled(true);

        choice1View.setText(choice1);
        choice2View.setText(choice2);
        choice3View.setText(choice3);
        choice4View.setText(choice4);


        //------------------------------------------------------------------------------------------
        // Choice 1
        //------------------------------------------------------------------------------------------
        choice1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence countryCapital = choice1View.getText();
                selectAnswer(countryCapital.toString());
            }
        });

        //------------------------------------------------------------------------------------------
        // Choice 2
        //------------------------------------------------------------------------------------------

        choice2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence countryCapital = choice2View.getText();
                selectAnswer(countryCapital.toString());
            }
        });

        //------------------------------------------------------------------------------------------
        // Choice 3
        //------------------------------------------------------------------------------------------

        choice3View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence countryCapital = choice3View.getText();
                selectAnswer(countryCapital.toString());
            }
        });

        //------------------------------------------------------------------------------------------
        // Choice 4
        //------------------------------------------------------------------------------------------

        choice4View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence countryCapital = choice4View.getText();
                selectAnswer(countryCapital.toString());
            }
        });
    }

    /**
     * answerQuestions
     *
     */
    private void answerQuestion(){

        choice1View = (Button) rootView.findViewById(R.id.choice1);
        choice2View = (Button) rootView.findViewById(R.id.choice2);
        choice3View = (Button) rootView.findViewById(R.id.choice3);
        choice4View = (Button) rootView.findViewById(R.id.choice4);

        choice1View.setEnabled(false);
        choice2View.setEnabled(false);
        choice3View.setEnabled(false);
        choice4View.setEnabled(false);

        String selectedAnswer = getArguments().getString("selected_answer", "");
        String currentAnswer = getArguments().getString("current_answer", "");

        Boolean test  = selectedAnswer.equals(currentAnswer);
        getArguments().putBoolean("evaluated_answer",test);

        CharSequence text;
        if ( test ){text = "Correct";}
        else{text = "Incorrect";}

        Context context = this.getContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        nextQuestionView = (Button) rootView.findViewById(R.id.action_next_question);
        actionAnswerView = (Button) rootView.findViewById(R.id.action_answer);
        confirmTextView = (TextView) rootView.findViewById(R.id.confirm_text);

        nextQuestionView.setVisibility(View.VISIBLE);
        confirmTextView.setVisibility(View.GONE);
        actionAnswerView.setVisibility(View.GONE);

        getArguments().clear();


    }

    private void nextQuestionOnClickListener(){
        getArguments().clear();
        getQuestion(true);
        setViews();
    }



    private ContentValues newQuestion(){

        String gameMode = "";
        Question questionObj = new Question(this.getContext());
        ContentValues contentValues = questionObj.getQuestion(gameMode);

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                int progress = (int) (long) (millisUntilFinished / 1000);
                gameIimer.setProgress(progress);
            }
            public void onFinish() {
                gameIimer.setProgress(0);
                gameIimer.setInnerBottomTextSize(36);
                gameIimer.setInnerBottomText("Time up!");

            }
        }.start();

        return  contentValues;

    }


    private void selectAnswer(String answer){
        actionAnswerView.setVisibility(View.VISIBLE);
        actionConfirmation.setVisibility(View.VISIBLE);
        actionConfirmation.setText("The capital is " + answer);
        getArguments().putString("selected_answer", answer);

    }

}
