package it.polito.tdp.extflightdelays.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		
		model.creaGrafo(400);
		
		System.out.println(model.testConnessione(model.aIdMap.get(11), model.aIdMap.get(67)));
		
		for(Airport a : model.trovaPercorso(model.aIdMap.get(11),  model.aIdMap.get(297))) {
			System.out.println(a.getId());
		}
 
	}

}
