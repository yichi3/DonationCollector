package model;

public class GeoLocation {
	private double lat;
	private double lng;
	
	public GeoLocation(double lat, double lon) {
		this.lat = lat;
		this.lng = lon;
	}
	
	public double getLat() {
		return this.lat;
	}
	
	public double getLng() {
		return this.lng;
	}

}
