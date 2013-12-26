/**
 * @file
 */
package weather.widget.settings;

import weather.widget.R;
import weather.widget.utils.DataBaseHelper;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Settingsmenu class.\n
 * catch settings from activity and handle settings in database.
 * @author Max
 */
public class Settingsmenu extends Activity{
	
	private DataBaseHelper dbHelper = null;
	private EditText ip = null;
	private EditText http = null;
	private EditText jsonTemp = null;
	private EditText jsonPres = null;
	private EditText jsonHum = null;
	private EditText jsonOwn1 = null;
	private EditText jsonOwn2 = null;
	private CheckBox boxTemp = null;
	private CheckBox boxPres = null;
	private CheckBox boxHum = null;
	private CheckBox boxOwn1 = null;
	private CheckBox boxOwn2 = null;
	private String TRUE = "1";
	private String FALSE = "0";

	/** 
	 * Called when the activity is first created.
	 * 
	 * fetch all settings out of database and prepare view for users.\n
	 * if save button is pressed, all settings are fetched out of view and saved in the database.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsmenu);
        
        dbHelper = new DataBaseHelper(getApplicationContext());
        dbHelper.openDataBase();
        
        boxTemp = (CheckBox) findViewById(R.id.checkBoxTemp);
        boxPres = (CheckBox) findViewById(R.id.checkBoxPres);
        boxHum = (CheckBox) findViewById(R.id.checkBoxHum);
        boxOwn1 = (CheckBox) findViewById(R.id.checkBoxOwn1);
        boxOwn2 = (CheckBox) findViewById(R.id.checkBoxOwn2);
        
        jsonTemp = (EditText) findViewById(R.id.editTextjson1);
        jsonPres = (EditText) findViewById(R.id.editTextjson2);
        jsonHum = (EditText) findViewById(R.id.editTextjson3);
        jsonOwn1 = (EditText) findViewById(R.id.editTextjson4);
        jsonOwn2 = (EditText) findViewById(R.id.editTextjson5);
        
        TextView text = (TextView) findViewById(R.id.topText);
        text.setFocusable(true);
        text.setFocusableInTouchMode(true);
        
        jsonTemp.setEnabled(false);
        jsonTemp.setText(R.string.jsonTemperatur);
        jsonPres.setEnabled(false);
        jsonPres.setText(R.string.jsonLuftdruck);
        jsonHum.setEnabled(false);
        jsonHum.setText(R.string.jsonLuftfeuchtigkeit);
        
        if(dbHelper.getSetting(dbHelper.getCheckboxTempID()).equals(TRUE))
        	boxTemp.setChecked(true);
        if(dbHelper.getSetting(dbHelper.getCheckboxPresID()).equals(TRUE))
        	boxPres.setChecked(true);
        if(dbHelper.getSetting(dbHelper.getCheckboxHumID()).equals(TRUE))
        	boxHum.setChecked(true);
        if(dbHelper.getSetting(dbHelper.getCheckboxOwn1ID()).equals(TRUE)){
        	boxOwn1.setChecked(true);
        	jsonOwn1.setText(dbHelper.getSetting(dbHelper.getJsonOwn1ID()));
        }
        if(dbHelper.getSetting(dbHelper.getCheckboxOwn2ID()).equals(TRUE)){
        	boxOwn2.setChecked(true);
        	jsonOwn2.setText(dbHelper.getSetting(dbHelper.getJsonOwn2ID()));
        }
        
        ip = (EditText) findViewById(R.id.editTextIPAddress);
        http = (EditText) findViewById(R.id.editTextHTTPCall);
        
        ip.setText(dbHelper.getSetting(dbHelper.getIpID()));
        http.setText(dbHelper.getSetting(dbHelper.getHttpID()));
        
        Button buttonSave = (Button) findViewById(R.id.save);
        dbHelper.close();
        
        // Alle Einstellungen werden übernommen, sobald auf Speichern geklickt wird.
        buttonSave.setOnClickListener(new View.OnClickListener() {
   			@Override
   			public void onClick(View v) {
   				dbHelper.openDataBaseRW();
   				String ipAddr = ip.getText().toString();
   				if(ipAddr.contains("http://"))
   					ipAddr = ipAddr.replace("http://", "");
   				
   				String httpCall = http.getText().toString();
   				dbHelper.setSetting(ipAddr, dbHelper.getIpID());
   				dbHelper.setSetting(httpCall, dbHelper.getHttpID());
   				
   				if(boxTemp.isChecked())
   					dbHelper.setSetting(TRUE, dbHelper.getCheckboxTempID());
   				else
   					dbHelper.setSetting(FALSE, dbHelper.getCheckboxTempID());
   				
   				if(boxPres.isChecked())
   					dbHelper.setSetting(TRUE, dbHelper.getCheckboxPresID());
   				else
   					dbHelper.setSetting(FALSE, dbHelper.getCheckboxPresID());
   				
   				if(boxHum.isChecked())
   					dbHelper.setSetting(TRUE, dbHelper.getCheckboxHumID());
   				else
   					dbHelper.setSetting(FALSE, dbHelper.getCheckboxHumID());
   				
   				if(boxOwn1.isChecked()){
   					dbHelper.setSetting(TRUE, dbHelper.getCheckboxOwn1ID());
   					dbHelper.setSetting(jsonOwn1.getText().toString(), dbHelper.getJsonOwn1ID());
   				}else{
   					dbHelper.setSetting(FALSE, dbHelper.getCheckboxOwn1ID());
   					dbHelper.setSetting(jsonOwn1.getText().toString(), dbHelper.getJsonOwn1ID());
   				}
   				if(boxOwn2.isChecked()){
   					dbHelper.setSetting(TRUE, dbHelper.getCheckboxOwn2ID());
   					dbHelper.setSetting(jsonOwn2.getText().toString(), dbHelper.getJsonOwn2ID());
   				}else{
   					dbHelper.setSetting(FALSE, dbHelper.getCheckboxOwn2ID());
   					dbHelper.setSetting(jsonOwn2.getText().toString(), dbHelper.getJsonOwn2ID());
   				}
   				
   				dbHelper.close();
   				
   				Toast.makeText(getApplicationContext(), getString(R.string.toastSaved), Toast.LENGTH_LONG).show();
   				
   				// TODO: redirect to Mainpage or graphview
   			}
        });
    }
    
}
