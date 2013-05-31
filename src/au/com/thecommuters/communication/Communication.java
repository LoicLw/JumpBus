package au.com.thecommuters.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;


public class Communication {

	public static JSONArray callServer(String url) {

		JSONArray response = null;

		BufferedReader in = null;
		HttpURLConnection conn = null;
		URL uri;
		try {
			uri = new URL(url);
			conn = (HttpURLConnection) uri.openConnection();
			conn.setRequestMethod("GET");
			conn.setDefaultUseCaches(false);
			conn.connect();

			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		String strResult = null;
		try {

			StringBuilder sb = new StringBuilder();
			String line = "";
			
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			conn.disconnect();
			strResult = sb.toString();
			
		} catch (ParseException e) {
			e.printStackTrace();

			return null;
		} catch (IOException e) {
			
			e.printStackTrace();
			return null;
		}

		try {
			response = new JSONArray(strResult);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return response;
	}

	
}
