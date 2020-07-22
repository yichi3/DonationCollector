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
	 * Servlet implementation class ComfirmPickUp
	 */
	
	public ConfirmPickUp() {
        super();
    }
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String itemId = request.getParameter("itemId");
		String ngoId = request.getParameter("pickUpNGOId");
		
		ElasticSearchConnection connection = new ElasticSearchConnection();
		connection.elasticSearchConnection();
		
		Map<String, Object> hit = connection.markItemComplete(itemId, ngoId);
		System.out.println(hit);
		if (hit.isEmpty()) {
			response.sendError(404, "cannot find this item");
			return;
		}
		response.setStatus(204);
	}

}
