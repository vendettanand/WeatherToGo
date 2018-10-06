package com.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WeatherDAO {
	//Persist response to Database
	public boolean cache(String from, String to, String jsonObj) throws Exception {
		Connection con = null;
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			Class.forName(driver).newInstance();
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","username","password");
			PreparedStatement ps = con.prepareStatement("INSERT INTO WEATHER(ORIGIN, DESTINATION, JOBJ) VALUES(?, ?, ?)");
			ps.setString(1, from);
			ps.setString(2, to);
			ps.setString(3, jsonObj);
			if(ps.executeUpdate()>0) {
				System.out.println("Write Success");
				return true;
			}
			else
				throw new Exception("Couln't persist data");
		} catch (Exception e) {
			throw e;
		}
		finally {
			con.close();
		}
	}
	//Fetch data from database
	public String deCache(String from, String to) throws Exception {
		Connection con = null;
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			Class.forName(driver).newInstance();
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","username","password");
			PreparedStatement ps = con.prepareStatement("SELECT JOBJ FROM WEATHER WHERE ORIGIN = ? AND DESTINATION = ?");
			ps.setString(1, from);
			ps.setString(2, to);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				String res = rs.getString("JOBJ");
				System.out.println("Fetch Successful");
				return res;
			}
			else
				throw new Exception("No Record found");
		} catch (Exception e) {
			throw e;
		}
		finally {
			con.close();
		}
	}
}
