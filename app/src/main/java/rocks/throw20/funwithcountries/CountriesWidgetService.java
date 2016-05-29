package rocks.throw20.funwithcountries;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by josel on 5/29/2016.
 */
public class CountriesWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CountriesWidgetDataProvider(getApplicationContext());
    }
}
