package db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONObject;

import entity.Item;

public class ElasticSearchConnection {
	private RestHighLevelClient client;

	public void elasticSearchConnection() {
		client = new RestHighLevelClient(RestClient.builder(new HttpHost(ElasticSearchDBUtil.INSTANCE,
				ElasticSearchDBUtil.PORT_NUM, ElasticSearchDBUtil.CONNECTION_TYPE)));
	}

	// Please change return type according to how you want status code to show up.
	public Map<String, String> addItem(Item item) {

		JSONObject itemObj = item.toJSONObject();
		JSONObject posterObj = itemObj.getJSONObject("posterUser");
		String posterId = posterObj.getString("userId");
		String posterFirstName = posterObj.getString("firstName");
		String posterLastName = posterObj.getString("lastName");
//		JSONObject ngoObj = itemObj.getJSONObject("NGOUser");
//		String ngoId = ngoObj.getString("userId");
//		String ngoName = ngoObj.getString("ngoName");

		String lat = itemObj.getString("lat");
		String lon = itemObj.getString("lon");

		String geoPint = lat + ", " + lon;

		XContentBuilder builder;
		try {
			builder = XContentFactory.jsonBuilder();
			builder.startObject();
			{
				builder.field("itemId", itemObj.getString("itemId"));
				builder.field("urlToImage", itemObj.getString("urlToImage"));
				builder.field("locationLatLon", geoPint);
				builder.field("locationAddress", itemObj.getString("location"));
				builder.field("itemName", itemObj.getString("itemName"));
				builder.field("size", itemObj.getString("size"));
				builder.field("posterId", posterId);
				builder.field("posterFirstName", posterFirstName);
				builder.field("posterLastName", posterLastName);
				builder.field("category", itemObj.getString("category"));
				builder.field("description", itemObj.getString("description"));
				builder.field("availablePickUpTime", itemObj.getJSONArray("schedule"));
				builder.field("itemStatus", itemObj.getString("status"));
//				builder.field("pickUpNGOId", ngoId);
//				builder.field("pickUpNGOName", ngoName);
//				builder.field("pickUpTime", itemObj.getString("pickUpDate"));
				// to-fix hard-coded for now
				builder.timeField("postDate", "2020-06-30");
			}
			builder.endObject();
			IndexRequest indexRequest = new IndexRequest("items").id(itemObj.getString("itemId")).source(builder);
			IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);

			System.out.println("get results => " + response.getResult());
			Map<String, String> statusCode = new HashMap<>();
			if (response.getResult().toString() == "CREATED") {
				statusCode.put("statusCode", "200");
			} else {
				statusCode.put("statusCode", "503");
			}
			return statusCode;
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, String> statusCode = new HashMap<>();
			statusCode.put("statusCode", "503");
			return statusCode;
		}

	}

	public ArrayList<Map<String, Object>> queryItemByLocation(double lat, double lng, double distance)
			throws IOException {

		ArrayList<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		SearchRequest searchRequest = new SearchRequest("items");

		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

		MatchQueryBuilder query = new MatchQueryBuilder("itemStatus", "PENDING");
		QueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery("locationLatLon").point(lat, lng)
				.distance(distance, DistanceUnit.KILOMETERS);
		QueryBuilder finalQuery = QueryBuilders.boolQuery().must(query).filter(geoDistanceQueryBuilder);
		sourceBuilder.query(finalQuery);
		searchRequest.source(sourceBuilder);

		try {
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			SearchHits hits = searchResponse.getHits();
			for (SearchHit hit : hits) {
				resultList.add(hit.getSourceAsMap());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultList;

	}

	public List<Map<String, Object>> queryItemByPosterId(String userId) {

		ArrayList<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		SearchRequest request = new SearchRequest("items");
		SearchSourceBuilder scb = new SearchSourceBuilder();

		MatchQueryBuilder mqb = new MatchQueryBuilder("posterId", userId);
		scb.query(mqb);

		request.source(scb);
		try {
			SearchResponse response = client.search(request, RequestOptions.DEFAULT);
			SearchHits hits = response.getHits();
			SearchHit[] searchHits = hits.getHits();
			for (SearchHit hit : searchHits) {
				resultList.add(hit.getSourceAsMap());
			}
			return resultList;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return resultList;
		}

	}

	public List<Map<String, Object>> queryItemByPickUpNGOId(String userId) {

		ArrayList<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		SearchRequest request = new SearchRequest("items");
		SearchSourceBuilder scb = new SearchSourceBuilder();

		MatchQueryBuilder mqb = new MatchQueryBuilder("pickUpNGOId", userId);
		scb.query(mqb);

		request.source(scb);
		try {
			SearchResponse response = client.search(request, RequestOptions.DEFAULT);
			SearchHits hits = response.getHits();
			SearchHit[] searchHits = hits.getHits();
			for (SearchHit hit : searchHits) {
				resultList.add(hit.getSourceAsMap());
			}
			return resultList;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return resultList;
		}

	}

	public Map<String, Object> queryItemByItemId(String itemId) {

		Map<String, Object> result;

		SearchRequest request = new SearchRequest("items");
		SearchSourceBuilder scb = new SearchSourceBuilder();

		IdsQueryBuilder iqb = new IdsQueryBuilder();
		iqb.addIds(itemId);
		scb.query(iqb);
		request.source(scb);
		try {
			SearchResponse response = client.search(request, RequestOptions.DEFAULT);
			SearchHits hits = response.getHits();
			SearchHit[] searchHits = hits.getHits();
			if (searchHits.length == 0) {
				result = new HashMap<String, Object>();
				return result;
			}
			result = searchHits[0].getSourceAsMap();

			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = new HashMap<String, Object>();
			return result;
		}
	}

	public Map<String, Object> updateItemPickUpInfo(String itemId, String NGOId, String pickUpNGOName,
			String pickUpTime) {

		UpdateByQueryRequest request = new UpdateByQueryRequest("items");

		StringBuilder queryString = new StringBuilder();
		queryString.append("if (ctx._source.itemId == ");
		queryString.append("'" + itemId + "'");
		queryString.append(" && ctx._source.itemStatus == 'PENDING') { " + "ctx._source.pickUpNGOId = ");
		queryString.append("'" + NGOId + "';");
		queryString.append("ctx._source.pickUpNGOName = ");
		queryString.append("'" + pickUpNGOName + "';");
		queryString.append("ctx._source.pickUpTime = ");
		queryString.append("'" + pickUpTime + "';");
		queryString.append("ctx._source.itemStatus = 'SCHEDULED';}");
		Script script = new Script(ScriptType.INLINE, "painless", queryString.toString(), Collections.emptyMap());

		request.setScript(script);

		try {
			client.updateByQuery(request, RequestOptions.DEFAULT);
			// System.out.println(bulkResponse);

			Map<String, Object> queryResult = queryItemByItemId(itemId);
			System.out.println(queryResult);
			return queryResult;
		} catch (IOException e) {
			e.printStackTrace();
			// Given error, return empty map
			return new HashMap<String, Object>();
		}

	}

	public Map<String, Object> deleteItem(String itemId) {

		UpdateByQueryRequest request = new UpdateByQueryRequest("items");

		StringBuilder queryString = new StringBuilder();
		queryString.append("if (ctx._source.itemId == ");
		queryString.append("'" + itemId + "'");
		queryString.append("&& ctx._source.itemStatus == 'PENDING') { " + "ctx._source.itemStatus = 'DELETED';}");

		request.setScript(new Script(ScriptType.INLINE, "painless", queryString.toString(), Collections.emptyMap()));

		try {
			BulkByScrollResponse bulkResponse = client.updateByQuery(request, RequestOptions.DEFAULT);
			System.out.print(bulkResponse);
			Map<String, Object> queryResult = queryItemByItemId(itemId);
			return queryResult;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Given error, return empty map
			return new HashMap<String, Object>();
		}
	}

	public Map<String, Object> markItemComplete(String itemId, String NGOId) {

		UpdateByQueryRequest request = new UpdateByQueryRequest("items");

		StringBuilder queryString = new StringBuilder();
		queryString.append("if (ctx._source.itemId == ");
		queryString.append("'" + itemId + "'");
		queryString.append("&& ctx._source.itemStatus == 'SCHEDULED' && ctx._source.pickUpNGOId == ");
		queryString.append("'" + NGOId + "')");
		queryString.append("{ctx._source.itemStatus = 'COLLECTED';}");

		request.setScript(new Script(ScriptType.INLINE, "painless", queryString.toString(), Collections.emptyMap()));
		System.out.print(queryString);
		try {
			BulkByScrollResponse bulkResponse = client.updateByQuery(request, RequestOptions.DEFAULT);
			System.out.print(bulkResponse);
			Map<String, Object> queryResult = queryItemByItemId(itemId);
			return queryResult;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Given error, return empty map
			return new HashMap<String, Object>();
		}
	}

	public void close() throws Exception {
		try {
			System.out.println("Closing elasticSearch  client");
			if (client != null) {
				client.close();
			}
		} catch (final Exception e) {
			System.out.print("Error closing ElasticSearch client: " + e);
		}
	}

}