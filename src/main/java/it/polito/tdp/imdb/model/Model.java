package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private ImdbDAO dao;

	private Map<Integer, Actor> mapActorsGenre;

	private SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo;
	
	private Simulatore s;

	public Model() {
		dao = new ImdbDAO();
		mapActorsGenre = new HashMap<Integer, Actor>();
	}

	public void creaGrafo(String genere) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		mapActorsGenre = dao.idMapActorsGenre(genere);
		// VERTICI
		Graphs.addAllVertices(grafo, mapActorsGenre.values());

		// ARCHI
		for (Adiacenza a : dao.getAdiacenzeGenre(genere)) {
			Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(), a.getPeso());
		}

	}

	public List<Actor> linkedActors(Actor a) {

		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector<>(grafo);
		List<Actor> ris = new ArrayList<Actor>(ci.connectedSetOf(a));
		ris.remove(a);

		Collections.sort(ris, new Comparator<Actor>() {

			@Override
			public int compare(Actor a, Actor a1) {
				return a.getLastName().compareTo(a1.getLastName());
			}
		});

		return ris;
	}

	public boolean grafoCreato() {
		if (grafo == null) {
			return false;
		} else {
			return true;
		}
	}

	public void simulate(int n) {
		s = new Simulatore(n, grafo);
		s.init();
		s.run();
	}
	
	public int numPause() {
		return s.getNumPause();
	}
	
	public List<Actor> getIntervistati() {
		return s.getAttoriIntervistati();
	}

	public List<String> getGeneri() {
		return dao.listGenres();
	}

	public Set<Actor> getVertici() {
		return grafo.vertexSet();
	}

	public Set<DefaultWeightedEdge> getArchi() {
		return grafo.edgeSet();
	}

}
