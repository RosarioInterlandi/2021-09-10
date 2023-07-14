package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private YelpDao dao;
	private Graph<Business, DefaultWeightedEdge> grafo;
	private List<Business> bestPath;
	private int bestScore;
	private Business source;

	public Model() {
		this.dao = new YelpDao();
	}

	public List<String> getCittà() {
		return this.dao.getCittà();
	}

	public void BuildGraph(String città) {
		this.grafo = new SimpleWeightedGraph<Business, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		// creazione vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(città));
		// creazione archi
		for (Business b1 : this.grafo.vertexSet()) {
			for (Business b2 : this.grafo.vertexSet()) {
				if (!b1.equals(b2)) {
					LatLng coordinate1 = new LatLng(b1.getLatitude(), b1.getLongitude());
					LatLng coordinate2 = new LatLng(b2.getLatitude(), b2.getLongitude());
					double peso = LatLngTool.distance(coordinate1, coordinate2, LengthUnit.KILOMETER);
					Graphs.addEdgeWithVertices(this.grafo, b1, b2, peso);
				}
			}
		}

	}

	public List<Business> getVertici() {
		List<Business> result = new ArrayList<>(this.grafo.vertexSet());
		Collections.sort(result);
		return result;
	}

	public Set<DefaultWeightedEdge> getArchi() {
		return this.grafo.edgeSet();
	}

	
	public BusinessAdiacente getAdiacente(Business Source) {
		this.source =Source;
		LatLng CoordinateSource = new LatLng(Source.getLatitude(), Source.getLongitude());

		List<Business> lista = Graphs.successorListOf(this.grafo, Source);
		double distanza = 0.0;
		Business best = null; 
		
		for (Business target: lista) {
			LatLng coordinateTarget = new LatLng(target.getLatitude(), target.getLongitude());
			double distanza2 = LatLngTool.distance(CoordinateSource, coordinateTarget, LengthUnit.KILOMETER);
			if (distanza2> distanza) {
				distanza = distanza2;
				best = target;
			}
		}
		BusinessAdiacente result = new BusinessAdiacente(best, distanza);
		return result;
		
	
	}
	
	public risultatoRicorsione getPath(Business target, Double stelleMinime){
		List<Business> parziale = new ArrayList<>();
		this.bestPath = new ArrayList<>();
		this.bestScore = 0;
		parziale.add(this.source);
		ricorsione(parziale, target, stelleMinime);
		double distanza = getDistanza(this.bestPath);
		risultatoRicorsione risultato = new risultatoRicorsione(this.bestPath, distanza);
		return risultato;
	}

	private void ricorsione(List<Business> parziale, Business target, Double stelleMinime) {
		//Business corrente
		Business current = parziale.get(parziale.size()-1);
		
		//Termine uscita
		if (current.equals(target)) {
			if (getScore(parziale)>this.bestScore) {
				this.bestScore = getScore(parziale);
				this.bestPath = new ArrayList<>(parziale);
				
			}
			return;
		}
		//Aggiungo elemento
		List<Business> successori = Graphs.successorListOf(this.grafo, current);
		for (Business b : successori) {
			if (b.getStars()> stelleMinime && !parziale.contains(b)) {
				parziale.add(b);
				ricorsione(parziale, target, stelleMinime);
				parziale.remove(b);
			}
		}
		
	}
	
	public int getScore(List<Business> parziale) {
		int score = 0;
		for (Business b: parziale) {
				score++;
		}
		return score;
		
	}
	
	public double getDistanza(List<Business> parziale) {
		Double distanza= 0.0;
		LatLng coordinateprecedenti = null;
		for (Business b: parziale) {
			LatLng coordinateb = new LatLng(b.getLatitude(), b.getLongitude());
			double distanza2 = 0.0;
			if (coordinateprecedenti == null) {
				distanza2 = LatLngTool.distance(new LatLng(source.getLatitude(), source.getLatitude()), coordinateb, LengthUnit.KILOMETER);
			}else {
				 distanza2 = LatLngTool.distance(coordinateprecedenti, coordinateb, LengthUnit.KILOMETER);
			}
			distanza+= distanza2;
			coordinateprecedenti = coordinateb;
		}
		return distanza;
		
	}
	
	
}
