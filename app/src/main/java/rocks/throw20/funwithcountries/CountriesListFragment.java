package rocks.throw20.funwithcountries;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
 * Created by josel on 3/27/2016.
 */
public class CountriesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_TAG = CountriesListFragment.class.getSimpleName();
    Cursor mCursor;
    private CountriesListAdapter countriesAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        countriesAdapter = new CountriesListAdapter(getContext(), mCursor);

        Log.e(LOG_TAG, "onCreateView -> " + true);
        View rootView = inflater.inflate(R.layout.fragment_countries_list, container, false);
        View recyclerView = rootView.findViewById(R.id.list);
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
        recyclerView.setAdapter(countriesAdapter);

    }
    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /** DetailFragmentCallback for when an item has been selected.*/
        void onItemSelected(Uri dateUri);
    }
    public CountriesListFragment(){

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;
        cursorLoader = new CursorLoader (
                getActivity(),
                Contract.CountryEntry.buildCountries(),
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

        countriesAdapter.changeCursor(data);
        countriesAdapter.notifyDataSetChanged();


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
