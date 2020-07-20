package rpc;

import java.io.IOException;
<<<<<<< HEAD
import java.io.PrintWriter;
=======
>>>>>>> a038e26c47ea2c00aeadd011ffa416d216e90c80
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

<<<<<<< HEAD
import entity.Category;
import entity.Item;
import entity.Status;
import entity.User;
import entity.UserType;
=======
import Entity.Category;
import Entity.Item;
import Entity.Status;
import Entity.User;
import Entity.UserType;
>>>>>>> a038e26c47ea2c00aeadd011ffa416d216e90c80
import db.GCSConnection;

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
		// Add authentication step?

		if (ServletFileUpload.isMultipartContent(request)) {
			// Read data from stream
			final FileItemFactory fileItemFactory = new DiskFileItemFactory();
			final ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
			List<FileItem> items = new ArrayList<>();
<<<<<<< HEAD

=======
			
>>>>>>> a038e26c47ea2c00aeadd011ffa416d216e90c80
			try {
				items = uploadHandler.parseRequest(request);
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Initialize item info array and list of images uploaded
			JSONArray itemInfo = new JSONArray();
			List<FileItem> itemImages = new ArrayList<>();
<<<<<<< HEAD

=======
			
>>>>>>> a038e26c47ea2c00aeadd011ffa416d216e90c80
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
<<<<<<< HEAD

			// For test purpose
			System.out.println(itemImages.get(0).getInputStream().toString());

=======
			
			// For test purpose
			System.out.println(itemImages.get(0).getInputStream().toString());
			
>>>>>>> a038e26c47ea2c00aeadd011ffa416d216e90c80
			// Do we need to keep a log of items that are failed to be uploaded
			for (int i = 0; i < itemInfo.length(); i++) {
				JSONObject itemObj = itemInfo.getJSONObject(i);
				// Extract poster user
				JSONObject userObj = itemObj.getJSONObject("posterUser");
				User posterUser = User.builder().userId(userObj.getString("userId")).name(userObj.getString("name"))
						.userType(UserType.valueOf(userObj.getString("userType"))).email(userObj.getString("email"))
						.address(userObj.getString("address")).build();

				// Extract NGO user
				JSONObject NGOObj = itemObj.getJSONObject("NGOUser");
				User NGOUser = User.builder().userId(NGOObj.getString("userId")).name(NGOObj.getString("name"))
						.userType(UserType.valueOf(NGOObj.getString("userType"))).email(NGOObj.getString("email"))
						.address(NGOObj.getString("address")).build();

				// Upload image to GCS and get urlToImage
				// FileItem image = itemImages.get(i);
<<<<<<< HEAD

=======
			
>>>>>>> a038e26c47ea2c00aeadd011ffa416d216e90c80
				// Generate item UUID
				UUID itemId = UUID.randomUUID();

				// Save to GCS
				String urlToImage = GCSConnection.uploadFile(itemImages.get(i), itemId);
<<<<<<< HEAD

				Item item = Item.builder().posterUser(posterUser).NGOUser(NGOUser).urlToImage(urlToImage).itemId(itemId)
						.itemName(itemObj.getString("itemName")).description(itemObj.getString("description"))
=======
				
				Item item = Item.builder().posterUser(posterUser).NGOUser(NGOUser).urlToImage(urlToImage).itemId(itemId)
						.itemName(itemObj.getString("itemName"))
						.description(itemObj.getString("description"))
>>>>>>> a038e26c47ea2c00aeadd011ffa416d216e90c80
						.category(Category.valueOf(itemObj.getString("category"))).size(itemObj.getString("size"))
						.schedule(RpcHelper.JSONArrayToList(itemObj.getJSONArray("schedule")))
						.location(itemObj.getString("location")).lat(Double.parseDouble(itemObj.getString("lat")))
						.lon(Double.parseDouble(itemObj.getString("lon")))
<<<<<<< HEAD
						.status(Status.valueOf(itemObj.getString("status"))).pickUpDate(itemObj.getString("pickUpDate"))
						.build();
=======
						.status(Status.valueOf(itemObj.getString("status")))
						.pickUpDate(itemObj.getString("pickUpDate")).build();
>>>>>>> a038e26c47ea2c00aeadd011ffa416d216e90c80

				// Save Item to ES
				// Boolean response = saveToES(item.toJSONObject);
				// Log if failed to upload this item
<<<<<<< HEAD

=======
				
>>>>>>> a038e26c47ea2c00aeadd011ffa416d216e90c80
				response.setContentType("application/json");
				response.getWriter().print(item.toJSONObject());
				response.getWriter().write("You have successfully uploaded all items");
			}
		}

	}
<<<<<<< HEAD

=======
>>>>>>> a038e26c47ea2c00aeadd011ffa416d216e90c80
}
