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
    public GameActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "container " + container);

        rootView = inflater.inflate(R.layout.fragment_game, container, false);
        nextQuestion(rootView, getArguments());

        Button actionAnswerView = (Button) rootView.findViewById(R.id.action_answer);
        actionAnswerView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                answerQuestion();
            }
        });

        final Button nextQuestionView = (Button) rootView.findViewById(R.id.action_next_question);
        nextQuestionView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextQuestion(rootView,getArguments());
            }
        });
        return rootView;
    }

    private void answerQuestion(){


        final Button choice1View = (Button) rootView.findViewById(R.id.choice1);
        final Button choice2View = (Button) rootView.findViewById(R.id.choice2);
        final Button choice3View = (Button) rootView.findViewById(R.id.choice3);
        final Button choice4View = (Button) rootView.findViewById(R.id.choice4);

        choice1View.setEnabled(false);
        choice2View.setEnabled(false);
        choice3View.setEnabled(false);
        choice4View.setEnabled(false);


        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String selectedAnswer = sharedPref.getString("selectedAnswer", "");
        String currentAnswer = sharedPref.getString("currentAnswer", "");

        boolean evaluateAnswer = selectedAnswer.equals(currentAnswer);

        CharSequence text;
        if ( evaluateAnswer ){
            text = "Correct";
        }
        else{
            text = "Incorrect";
        }

        Context context = this.getContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        final Button nextQuestionView = (Button) rootView.findViewById(R.id.action_next_question);
        final Button actionAnswerView = (Button) rootView.findViewById(R.id.action_answer);
        final TextView confirmTextView = (TextView) rootView.findViewById(R.id.confirm_text);

        nextQuestionView.setVisibility(View.VISIBLE);
        confirmTextView.setVisibility(View.GONE);
        actionAnswerView.setVisibility(View.GONE);



    }


    private void nextQuestion(final View rootView, Bundle args){
        String gameMode = getArguments().getString("gameMode");
        String gameTitle = getArguments().getString("gameTile");

        Question questionObj = new Question(this.getContext());
        ContentValues contentValues = questionObj.getQuestion(gameMode);

        String countryName = contentValues.getAsString("country_name");
        String countryCapital = contentValues.getAsString("country_capital");

        String question = contentValues.getAsString("question");
        String answer = contentValues.getAsString("answer");
        String choice1 = contentValues.getAsString("choice1");
        String choice2 = contentValues.getAsString("choice2");
        String choice3 = contentValues.getAsString("choice3");
        String choice4 = contentValues.getAsString("choice4");

        // Save the current answer
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit ();
        editor.putString("currentAnswer",countryCapital);
        editor.apply();

        Log.e(LOG_TAG, "country_name " + countryName);
        Log.e(LOG_TAG, "country_capital " + countryCapital);
        Log.e(LOG_TAG, "question " + question);
        Log.e(LOG_TAG, "answer " + answer);
        Log.e(LOG_TAG, "choice1 " + choice1);
        Log.e(LOG_TAG, "choice2 " + choice2);
        Log.e(LOG_TAG, "choice3 " + choice3);
        Log.e(LOG_TAG, "choice4 " + choice4);

        TextView questionView = (TextView) rootView.findViewById(R.id.question);
        final Button choice1View = (Button) rootView.findViewById(R.id.choice1);
        final Button choice2View = (Button) rootView.findViewById(R.id.choice2);
        final Button choice3View = (Button) rootView.findViewById(R.id.choice3);
        final Button choice4View = (Button) rootView.findViewById(R.id.choice4);

        final TextView actionConfirmation = (TextView) rootView.findViewById(R.id.confirm_text);
        final Button actionAnswer = (Button) rootView.findViewById(R.id.action_answer);

        actionConfirmation.setVisibility(View.GONE);
        actionAnswer.setVisibility(View.GONE);

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
                actionAnswer.setVisibility(View.VISIBLE);
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
                actionAnswer.setVisibility(View.VISIBLE);
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
                actionAnswer.setVisibility(View.VISIBLE);
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
                actionAnswer.setVisibility(View.VISIBLE);
                actionConfirmation.setVisibility(View.VISIBLE);
                actionConfirmation.setText("The capital is " + countryCapital);
                // Store the selection in SharedPreferences
                selectAnswer(countryCapital.toString());
                return true;
            }
        });


        Log.e(LOG_TAG, "questionView " + questionView);

        questionView.setText(question);

        choice1View.setEnabled(true);
        choice2View.setEnabled(true);
        choice3View.setEnabled(true);
        choice4View.setEnabled(true);


        choice1View.setText(choice1);
        choice2View.setText(choice2);
        choice3View.setText(choice3);
        choice4View.setText(choice4);

        final Button nextQuestionView = (Button) rootView.findViewById(R.id.action_next_question);
        nextQuestionView.setVisibility(View.GONE);

    }

    private void selectAnswer(String answer){
        // Store the selection in SharedPreferences
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit ();
        editor.putString("selectedAnswer",answer);
        editor.apply();
    }

}
