package MNIST;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import MNIST.Decoder.Digit;
import de.wettbot.structure.InputNeuron;
import de.wettbot.structure.NeuralNetwork;
import de.wettbot.structure.OutputNeuron;

public class MNISTLearn {
	public static List<Digit> digits;
	public static List<Digit> digitsTest;
	public static NeuralNetwork nn = new NeuralNetwork();
	public static InputNeuron[][] inputs = new InputNeuron[28][28];
	public static OutputNeuron[] outputs = new OutputNeuron[10];
	
	
	public static void main(String[] args) throws IOException {
		digits = MNIST.Decoder.loadDataSet("src/train-images.idx3-ubyte", "src/train-labels.idx1-ubyte");
		digitsTest = MNIST.Decoder.loadDataSet("src/t10k-images.idx3-ubyte", "t10k-labels.idx1-ubyte");
		
		for(int i = 0; i < 28; i++) {
			for(int k = 0; k < 28; k++) {
				inputs[i][k] = nn.createNewInput();
			}
		}
		
		for(int i = 0; i < 10; i++) {
			outputs[i] = nn.createNewOutput();
		}
		
		Random rand = new Random();
		float[] weights = new float[28 * 28 * 10];
		for(int i = 0; i < weights.length; i++) {
			weights[i] = rand.nextFloat(); 
		}
	}
}
