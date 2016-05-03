package rocks.throw20.funwithcountries;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by josel on 5/1/2016.
 */
public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder> {
    private final String LOG_TAG = ScoresAdapter.class.getSimpleName();

    private final Context mContext;
    private Cursor mCursor;

    //The Adapter Constructor
    public ScoresAdapter(Context context, Cursor scoresCursor) {
        mContext = context;
        mCursor = scoresCursor;
        Log.e(LOG_TAG, "Constructor -> " + true);
    }

    /**
     * ViewHolder
     * Defines the view to be recycled
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView viewScoresDate;
        public final TextView viewScoresGameMode;
        public final TextView viewScoresGameScore;
        public final TextView viewScoresGameScorePercent;

        public ViewHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.score_item);
            viewScoresDate = (TextView) view.findViewById(R.id.score_date);
            viewScoresGameMode = (TextView) view.findViewById(R.id.score_game_mode);
            viewScoresGameScore = (TextView) view.findViewById(R.id.score_game_score);
            viewScoresGameScorePercent = (TextView) view.findViewById(R.id.score_game_percent);

        }

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(LOG_TAG, "onCreateViewHolder -> " + true);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_scores, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        Log.e(LOG_TAG, "bind " + mCursor);

        String scoreDate = mCursor.getString(1);
        String scoreGameMode = mCursor.getString(2);
        String scoreCountQuestions = mCursor.getString(3);
        String scoreGameScore = mCursor.getString(4);


        // Convert the gameMode to title case
        StringBuilder s1 = new StringBuilder(scoreGameMode);
        s1.replace(0, s1.length(), s1.toString().toLowerCase());
        s1.setCharAt(0, Character.toTitleCase(s1.charAt(0)));
        scoreGameMode = s1.toString();

        // Build the score display
        String scoreDisplay = scoreGameScore + "/" + scoreCountQuestions;

        // Get the score %
        double num = Double.parseDouble(scoreGameScore) / Double.parseDouble(scoreCountQuestions);
        NumberFormat defaultFormat = NumberFormat.getPercentInstance();
        defaultFormat.setMinimumFractionDigits(0);
        String scorePercent = defaultFormat.format(num);


        //Log.e(LOG_TAG, "scoreDate " + scoreDate);
        //Log.e(LOG_TAG, "scoreGameMode " + scoreGameMode);
        //Log.e(LOG_TAG, "scoreGameScore " + scoreGameScore);
        Log.e(LOG_TAG, "num " + Integer.parseInt(scoreGameScore));
        Log.e(LOG_TAG, "num " + Integer.parseInt(scoreCountQuestions));
        Log.e(LOG_TAG, "num " + num);

        holder.viewScoresDate.setText(scoreDate);
        holder.viewScoresGameMode.setText(scoreGameMode);
        holder.viewScoresGameScore.setText(scoreDisplay);
        holder.viewScoresGameScorePercent.setText(scorePercent);
    }


    @Override
    public int getItemCount() {
        //Log.e(LOG_TAG, "getItemCount -> " + mCursor.getCount());
        if ( mCursor != null ) {
            return mCursor.getCount();
        }else{
            return 0;
        }
    }

    /**
     * changeCursor
     * Called from the fragment to change the cursor once the data is loaded
     */
    public void changeCursor(Cursor cursor) {
        Log.e(LOG_TAG, "changeCursor -> " + cursor.getCount());
        mCursor = cursor;
    }


}
