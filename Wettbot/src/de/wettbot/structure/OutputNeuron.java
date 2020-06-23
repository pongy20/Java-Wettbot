package de.wettbot.structure;

import java.util.List;
import de.wettbot.functions.*;

import java.util.ArrayList;

public class OutputNeuron extends Neuron{

	private List<Connection> connections = new ArrayList<>();
	private ActivationFunction activationFunktion = ActivationFunction.hyperbol;
	
	@Override
	public float getValue() {
		float sum = 0;
		for(Connection c : connections) {
			sum += c.getValue();
		}
		return activationFunktion.activation(sum);
	}
	
	public void addConnection(Connection c) {
		connections.add(c);
	}
	
}
