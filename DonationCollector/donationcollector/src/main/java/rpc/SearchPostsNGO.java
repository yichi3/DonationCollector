package rpc;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONArray;

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
		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(new HttpHost("35.225.69.232", 9200, "http")));
		
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
        
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        
        JSONArray array = new JSONArray();
        for (SearchHit hit : hits) {
        	// need to implement hit to json transformation!!!
        	array.put(Geo_Service.getObjectFrom_ES_Hit(hit, Item.class));
        }
        RpcHelper.writeJsonArray(response, array);
        		
    }
}