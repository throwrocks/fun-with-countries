package rocks.throw20.funwithcountries;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by josel on 3/26/2016.
 * CountriesListAdapter
 * Adapter class that extends RecyclerView.Adapter
 * Configured to be updated via a Fragment LoaderManager
 */
public class CountriesListAdapter extends RecyclerView.Adapter<CountriesListAdapter.ViewHolder> {

    // Set local variables
    private final String LOG_TAG = CountriesListAdapter.class.getSimpleName();
    private boolean mTwoPane;
    private Cursor mCursor;
    private final Context mContext;

    //The Adapter Constructor
    public CountriesListAdapter(Context context, Cursor countriesCursor) {
        mContext = context;
        mCursor = countriesCursor;
        Log.e(LOG_TAG, "Constructor -> " + true);
    }

    /**
     * ViewHolder
     * Defines the view to be recycled
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView viewCountryId;
        public final TextView viewCountryName;
        public final TextView viewCountryCapital;
        public final TextView viewCountrySubRegion;

        public ViewHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.country_item);
            //viewCountryId = (TextView) view.findViewById(R.id.country_id);
            viewCountryName = (TextView) view.findViewById(R.id.country_name);
            viewCountryCapital = (TextView) view.findViewById(R.id.country_capital);
            viewCountrySubRegion = (TextView) view.findViewById(R.id.country_subregion);
        }

    }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Log.e(LOG_TAG, "onCreateViewHolder -> " + true);
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_countries, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

           // Log.e(LOG_TAG, "onBindviewHolder -> " + true);
            mCursor.moveToPosition(position);

            // The country id
            //String countryId = mCursor.getString(1);

            // The country name
            String countryName = mCursor.getString(2);
            // The country capital
            String countryCapital = mCursor.getString(3);
            if ( countryCapital.isEmpty() ){ countryCapital ="N/A";}
            String countryCapitalLine = "Capital: " + countryCapital;
            // The country subregion
            String countrySubregion = mCursor.getString(5);
            if ( countrySubregion.isEmpty() ){ countrySubregion ="Unknown";}
            String countrySubregionLine = "Subregion: " + countrySubregion;

            //holder.viewCountryId.setText(mCursor.getString(1));
            holder.viewCountryName.setText(countryName);
            holder.viewCountryCapital.setText(countryCapitalLine);
            holder.viewCountrySubRegion.setText(countrySubregionLine);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        CharSequence text = "Toast";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(mContext, text, duration);
                        toast.show();
                }
            });
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

