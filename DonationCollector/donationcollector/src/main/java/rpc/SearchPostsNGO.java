package rpc;

import java.io.IOException;

import javax.servlet.ServletException;
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// assume the distance is a double
		double distance = Double.parseDouble(request.getParameter("distance"));
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
		String POST_INDEX = "post";

		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(new HttpHost("35.225.69.232", 9200, "http")));

		// 1. create the SearchRequest
		SearchRequest searchRequest = new SearchRequest();

		// 2. Most search parameters are added to SearchsearchSourceBuilder
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		// 3. Add a match_all query to the SearchsearchSourceBuilder
		QueryBuilder qb = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(
				QueryBuilders.geoDistanceQuery(POST_INDEX).point(lat, lon).distance(distance, DistanceUnit.KILOMETERS));
		searchSourceBuilder.query(qb);

		// 4. Add the SearchsearchSourceBuilder to the SearchRequest
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHits = hits.getHits();

		JSONArray array = new JSONArray();
		for (SearchHit hit : searchHits) {
			array.put(hit.getSourceAsString());
		}
		client.close();
		
		RpcHelper.writeJSONArray(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
