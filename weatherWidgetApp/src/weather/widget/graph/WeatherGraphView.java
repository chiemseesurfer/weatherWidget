/**
 * @file
 */
package weather.widget.graph;

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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

/**
 * WeatherGraphView class. \n
 * generates and displays graphical view for sensor values.
 * @author Max
 *
 */
public class WeatherGraphView extends Activity {
	
	private UsefullFunctions useFunc = new UsefullFunctions(this);
	private String TRUE = "1";
	private Integer MAX_NUM_HORIZONTAL_LABELS = 7;

	/** 
	 * Called when activity is first created. 
	 * 
	 * fetches all sensor-values out of database and display graphic view of 
	 * each sensor in own LinearLayout.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graphs);
		
		int idCounter = 0;
		
		// IDs der LinearLayouts in ein Array schreiben, 
		// damit keine Lücken zwischen den Layouts enstehen, 
		// wenn nicht der Reihe nach ausgewählt sind
		int[] ids = {R.id.graph2,R.id.graph3,R.id.graph4,R.id.graph5,R.id.graph6};

		DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
		dbHelper.openDataBase();
		
		// Display all graphs in one big overview
		GraphView graphView = new LineGraphView(this,
												getString(R.string.Overview));
		
		// Temperatur
		if(dbHelper.getSetting(dbHelper.getCheckboxTempID()).equals(TRUE))
		{
			GraphViewData[] tempData = dbHelper.getData(dbHelper.getKeyTemp());
			GraphViewSeries tempSeries = new GraphViewSeries(getString(R.string.Temperature), new GraphViewSeriesStyle(Color.RED, 3), tempData);
			graphView.addSeries(tempSeries);
			
			// graph with dynamically generated horizontal and vertical labels
			GraphView graphViewTemp = new LineGraphView(
					this // context
					, getString(R.string.Temp) // heading
			);
			graphViewTemp.addSeries(tempSeries);
			((LineGraphView) graphViewTemp).setDrawBackground(true);
			((LineGraphView) graphViewTemp).getGraphViewStyle().setNumHorizontalLabels(MAX_NUM_HORIZONTAL_LABELS);
			LinearLayout TempLayout = (LinearLayout) findViewById(ids[idCounter]);
			TempLayout.addView(graphViewTemp);
			idCounter++;
		}
		
		// Luftdruck
		if(dbHelper.getSetting(dbHelper.getCheckboxPresID()).equals(TRUE)){
			GraphViewData[] presData = dbHelper.getData(dbHelper.getKeyPres());
			GraphViewSeries presSeries = new GraphViewSeries(getString(R.string.Presssure), new GraphViewSeriesStyle(Color.GREEN, 3), presData);
			graphView.addSeries(presSeries);
			
			GraphView graphViewPres = new LineGraphView(this, getString(R.string.Pres));
			graphViewPres.addSeries(presSeries);
			((LineGraphView) graphViewPres).setDrawBackground(true);
			((LineGraphView) graphViewPres).getGraphViewStyle().setNumHorizontalLabels(MAX_NUM_HORIZONTAL_LABELS);
			LinearLayout PresLayout = (LinearLayout) findViewById(ids[idCounter]);
			PresLayout.addView(graphViewPres);
			idCounter++;
		}
		
		// Luftfeuchtigkeit
		if(dbHelper.getSetting(dbHelper.getCheckboxHumID()).equals(TRUE)){
			GraphViewData[] humData = dbHelper.getData(dbHelper.getKeyHum());
			GraphViewSeries humSeries = new GraphViewSeries(getString(R.string.Humidity), new GraphViewSeriesStyle(Color.BLUE, 3), humData);
			graphView.addSeries(humSeries);
			
			GraphView graphViewHum = new LineGraphView(this, getString(R.string.Hum));
			graphViewHum.addSeries(humSeries);
			((LineGraphView) graphViewHum).setDrawBackground(true);
			((LineGraphView) graphViewHum).getGraphViewStyle().setNumHorizontalLabels(MAX_NUM_HORIZONTAL_LABELS);
			LinearLayout HumLayout = (LinearLayout) findViewById(ids[idCounter]);
			HumLayout.addView(graphViewHum);
			idCounter++;
		}
		
		// Eigener Sensor 1
		if(dbHelper.getSetting(dbHelper.getCheckboxOwn1ID()).equals(TRUE)){
			GraphViewData[] ownData = dbHelper.getData(dbHelper.getKeyOwn1());
			GraphViewSeries ownSeries = new GraphViewSeries(dbHelper.getSetting(dbHelper.getJsonOwn1ID()), new GraphViewSeriesStyle(Color.GRAY, 3), ownData);
			graphView.addSeries(ownSeries);
			
			GraphView graphViewOwn = new LineGraphView(this, getString(R.string.ownSensor));
			graphViewOwn.addSeries(ownSeries);
			((LineGraphView) graphViewOwn).setDrawBackground(true);
			((LineGraphView) graphViewOwn).getGraphViewStyle().setNumHorizontalLabels(MAX_NUM_HORIZONTAL_LABELS);
			LinearLayout OwnLayout = (LinearLayout) findViewById(ids[idCounter]);
			OwnLayout.addView(graphViewOwn);
			idCounter++;
		}
		
		// Eigener Sensor 2
		if(dbHelper.getSetting(dbHelper.getCheckboxOwn2ID()).equals(TRUE)){
			GraphViewData[] ownData = dbHelper.getData(dbHelper.getKeyOwn2());
			GraphViewSeries ownSeries = new GraphViewSeries(dbHelper.getSetting(dbHelper.getJsonOwn2ID()), new GraphViewSeriesStyle(Color.WHITE, 3), ownData);
			graphView.addSeries(ownSeries);
			
			GraphView graphViewOwn = new LineGraphView(this, getString(R.string.ownSensor));
			graphViewOwn.addSeries(ownSeries);
			((LineGraphView) graphViewOwn).setDrawBackground(true);
			((LineGraphView) graphViewOwn).getGraphViewStyle().setNumHorizontalLabels(MAX_NUM_HORIZONTAL_LABELS);
			LinearLayout OwnLayout = (LinearLayout) findViewById(ids[idCounter]);
			OwnLayout.addView(graphViewOwn);
			idCounter++;
		}
		
		graphView.setShowLegend(true);
		graphView.setLegendAlign(LegendAlign.MIDDLE);
		graphView.setLegendWidth(350);
		((LineGraphView) graphView).getGraphViewStyle().setNumHorizontalLabels(MAX_NUM_HORIZONTAL_LABELS);
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
