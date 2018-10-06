package com.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.DAO.WeatherDAO;
import com.Directions.Directions;
import com.Directions.Weather;
import com.Model.DirectionBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.LatLng;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Gson gson = new Gson();
    /**
     * Default constructor. 
     */
	//
    public Controller() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] input = request.getReader().readLine().split("_____");
		PrintWriter writer = response.getWriter();
		try {
			if(input == null || input.length==0) {
				writer.write("Enter an input");
				return;
			}
			String from = input[0].split("::")[1];
			String to = input[1].split("::")[1];
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			long startTime = System.currentTimeMillis();
			String res = getFromDB(from, to);
			long endTime = 0;
			long duration=0;
			if(res == null) {
				DirectionBean bean = getDirectionAndWaypoints(from, to);
				res = gson.toJson(bean);
				boolean write = writeToDB(from, to, res);
				if(!write)
					System.out.println("Failed to write to DB");
				endTime = System.currentTimeMillis();
				duration = endTime-startTime;
				System.out.println("Time taken without DB ---> "+duration+" ms");
			}
			else 
				System.out.println("Time taken with DB ---> "+duration+" ms");
			writer.write(res);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("_________________________________________________________________");
		}
	}
	//Get directions and weather information
	public DirectionBean getDirectionAndWaypoints(String from, String to) throws Exception{
		Directions directions = new Directions();
		try {
		DirectionsResult result = directions.getDirections(from, to);
		if(result==null)
			throw new Exception("No direction found exception");
		int rlength = result.routes[0].legs[0].steps.length;
		DirectionsStep[] steps = result.routes[0].legs[0].steps;
		int reducedlength = (int)Math.floor(rlength/5);
		List<LatLng> latLngList = new ArrayList<>();
		if(rlength<=5) {
			for(int i=0; i<rlength; i++) 
				latLngList.add(steps[i].startLocation);
		}
		else {
			for(int i=1; i<6; i++) {
				if(i > 3)
					latLngList.add(steps[i*(reducedlength-1)].startLocation);
				else
					latLngList.add(steps[i*reducedlength].startLocation);
			}
		}
		Weather weather = new Weather();
		List<String> weatherList = weather.getWeather(latLngList);
		DirectionBean bean = new DirectionBean(result, weatherList);
		return bean;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}
	//Persist data to Database
	public boolean writeToDB(String from, String to, String jObj) {
		WeatherDAO dao = new WeatherDAO();
		try {
			return dao.cache(from, to, jObj);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	//Fetch data from the database
	public String getFromDB(String from, String to) {
		WeatherDAO dao = new WeatherDAO();
		try {
			return dao.deCache(from, to);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}
