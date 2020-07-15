package rpc;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.ElasticSearchConnection;

public class ConfirmPickUp extends HttpServlet {
	
	/**
	 * Servlet implementation class ComfirmPickUp
	 */
	
	public ConfirmPickUp() {
        super();
        // TODO Auto-generated constructor stub
    }
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String itemId = request.getParameter("item_id");
		
		ElasticSearchConnection connection = new ElasticSearchConnection();
		
		Map<String, Object>  hit = connection.queryItemByItemId(itemId);
		if (result == null || !result.containsKey("item_id")) {
			response.sendError(404, "cannot find this item");
			return;
		}
		
		Item item = hit.get("item_id");
		item.getBuilder().status(Status.COLLECTED).build();
		connection.add(item);
		response.setStatus(204);
	}

}
