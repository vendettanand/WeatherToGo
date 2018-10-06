package com.Directions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;


public class Directions {
	private static String API_KEY = "API_KEY";
	private static GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();
	//Access the Google Maps API to get Map data
	public DirectionsResult getDirections(String from, String to) throws Exception{
		DirectionsResult request=null;
		long startTime = System.currentTimeMillis();
			try {
				request = DirectionsApi.newRequest(context).mode(TravelMode.DRIVING).origin(from).destination(to).await();
			} catch (Exception e) {
				throw e;
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Time for Directions data retrieval "+(endTime-startTime)+" ms");
			return request;
	}
}
