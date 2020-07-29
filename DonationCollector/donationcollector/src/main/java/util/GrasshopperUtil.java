package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

import entity.GeoLocation;

public class GrasshopperUtil {
	public static JSONObject sendRequest(JSONObject request) {
		try {
			URL url = new URL("https://graphhopper.com/api/1/vrp?key=" + GrasshopperCredentials.apiKey);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);
			String jsonInputString = request.toString();
			try (OutputStream os = con.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				return new JSONObject(response.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static JSONObject parseRequest(JSONObject request) {
		// Create result JSONObject
		JSONObject result = new JSONObject();

		// Parse "vehicles" field in result JSONObject
		JSONArray vehicles = new JSONArray();
		JSONObject vehiclesObj = new JSONObject();
		JSONObject requestStartObj = request.getJSONObject("start");
		String NGOId = requestStartObj.getString("NGOName");
		String NGOAddress = requestStartObj.getString("NGOAddress");
		GeoCoding geo = new GeoCoding();
		GeoLocation loc = new GeoLocation(-1, -1);
		try {
			loc = geo.parseAddress(NGOAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
		vehiclesObj.put("vehicle_id", NGOId);
		JSONObject startAddress = new JSONObject();
		startAddress.put("location_id", NGOAddress);
		startAddress.put("lon", loc.getLng());
		startAddress.put("lat", loc.getLat());
		vehiclesObj.put("start_address", startAddress);
		vehiclesObj.put("return_to_depot", false);
		vehicles.put(vehiclesObj);
		result.put("vehicles", vehicles);

		// Parse "services" field in result JSONObject
		JSONArray waypoints = request.getJSONArray("waypoints");
		JSONArray services = new JSONArray();
		int count = 0;
		while (count < waypoints.length() && count < 4) {
			JSONObject waypoint = waypoints.getJSONObject(count);
			String itemId = waypoint.getString("itemId");
			String itemName = waypoint.getString("itemName");
			String itemAddress = waypoint.getString("address");
			GeoCoding itemGeo = new GeoCoding();
			GeoLocation itemLoc = new GeoLocation(-1, -1);
			try {
				itemLoc = itemGeo.parseAddress(itemAddress);
			} catch (Exception e) {
				e.printStackTrace();
			}

			JSONObject service = new JSONObject();
			service.put("id", itemId);
			service.put("name", itemName);
			JSONObject address = new JSONObject();
			address.put("location_id", itemAddress);
			address.put("lon", itemLoc.getLng());
			address.put("lat", itemLoc.getLat());
			service.put("address", address);

			services.put(service);
			count++;
		}

		result.put("services", services);
		return result;
	}
}
