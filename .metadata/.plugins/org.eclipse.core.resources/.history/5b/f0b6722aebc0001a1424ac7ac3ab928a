package model;

public class User{
	private String userId;
	private String name;
	private UserType userType;
	private String email;
	private String address;
	
	
	private User(UserBuilder builder) {
		this.userId= builder.userId;
		this.name = builder.name;
		this.userType = builder.userType;
		this.email = builder.email;
		this.address = builder.address;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public String getName() {
		return name;
	}
	
	public UserType getUserType() {
		return userType;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getAddress() {
		return address;
	}
	
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("userId", userId);
			obj.put("name", name);
			obj.put("userType", userType);
			obj.put("email", email);
			obj.put("address", address);
			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;	
	}
	
	public static class UserBuilder {
		private String userId;
		private String userName;
		private UserType userType;
		private String email;
		private String address;
		
		public void setUserId(String userId) {
			this.userId = userId;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public void setUserType(UserType userType) {
			this.userType = userType;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}
		
		public void setAddress(String address) {
			this.address = address;
		}
		
		public User build() {
			return new User(this);
		}
	}
	
	// UserBuilder builder = new UserBuilder();
	// builder.setUserId(...);
	// builder.set...
	// User user = builder.build();
		
}