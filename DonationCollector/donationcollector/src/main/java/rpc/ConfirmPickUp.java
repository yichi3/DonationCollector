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
    }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String itemId = request.getParameter("itemId");
		String ngoId = request.getParameter("ngoId");
		
		ElasticSearchConnection connection = new ElasticSearchConnection();
		connection.elasticSearchConnection();
		
		Map<String, Object> hit = connection.markItemComplete(itemId, ngoId);
		System.out.println("111");
		System.out.println(hit);
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (hit.isEmpty()) {
			response.sendError(404, "cannot find this item");
			return;
		}
		response.setStatus(204);
	}

}
