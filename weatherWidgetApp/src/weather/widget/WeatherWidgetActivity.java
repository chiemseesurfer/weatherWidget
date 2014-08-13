/**
 * @file
 * 
 */
package weather.widget;

import weather.widget.utils.DataBaseHelper;
import weather.widget.utils.UpdateWidgetService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/** \mainpage Android Weather Widget
 *
 * \section intro_sec Introduction
 *
 * This Widget offers the possibility to display Weatherdata which are measured by
 * the Arduino Weatherstation.
 *
 * \section install_sec Installation
 *
 * The Widget is available at the Google Play Store.
 *
 */

/**
 * WeatherWidgetActivity class. \n
 * start class for Weather Widget.
 * @author Max
 *
 */
public class WeatherWidgetActivity extends AppWidgetProvider {

	/**
	 * called of android operating system (default every 30 min)
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		DataBaseHelper dbHelper = new DataBaseHelper(context);
		dbHelper.openDataBaseRW();
		dbHelper.checkActual();
		dbHelper.checkHistory();
		dbHelper.checkSettings();
		dbHelper.close();
		
		 //Get all widget ids
		ComponentName thisWidget = new ComponentName(context,
				WeatherWidgetActivity.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		// Build the intent to call the service
		Intent intent = new Intent(context.getApplicationContext(),
				UpdateWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
	    
		// Update the widgets via the service
		context.startService(intent);
	}
}
