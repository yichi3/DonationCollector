package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import util.DirectionsAPIUtil;
import util.GrasshopperUtil;

/**
 * Servlet implementation class createRoute
 */
public class createRoute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public createRoute() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject jsonRequest = RpcHelper.readJSONObject(request);
		JSONObject parsedRequest = GrasshopperUtil.parseRequest(jsonRequest);
		JSONObject GHResponse = GrasshopperUtil.sendRequest(parsedRequest);

		JSONObject result = new JSONObject();
		JSONArray itemsArray = new JSONArray();
		
		JSONArray fromParsedRequest = parsedRequest.getJSONArray("services");
		for (int i = 0; i < fromParsedRequest.length(); i++) {
			JSONObject itemInfo = fromParsedRequest.getJSONObject(i);
			JSONObject item = new JSONObject();
			item.put("itemId", itemInfo.getString("id"));
			item.put("itemName", itemInfo.getString("name"));
			JSONObject address = itemInfo.getJSONObject("address");
			item.put("address", address.getString("location_id"));
			item.put("lat", Double.parseDouble(address.get("lat").toString()));
			item.put("lon", Double.parseDouble(address.get("lon").toString()));
			itemsArray.put(item);
		}
		
		result.put("items", itemsArray);

		String url = DirectionsAPIUtil.parseToUrl(GHResponse);
		JSONObject directionsResponse = DirectionsAPIUtil.sendRequest(url);
		
		JSONArray routes = directionsResponse.getJSONArray("routes");
		JSONObject routeObject = routes.getJSONObject(0);
		JSONArray legs = routeObject.getJSONArray("legs");
		
		JSONObject route = new JSONObject();
		int count = 0;
		
		for (int i = 0; i < legs.length(); i++) {
			JSONObject leg = legs.getJSONObject(i);
			JSONArray steps = leg.getJSONArray("steps");
			for (int j = 0; j < steps.length(); j++) {
				JSONObject step = steps.getJSONObject(j);
				if (j == 0) {
					JSONObject startLocation = step.getJSONObject("start_location");
					JSONObject point = new JSONObject();
					point.put("lat", Double.parseDouble(startLocation.get("lat").toString()));
					point.put("lng", Double.parseDouble(startLocation.get("lng").toString()));
					route.put(Integer.toString(count), point);
					count++;
				}
				JSONObject endLocation = step.getJSONObject("end_location");
				JSONObject point = new JSONObject();
				point.put("lat", Double.parseDouble(endLocation.get("lat").toString()));
				point.put("lng", Double.parseDouble(endLocation.get("lng").toString()));
				route.put(Integer.toString(count), point);
				count++;
			}
		}
		
		result.put("route", route);
		result.put("steps", Integer.toString(count)); 
		RpcHelper.writeJsonObject(response, result);
	}

}
