package db;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.json.JSONObject;

import Entity.User;

public class ElasticSearchConnection {
	private RestHighLevelClient client;

	public void elasticSearchConnection() {
		client = new RestHighLevelClient(RestClient.builder(new HttpHost(ElasticSearchDBUtil.INSTANCE,
				ElasticSearchDBUtil.PORT_NUM, ElasticSearchDBUtil.CONNECTION_TYPE)));
	}

	// Change this type to the actual user type once ready
	public void addUser(User user) {
		JSONObject userObj = user.toJSONObject();
		XContentBuilder builder;
		try {
			builder = XContentFactory.jsonBuilder();
			builder.startObject();
			{
				builder.field("userId", userObj.getString("user_id"));
				builder.field("userName", userObj.getString("name"));
				builder.field("userType", userObj.getString("UserType"));
				builder.field("email", userObj.getString("email"));
				builder.field("address", userObj.getString("address"));
				// to-fix hard-coded for now
				builder.timeField("postDate", "2020-06-30");
			}
			builder.endObject();
			IndexRequest indexRequest = new IndexRequest("users").id(userObj.getString("user_id")).source(builder);
			IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
			System.out.print(response);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// placeholder
//	public void addItem(MockItem item) {
//
//	}

	// placeholder
//	public void queryItemByLocation() {
//
//	}

	// Placeholder
//	public void queryItemByUserId() {
//
//	}	

}
