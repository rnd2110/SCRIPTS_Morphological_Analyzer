package edu.columbia.material.morph.analysis.models;

public class POSProb {
	
	private String tag;
	private double prob;
		
	public POSProb(String tag, double prob) {
		this.tag = tag;
		this.prob = prob;
	}
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public double getProb() {
		return prob;
	}
	public void setProb(double prob) {
		this.prob = prob;
	}
	
}
