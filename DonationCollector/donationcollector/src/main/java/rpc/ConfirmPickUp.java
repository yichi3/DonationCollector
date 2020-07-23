package rpc;

import java.util.Map;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.ElasticSearchConnection;
import entity.Item;
import entity.Status;

public class ConfirmPickUp extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3257472465914153725L;

	/**
	 * Servlet implementation class ComfirmPickUp
	 */
	
	public ConfirmPickUp() {
        super();
        // TODO Auto-generated constructor stub
    }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String itemId = request.getParameter("itemId");
		
		ElasticSearchConnection connection = new ElasticSearchConnection();
		
		Map<String, Object>  hit = connection.queryItemByItemId(itemId);
		if (hit == null || !hit.containsKey("itemId")) {
			response.sendError(404, "cannot find this item");
			return;
		}
		
		Item item = (Item) hit.get("itemId");
		Item.builder().status(Status.COLLECTED).build();
		connection.addItem(item);
		response.setStatus(204);
	}

}
