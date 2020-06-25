package de.wettbot.structure;

import java.util.List;
import de.wettbot.functions.*;

import java.util.ArrayList;

public class OutputNeuron extends Neuron{

	private List<Connection> connections = new ArrayList<>();
	private ActivationFunction activationFunktion = ActivationFunction.identity;
	
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

	public void deltaLearning(float epsilon, float smallDelta) {
		for(int i = 0; i < connections.size(); i++) {
			float bigDelta = epsilon * smallDelta * connections.get(i).getNeuron().getValue();
			connections.get(i).addWeight(bigDelta);
		}
	}
}
