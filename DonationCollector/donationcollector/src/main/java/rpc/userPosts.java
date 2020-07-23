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

/**
 * Servlet implementation class UserPosts
 */
public class userPosts extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public userPosts() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

// 		endpoint: /userposts
//		1. get request userId

		if(request.getParameter("userId") == null) {
			response.setStatus(404);
			response.getWriter().write("Please indicate a user.");
			return;
		}
		
		String userId = request.getParameter("userId");

//		2. query on ES

		ElasticSearchConnection es = new ElasticSearchConnection();
		es.elasticSearchConnection();

// 3. convert db response

		try {	
			List<Map<String, Object>> dbResponse = es.queryItemByPosterId(userId);
	
			JSONArray items = new JSONArray();

			for (Map<String, Object> post : dbResponse) {

				JSONObject item = new JSONObject();

				item.put("itemId", post.get("itemId"));
				item.put("urlToImage", post.get("urlToImage"));
				item.put("locationLatLon", post.get("locationLatLon"));
				item.put("locationAddress", post.get("locationAddress"));
				item.put("userId", post.get("posterId"));
//				get username from firebase
				item.put("category", post.get("category"));
				item.put("description", post.get("description"));
				item.put("size", post.get("size"));
				item.put("itemStatus", post.get("itemStatus"));
				item.put("pickUpNGOId", post.get("pickUpNGOId"));
//				get ngo user name & other info from firebase		
				item.put("pickUpTime", post.get("pickUpTime"));
				item.put("postDate", post.get("postDate"));
				item.put("availablePickUpTime", post.get("availablePickUpTime"));

				items.put(item);
			}

			response.setContentType("application/json");
			response.getWriter().print(items);
			
		}catch(Exception e) {
			response.setStatus(503);
			response.getWriter().write("Database error.");
		}
	}
}
