/**
 * @file
 * @author Max
 */
package weather.widget.webcall;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import weather.widget.R;
import weather.widget.utils.DataBaseHelper;
import weather.widget.utils.UsefullFunctions;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * MyWebservice class. \n
 * It handles all webservice calls in its own thread and updates the database and the widgets after it is finished.
 * 
 * @author Max
 *
 */
public class MyWebservice extends Thread {

	private static final String LOG = "weather.widget"; 	/**< identifier for this app in logcat. */
	private String entityContent = null; 					/**< contains input from webservice. */
	private DataBaseHelper dbHelper = null; 				/**< class to handle database calls. */
	private UsefullFunctions _useFunc = null; 				/**< class for other usefull functions. */
	private int[] _allWidgetIds = null; 					/**< contains id numbers of all active widgets. */
	private Context _context = null; 						/**< widget context. */
	private AppWidgetManager _manager = null; 				/**< necessary to handle widget update. */
	private String TRUE = "1"; 								/**< boolean for database. */

	/**
	 * execute. This is the "main"-function fo MyWebservice.
	 * @param context context in which the widget is located.
	 * @param widgetIds	array of all integer widget identifiers.
	 * @param manager AppWidgetManager to manage widget updates.
	 */
	public void execute(Context context, int[] widgetIds, AppWidgetManager manager){
		_allWidgetIds = widgetIds;
		_context = context;
		_manager = manager;
		_useFunc = new UsefullFunctions();
		
		dbHelper = new DataBaseHelper(context);
		dbHelper.openDataBaseRW();

		/**
		 * allows non-"edt" thread to be re-inserted into the "edt" queue.
		 */
		final Handler uiThreadCallback = new Handler();

		/**
		 * performs rendering in the "edt" thread, after background operation is
		 * complete.
		 */
		final Runnable runInUIThread = new Runnable() {
			public void run() {
				dataBaseUpdate();
				widgetUpdate();
			}
		};
		new Thread() {
			@Override
			public void run() {
				_doInBackgroundGet();
				uiThreadCallback.post(runInUIThread);
			}
		}.start();
	}

	/**
	 * runs in own thread, handles httpGet and prepares the entitiyContent.
	 */
	private void _doInBackgroundGet() {
		InputStream is = null;	/**< variable to handle content of Webpage */

		try {
			String webCall = dbHelper.getSetting(dbHelper.getIpID()) + dbHelper.getSetting(dbHelper.getHttpID());
			if(!webCall.startsWith("http://")) webCall = "http://" + webCall;
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(webCall);
			
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			byte buffer[] = new byte[2024];
			is = entity.getContent();
			int numBytes = is.read(buffer);
			is.close();

			entityContent = new String(buffer, 0, numBytes);

		} catch (ClientProtocolException e) {
			entityContent = "";
		} catch (IOException e) {
			entityContent = "";
		} catch (ArrayIndexOutOfBoundsException e){
			// if this Exception occurs, there is an error at byte[] and this can't be fixed!
			Log.d(LOG, "Array Exception ");
		}
	}

	/**
	 * split json-string in json objects, fill in the String variables and update database.
	 */
	private void dataBaseUpdate(){
		JSONObject json = null; /**< new jsonObject for entityContent */
		
		String Temp = null, Hum = null, Pres = null, own1 = null, own2 = null;
		
		if (entityContent.length() != 0){
			try {
				json = new JSONObject(entityContent);
				if(dbHelper.getSetting(dbHelper.getCheckboxTempID()).equals(TRUE))
					Temp = json.getString(_context.getString(R.string.jsonTemperatur));
			}catch (JSONException e){
				Temp = "0";
				Log.e(LOG, "JSON Exception at Temp");
			}
			try {
				if(dbHelper.getSetting(dbHelper.getCheckboxPresID()).equals(TRUE))
					Pres = json.getString(_context.getString(R.string.jsonLuftdruck));
			}catch (JSONException e){
				Pres = "0";
				Log.e(LOG, "JSON Exception at Pres");
			}
			try{
				if(dbHelper.getSetting(dbHelper.getCheckboxHumID()).equals(TRUE))
					Hum = json.getString(_context.getString(R.string.jsonLuftfeuchtigkeit));
			}catch (JSONException e){
				Hum = "0";
				Log.e(LOG, "JSON Exception at Hum");
			}
			try{
				if(dbHelper.getSetting(dbHelper.getCheckboxOwn1ID()).equals(TRUE))
					own1 = json.getString(dbHelper.getSetting(dbHelper.getJsonOwn1ID()));
			}catch (JSONException e){
				own1 = "0";
				Log.e(LOG, "JSON Exception at Own1");
			}
			try{
				if(dbHelper.getSetting(dbHelper.getCheckboxOwn2ID()).equals(TRUE))
					own2 = json.getString(dbHelper.getSetting(dbHelper.getJsonOwn2ID()));
			}catch (JSONException e){
				own2 = "0";
				Log.e(LOG, "JSON Exception at Own2");
			}
					
			dbHelper.updateActual(Temp, Pres, Hum, own1, own2); 
			dbHelper.insertHistory(Temp,Pres, Hum, own1, own2);
		}
	}
	
	/**
	 * update all widgets on screen.
	 */
	private void widgetUpdate(){
		_useFunc.widgetUpdate(_allWidgetIds, _context, _manager, dbHelper);
		dbHelper.close();
	}
}