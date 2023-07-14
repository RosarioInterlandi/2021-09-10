package it.polito.tdp.yelp.model;

import java.util.List;

public class risultatoRicorsione {
	private List<Business> BestPath;
	private Double distanza;
	public risultatoRicorsione(List<Business> bestPath, Double distanza) {
		super();
		BestPath = bestPath;
		this.distanza = distanza;
	}
	public List<Business> getBestPath() {
		return BestPath;
	}
	public void setBestPath(List<Business> bestPath) {
		BestPath = bestPath;
	}
	public Double getDistanza() {
		return distanza;
	}
	public void setDistanza(Double distanza) {
		this.distanza = distanza;
	}
	
	
	
}
