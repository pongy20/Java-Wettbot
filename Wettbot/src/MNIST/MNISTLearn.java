package MNIST;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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
		digits = Decoder.loadDataSet("src/train-images.idx3-ubyte", "src/train-labels.idx1-ubyte");
		digitsTest = Decoder.loadDataSet("src/t10k-images.idx3-ubyte", "src/t10k-labels.idx1-ubyte");
		
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
		nn.createFullMesh(weights);
		
		
		float epsilon = 0.01f;
		
		while(true) {
			test();
			for(int i = 0; i < digits.size(); i++) {
				for(int x = 0; x < 28; x++) {
					for(int y = 0; y < 28; y++) {
						inputs[x][y].setValue(Decoder.toUnsignedByte(digits.get(i).data[x][y]) / 255f);
					}
				}
				float[] shoulds = new float[10];
				shoulds[digits.get(i).label] = 1;
				nn.deltaLearning(shoulds, epsilon);
			}
			epsilon *= 0.9f;
		}
	}
	
	public static void test() {
		int correct = 0;
		int incorrect = 0;
		
		for(int i = 0; i < digitsTest.size(); i++) {
			for(int x = 0; x < 28; x++) {
				for(int y = 0; y < 28; y++) {
					inputs[x][y].setValue(Decoder.toUnsignedByte(digitsTest.get(i).data[x][y]) / 255f);
				}
			}
			
			ProbabilityDigit[] probs = new ProbabilityDigit[10];
			for(int k = 0; k < probs.length; k++) {
				probs[k] = new ProbabilityDigit(k, outputs[k].getValue());
				System.out.println(outputs[k].getValue());
			}
			System.out.println();
			System.out.println("-------------------");
			System.out.println();
			
			Arrays.sort(probs, Collections.reverseOrder());
			boolean wasCorrect = false;
			for(int k = 0; k < 9; k++) {
				if(digitsTest.get(i).label == probs[k].DIGIT) {
					wasCorrect = true;
				}
			}
			
			if(wasCorrect) {
				correct++;
			} else {
				incorrect++;
			}
		}
		
		float percentage = (float) correct / (float) (correct + incorrect);
		System.out.println(percentage);
	}
	
	public static class ProbabilityDigit implements Comparable<ProbabilityDigit>{
		public final int DIGIT;
		public float probability;
		
		public ProbabilityDigit(int digit, float probability) {
			this.DIGIT = digit;
			this.probability = probability;
		}
		@Override
		public int compareTo(ProbabilityDigit o) {
			if(probability == o.probability) {
				return 0;
			} else if(probability > o.probability) {
				return 1;
			} else {
				return -1;
			}
		}
		
	}
}
