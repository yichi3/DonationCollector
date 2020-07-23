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
		testPickup();
	}

	public static void testPickup() {
		ElasticSearchConnection es = new ElasticSearchConnection();
		es.elasticSearchConnection();
		es.updateItemPickUpInfo("b2c7d235-9e43-4ea0-84ce-d74b34734de7", "2222", "red Cross", "2020-07-10");
		try {
			es.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// use this to add fake records to DB if it fails
	public static void uploadItem() {

		// Initialize connection
		ElasticSearchConnection es = new ElasticSearchConnection();
		es.elasticSearchConnection();

		// Use this to create a user object
		User posterUser = User.builder().userId("456").firstName("Arianna").lastName("Grande")
				.userType(UserType.INDIVIDUAL).email("taytay@gmail.com").address("New York, USA").build();
		// Call this to add user record to users index
		// Note that under the new architecture, this is not needed as user info are
		// saved in firebase

		// To test post creation, first we need to create a NGO user

		User NGOUser = User.builder().userId("00000").ngoName("Placeholder").userType(UserType.NGO).build();
		// Create dummy available pick up time
		List<String> schedule = Arrays.asList("2020-06-30", "2020-07-10");
		// Create new user ID
		UUID itemId = UUID.randomUUID();

		// Create test item
		Item item = Item.builder().posterUser(posterUser).urlToImage("acb@google.cloud.com").itemId(itemId)
				.description("A pair of heels").category(Category.APPAREL).size("2x2x2").schedule(schedule)
				.location("2464 W El Camino Real C, Mountain View, CA 94040").lat(37.399180).lon(-122.108690)
				.status(Status.PENDING).pickUpDate("2020-06-10").NGOUser(NGOUser).build();
		// Then call the ES client's addItem method to upload to ES
		es.addItem(item);

		UUID itemId2 = UUID.randomUUID();

		Item item2 = Item.builder().posterUser(posterUser).urlToImage("acb@google.cloud.com").itemId(itemId2)
				.description("7 rings").category(Category.APPAREL).size("2x2x2").schedule(schedule)
				.location("2464 W El Camino Real C, Mountain View, CA 94040").lat(37.399180).lon(-122.108690)
				.status(Status.COLLECTED).pickUpDate("2020-06-10").NGOUser(NGOUser).build();
		// Then call the ES client's addItem method to upload to ES
		es.addItem(item2);

		UUID itemId3 = UUID.randomUUID();

		Item item3 = Item.builder().posterUser(posterUser).urlToImage("acb@google.cloud.com").itemId(itemId3)
				.description("Diamond").category(Category.APPAREL).size("2x2x2").schedule(schedule)
				.location("2464 W El Camino Real C, Mountain View, CA 94040").lat(37.399180).lon(-122.108690)
				.status(Status.DELETED).pickUpDate("2020-06-10").NGOUser(NGOUser).build();
		// Then call the ES client's addItem method to upload to ES
		es.addItem(item3);

		UUID itemId4 = UUID.randomUUID();

		Item item4 = Item.builder().posterUser(posterUser).urlToImage("acb@google.cloud.com").itemId(itemId4)
				.description("Ferrari").category(Category.APPAREL).size("2x2x2").schedule(schedule)
				.location("2464 W El Camino Real C, Mountain View, CA 94040").lat(37.399180).lon(-122.108690)
				.status(Status.SCHEDULED).pickUpDate("2020-06-10").NGOUser(NGOUser).build();
		// Then call the ES client's addItem method to upload to ES
		es.addItem(item4);
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