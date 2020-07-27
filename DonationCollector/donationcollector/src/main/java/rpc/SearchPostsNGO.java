package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import com.google.maps.errors.ApiException;

import db.ElasticSearchConnection;
import entity.GeoLocation;
import entity.UserType;
import util.GeoCoding;

/**
 * Servlet implementation class SearchPostsNGO
 */
public class SearchPostsNGO extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchPostsNGO() {
		super();

		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String address = request.getParameter("address");
		double distance = Double.parseDouble(request.getParameter("distance"));

		try {
			GeoCoding geocoding = new GeoCoding();
			GeoLocation geolocation = geocoding.parseAddress(address);
			double lat = geolocation.getLat();
			double lng = geolocation.getLng();
			
			ElasticSearchConnection connection = new ElasticSearchConnection();
			connection.elasticSearchConnection();

			ArrayList<Map<String, Object>> hits = connection.queryItemByLocation(lat, lng, distance);
			System.out.println("response number: " + hits.size());
			connection.close();
			
			JSONArray resultArray = new JSONArray();
			
			for (Map<String, Object> post : hits) {
				JSONObject item = new JSONObject();

				item.put("itemId", post.get("itemId"));
				item.put("urlToImage", post.get("urlToImage"));

				String locationLatLon = post.get("locationLatLon").toString();
				List<String> latNLon = RpcHelper.parseLocation(locationLatLon);
				item.put("lat", Double.parseDouble(latNLon.get(0)));
				item.put("lon", Double.parseDouble(latNLon.get(1)));
				item.put("address", post.get("locationAddress"));

				JSONObject posterUser = new JSONObject();
				posterUser.put("userId", post.get("posterId"));
				posterUser.put("firstName", post.get("posterFirstName"));
				posterUser.put("lastName", post.get("posterLastName"));
				posterUser.put("userType", UserType.INDIVIDUAL);
				posterUser.put("email", "null");
				posterUser.put("address", "null");
				item.put("posterUser", posterUser);

				item.put("itemName", post.get("itemName"));
				item.put("category", post.get("category"));
				item.put("description", post.get("description"));
				item.put("size", post.get("size"));
				item.put("status", post.get("itemStatus"));

				JSONObject NGOUser = new JSONObject();
				NGOUser.put("userId", post.get("pickUpNGOId"));
				NGOUser.put("ngoName", post.get("pickUpNGOName"));
				NGOUser.put("userType", UserType.NGO);
				NGOUser.put("email", "null");
				NGOUser.put("address", "null");
				item.put("NGOUser", NGOUser);

				item.put("pickUpDate", post.get("pickUpTime"));
//				item.put("postDate", post.get("postDate"));
				item.put("schedule", new JSONArray(post.get("availablePickUpTime").toString()));

			}
			RpcHelper.writeJsonArray(response, resultArray);

		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
