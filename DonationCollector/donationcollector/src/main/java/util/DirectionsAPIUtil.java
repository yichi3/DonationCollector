package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class DirectionsAPIUtil {
	static final String prefix = "https://maps.googleapis.com/maps/api/directions/json?";

	public static JSONObject sendRequest(String urlString) {
		try {
			urlString = urlString.replace(" ", "%20");
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				// print result
				return new JSONObject(response.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static String parseToUrl(JSONObject GHResponse) {
		StringBuilder sb = new StringBuilder();
		sb.append(prefix);

		JSONObject solution = GHResponse.getJSONObject("solution");
		JSONArray routes = solution.getJSONArray("routes");
		JSONObject route = routes.getJSONObject(0);
		JSONArray activities = route.getJSONArray("activities");

		String start = new String(), end = new String();
		StringBuilder waypoints = new StringBuilder("waypoints=");
		for (int i = 0; i < activities.length(); i++) {
			JSONObject activity = activities.getJSONObject(i);
			if (activity.getString("type").equalsIgnoreCase("start")) {
				start = new String("origin=" + activity.getString("location_id") + "&");
			} else if (i == activities.length() - 1) {
				end = new String("destination=" + activity.getString("location_id") + "&");
			} else {
				waypoints.append(activity.getString("location_id"));
				waypoints.append("|");
			}
		}

		waypoints.deleteCharAt(waypoints.length() - 1);
		sb.append(start).append(end).append(waypoints).append("&key=").append(DirectionsAPICredentials.apiKey);

		return sb.toString();
	}
}
