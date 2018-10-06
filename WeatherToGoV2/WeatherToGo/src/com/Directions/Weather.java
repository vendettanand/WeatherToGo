package com.Directions;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import com.google.maps.model.LatLng;

public class Weather {
	String URL_API = "http://api.openweathermap.org/data/2.5/weather";
	String API_KEY = "API_KEY";

	Client client = ClientBuilder.newClient();
	//Access the Open Weather API to get Weather data for the waypoints
	public List<String> getWeather(List<LatLng> list) throws Exception{
		List<String> weatherList = new ArrayList<>();
		long startTime = System.currentTimeMillis();
		try {
			for(LatLng s: list) 
				weatherList.add(client.target(URL_API).queryParam("lat", s.lat).queryParam("lon", s.lng).queryParam("appid", API_KEY).request(MediaType.APPLICATION_JSON).get(String.class));
			return weatherList;
		}
		catch (Exception e) {
			throw e;
		} finally {
			long endTime = System.currentTimeMillis();
			System.out.println("Time for Weather data retrieval "+(endTime-startTime)+" ms");
			client.close();
		}
	}
	}
