package rocks.throw20.funwithcountries;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


/**
 * Created by josel on 5/29/2016.
 */
public class CountriesWidgetProvider extends AppWidgetProvider {
    public static final String LOG_TAG = "WidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        for (int widgetId : appWidgetIds) {
            RemoteViews mView = initViews(context);


            appWidgetManager.updateAppWidget(widgetId, mView);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews initViews(Context context) {

        RemoteViews mView = new RemoteViews(context.getPackageName(), R.layout.countries_widget);
        Intent intent = new Intent(context, CountriesWidgetService.class);
        mView.setRemoteAdapter(R.id.widgetCollectionList, intent);

        return mView;
    }
}
