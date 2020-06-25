package de.wettbot.functions;

public class Sigmoid implements ActivationFunction{

	@Override
	public float activation(float input) {
		return (float) (1f / (1f + Math.pow(Math.E, -input)));
	}

	@Override
	public float derivative(float input) {
		float sig = activation(input);
		return sig * (1 - sig);
	}

}
