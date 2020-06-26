package de.wettbot.structure;

import java.util.List;
import de.wettbot.functions.*;

import java.util.ArrayList;

public class OutputNeuron extends Neuron{

	private List<Connection> connections = new ArrayList<>();
	private ActivationFunction activationFunktion = ActivationFunction.sigmoid;
	private float smallDelta = 0;
	private float value = 0;
	private boolean bool = false;
	
	@Override
	public float getValue() {
		if(!bool) {
			float sum = 0;
			for(Connection c : connections) {
				sum += c.getValue();
			}
			value = activationFunktion.activation(sum);
			bool = true;
		}
		return value;
	}
	
	public void addConnection(Connection c) {
		connections.add(c);
	}
	
	public void reset() {
		bool = false;
		smallDelta = 0;
	}

	public void deltaLearning(float epsilon) {
		float bigData = activationFunktion.derivative(getValue()) * epsilon * smallDelta;
		for(int i = 0; i < connections.size(); i++) {
			float bigDelta = bigData * connections.get(i).getNeuron().getValue();
			connections.get(i).addWeight(bigDelta);
		}
	}
	
	public void setFunction(ActivationFunction af) {
		this.activationFunktion = af;
	}
	
	public void calculateOutputDelta(float should) {
		smallDelta = should - getValue();
	}
	
	public void backPropagate() {
		for(Connection c: connections) {
			Neuron n = c.getNeuron();
			if(n instanceof OutputNeuron) {
				OutputNeuron on = (OutputNeuron) n;
				on.smallDelta += this.smallDelta * c.getWeight();
			}
		}
	}
}
