package de.wettbot.structure;

public class Connection {

	private Neuron neuron;
	private float weight;
	
	public Connection(Neuron neuron, float weight) {
		this.neuron = neuron;
		this.weight = weight;
	}
	
	public float getValue() {
		return this.weight;
	}

}
