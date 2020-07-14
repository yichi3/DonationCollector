package db;

import Entity.User;
import Entity.UserType;

public class Runner {
	public static void main(String[] args) {

		User posterUser = User.builder().userId("464645464").name("Kim Yoo Jeong").userType(UserType.INDIVIDUAL)
				.email("crazyperson@gmail.com").address("Seoul Korea").build();
		ElasticSearchConnection es = new ElasticSearchConnection();
		es.elasticSearchConnection();
		es.addUser(posterUser);
	}
}