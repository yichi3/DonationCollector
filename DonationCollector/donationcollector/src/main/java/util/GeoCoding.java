package util;

import java.io.IOException;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import entity.GeoLocation;

public class GeoCoding {
	
	private static final String API_KEY = "AIzaSyAhB-0E2ryt2Xd6SZqiRI-Yt3IKP30mUn4";
	
	
	public GeoLocation parseAddress(String address) throws ApiException, InterruptedException, IOException {
		
		if (address == null || address.length() == 0) {
			return null;
		}
		
		GeoApiContext context = new GeoApiContext.Builder()
			    .apiKey(API_KEY)
			    .build();
	
		GeocodingResult[] request =  GeocodingApi.geocode(context, address).await();
		LatLng location = request[0].geometry.location;
		double lat = location.lat;
		double lng = location.lng;
		GeoLocation result = new GeoLocation(lat, lng);
		return result;
	}
	
//	public static void main(String[] args) throws ApiException, InterruptedException, IOException {
//		String address = "2115 Westlake Ave, Seattle, WA 98121";
//		double lat = parseAddress(address).getLat();
//		double lng = parseAddress(address).getLng();
//		System.out.println(lat);
//		System.out.println(lng);
//	}
}


