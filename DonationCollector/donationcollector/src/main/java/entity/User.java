package entity;

import org.json.JSONObject;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
	private String userId;
	private String firstName;
	private String lastName;
	private String ngoName;
	private UserType userType;
	private String email;
	private String address;

	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("user_id", userId);
		obj.put("firstName", firstName);
		obj.put("lastName", lastName);
		obj.put("ngoName", ngoName);
		obj.put("UserType", userType.toString());
		obj.put("email", email);
		obj.put("address", address);
		return obj;
	}
}
