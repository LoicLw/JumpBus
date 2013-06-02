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
	private static String bus_name_from_google = "";

	public static String getBus_name_from_google() {
		return bus_name_from_google;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		saveBtn = (Button) findViewById(R.id.saveProfileBtn);

		saveBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		Intent i = new Intent(this, SolutionActivity.class);
		getTransitRoute();
		i.putExtra("BusNum", "");
		startActivity(i);

	}

	public void getTransitRoute() {
		
		long unixTime = System.currentTimeMillis()/1000L;
		
		//For Sunday at 5pm:
		//Integer time_when_leaving = 1370158200;

		String url = "http://maps.googleapis.com/maps/api/directions/json?origin="
				+ "211%20Victoria%20Square%20Adelaide%20SA%205000&"
				+ "destination=Tea%20Tree%20Plaza,%20SA"
				+ ""
				+ "&sensor=false&departure_time="
				+ unixTime + "&mode=transit";

		NetworkOp_ForGoogleAPI net = new NetworkOp_ForGoogleAPI();
		net.execute(url);

	}

	private class NetworkOp_ForGoogleAPI extends
			AsyncTask<String, Integer, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {

			return Communication.callServer(params[0]);
		}

		@Override
		protected void onPostExecute(JSONObject result) {

			try {
				JSONArray oa = (JSONArray) result.opt("routes");
				JSONObject jo = oa.getJSONObject(0);

				JSONArray ja_legs = jo.getJSONArray("legs");
				JSONObject jo_legs = ja_legs.getJSONObject(0);
				JSONArray ja_steps = jo_legs.getJSONArray("steps");
				JSONObject jo_steps = ja_steps.getJSONObject(1); // The second
																	// part is
																	// the bus
																	// part

				JSONObject jo_transit_details = jo_steps
						.getJSONObject("transit_details");
				JSONObject jo_line = jo_transit_details.getJSONObject("line");
				bus_name_from_google = jo_line.getString("short_name");
				System.out.println("Bus number: " + bus_name_from_google);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

}
