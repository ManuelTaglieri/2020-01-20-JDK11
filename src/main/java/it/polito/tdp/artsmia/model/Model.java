package it.polito.tdp.artsmia.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<Artist, DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer, Artist> idMap;
	private String ruolo;
	
	public Model() {
		this.dao = new ArtsmiaDAO();
	}
	
	public void creaGrafo(String ruolo) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.idMap = new HashMap<>();
		this.ruolo = ruolo;
		this.dao.getVertici(idMap, ruolo);
		Graphs.addAllVertices(grafo, idMap.values());
		
		for (Adiacenza a : this.dao.getArchi(idMap, ruolo)) {
			Graphs.addEdge(grafo, a.getA1(), a.getA2(), a.getPeso());
		}
		
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<String> getRuoli() {
		return this.dao.getRuoli();
	}
	
	public List<Adiacenza> getArchi() {
		List<Adiacenza> risultato = new LinkedList<>(this.dao.getArchi(idMap, ruolo));
		Collections.sort(risultato);
		return risultato;
	}

}
