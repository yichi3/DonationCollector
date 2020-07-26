package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

import db.ElasticSearchConnection;
import db.GCSConnection;
import entity.Category;
import entity.GeoLocation;
import entity.Item;
import entity.Status;
import entity.User;
import entity.UserType;
import util.GeoCoding;

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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (ServletFileUpload.isMultipartContent(request)) {
			// Read data from stream
			final FileItemFactory fileItemFactory = new DiskFileItemFactory();
			final ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
			List<FileItem> items = new ArrayList<>();
			try {
				items = uploadHandler.parseRequest(request);
			} catch (FileUploadException e) {
				response.sendError(400, "Invalid request type");
				e.printStackTrace();
				return;
			}

			// Initialize item info array and list of images uploaded
			JSONArray itemInfo = new JSONArray();
			List<FileItem> itemImages = new ArrayList<>();

			for (Iterator<FileItem> it = items.iterator(); it.hasNext();) {
				final FileItem item = (FileItem) it.next();

				if (!item.isFormField()) {
					// for debug purposes
					System.out.println("File Uploaded");
					System.out.println(item.getContentType());
				} else {
					itemInfo = new JSONArray(item.getString());
					it.remove();
				}
			}
			// Get list of itemImages
			itemImages = items;

			// Do we need to keep a log of items that are failed to be uploaded
			for (int i = 0; i < itemInfo.length(); i++) {
				JSONObject itemObj = itemInfo.getJSONObject(i);

				// Extract poster user
				JSONObject userObj = itemObj.getJSONObject("posterUser");
				User posterUser = User.builder().userId(userObj.getString("userId"))
						.firstName(userObj.getString("firstName")).lastName(userObj.getString("lastName"))
						.userType(UserType.valueOf(userObj.getString("userType"))).email(userObj.getString("email"))
						.address(userObj.getString("address")).build();

				// Generate item UUID
				UUID itemId = UUID.randomUUID();

				// Save to GCS
				String urlToImage = GCSConnection.uploadFile(itemImages.get(i), itemId);

				// Get lat and lon
				GeoCoding geo = new GeoCoding();
				GeoLocation loc = new GeoLocation(-1, -1);
				try {
					loc = geo.parseAddress(itemObj.getString("location"));
				} catch (Exception e) {
					response.sendError(500, "Failed to parse address");
					e.printStackTrace();
					return;
				}

				Item item = Item.builder().posterUser(posterUser).NGOUser(User.builder().userType(UserType.NGO).build())
						.urlToImage(urlToImage).itemId(itemId).itemName(itemObj.getString("itemName"))
						.description(itemObj.getString("description"))
						.category(Category.valueOf(itemObj.getString("category"))).size(itemObj.getString("size"))
						.schedule(RpcHelper.JSONArrayToList(itemObj.getJSONArray("schedule")))
						.location(itemObj.getString("location")).lat(loc.getLat()).lon(loc.getLng())
						.status(Status.valueOf(itemObj.getString("status"))).pickUpDate(new String()).build();

				// Save item to ES
				ElasticSearchConnection esClient = new ElasticSearchConnection();
				esClient.elasticSearchConnection();
				Map<String, String> esResponse = esClient.addItem(item);
				try {
					esClient.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (esResponse.get("statusCode") == "503") {
					response.sendError(503, "Failed to upload item info to elastic search");
					return;
				}
			}
		}
		response.getWriter().write("You have successfully uploaded all items");
		response.setStatus(200);
	}
}
