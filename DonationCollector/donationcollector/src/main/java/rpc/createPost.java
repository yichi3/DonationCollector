package rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.juli.logging.Log;
import org.json.JSONObject;

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
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// Step 1: Read request body
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject obj = new JSONObject(sb.toString());
		
		// Step 2: Parse information
		Integer userId = Integer.parseInt(obj.getString("user_id"));
		String location = obj.getString("location");
		Double lat = Double.parseDouble(obj.getString("lat"));
		Double lon = Double.parseDouble(obj.getString("lon"));
		UUID itemId = UUID.randomUUID();
		String description = obj.getString("description");
		
		// Step 3: Create item object
		
		// Step 4: save image to GCS and get an urlToImage
		// String urlToImage = saveToGCS(formFile);
		
		// Step 5: save item to ES
		
		// Step 6: set response status to 200
		
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		JSONObject testObj = new JSONObject();
		testObj.put("user_id", userId);
		testObj.put("item_id", itemId);
		testObj.put("location", location);
		testObj.put("lat", lat);
		testObj.put("lon", lon);
		testObj.put("description", description);
		writer.print(testObj);
	}

}
