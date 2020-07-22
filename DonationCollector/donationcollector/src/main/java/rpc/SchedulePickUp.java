package rpc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.ElasticSearchConnection;
import entity.Item;
import entity.Status;

/**
 * Servlet implementation class SchedulePickUp
 */
public class SchedulePickUp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SchedulePickUp() {
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String itemId = request.getParameter("item_id");

		ElasticSearchConnection connection = new ElasticSearchConnection();
		connection.elasticSearchConnection();

		Map<String, Object> hit = connection.queryItemByItemId(itemId);
		if (hit == null || !hit.containsKey("item_id")) {
			response.sendError(404, "cannot find this item");
			return;
		}

		Item item = (Item) hit.get("item_id");
		item.builder().status(Status.SCHEDULED).build();
		connection.addItem(item);
		response.setStatus(204);
	}

}

// map empty 
