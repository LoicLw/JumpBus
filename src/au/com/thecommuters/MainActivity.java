package au.com.thecommuters;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import au.com.thecommuters.communication.Communication;

public class MainActivity extends Activity implements OnClickListener {

	private Button saveBtn;
	private ArrayList<BusDataFromRequest> bus_requests = new ArrayList<BusDataFromRequest>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		saveBtn = (Button) findViewById(R.id.saveProfileBtn);

		saveBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		String stopId = "1415";
		Integer interval = 30;
		
		String url = "http://realtime.adelaidemetro.com.au/SiriWebServiceSAVM/SiriStopMonitoring.svc/json/SM?MonitoringRef="+
				stopId +"&LineRef=&PreviewInterval="+ interval.toString() +
				"&StartTime=&DirectionRef=&StopMonitoringDetailLevel=normal&MaximumStopVisits=20&Item=1";
		
		NetworkOp net = new NetworkOp();
		net.execute(url);
		
		//Intent i = new Intent(this,SolutionActivity.class);
		
	}
	
	private class NetworkOp extends AsyncTask<String, Integer, JSONObject>{

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			return Communication.callServer(params[0]);
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {		
		 JSONArray oa = (JSONArray) result.opt("StopMonitoringDelivery");
		 try {
			JSONObject jo = oa.getJSONObject(0);		
			JSONArray ja_montored_stop_visit = jo.getJSONArray("MonitoredStopVisit");
			
			for (int i=0; i < ja_montored_stop_visit.length(); i++ ){
				JSONObject jo2 = ja_montored_stop_visit.getJSONObject(i);					
				JSONObject jo_monitored_vehicle_journey = jo2.getJSONObject("MonitoredVehicleJourney");		
	
				String time = jo_monitored_vehicle_journey.getString("DestinationAimedArrivalTime");
			
				JSONObject jo_direction = jo_monitored_vehicle_journey.getJSONObject("DirectionRef");
				String direction = jo_direction.getString("Value");
				
				JSONObject jo_line = jo_monitored_vehicle_journey.getJSONObject("LineRef");
				String line = jo_line.getString("Value");	
				
				bus_requests.add(new BusDataFromRequest(time, direction, line));
				System.out.println(time + ' ' + direction + ' ' + line);
			}
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		}
		
	}

}
