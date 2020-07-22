package db;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import entity.Category;
import entity.Item;
import entity.Status;
import entity.User;
import entity.UserType;

public class Runner {
	public static void main(String[] args) {
		uploadItem();
	}

	// use this to add fake records to DB if it fails
	public static void uploadItem() {

		// Initialize connection
		ElasticSearchConnection es = new ElasticSearchConnection();
		es.elasticSearchConnection();

		// Use this to create a user object
		User posterUser = User.builder().userId("123").name("Wonder Women").userType(UserType.INDIVIDUAL)
				.email("wonder@gmail.com").address("Los Angeles, USA").build();
		// Call this to add user record to users index
		// Note that under the new architecture, this is not needed as user info are
		// saved in firebase

		// To test post creation, first we need to create a NGO user

		User NGOUser = User.builder().userId("11111").name("UNICEF").userType(UserType.NGO).email("uniced@gmail.com")
				.address("Malibu, California").build();
		// Create dummy available pick up time
		List<String> schedule = Arrays.asList("2020-06-30", "2020-07-10");
		// Create new user ID
		UUID itemId = UUID.randomUUID();

		// Create test item
		Item item = Item.builder().posterUser(posterUser).NGOUser(NGOUser).urlToImage("acb@google.cloud.com")
				.itemId(itemId).description("A pair of shoes").category(Category.APPAREL).size("2x2x2")
				.schedule(schedule).location("125 S Frances St, Sunnyvale, CA 94086").lat(37.377628).lon(-122.031052)
				.status(Status.PENDING).pickUpDate("2020-06-10").build();
		// Then call the ES client's addItem method to upload to ES
		es.addItem(item);

		Item item2 = Item.builder().posterUser(posterUser).NGOUser(NGOUser).urlToImage("acb@google.cloud.com")
				.itemId(itemId).description("A watch").category(Category.APPAREL).size("2x2x2").schedule(schedule)
				.location("125 S Frances St, Sunnyvale, CA 94086").lat(37.377628).lon(-122.031052)
				.status(Status.COLLECTED).pickUpDate("2020-06-10").build();
		// Then call the ES client's addItem method to upload to ES
		es.addItem(item2);

		Item item3 = Item.builder().posterUser(posterUser).NGOUser(NGOUser).urlToImage("acb@google.cloud.com")
				.itemId(itemId).description("A cuo").category(Category.APPAREL).size("2x2x2").schedule(schedule)
				.location("125 S Frances St, Sunnyvale, CA 94086").lat(37.377628).lon(-122.031052)
				.status(Status.DELETED).pickUpDate("2020-06-10").build();
		// Then call the ES client's addItem method to upload to ES
		es.addItem(item3);

		Item item4 = Item.builder().posterUser(posterUser).NGOUser(NGOUser).urlToImage("acb@google.cloud.com")
				.itemId(itemId).description("A poster").category(Category.APPAREL).size("2x2x2").schedule(schedule)
				.location("125 S Frances St, Sunnyvale, CA 94086").lat(37.377628).lon(-122.031052)
				.status(Status.SCHEDULED).pickUpDate("2020-06-10").build();
		// Then call the ES client's addItem method to upload to ES
		es.addItem(item4);
		
		Item item5 = Item.builder().posterUser(posterUser).NGOUser(NGOUser).urlToImage("acb@google.cloud.com")
				.itemId(itemId).description("A desk").category(Category.FURNITURE).size("2x2x2").schedule(schedule)
				.location("125 S Frances St, Sunnyvale, CA 94086").lat(37.377628).lon(-122.031052)
				.status(Status.PENDING).pickUpDate("2020-06-10").build();
		es.addItem(item5);
		// Then call the ES client's addItem method to upload to ES
		try {
			es.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//
//		}
	}
}