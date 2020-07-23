package rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Category;
import entity.Item;
import entity.Status;
import entity.User;
import entity.UserType;

/**
 * Servlet implementation class createPost
 */
public class createPost extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public createPost() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// Add authentication step?
		
		JSONArray arr = RpcHelper.readJSONArray(request);
		
		// Do we need to keep a log of items that are failed to be uploaded
		
		for (int i = 0; i < arr.length(); i++) {
			JSONObject itemObj = arr.getJSONObject(i);
			// Extract poster user 
			JSONObject userObj = itemObj.getJSONObject("posterUser");
			User posterUser = User.builder()
					.userId(userObj.getString("user_id"))
					.firstName("firstName")
					.lastName("lastName")
					.userType(UserType.valueOf(userObj.getString("UserType")))
					.email(userObj.getString("email"))
					.address(userObj.getString("address")).build();
			
			// Extract NGO user
			JSONObject NGOObj = itemObj.getJSONObject("NGOUser");
			User NGOUser = User.builder()
					.userId(NGOObj.getString("user_id"))
					.ngoName(NGOObj.getString("ngoName"))
					.userType(UserType.valueOf(NGOObj.getString("UserType")))
					.email(NGOObj.getString("email"))
					.address(NGOObj.getString("address")).build();
			
			// Upload image to GCS and get urlToImage
			// String urlToImage = saveToGCS(itemObj.image.toJSONObject)
			String urlToImage = "testString";
			
			// Generate item UUID
			UUID itemId = UUID.randomUUID();
			
			Item item = Item.builder()
					.posterUser(posterUser)
					.NGOUser(NGOUser)
					.urlToImage(urlToImage)
					.itemId(itemId)
					.description(itemObj.getString("description"))
					.category(Category.valueOf(itemObj.getString("category")))
					.size(itemObj.getString("size"))
					.schedule(RpcHelper.JSONArrayToList(itemObj.getJSONArray("schedule")))
					.location(itemObj.getString("location"))
					.lat(Double.parseDouble(itemObj.getString("lat")))
					.lon(Double.parseDouble(itemObj.getString("lon")))
					.status(Status.valueOf(itemObj.getString("status")))
					.pickUpDate(itemObj.getString("pick_up_date"))
					.build();
			
			// Save Item to ES
			// Boolean response = saveToES(item.toJSONObject);
			// Log if failed to upload this item
		}
		
		response.setContentType("application/json");
		response.getWriter().write("You have successfully uploaded all items");
	}
 
}
