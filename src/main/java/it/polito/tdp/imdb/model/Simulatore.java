package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Simulatore {

	// input
	SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo;
	int days;

	// output
	int numPause;
	Map<Integer, Actor> mapIntervistati;
	List<Actor> listIntervistabili;

	public Simulatore(int numAttori, SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo) {
		super();
		this.days = numAttori;
		this.grafo = grafo;
	}

	public void init() {
		mapIntervistati = new HashMap<Integer, Actor>();
		numPause = 0;
		listIntervistabili = new ArrayList<>(grafo.vertexSet());
	}

	public void run() {
		for (int i = 1; i < this.days; i++) {
			Random rand = new Random();

			if (i == 1 || !mapIntervistati.containsKey(i - 1)) {
				Actor a = listIntervistabili.get(rand.nextInt(listIntervistabili.size()));
				mapIntervistati.put(i, a);
				listIntervistabili.remove(a);
				System.out.println(
						"Giorno " + i + " intervistato casualmente: " + a.getFirstName() + " " + a.getLastName());
				continue;
			}

			if (i >= 3 && mapIntervistati.containsKey(i - 1) && mapIntervistati.containsKey(i - 2)
					&& mapIntervistati.get(i - 1).getGender().equals(mapIntervistati.get(i - 2).getGender())) {
				// 90% chance di pausa
				if (rand.nextFloat() <= 0.9) {
					numPause++;
					System.out.println("Giorno " + i + ": PAUSA (" + numPause + ")");
					continue;
				}
			}

			// GET CONSIGLI 40% - RANDOM 60%

			if (rand.nextFloat() <= 0.6) {
				Actor a = listIntervistabili.get(rand.nextInt(listIntervistabili.size()));
				mapIntervistati.put(i, a);
				listIntervistabili.remove(a);
				System.out.println(
						"Giorno " + i + " intervistato casualmente: " + a.getFirstName() + " " + a.getLastName());
				continue;
			} else {
				Actor ultimoAttore = mapIntervistati.get(i - 1);
				Actor recommended = this.consigliaAttore(ultimoAttore);
				if (recommended == null || !listIntervistabili.contains(recommended)) {
					Actor a = listIntervistabili.get(rand.nextInt(listIntervistabili.size()));
					mapIntervistati.put(i, a);
					listIntervistabili.remove(a);
					System.out.println(
							"Giorno " + i + " intervistato casualmente: " + a.getFirstName() + " " + a.getLastName());
					continue;
				} else {
					mapIntervistati.put(i, recommended);
					listIntervistabili.remove(recommended);
					System.out.println("Giorno " + i + " intervistato su cosniglio: " + recommended.getFirstName() + " "
							+ recommended.getLastName() + "[" + ultimoAttore.getFirstName() + " "
							+ ultimoAttore.getLastName() + "]");
				}
			}

		}
	}

	public Actor consigliaAttore(Actor ultimoAttore) {
		Actor ris = null;
		int pesoMax = 0;

		for (Actor a : Graphs.neighborListOf(grafo, ultimoAttore)) {

			int peso = (int) grafo.getEdgeWeight(grafo.getEdge(ultimoAttore, a));

			if (peso >= pesoMax) {
				ris = a;
				pesoMax = peso;
			}
		}

		return ris;
	}

	public int getNumPause() {
		return numPause;
	}

	public List<Actor> getAttoriIntervistati() {
		return new ArrayList<>(mapIntervistati.values());
	}

}
