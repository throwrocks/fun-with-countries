package rocks.throw20.funwithcountries;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rocks.throw20.funwithcountries.Data.Contract;

/**
 * A placeholder fragment containing a simple view.
 */
public class ScoresActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = ScoresActivityFragment.class.getSimpleName();
    private Cursor mCursor;
    private ScoresAdapter scoresAdapter;

    public ScoresActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        scoresAdapter = new ScoresAdapter(getContext(), mCursor);

        Log.e(LOG_TAG, "onCreateView -> " + true);
        View rootView = inflater.inflate(R.layout.fragment_scores_list, container, false);
        View recyclerView = rootView.findViewById(R.id.scores_list);
        if ( recyclerView != null ) {
            setupRecyclerView((RecyclerView) recyclerView);
        }
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
        Log.e(LOG_TAG, "onActivityCreated -> " + true);
    }


    /**
     * setupRecyclerView
     * Method to set the adapter on the RecyclerView
     * @param recyclerView the recycler view
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Log.e(LOG_TAG, "setupRecyclerView -> " + true);
        Log.e(LOG_TAG, "recyclerView -> " + recyclerView);
        recyclerView.setAdapter(scoresAdapter);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;
        cursorLoader = new CursorLoader (
                getActivity(),
                Contract.ScoreEntry.buildScores(),
                null,
                null,
                null,
                null);

        Log.e(LOG_TAG, "onCreateLoader -> " + true);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Log.e(LOG_TAG, "onLoadFinished -> " + true);

        scoresAdapter.changeCursor(data);
        scoresAdapter.notifyDataSetChanged();


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
