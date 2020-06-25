package de.wettbot.functions;

public interface ActivationFunction {
	public static Identity identity = new Identity();
	public static FunctionBoolean bool = new FunctionBoolean();
	public static Sigmoid sigmoid = new Sigmoid();
	public static HyperbolicTang hyperbol = new HyperbolicTang();
	
	public float activation(float input);
	public float derivative(float input);
	
}
