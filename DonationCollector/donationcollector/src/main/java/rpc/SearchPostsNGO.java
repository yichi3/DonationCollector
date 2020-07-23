package rpc;

import java.io.IOException;
import java.util.ArrayList;
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

			connection.close();
			geolocation = geocoding.parseAddress(address);
			JSONArray resultArray = new JSONArray();
			for (Map<String, Object> hit : hits) {
				Gson gson = new Gson();
				String itemString = gson.toJson(hit);
				JSONObject obj = new JSONObject(itemString);
				resultArray.put(obj);
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
