package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

import entity.FirebaseUser;

public class Firebase {

	public FirebaseUser getFirebaseUserInfo(String userId) throws Exception {
		try {
			String urlString = "https://donationcollector.firebaseio.com/users/" + userId + ".json";

			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
			}
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String message = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				message += line;
				System.out.println(message);
			}
			conn.disconnect();
			Gson gson = new Gson();
			FirebaseUser firebaseUser = gson.fromJson(message, FirebaseUser.class);
			System.out.println("userId:" + userId);
			System.out.println("firebase user" + firebaseUser);
			System.out.println("output" + message);
			return firebaseUser;

		} catch (Exception e) {
			System.out.println("Exception in FirebaseClientGet:- " + e);
			throw e;
		}
	}
}
