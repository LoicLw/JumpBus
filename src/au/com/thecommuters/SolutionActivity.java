package au.com.thecommuters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import au.com.thecommuters.adapter.BusInfoListAdapter;
import au.com.thecommuters.communication.Communication;
import au.com.thecommuters.model.BusDataFromRequest;
import au.com.thecommuters.model.Constant;
import au.com.thecommuters.service.SolutionFinder;

public class SolutionActivity extends Activity implements OnItemClickListener{
	
	private Set<BusDataFromRequest> bus_requests = new HashSet<BusDataFromRequest>();
	
	private ProgressBar progressWait;
	
	private ListView dataList;
	
	private String busNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
				
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_solution);
		
		progressWait = (ProgressBar)findViewById(R.id.progressWait);
		
		progressWait.setVisibility(View.VISIBLE);
		
		dataList = (ListView)findViewById(R.id.listView);
		
		busNum = (String)getIntent().getExtras().get("BusNum");
		
		dataList.setOnItemClickListener(this);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		String stopId = "100083";
		int interval = 30;
		
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
			//BusDataFromRequest preData = null;
			
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
					
				//System.out.println("bus " + line + " with direction: "+ direction +" aimed time is: "+new Date(data.getAimedArrivalTime())+" and the latest expect arrival time is "+new Date(data.getLatestExpArrivalTime()) );
			}
			
			BusInfoListAdapter bla = new BusInfoListAdapter(SolutionActivity.this, bus_requests);
			
			dataList.setAdapter(bla);

			
			
			
			progressWait.setVisibility(View.GONE);
			
		} catch (JSONException e) {
			e.printStackTrace();
			progressWait.setVisibility(View.GONE);
			Toast.makeText(SolutionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		LayoutInflater inflater = LayoutInflater.from(this);
		final View textEntryView = inflater.inflate(
				R.layout.bus_info_item, null);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.UK);

		TextView busLine =  (TextView)textEntryView.findViewById(R.id.lineTxt);
		TextView aimedArrivalTime = (TextView)textEntryView.findViewById(R.id.aimedTime);
		ImageView recommand = (ImageView)textEntryView.findViewById(R.id.imageView1);
		recommand.setVisibility(View.GONE);
		TextView expArrivalTime = (TextView)textEntryView.findViewById(R.id.expectArriveTime);
		TextView driver = (TextView)textEntryView.findViewById(R.id.busStatus);
		BusDataFromRequest busInfo = (BusDataFromRequest)arg0.getItemAtPosition(arg2);
		busLine.setText("Bus Line:"+busInfo.getLine());
		aimedArrivalTime.setText("Aimed arrival time: "+sdf.format(new Date(busInfo.getAimedArrivalTime())));
		expArrivalTime.setText("Expected arrival time: "+ sdf.format(new Date(busInfo.getLatestExpArrivalTime())));
		
		driver.setText("Driver's ref: 43039");
		
		Date alarm = new Date(busInfo.getLatestExpArrivalTime()-Constant.BUS_DURATION_TO_KLEMZIG);
		textEntryView.setBackgroundColor(Color.rgb(131, 177, 150));
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
				adb.setTitle("Solution choose confirm");
				adb.setView(textEntryView);
				
				adb.setPositiveButton("Set Alarm and notify the driver", new ConfirmClickListener(alarm) );
				adb.setNegativeButton("Cancel", null);
				adb.show();       
		
	}
	
	private class ConfirmClickListener implements DialogInterface.OnClickListener{

		private Date alarmTime;
		
		public ConfirmClickListener(Date alarm){
			this.alarmTime = alarm;
		}
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			
			
			Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
		    i.putExtra(AlarmClock.EXTRA_HOUR, alarmTime.getHours());
		    i.putExtra(AlarmClock.EXTRA_MINUTES, alarmTime.getMinutes());
		    i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
		    
		    startActivity(i);
			Toast.makeText(SolutionActivity.this, "Alarm has been set and driver is notified", Toast.LENGTH_LONG).show();
			Intent service = new Intent(SolutionActivity.this,SolutionFinder.class);
			startService(service);
		}
		
	}

}
