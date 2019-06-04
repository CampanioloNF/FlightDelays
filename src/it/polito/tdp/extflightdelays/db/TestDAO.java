package it.polito.tdp.extflightdelays.db;

import java.util.HashMap;
import java.util.Map;

import it.polito.tdp.extflightdelays.model.Airport;

public class TestDAO {

	public static void main(String[] args) {

		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();
		 Map<Integer, Airport> aIdMap= new HashMap<>();

		System.out.println(dao.loadAllAirlines().size());
		System.out.println(dao.loadAllAirports(aIdMap).size());
		System.out.println(dao.getRotte(400, aIdMap).size());
		System.out.println(aIdMap.size());
	}

}
