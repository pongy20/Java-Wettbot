package de.wettbot.functions;

public class Identity implements ActivationFunction {

	@Override
	public float activation(float input) {
		return input;
	}

}
