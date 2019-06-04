package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {

	private SimpleWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	 Map<Integer, Airport> aIdMap;
	private Map<Airport, Airport> visite;	
	public Model(){
	
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.aIdMap = new HashMap<>();
		this.visite = new HashMap<>();
	}
	
	public void creaGrafo(int distanzaMedia) {
		
		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();
		
		dao.loadAllAirports(aIdMap);
		
		//Aggiungo i grafici
		Graphs.addAllVertices(grafo, aIdMap.values());
		
		System.out.println(grafo.vertexSet().size());
		
		for(Rotta rotta : dao.getRotte(distanzaMedia, aIdMap)) {
			//controllo se esiste un arco
			// se esiste aggiorno il peso
			
			DefaultWeightedEdge edge = grafo.getEdge(rotta.getSource(), rotta.getDestination());
			
			if(edge==null) {
				
				Graphs.addEdgeWithVertices(grafo, rotta.getSource(), rotta.getDestination(), rotta.getAvg());
				
			}
			else {
				
				System.out.println("Aggiornato Peso!");
				double peso = grafo.getEdgeWeight(edge);
				double newPeso = (peso + rotta.getAvg())/2;
				grafo.setEdgeWeight(edge, newPeso);
				
			}
				
			
			
		}
		
		System.out.println("Vertici: " +grafo.vertexSet().size()+" Archi: "+grafo.edgeSet().size());
		
	}
	
	public Boolean testConnessione (Airport partenza, Airport destinazione) {
		
		Set<Airport> visitati = new HashSet<>();
		BreadthFirstIterator<Airport, DefaultWeightedEdge> it = new BreadthFirstIterator<>(this.grafo, partenza);
		
		while(it.hasNext()) {
			visitati.add(it.next());	
			}
		
		if(visitati.contains(destinazione)) 
			return true;
		else
	        return false;
		
		
	}
	
	public List<Airport> trovaPercorso(Airport partenza, Airport destinazione){
		
		List<Airport> visitati = new ArrayList<>();
        BreadthFirstIterator<Airport, DefaultWeightedEdge> it = new BreadthFirstIterator<>(this.grafo, partenza);
        
        visite.put(partenza, null);
        
        it.addTraversalListener(new TraversalListener<Airport,DefaultWeightedEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> ev) {
				
				Airport sorgente = grafo.getEdgeSource(ev.getEdge());
				Airport destinazione = grafo.getEdgeTarget(ev.getEdge());
				
				if(!visite.containsKey(destinazione) && visite.containsKey(sorgente))
					visite.put(destinazione, sorgente);
				else if(!visite.containsKey(sorgente) && visite.containsKey(destinazione))
					visite.put(sorgente, destinazione);
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Airport> arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Airport> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
		
		while(it.hasNext()) {
		     it.next();	
			}
		
		if(!visite.containsKey(destinazione))
		   return null;
		else {
			
			Airport step = destinazione;
			
			while(step!=null) {
				visitati.add(step);
				step = visite.get(step);
			}
			
			
			
			return visitati;
		}
		
	}
}
