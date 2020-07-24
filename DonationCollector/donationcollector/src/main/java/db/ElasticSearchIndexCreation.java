package db;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;

public class ElasticSearchIndexCreation {
	// Run this as Java application to reset the database.
	// to-do: should rename to index

	public static void main(String[] args) {
		itemsIndex();
	}

	private static void itemsIndex() {
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(
				ElasticSearchDBUtil.INSTANCE, ElasticSearchDBUtil.PORT_NUM, ElasticSearchDBUtil.CONNECTION_TYPE)));

		// Check if Document exist
		checkIndexExists("items", client);
		// Create item document

		CreateIndexRequest itemsDocumentRequest = new CreateIndexRequest("items");
		Map<String, Object> itemsDocumentProperties = new HashMap<>();

		// itemId field
		Map<String, Object> itemId = new HashMap<>();
		itemId.put("type", "text");
		itemsDocumentProperties.put("itemId", itemId);
		// urlToImage field
		Map<String, Object> urlToImage = new HashMap<>();
		urlToImage.put("type", "text");
		itemsDocumentProperties.put("urlToImage", urlToImage);
		// location in latitude, longitude field
		Map<String, Object> locationLatLon = new HashMap<>();
		locationLatLon.put("type", "geo_point");
		itemsDocumentProperties.put("locationLatLon", locationLatLon);
		// location in address field
		Map<String, Object> locationAddress = new HashMap<>();
		locationAddress.put("type", "text");
		itemsDocumentProperties.put("locationAddress", locationAddress);
		// itemName field
		Map<String, Object> itemName = new HashMap<>();
		itemName.put("type", "text");
		itemsDocumentProperties.put("itemName", itemName);
		// posterId
		Map<String, Object> posterId = new HashMap<>();
		posterId.put("type", "text");
		itemsDocumentProperties.put("posterId", posterId);
		// posterFirstName
		Map<String, Object> posterFirstName = new HashMap<>();
		posterFirstName.put("type", "text");
		itemsDocumentProperties.put("posterFirstName", posterFirstName);
		// posterLastName
		Map<String, Object> posterLastName = new HashMap<>();
		posterLastName.put("type", "text");
		itemsDocumentProperties.put("posterLastName", posterLastName);
		// category
		Map<String, Object> category = new HashMap<>();
		category.put("type", "text");
		itemsDocumentProperties.put("category", category);
		// description
		Map<String, Object> description = new HashMap<>();
		description.put("type", "text");
		itemsDocumentProperties.put("description", description);
		// size
		Map<String, Object> size = new HashMap<>();
		size.put("type", "text");
		itemsDocumentProperties.put("size", size);
		// AvailablePickUpTime - Elastic allows array of elements of the same type by
		// default
		Map<String, Object> availablePickUpTime = new HashMap<>();
		availablePickUpTime.put("type", "date");
		itemsDocumentProperties.put("availablePickUpTime", availablePickUpTime);
		// itemStatus
		Map<String, Object> itemStatus = new HashMap<>();
		itemStatus.put("type", "text");
		itemsDocumentProperties.put("itemStatus", itemStatus);
		// pickUpNGOId
		Map<String, Object> pickUpNGOId = new HashMap<>();
		pickUpNGOId.put("type", "text");
		itemsDocumentProperties.put("pickUpNGOId", pickUpNGOId);
		// pickUpNGOName
		Map<String, Object> pickUpNGOName = new HashMap<>();
		pickUpNGOName.put("type", "text");
		itemsDocumentProperties.put("pickUpNGOName", pickUpNGOName);
		// selectedPickUpTime
		Map<String, Object> selectedPickUpTime = new HashMap<>();
		selectedPickUpTime.put("type", "date");
		itemsDocumentProperties.put("pickUpTime", selectedPickUpTime);

		Map<String, Object> itemsMapping = new HashMap<>();
		itemsMapping.put("properties", itemsDocumentProperties);

		itemsDocumentRequest.mapping(itemsMapping);

		try {
			CreateIndexResponse createItemsDocumentResponse = client.indices().create(itemsDocumentRequest,
					RequestOptions.DEFAULT);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void checkIndexExists(String document, RestHighLevelClient client) {
		GetIndexRequest request = new GetIndexRequest(document);
		try {
			boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
			if (exists == true) {
				DeleteIndexRequest deleteRequest = new DeleteIndexRequest(document);
				org.elasticsearch.action.support.master.AcknowledgedResponse deleteIndexResponse = client.indices()
						.delete(deleteRequest, RequestOptions.DEFAULT);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}