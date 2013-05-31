package au.com.thecommuters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import au.com.thecommuters.communication.Communication;

public class MainActivity extends Activity implements OnClickListener {

	private Button saveBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		saveBtn = (Button) findViewById(R.id.saveProfileBtn);

		saveBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String url = "http://realtime.adelaidemetro.com.au/SiriWebServiceSAVM/SiriStopMonitoring.svc/json/SM?MonitoringRef=1415&LineRef=&PreviewInterval=30&StartTime=&DirectionRef=&StopMonitoringDetailLevel=normal&MaximumStopVisits=20&Item=1";
		
		NetworkOp net = new NetworkOp();
		
		net.execute(url);
		
		//Intent i = new Intent(this,SolutionActivity.class);
		
	}
	
	private class NetworkOp extends AsyncTask<String, Integer, JSONArray>{

		@Override
		protected JSONArray doInBackground(String... params) {
			// TODO Auto-generated method stub
			return Communication.callServer(params[0]);
		}
		
		@Override
		protected void onPostExecute(JSONArray result) {
			
			for(int i= 0 ;i<result.length();i++){
				JSONObject jo = (JSONObject)result.opt(i);
				try {
					System.out.println(jo.getString("ResponseTimestamp"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

}
