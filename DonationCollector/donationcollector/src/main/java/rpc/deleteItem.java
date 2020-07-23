package rpc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.ElasticSearchConnection;

/**
 * Servlet implementation class DeleteItem
 */
public class deleteItem extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public deleteItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

// 		endpoint: /deleteitem
//		1. get request userId

		if (request.getParameter("userId") == null) {
			response.setStatus(404);
			response.getWriter().write("Please indicate the user.");
			return;
		}
		if (request.getParameter("itemId") == null) {
			response.setStatus(404);
			response.getWriter().write("Please indicate the item.");
			return;
		}

		String userId = request.getParameter("userId");
		String itemId = request.getParameter("itemId");


//		2. query on ES

		ElasticSearchConnection es = new ElasticSearchConnection();
		es.elasticSearchConnection();

// 3. convert db response
		try {
			Map<String, Object> dbResponse = es.deleteItem(itemId);

			if (dbResponse.size() == 0) {
				response.getWriter().write("The item doesn't exist");
				response.setStatus(400);
				return;
			}

//			3. check whether the item belongs to the user

			String posterId = (String) dbResponse.get("posterId");
			
			if (!posterId.equals(userId)) {
				response.getWriter().write("The item doesn't belong to the user");
				response.setStatus(401);
				return;
			}			

//			4. check item status

			String status = (String) dbResponse.get("itemStatus");		

			if (status.equals("SCHEDULED")) {
				response.getWriter().write("The item has been scheduled and can not be deleted.");
				response.setStatus(403);
				return;
			}

			if (status.equals("COLLECTED")) {
				response.getWriter().write("The item has been collected and can not be deleted.");
				response.setStatus(403);
				return;
			}

			if (status.equals("DELETED")) {
				response.getWriter().write("The item has been deleted.");
				response.setStatus(200);
				return;
			}

		} catch (Exception e) {
			response.setStatus(503);
			response.getWriter().write("Database error.");
		}
	}
}
