package com.Model;

import java.util.List;

import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;

//A java bean class to be passed around
public class DirectionBean {
	private DirectionsResult result;
	private List<String> weatherList;
	public DirectionBean(DirectionsResult r, List<String> w){
		result = r;
		weatherList = w;
	}
	public DirectionsResult getResult() {
		return result;
	}
	public void setResult(DirectionsResult result) {
		this.result = result;
	}
	public List<String> getWeatherList() {
		return weatherList;
	}
	public void setWeatherList(List<String> weatherList) {
		this.weatherList = weatherList;
	}	
}
