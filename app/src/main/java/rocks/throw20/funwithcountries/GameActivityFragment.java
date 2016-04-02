package rocks.throw20.funwithcountries;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by josel on 4/2/2016.
 */
public class GameActivityFragment extends Fragment{
    private static final String LOG_TAG = GameActivityFragment.class.getSimpleName();

    public GameActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);


        String gameMode = getArguments().getString("gameMode");
        String gameTitle = getArguments().getString("gameTile");

        Question questionObj = new Question(this.getContext());
        ContentValues contentValues = questionObj.getQuestion(gameMode);

        String countryName = contentValues.getAsString("country_name");

        String question = contentValues.getAsString("question");
        String answer = contentValues.getAsString("answer");
        String choice1 = contentValues.getAsString("choice1");
        String choice2 = contentValues.getAsString("choice2");
        String choice3 = contentValues.getAsString("choice3");
        String choice4 = contentValues.getAsString("choice4");

        Log.e(LOG_TAG, "question " + question);

        TextView questionView = (TextView) rootView.findViewById(R.id.question);
        Button choice1View = (Button) rootView.findViewById(R.id.choice1);
        Button choice2View = (Button) rootView.findViewById(R.id.choice2);
        Button choice3View = (Button) rootView.findViewById(R.id.choice3);
        Button choice4View = (Button) rootView.findViewById(R.id.choice4);

        Log.e(LOG_TAG, "questionView " + questionView);

        questionView.setText(question);
        choice1View.setText(choice1);
        choice2View.setText(choice2);
        choice3View.setText(choice3);
        choice4View.setText(choice4);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
