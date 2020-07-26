package rpc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.ElasticSearchConnection;

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
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (hit.isEmpty()) {
			System.out.println("empty");
			response.sendError(404, "cannot find this item");
			return;
		}
		String status = (String) hit.get("itemStatus");
		if (status.contentEquals("COLLECTED")) {
			response.getWriter().write("Successfully mark pickup complete.");
			response.setStatus(204);
			return;
		} else {
			System.out.println("other issues");
			response.sendError(404, "Other errors, potentially DB");
			return;
		}
	}

}