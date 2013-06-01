package au.com.thecommuters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import au.com.thecommuters.communication.Communication;
import au.com.thecommuters.model.BusDataFromRequest;

public class SolutionActivity extends Activity{
	
	private List<BusDataFromRequest> bus_requests = new ArrayList<BusDataFromRequest>();
	
	private ProgressBar progressWait;
	
	private ListView dataList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
				
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_solution);
		
		progressWait = (ProgressBar)findViewById(R.id.progressWait);
		
		progressWait.setVisibility(View.VISIBLE);
		
		dataList = (ListView)findViewById(R.id.listView);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		String stopId = "1415";
		int interval = 100;
		
		String url = "http://realtime.adelaidemetro.com.au/SiriWebServiceSAVM/SiriStopMonitoring.svc/json/SM?MonitoringRef="+
				stopId +"&LineRef=&PreviewInterval="+ interval +
				"&StartTime=&DirectionRef=&StopMonitoringDetailLevel=normal&MaximumStopVisits=20&Item=1";
		
		NetworkOp net = new NetworkOp();
		net.execute(url);
		
	}
	
	private class NetworkOp extends AsyncTask<String, Integer, JSONObject>{

		
		@Override
		protected JSONObject doInBackground(String... params) {
			//progressWait.setVisibility(View.VISIBLE);
			return Communication.callServer(params[0]);
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {		
		 JSONArray oa = (JSONArray) result.opt("StopMonitoringDelivery");
		 try {
			JSONObject jo = oa.getJSONObject(0);		
			JSONArray ja_montored_stop_visit = jo.getJSONArray("MonitoredStopVisit");
			BusDataFromRequest preData = null;
			
			for (int i=0; i < ja_montored_stop_visit.length(); i++ ){
				JSONObject jo2 = ja_montored_stop_visit.getJSONObject(i);					
				JSONObject jo_monitored_vehicle_journey = jo2.getJSONObject("MonitoredVehicleJourney");		
	
				JSONObject monitoredCall = jo_monitored_vehicle_journey.getJSONObject("MonitoredCall");
				
				String aimedTime = monitoredCall.getString("AimedArrivalTime");
				
				String latestExpArrTime = monitoredCall.getString("LatestExpectedArrivalTime");
			
				JSONObject jo_direction = jo_monitored_vehicle_journey.getJSONObject("DirectionRef");
				String direction = jo_direction.getString("Value");
				
				JSONObject jo_line = jo_monitored_vehicle_journey.getJSONObject("LineRef");
				String line = jo_line.getString("Value");	
				
				BusDataFromRequest data = new BusDataFromRequest(Long.parseLong(aimedTime.substring(aimedTime.indexOf("(")+1, aimedTime.indexOf("+"))), direction, line,Long.parseLong(latestExpArrTime.substring(latestExpArrTime.indexOf("(")+1, latestExpArrTime.indexOf("+"))));
				//if(preData == null){
					//preData = data;
					bus_requests.add(data);
			//	}
					
				System.out.println(direction + ' ' + line);
			}
			
			//BusInfoListAdapter bla = new BusInfoListAdapter(SolutionActivity.this, bus_requests);
			
			//dataList.setAdapter(bla);
			//Iterator<BusDataFromRequest> i = bus_requests.iterator();
			for(BusDataFromRequest info : bus_requests){
				// info = i.next();
				long delay = info.getLatestExpArrivalTime() - info.getAimedArrivalTime();
				if(delay > 0)
				Toast.makeText(SolutionActivity.this, "The bus "+info.getLine()+" is delayed "+ (delay/1000)%60 + "minutes", Toast.LENGTH_SHORT).show();
				
				else if(delay == 0)
					Toast.makeText(SolutionActivity.this, "Bus is on time", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(SolutionActivity.this, "Bus is " +(delay/1000)%60 +" minutes ahead of schdule", Toast.LENGTH_SHORT).show();
			}
			
			
			
			progressWait.setVisibility(View.GONE);
			
		} catch (JSONException e) {
			e.printStackTrace();
			progressWait.setVisibility(View.GONE);
			Toast.makeText(SolutionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

		}
		
	}

}
