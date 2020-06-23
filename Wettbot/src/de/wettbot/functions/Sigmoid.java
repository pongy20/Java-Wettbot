package de.wettbot.functions;

public class Sigmoid implements ActivationFunction{

	@Override
	public float activation(float input) {
		return (float) (1f / (1f + Math.pow(Math.E, -input)));
	}

}
