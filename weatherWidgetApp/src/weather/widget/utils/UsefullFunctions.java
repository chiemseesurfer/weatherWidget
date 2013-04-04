/**
 * @file
 */
package weather.widget.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.RemoteViews;
import weather.widget.R;
import weather.widget.WeatherWidgetActivity;
import weather.widget.graph.WeatherGraphView;
import weather.widget.settings.Settingsmenu;

/**
 * UsefullFunctions class.\n
 * prepare functions who are used in different classes.
 * @author Max
 *
 */
public class UsefullFunctions {
	
	private UpdateWidgetService _activity = null;	/**< own private variable for updateWidget class */
	private Activity act = null;					/**< contains activity which calls this class */
	
	/**
	 * special constructor for UpdateWidgetService (it is just a service and not a activity).
	 * @param activity	UpdateWidgetService
	 */
	public UsefullFunctions(UpdateWidgetService activity){
		_activity = activity;
	}
	
	/**
	 * constructor for each other activity.
	 * @param activity	normal Activity
	 */
	public UsefullFunctions(Activity activity){
		act = activity;
	}
	
	/**
	 * constructor without any parameter (used in MyWebservice)
	 */
	public UsefullFunctions(){
		
	}
	
	/**
	 * small function to return chosen menu.
	 * @param selected MenuItem
	 * @return new intent to start new activity
	 */
	public Intent getMenuIntent(int selected){
		Intent returnIntent = null;
		switch (selected) {
			case R.id.MenuSettings:		returnIntent = new Intent(act.getApplicationContext(), Settingsmenu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    										break;
		}
		return returnIntent;
	}
	
	/**
	 * check if a WLAN or MOBILE network connection is available.
     * 
     * @return true if a network connection is available (else false)
     */
    public boolean haveNetworkConnection() {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = (ConnectivityManager) _activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                    if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                            if (ni.isConnected())
                                    haveConnectedWifi = true;
                    if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                            if (ni.isConnected())
                                    haveConnectedMobile = true;
            }
            return haveConnectedWifi || haveConnectedMobile;
    }
    
    /**
     * update all widgets
     * @param allWidgetIds int array of all widget identifiers
     * @param context context of update class
     * @param manager AppWidgetManager
     * @param dbHelper to get a open database
     */
    public void widgetUpdate(int[] allWidgetIds, Context context, AppWidgetManager manager, DataBaseHelper dbHelper){
    	for (int widgetId : allWidgetIds){
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);
			
			/** 
			 * update Date
			 */
			String date = dbHelper.getDate();
			if(date != null)
				remoteViews.setTextViewText(R.id.date, date);
			
			/**
			 * get values out of database
			 */
			String value = dbHelper.getValueActual();
			if(value != null || !"".equals(value))
				remoteViews.setTextViewText(R.id.tempValue, value);
			
			/**
			 * paint black font color.
			 */
			remoteViews.setTextColor(R.id.date, Color.BLACK);
			remoteViews.setTextColor(R.id.tempValue, Color.BLACK);
			
			/**
			 * activate sync button in widget
			 */
			Intent clickIntent = new Intent(context, WeatherWidgetActivity.class);
			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
			
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					context, 0, clickIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			
			remoteViews.setOnClickPendingIntent(R.id.buttonSynchronize, pendingIntent);
			
			/**
			 * activate graph activity if widget is clicked
			 */
			Intent settingsIntent = new Intent(context, WeatherGraphView.class);
			PendingIntent setIntent  = PendingIntent.getActivity(context, 0, settingsIntent, 0);
			remoteViews.setOnClickPendingIntent(R.id.tempValue, setIntent);
			remoteViews.setOnClickPendingIntent(R.id.date, setIntent);
			
			
			/* Idee für Alarm um kürzer als 30 min das Widget upzudaten */
			// AlarmManager _alarm = null;
			//_alarm.cancel(pendingIntent);
			//long interval = 1000 * 60 * 5; // 5 Minuten
			/*long interval = 1000 * 60 * 20; // 20 min
			_alarm.setRepeating(AlarmManager.ELAPSED_REALTIME,
					SystemClock.elapsedRealtime(), interval, pendingIntent);
			*/
			
			manager.updateAppWidget(widgetId, remoteViews);
			
		}
    }


}
