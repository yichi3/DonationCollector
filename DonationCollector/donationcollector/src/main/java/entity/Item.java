package entity;

import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Item {
	private User posterUser;
	private User NGOUser;

	private String urlToImage;
	private UUID itemId;

	private String itemName;
	private String description;
	private Category category;
	private String size;
	private List<String> schedule;
	private String location;
	private Double lat;
	private Double lon;

	private Status status;
	private String pickUpDate;

	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("posterUser", posterUser.toJSONObject());
		obj.put("NGOUser", NGOUser.toJSONObject());
		obj.put("urlToImage", urlToImage);
<<<<<<< HEAD:DonationCollector/donationcollector/src/main/java/entity/Item.java
		obj.put("itemId", itemId.toString());
=======
		obj.put("itemId", itemId);
>>>>>>> a038e26c47ea2c00aeadd011ffa416d216e90c80:DonationCollector/donationcollector/src/main/java/Entity/Item.java
		obj.put("itemName", itemName);
		obj.put("description", description);
		obj.put("category", category.toString());
		obj.put("size", size);
		obj.put("schedule", new JSONArray(schedule));
		obj.put("location", location);
		obj.put("lat", lat.toString());
		obj.put("lon", lon.toString());
		obj.put("status", status.toString());
		obj.put("pickUpDate", pickUpDate);
		return obj;
	}
}
