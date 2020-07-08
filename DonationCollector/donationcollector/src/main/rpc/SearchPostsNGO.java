import javax.servlet.http.HttpServletRequest;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// assume the distance is a double for now, depending on request
    	double distance = Double.parseDouble(request.getParameter("distance"));
    	double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon")); 
		
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		QueryBuilder query = QueryBuilders.matchAllQuery();
		QueryBuilder geoDistanceQueryBuilder = QueryBuilders
	            .geoDistanceQuery("geoPoint")
	            .point(lat, lon)
	            .distance(distance, DistanceUnit.KILOMETERS);
		QueryBuilder finalQuery = QueryBuilders.boolQuery().must(query).filter(geoDistanceQueryBuilder);
		sourceBuilder.query(finalQuery);
        searchRequest.source(sourceBuilder);
        
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] hits = searchResponse.getHits();
        
        JSONArray array = new JSONArray();
        for (SearchHit hit : hits) {
        	array.put(GeoService.getObjectFrom_ES_Hit(hit, Item.class));
        }
        RpcHelper.writeJsonArray(response, array);
        		
    }
}