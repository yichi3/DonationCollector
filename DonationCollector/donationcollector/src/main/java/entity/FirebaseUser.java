package entity;

import org.json.JSONObject;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FirebaseUser {
	private String emailAddress;
	private String firstName;
	private String lastName;
	private String organizationName;
	private String phone;
	private boolean user;

	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("emailAddress", emailAddress);
		obj.put("firstName", firstName);
		obj.put("lastName", lastName);
		obj.put("organizationName", organizationName);
		obj.put("phone", phone);
		obj.put("user", user);
		return obj;
	}
}