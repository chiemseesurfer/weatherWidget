/**
 * @file
 */
package weather.widget.graph;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;

import weather.widget.R;
import weather.widget.utils.DataBaseHelper;
import weather.widget.utils.UsefullFunctions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

/**
 * WeatherGraphView class. \n
 * generates and displays graphical view for sensor values.
 * @author Max
 *
 */
public class WeatherGraphView extends Activity {
	
	private UsefullFunctions useFunc = new UsefullFunctions(this);
	private String TRUE = "1";

	/** 
	 * Called when activity is first created. 
	 * 
	 * fetches all sensor-values out of database and display graphic view of each sensor in own LinearLayout.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graphs);
		
		int counter = 0;
		
		// IDs der LinearLayouts in ein Array schreiben, 
		// damit keine Lücken zwischen den Layouts enstehen, 
		// wenn nicht der Reihe nach ausgewählt sind
		int[] ids = {R.id.graph2,R.id.graph3,R.id.graph4,R.id.graph5,R.id.graph6};

		DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
		dbHelper.openDataBase();
		
		GraphView graphView = new LineGraphView(this, getString(R.string.Overview));
		
		// Temperatur
		if(dbHelper.getSetting(dbHelper.getCheckboxTempID()).equals(TRUE))
		{
			GraphViewData[] tempData = dbHelper.getData(dbHelper.getKeyTemp());
			GraphViewSeries tempSeriesOverall = new GraphViewSeries(getString(R.string.Temperature), new GraphViewStyle(Color.RED, 3), tempData);
			GraphViewSeries tempSeries = new GraphViewSeries(tempData);
			graphView.addSeries(tempSeriesOverall);
			
			// graph with dynamically genereated horizontal and vertical labels
			GraphView graphViewTemp = new LineGraphView(
					this // context
					, getString(R.string.Temp) // heading
			);
			graphViewTemp.addSeries(tempSeries);
			((LineGraphView) graphViewTemp).setDrawBackground(true);
			LinearLayout TempLayout = (LinearLayout) findViewById(ids[counter]);
			TempLayout.addView(graphViewTemp);
			counter++;
		}
		
		// Luftdruck
		if(dbHelper.getSetting(dbHelper.getCheckboxPresID()).equals(TRUE)){
			GraphViewData[] presData = dbHelper.getData(dbHelper.getKeyPres());
			GraphViewSeries presSeries = new GraphViewSeries(presData);
			GraphViewSeries presSeriesOverall = new GraphViewSeries(getString(R.string.Presssure), new GraphViewStyle(Color.GREEN, 3), presData);
			graphView.addSeries(presSeriesOverall);
			
			GraphView graphViewPres = new LineGraphView(this, getString(R.string.Pres));
			graphViewPres.addSeries(presSeries);
			((LineGraphView) graphViewPres).setDrawBackground(true);
			LinearLayout PresLayout = (LinearLayout) findViewById(ids[counter]);
			PresLayout.addView(graphViewPres);
			counter++;
		}
		
		// Luftfeuchtigkeit
		if(dbHelper.getSetting(dbHelper.getCheckboxHumID()).equals(TRUE)){
			GraphViewData[] humData = dbHelper.getData(dbHelper.getKeyHum());
			GraphViewSeries humSeriesOverall = new GraphViewSeries(getString(R.string.Humidity), new GraphViewStyle(Color.BLUE, 3), humData);
			GraphViewSeries humSeries = new GraphViewSeries(humData);
			graphView.addSeries(humSeriesOverall);
			
			GraphView graphViewHum = new LineGraphView(this, getString(R.string.Hum));
			graphViewHum.addSeries(humSeries);
			((LineGraphView) graphViewHum).setDrawBackground(true);
			LinearLayout HumLayout = (LinearLayout) findViewById(ids[counter]);
			HumLayout.addView(graphViewHum);
			counter++;
		}
		
		// Eigener Sensor 1
		if(dbHelper.getSetting(dbHelper.getCheckboxOwn1ID()).equals(TRUE)){
			GraphViewData[] ownData = dbHelper.getData(dbHelper.getKeyOwn1());
			GraphViewSeries ownSeriesOverall = new GraphViewSeries(getString(R.string.ownSensor), new GraphViewStyle(Color.GRAY, 3), ownData);
			GraphViewSeries ownSeries = new GraphViewSeries(ownData);
			graphView.addSeries(ownSeriesOverall);
			
			GraphView graphViewOwn = new LineGraphView(this, getString(R.string.ownSensor));
			graphViewOwn.addSeries(ownSeries);
			((LineGraphView) graphViewOwn).setDrawBackground(true);
			LinearLayout OwnLayout = (LinearLayout) findViewById(ids[counter]);
			OwnLayout.addView(graphViewOwn);
			counter++;
		}
		
		// Eigener Sensor 2
		if(dbHelper.getSetting(dbHelper.getCheckboxOwn2ID()).equals(TRUE)){
			GraphViewData[] ownData = dbHelper.getData(dbHelper.getKeyOwn2());
			GraphViewSeries ownSeriesOverall = new GraphViewSeries(getString(R.string.ownSensor), new GraphViewStyle(Color.WHITE, 3), ownData);
			GraphViewSeries ownSeries = new GraphViewSeries(ownData);
			graphView.addSeries(ownSeriesOverall);
			
			GraphView graphViewOwn = new LineGraphView(this, getString(R.string.ownSensor));
			graphViewOwn.addSeries(ownSeries);
			((LineGraphView) graphViewOwn).setDrawBackground(true);
			LinearLayout OwnLayout = (LinearLayout) findViewById(ids[counter]);
			OwnLayout.addView(graphViewOwn);
			counter++;
		}
		
		graphView.setShowLegend(true);
		LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
		layout.addView(graphView);
		
		dbHelper.close();
	}
	
	/**
	 * enable settings-button
	 */
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }   
	
	/**
	 * called if a menu item is selected
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent menuIntent = useFunc.getMenuIntent(item.getItemId());
        startActivityForResult(menuIntent,0);
        return true;
    }
}
