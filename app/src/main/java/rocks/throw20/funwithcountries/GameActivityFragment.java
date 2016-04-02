package rocks.throw20.funwithcountries;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by josel on 4/2/2016.
 */
public class GameActivityFragment extends Fragment{
    private static final String LOG_TAG = GameActivityFragment.class.getSimpleName();

    View rootView;
    Button nextQuestionView;

    TextView actionConfirmation;
    TextView questionView;
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
            getQuestionValues(true);
        }
        else{
            getQuestionValues(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "container " + container);
        Log.e(LOG_TAG, "getArguments " + getArguments());
        rootView = inflater.inflate(R.layout.fragment_game, container, false);
        Log.e(LOG_TAG, "savedInstanceState " + savedInstanceState);


        setViews();


        return rootView;
    }

    private void getQuestionValues(Boolean isnew){
        Log.e(LOG_TAG, "getQuestionValues " + true);
        Bundle args = getArguments();
        Log.e(LOG_TAG, "args " + args);
        if ( isnew ) {
            ContentValues contentValues = nextQuestion();
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
            args.putString("question", question);
            args.putString("answer", answer);
            args.putString("choice1", choice1);
            args.putString("choice2", choice2);
            args.putString("choice3", choice3);
            args.putString("choice4", choice4);


            // Save the current answer
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("currentAnswer", countryCapital);
            editor.apply();


        }


        Log.e(LOG_TAG, "country_name " + countryName);
        Log.e(LOG_TAG, "country_capital " + countryCapital);
        Log.e(LOG_TAG, "question " + question);
        Log.e(LOG_TAG, "answer " + answer);
        Log.e(LOG_TAG, "choice1 " + choice1);
        Log.e(LOG_TAG, "choice2 " + choice2);
        Log.e(LOG_TAG, "choice3 " + choice3);
        Log.e(LOG_TAG, "choice4 " + choice4);


    }


    private void setViews(){
        Log.e(LOG_TAG, "setViews " + true);
        Log.e(LOG_TAG, "setViews " + getArguments());
        countryName = getArguments().getString("country_name");
        countryCapital = getArguments().getString("country_capital");
        question = getArguments().getString("question");
        answer = getArguments().getString("answer");
        choice1 = getArguments().getString("choice1");
        choice2 = getArguments().getString("choice2");
        choice3 = getArguments().getString("choice3");
        choice4 = getArguments().getString("choice4");


        questionView = (TextView) rootView.findViewById(R.id.question);
        choice1View = (Button) rootView.findViewById(R.id.choice1);
        choice2View = (Button) rootView.findViewById(R.id.choice2);
        choice3View = (Button) rootView.findViewById(R.id.choice3);
        choice4View = (Button) rootView.findViewById(R.id.choice4);


        actionConfirmation = (TextView) rootView.findViewById(R.id.confirm_text);
        actionAnswerView = (Button) rootView.findViewById(R.id.action_answer);
        nextQuestionView = (Button) rootView.findViewById(R.id.action_next_question);

        actionConfirmation.setVisibility(View.GONE);
        actionAnswerView.setVisibility(View.GONE);


        actionAnswerView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                answerQuestion();
            }
        });

        nextQuestionView.setVisibility(View.GONE);
        nextQuestionView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e(LOG_TAG, "onClick " + true);
                getArguments().clear();
                nextQuestion();
                setViews();
            }
        });

        choice1View.setPressed(false);
        choice2View.setPressed(false);
        choice3View.setPressed(false);
        choice4View.setPressed(false);

        choice1View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CharSequence countryCapital = choice1View.getText();
                choice1View.setPressed(true);
                choice2View.setPressed(false);
                choice3View.setPressed(false);
                choice4View.setPressed(false);
                actionAnswerView.setVisibility(View.VISIBLE);
                actionConfirmation.setVisibility(View.VISIBLE);
                actionConfirmation.setText("The capital is " + countryCapital);
                // Store the selection in SharedPreferences
                selectAnswer(countryCapital.toString());
                return true;
            }

        });
        choice2View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CharSequence countryCapital = choice2View.getText();
                choice1View.setPressed(false);
                choice2View.setPressed(true);
                choice3View.setPressed(false);
                choice4View.setPressed(false);
                actionAnswerView.setVisibility(View.VISIBLE);
                actionConfirmation.setVisibility(View.VISIBLE);
                actionConfirmation.setText("The capital is " + countryCapital);
                // Store the selection in SharedPreferences
                selectAnswer(countryCapital.toString());
                return true;
            }
        });
        choice3View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CharSequence countryCapital = choice3View.getText();
                choice1View.setPressed(false);
                choice2View.setPressed(false);
                choice3View.setPressed(true);
                choice4View.setPressed(false);
                actionAnswerView.setVisibility(View.VISIBLE);
                actionConfirmation.setVisibility(View.VISIBLE);
                actionConfirmation.setText("The capital is " + countryCapital);
                // Store the selection in SharedPreferences
                selectAnswer(countryCapital.toString());
                return true;
            }
        });
        choice4View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CharSequence countryCapital = choice4View.getText();
                choice1View.setPressed(false);
                choice2View.setPressed(false);
                choice3View.setPressed(false);
                choice4View.setPressed(true);
                actionAnswerView.setVisibility(View.VISIBLE);
                actionConfirmation.setVisibility(View.VISIBLE);
                actionConfirmation.setText("The capital is " + countryCapital);
                // Store the selection in SharedPreferences
                selectAnswer(countryCapital.toString());
                return true;
            }
        });


        questionView.setText(question);

        choice1View.setEnabled(true);
        choice2View.setEnabled(true);
        choice3View.setEnabled(true);
        choice4View.setEnabled(true);


        choice1View.setText(choice1);
        choice2View.setText(choice2);
        choice3View.setText(choice3);
        choice4View.setText(choice4);
    }

    private void answerQuestion(){

        choice1View = (Button) rootView.findViewById(R.id.choice1);
        choice2View = (Button) rootView.findViewById(R.id.choice2);
        choice3View = (Button) rootView.findViewById(R.id.choice3);
        choice4View = (Button) rootView.findViewById(R.id.choice4);

        choice1View.setEnabled(false);
        choice2View.setEnabled(false);
        choice3View.setEnabled(false);
        choice4View.setEnabled(false);


        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String selectedAnswer = sharedPref.getString("selectedAnswer", "");
        String currentAnswer = sharedPref.getString("currentAnswer", "");

        boolean evaluateAnswer = selectedAnswer.equals(currentAnswer);

        CharSequence text;
        if ( evaluateAnswer ){text = "Correct";}
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
        getQuestionValues(true);
        setViews();

    }

    private ContentValues nextQuestion(){
        Log.e(LOG_TAG, "nextQuestion " + true);
        String gameMode = "";


        // Get a new question
        Question questionObj = new Question(this.getContext());
        ContentValues contentValues = questionObj.getQuestion(gameMode);
        return  contentValues;

    }

    private void selectAnswer(String answer){
        Log.e(LOG_TAG, "selectAnswer " + true);
        // Store the selection in SharedPreferences
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit ();
        editor.putString("selectedAnswer",answer);
        editor.apply();
    }

}
