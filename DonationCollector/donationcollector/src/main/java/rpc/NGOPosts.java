package rpc;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import db.ElasticSearchConnection;
import entity.UserType;

/**
 * Servlet implementation class NGOPosts
 */
public class NGOPosts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NGOPosts() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 1. get ngoId
		String ngoId = request.getParameter("ngoId");
		if (ngoId == null) {
			response.setStatus(404);
			response.getWriter().write("Please indicate an NGO user.");
			return;
		}
		
		// 2. query on ES

		ElasticSearchConnection es = new ElasticSearchConnection();
		es.elasticSearchConnection();

		// 3. parse hits
		try {
			List<Map<String, Object>> dbResponse = es.queryItemByPickUpNGOId(ngoId);
			System.out.println("got db response");
			System.out.println("response number: " + dbResponse.size());
			JSONArray items = new JSONArray();

			for (Map<String, Object> post : dbResponse) {
				if (!((String) post.get("itemStatus")).equals("SCHEDULED")) {
					continue;
				}

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

				items.put(item);
			}

			response.setContentType("application/json");
			response.getWriter().print(items);

			es.close();

		} catch (Exception e) {
			/*
			 * response.setStatus(503); response.getWriter().write("Database error.");
			 */
			e.printStackTrace();
		}
		
	}

}
