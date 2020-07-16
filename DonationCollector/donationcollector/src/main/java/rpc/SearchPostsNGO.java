package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import db.ElasticSearchConnection;

/**
 * Servlet implementation class SearchPostsNGO
 */

public class SearchPostsNGO extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchPostsNGO() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
	 * @throws IOException 
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	// assume the distance is a double for now, depending on request
    	double distance = Double.parseDouble(request.getParameter("distance"));
    	double lat = Double.parseDouble(request.getParameter("lat"));
		double lng = Double.parseDouble(request.getParameter("lon")); 
		ElasticSearchConnection connection = new ElasticSearchConnection();
		ArrayList<Map<String, Object>> hits = connection.queryItemByLocation(lat, lng, distance);
        for (Map<String, Object> hit : hits) {
        	// need to parse map
        }
        // original code: 
        //JSONArray array = new JSONArray();
        // for (SearchHit hit : hits) {
        //	array.put(hit.getSourceAsString());
        //}
        //RpcHelper.writeJsonArray(response, array);
        		
    }
}