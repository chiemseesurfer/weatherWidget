/**
 * @file
 */
package weather.widget.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.jjoe64.graphview.GraphView.GraphViewData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DataBaseHelper class. \n
 * handle sqlite database on my own.
 * 
 * database name: weatherStationWidget.sqlite \n
 * table values structure : ID | Date |Temperatur | Luftdruck | Humidity | own1 | own2 \n
 * table setting structure : setting | value
 * 
 * @author Max
 *
 */
public class DataBaseHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;					/**< actual database version number */
    private SQLiteDatabase myDataBase; 								/**< database handler */
	
	private static String tableNameActual = "weatherValues";		/**< contains actual values */
	private static String tableNameHistory = "weatherHistory";		/**< contains value history (30 values) */
	private static String tableNameSettings = "weatherSettings";	/**< contains actual settings (like ip-address) */
	
	/* Datenbankaufbau für Aktuelle Werte */
	private static final String CREATE_TABLE_ACTUAL = "CREATE TABLE "+ tableNameActual + " ("+ 
			"id INTEGER PRIMARY KEY, " + 
			"date TEXT," +
			"temp TEXT, " + 
			"pressure TEXT, " + 
			"humidity TEXT, " +
			"own1 TEXT, " +
			"own2 TEXT)";
	
	/* Datenbankaufbau für History */
	private static final String CREATE_TABLE_HISTORY = "CREATE TABLE " + tableNameHistory + " ("+
			"id INTEGER PRIMARY KEY, " + 
			"date TEXT," +
			"temp TEXT, " + 
			"pressure TEXT, " + 
			"humidity TEXT, " +
			"own1 TEXT, " +
			"own2 TEXT)";
	
	/* Datenbankaufbau für Einstellungen */
	private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE " + tableNameSettings + " ("+
			"setting TEXT, " +
			"value TEXT)";
	
	private static String DB_NAME = "weatherStationWidget.sqlite";				/**< database Name */
	private static String KEY_ID = "id";										/**< Key for id column [value table] */
	private static String KEY_TEMP = "temp";									/**< Key for temperature value column [value table] */
	private static String KEY_PRES = "pressure";								/**< Key for pressure value column [value table] */
	private static String KEY_HUM = "humidity";									/**< Key for humidity value column [value table] */
	private static String KEY_DATE = "date";									/**< Key for date column [value table] */
	private static String KEY_SETTING = "setting";								/**< Key for setting column [setting table] */
	private static String KEY_VALUE = "value";									/**< Key for settings value column [setting table] */
	private static String KEY_OWN1 = "own1";									/**< Key for first own sensor value [value table] */
	private static String KEY_OWN2 = "own2";									/**< Key for second own sensor value [value table] */
	private static String VALUE_ACT_TABLE_ID []= {"1"};
	private static String SETTING_IP_ID [] = {"IP"};
	private static String SETTING_HTTP_ID [] = {"HTTP"};
	private static String SETTING_COUNT_ID [] = {"COUNT"};
	private static String SETTING_MAX_ID [] = {"MAX"};
	private static String SETTING_CHECKBOX_TEMP_ID [] = {"checkBoxTemp"};
	private static String SETTING_CHECKBOX_PRES_ID [] = {"checkBoxPres"};
	private static String SETTING_CHECKBOX_HUM_ID [] = {"checkBoxHum"};
	private static String SETTING_CHECKBOX_OWN1_ID [] = {"checkBoxOwn1"};
	private static String SETTING_CHECKBOX_OWN2_ID [] = {"checkBoxOwn2"};
	private static String SETTING_JSON_OWN1_ID [] = {"jsonOwn1"};
	private static String SETTING_JSON_OWN2_ID [] = {"jsonOwn2"};
	private static String TRUE = "1";
	private static String FALSE = "0";
	private static Integer MAX_VALUE = 30;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy - HH.mm", Locale.GERMAN);
	
	/**
	 * Constructor
	 * @param context of application
	 */
	public DataBaseHelper(Context context){
		super(context, DB_NAME, null, DATABASE_VERSION);
	}

	/**
	 * called if DATABASE_VERSION is changed or first call of DataBaseHelper
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_ACTUAL);
		db.execSQL(CREATE_TABLE_HISTORY);
		db.execSQL(CREATE_TABLE_SETTINGS);
	}

	/**
	 * get table id for json-value of first sensor (jsonOwn1)
	 * @return String[] json database ID
	 */
	public String[] getJsonOwn1ID(){
		return SETTING_JSON_OWN1_ID;
	}
	
	/**
	 * get table id for json-value of second sensor (jsonOwn2)
	 * @return String[] json database id
	 */
	public String[] getJsonOwn2ID(){
		return SETTING_JSON_OWN2_ID;
	}
	
	/**
	 * get table id for checkbox id of second sensor (checkBoxOwn2)
	 * @return String[] checkbox database id
	 */
	public String[] getCheckboxOwn2ID(){
		return SETTING_CHECKBOX_OWN2_ID;
	}
	
	/**
	 * get table id for checkbox id of first sensor (checkBoxOwn1)
	 * @return String[] checkbox database id
	 */
	public String[] getCheckboxOwn1ID(){
		return SETTING_CHECKBOX_OWN1_ID;
	}
	
	/**
	 * get table id for checkbox id of humidity sensor (checkBoxHum)
	 * @return String[] checkbox database id
	 */
	public String[] getCheckboxHumID(){
		return SETTING_CHECKBOX_HUM_ID;
	}
	
	/**
	 * get table id for checkbox id of pressure sensor (checkBoxPres)
	 * @return String[] checkbox database id
	 */
	public String[] getCheckboxPresID(){
		return SETTING_CHECKBOX_PRES_ID;
	}
	
	/**
	 * get table id for checkbox id of temperature sensor (checkBoxTemp)
	 * @return String[] checkbox database id
	 */
	public String[] getCheckboxTempID(){
		return SETTING_CHECKBOX_TEMP_ID;
	}
	
	/**
	 * get table id for http setting (HTTP)
	 * @return String[] http id
	 */
	public String[] getHttpID(){
		return SETTING_HTTP_ID;
	}
	
	/**
	 * get table id for ip setting (IP)
	 * @return String[] ip id
	 */
	public String[] getIpID(){
		return SETTING_IP_ID;
	}
	
	/**
	 * get database key for pressure value (pressure)
	 * @return String Key for pressure value
	 */
	public String getKeyPres(){
		return KEY_PRES;
	}
	
	/**
	 * get database key for humidity value (humidity)
	 * @return String Key for humidity sensor
	 */
	public String getKeyHum(){
		return KEY_HUM;
	}
	
	/**
	 * get database key for own sensor 2 (own2)
	 * @return String Key for own sensor 2
	 */
	public String getKeyOwn2(){
		return KEY_OWN2;
	}
	
	/**
	 * get database key for own sensor 1 (own1)
	 * @return String Key for own sensor 1
	 */
	public String getKeyOwn1(){
		return KEY_OWN1;
	}
	
	/**
	 * get database key for temperature value (temperature)
	 * @return String Key for temperature sensor
	 */
	public String getKeyTemp(){
		return KEY_TEMP;
	}
	
	/**
	 * open readable database
     * @throws SQLException if it is not possible to open the database
     */
    public void openDataBase() throws SQLException {
    	myDataBase = this.getReadableDatabase();
    }

    /**
     * open writable databse
     * @throws SQLException if it is not possible to open the database
     */
    public void openDataBaseRW() throws SQLException {
    	
    	myDataBase = this.getWritableDatabase();
    }
	
	/**
	 * check if we have already values in the database table for actual values. \n
	 * if we have no values, insert 0 as default value.
	 */
	public void checkActual(){
		ContentValues values = new ContentValues();
		
		Cursor cursor = myDataBase.rawQuery("SELECT * FROM " + tableNameActual, null);
		if(!cursor.moveToFirst()){
			String currentDateandTime = sdf.format(new Date());
		
			values.put(KEY_ID, 1);
			values.put(KEY_DATE, currentDateandTime);
			values.put(KEY_TEMP, "0");
			values.put(KEY_PRES, "0");
			values.put(KEY_HUM, "0");
			values.put(KEY_OWN1, "0");
			values.put(KEY_OWN2, "0");
		
			myDataBase.insert(tableNameActual, null, values);
		}
	}
	
	/**
	 * check if we have already values in the database table for history values. \n
	 * if we have no values, insert 0 as default value.
	 */
	public void checkHistory(){
		ContentValues values = null;
		
		Cursor cursor = myDataBase.rawQuery("SELECT * FROM " + tableNameHistory, null);
		if(!cursor.moveToFirst()){
		
			String currentDateandTime = sdf.format(new Date());
		
			for (Integer i = 0; i <= MAX_VALUE; i++){
				values = new ContentValues();
				values.put(KEY_ID, i.toString());
				values.put(KEY_DATE, currentDateandTime);
				values.put(KEY_TEMP, "0");
				values.put(KEY_PRES, "0");
				values.put(KEY_HUM, "0");
				values.put(KEY_OWN1, "0");
				values.put(KEY_OWN2, "0");
				
				myDataBase.insert(tableNameHistory, null, values);
			}
		}
	}
	
	/**
	 * check if we have already values in the database table for settings.
	 * if we have no values, insert default values:
	 * 
	 * IP = 192.168.1.254 \n
	 * HTTP = /json \n
	 * MAX = 30 (maximum count for history table) \n
	 * COUNT = 0 (actual count for history table) \n
	 * checkBoxTemp = FALSE \n
	 * checkBoxPres = FALSE \n
	 * checkBoxHum = FALSE \n
	 * checkBoxOwn1 = FALSE \n
	 * checkBoxOwn2 = FALSE \n
	 * jsonOwn1 = 0 \n
	 * jsonOwn2 = 0
	 */
	public void checkSettings(){
		
		ContentValues settings = new ContentValues();
		ContentValues httpSettings = new ContentValues();
		ContentValues insertMax = new ContentValues();
		ContentValues insertCount = new ContentValues();
		ContentValues checkBoxTemp = new ContentValues();
		ContentValues checkBoxPres = new ContentValues();
		ContentValues checkBoxHum = new ContentValues();
		ContentValues checkBoxOwn1 = new ContentValues();
		ContentValues checkBoxOwn2 = new ContentValues();
		ContentValues jsonOwn1 = new ContentValues();
		ContentValues jsonOwn2 = new ContentValues();
		
		Cursor cursor = myDataBase.rawQuery("SELECT * FROM " + tableNameSettings, null);
		if(!cursor.moveToFirst()){
		
			settings.put(KEY_SETTING, "IP");
			settings.put(KEY_VALUE, "192.168.1.254");
			httpSettings.put(KEY_SETTING, "HTTP");
			httpSettings.put(KEY_VALUE, "/json");
			insertMax.put(KEY_SETTING, "MAX");
			insertMax.put(KEY_VALUE, "30");
			insertCount.put(KEY_SETTING, "COUNT");
			insertCount.put(KEY_VALUE, "0");
			checkBoxTemp.put(KEY_SETTING, "checkBoxTemp");
			checkBoxTemp.put(KEY_VALUE, FALSE);
			checkBoxPres.put(KEY_SETTING, "checkBoxPres");
			checkBoxPres.put(KEY_VALUE, FALSE);
			checkBoxHum.put(KEY_SETTING, "checkBoxHum");
			checkBoxHum.put(KEY_VALUE, FALSE);
			checkBoxOwn1.put(KEY_SETTING, "checkBoxOwn1");
			checkBoxOwn1.put(KEY_VALUE, FALSE);
			checkBoxOwn2.put(KEY_SETTING, "checkBoxOwn2");
			checkBoxOwn2.put(KEY_VALUE, FALSE);
			jsonOwn1.put(KEY_SETTING, "jsonOwn1");
			jsonOwn1.put(KEY_VALUE, "0");
			jsonOwn2.put(KEY_SETTING, "jsonOwn2");
			jsonOwn2.put(KEY_VALUE, "0");
		
			myDataBase.insert(tableNameSettings, null, settings);
			myDataBase.insert(tableNameSettings, null, httpSettings);
			myDataBase.insert(tableNameSettings, null, insertMax);
			myDataBase.insert(tableNameSettings, null, insertCount);
			myDataBase.insert(tableNameSettings, null, checkBoxTemp);
			myDataBase.insert(tableNameSettings, null, checkBoxPres);
			myDataBase.insert(tableNameSettings, null, checkBoxHum);
			myDataBase.insert(tableNameSettings, null, checkBoxOwn1);
			myDataBase.insert(tableNameSettings, null, checkBoxOwn2);
			myDataBase.insert(tableNameSettings, null, jsonOwn1);
			myDataBase.insert(tableNameSettings, null, jsonOwn2);
		}
	}

	/**
	 * called if DATABASE_VERSION is changed
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + tableNameActual);
		db.execSQL("DROP TABLE IF EXISTS " + tableNameSettings);
		db.execSQL("DROP TABLE IF EXISTS " + tableNameHistory);
		onCreate(db);
	}
	
	/**
	 * update setting in database
	 * @param value new value of setting
	 * @param setting key of setting
	 */
	public void setSetting(String value, String[] setting){
		ContentValues settings = new ContentValues();
		
		settings.put(KEY_VALUE, value);
		
		myDataBase.update(tableNameSettings, settings, KEY_SETTING + " = ?", setting);
	}
	
	/**
	 * catch setting out of database
	 * @param setting key of setting
	 * @return String setting value
	 */
    public String getSetting(String[] setting){
    	String result = null;
		
		Cursor cursor = myDataBase.query(tableNameSettings, new String[] { KEY_VALUE }, KEY_SETTING + "=?", setting, null ,null, null, null);
		if(cursor.moveToFirst()){
			result = new String(cursor.getString(0));
		}
		cursor.close();
		
		return result;
    }
	
	/**
	 * overwritten stanard method to close database
     */
    @Override
	public synchronized void close() {
 
    	if(myDataBase != null) {
    		myDataBase.close();
    	}
 
    	super.close();
	}
	
	/**
	 * update actual table with actual sensor values
	 * @param temp	actual temperature value
	 * @param pres	actual pressure value
	 * @param hum	actual humidity value
	 * @param own1	actual value of first own sensor
	 * @param own2 	actual value of second own sensor
	 */
	public void updateActual(String temp, String pres, String hum, String own1, String own2){
		ContentValues valuesAct = new ContentValues();
		
		String currentDateandTime = sdf.format(new Date());
		
		Cursor mCursor = myDataBase.rawQuery("SELECT * FROM " + tableNameActual, null);
		
		if (mCursor.moveToFirst()){ // check if database is empty
		
			valuesAct.put(KEY_DATE, currentDateandTime);
			valuesAct.put(KEY_TEMP, temp);
			valuesAct.put(KEY_PRES, pres);
			valuesAct.put(KEY_HUM, hum);
			valuesAct.put(KEY_OWN1, own1);
			valuesAct.put(KEY_OWN2, own2);
		
			myDataBase.update(tableNameActual, valuesAct, KEY_ID + " = ?", VALUE_ACT_TABLE_ID);
		}
		mCursor.close();
	}
	
	/**
	 * update history table with actual values
	 * @param temp	actual temperature value
	 * @param pres	actual pressure value
	 * @param hum	actual humidity value
	 * @param own1	actual value of first own sensor
	 * @param own2 	actual value of second own sensor
	 */
	public void insertHistory(String temp, String pres, String hum, String own1, String own2){
		ContentValues valuesAct = new ContentValues();
		
		String currentDateandTime = sdf.format(new Date());
		
		Cursor mCursor = myDataBase.rawQuery("SELECT * FROM " + tableNameHistory, null);
		
		Integer counter = Integer.valueOf(this.getSetting(SETTING_COUNT_ID));
		Integer max = Integer.valueOf(this.getSetting(SETTING_MAX_ID));
		
		if(counter > max){
			counter = 0;
		}
		valuesAct.put(KEY_DATE, currentDateandTime);
		valuesAct.put(KEY_TEMP, temp);
		valuesAct.put(KEY_PRES, pres);
		valuesAct.put(KEY_HUM, hum);
		valuesAct.put(KEY_OWN1, own1);
		valuesAct.put(KEY_OWN2, own2);
		myDataBase.update(tableNameHistory, valuesAct, KEY_ID + "=?", new String[] { counter.toString() });
		
		this.setSetting(Integer.toString(counter + 1), SETTING_COUNT_ID);
		mCursor.close();
	}
	
	/**
	 * fetch actual sensor values out of database
	 * @return String actual values to display them direct in widget
	 */
	public String getValueActual(){
		StringBuffer result = new StringBuffer();
		result.append("");
		
		Cursor cursor = myDataBase.query(tableNameActual, new String[] { KEY_TEMP, KEY_PRES, KEY_HUM, KEY_OWN1, KEY_OWN2 }
			, KEY_ID + "=?", VALUE_ACT_TABLE_ID, null, null, null, null);
		
		if (cursor.moveToFirst()){
			String humidity = cursor.getString(2);
			if( humidity == null){
				humidity = "0";
			}else{
				humidity = humidity.replaceAll("\n", "");
			}
			String temp = cursor.getString(0);
			if( temp == null){
				temp = "0";
			}
			String pres = cursor.getString(1);
			if( pres == null){
				pres = "0";
			}
			String own1 = cursor.getString(3);
			if( own1 == null){
				own1 = "0";
			}
			String own2 = cursor.getString(4);
			if( own2 == null){
				own2 = "0";
			}

			
			if(this.getSetting(SETTING_CHECKBOX_TEMP_ID).equals(TRUE) && !temp.equals("0"))
				result.append(temp + " °C ");
			if(this.getSetting(SETTING_CHECKBOX_PRES_ID).equals(TRUE) && !pres.equals("0"))
				result.append(pres + " hPa ");
			if(this.getSetting(SETTING_CHECKBOX_HUM_ID).equals(TRUE) && !humidity.equals("0"))
				result.append(humidity + " % ");
			if(this.getSetting(SETTING_CHECKBOX_OWN1_ID).equals(TRUE) && !own1.equals("0"))
				result.append(own1 + " ");
			if(this.getSetting(SETTING_CHECKBOX_OWN2_ID).equals(TRUE) && !own2.equals("0"))
				result.append(own2 + " ");
		}
		
		cursor.close();
		
		return result.toString();
	}
	
	/**
	 * fetch values in history table and modify them to show in GraphView
	 * @param column for example temperature
	 * @return GraphViewData[] to use in GraphView
	 */
	public GraphViewData[] getData(String column){
		
		List <GraphViewData> list = new ArrayList<GraphViewData>();
		
		Cursor cursor = myDataBase.rawQuery("SELECT " + column + " FROM " + tableNameHistory, null);
		
		Integer counter = Integer.valueOf(this.getSetting(SETTING_COUNT_ID));
		Integer max = Integer.valueOf(this.getSetting(SETTING_MAX_ID));
		
		int i = 0;
		if(counter < max){
			if(cursor.moveToPosition(counter)){
				do{
					list.add(new GraphViewData((double)i, Double.valueOf(cursor.getString(0))));
					i++;
				}while(cursor.moveToNext());
			}
			if(cursor.moveToFirst()){
				do{
					list.add(new GraphViewData((double)i, Double.valueOf(cursor.getString(0))));
					i++;
				}while(cursor.moveToNext() && i < max);
			}
		}else{
			if (cursor.moveToFirst()){
				do{
					list.add(new GraphViewData((double)i, Double.valueOf(cursor.getString(0))));
					i++;
				}while(cursor.moveToNext());
			}
		}
		
		GraphViewData[] result = list.toArray(new GraphViewData[list.size()]);
		cursor.close();
		
		return result;	
	}
	
	/**
	 * fetch date and time of actual values out of database
	 * @return String date + time
	 */
	public String getDate(){
		String result = null;
		
		Cursor cursor = myDataBase.query(tableNameActual, new String[] { KEY_DATE}
			, KEY_ID + "=?", VALUE_ACT_TABLE_ID, null, null, null, null);
		
		if (cursor.moveToFirst()){
			result = new String(cursor.getString(0));
		}
		
		cursor.close();
		return result;
	}
}
