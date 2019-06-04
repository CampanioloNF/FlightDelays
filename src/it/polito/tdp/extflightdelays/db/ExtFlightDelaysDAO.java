package it.polito.tdp.extflightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.extflightdelays.model.Airline;
import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Flight;
import it.polito.tdp.extflightdelays.model.Rotta;

public class ExtFlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		String sql = "SELECT * from airlines";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRLINE")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> loadAllAirports(Map<Integer, Airport> idMap) {
		String sql = "SELECT * FROM airports";
		List<Airport> result = new ArrayList<Airport>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				if(!idMap.containsKey(rs.getInt("ID"))) {
				
				Airport airport = new Airport(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRPORT"),
						rs.getString("CITY"), rs.getString("STATE"), rs.getString("COUNTRY"), rs.getDouble("LATITUDE"),
						rs.getDouble("LONGITUDE"), rs.getDouble("TIMEZONE_OFFSET"));
				idMap.put(rs.getInt("ID"), airport);
				result.add(airport);
			   
				}
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Rotta> getRotte(int distanzaMedia, Map<Integer, Airport> idMap){

		 final String sql = "SELECT flights.ORIGIN_AIRPORT_ID, flights.DESTINATION_AIRPORT_ID, AVG(flights.DISTANCE) as avgg FROM flights GROUP BY flights.ORIGIN_AIRPORT_ID, flights.DESTINATION_AIRPORT_ID HAVING avgg > ? ";
		 List<Rotta> result = new ArrayList<>();
		
		 try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				
				st.setInt(1, distanzaMedia);
				
				ResultSet rs = st.executeQuery();

				while (rs.next()) {

				if(idMap.get(rs.getInt("ORIGIN_AIRPORT_ID"))!= null && idMap.get(rs.getInt("DESTINATION_AIRPORT_ID"))!=null) {
					
					Rotta rotta = new Rotta(idMap.get(rs.getInt("ORIGIN_AIRPORT_ID")), idMap.get(rs.getInt("DESTINATION_AIRPORT_ID")),rs.getDouble("avgg"));
					result.add(rotta);
					
					}
				}

				conn.close();
				return result;

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Errore connessione al database");
				throw new RuntimeException("Error Connection Database");
			}
	}
}
