/**
 * @file
 */
package weather.widget.utils;

import weather.widget.webcall.MyWebservice;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;

/**
 * UpdateWidgetService class.\n
 * class which is called on update call of widget.
 * @author Max
 *
 */
public class UpdateWidgetService extends Service {
	
	private UsefullFunctions useFunc = null; 	/**< needed to check network connection */

	@Override
	public void onStart(Intent intent, int startId) {
		
		DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
		dbHelper.openDataBase();
		/* Manager für alle Widgets */
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
				.getApplicationContext());
		
		/* finde alle Widgets die angelegt wurden */
		int[] allWidgetIds = intent
				.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		
		/* aktualisiere Messwerte in der Datenbank durch einen Webcall */
		useFunc = new UsefullFunctions(this);
		
		/* alle Widgets sollen aktualisiert werden */
		useFunc.widgetUpdate(allWidgetIds, getApplicationContext(), appWidgetManager, dbHelper);
		
		if(useFunc.haveNetworkConnection())
			new MyWebservice().execute(this.getApplicationContext(), allWidgetIds, appWidgetManager);
		
		stopSelf();
		
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}