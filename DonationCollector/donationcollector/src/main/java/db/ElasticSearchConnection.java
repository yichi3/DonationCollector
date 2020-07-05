package db;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;

public class ElasticSearchConnection {

	public static void main(String[] args) {
		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(new HttpHost("35.225.69.232", 9200, "http")));

		CreateIndexRequest request = new CreateIndexRequest("test");

		request.source(
				"{\n" + "    \"mappings\" : {\n" + "        \"properties\" : {\n"
						+ "            \"message\" : { \"type\" : \"text\" }\n" + "        }\n" + "    },\n"
						+ "    \"aliases\" : {\n" + "        \"test_alias\" : {}\n" + "    }\n" + "}",
				XContentType.JSON);
		try {
			CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
