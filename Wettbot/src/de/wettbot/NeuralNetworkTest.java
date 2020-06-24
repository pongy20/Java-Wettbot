package de.wettbot;

import de.wettbot.structure.InputNeuron;
import de.wettbot.structure.NeuralNetwork;
import de.wettbot.structure.OutputNeuron;

public class NeuralNetworkTest {

	public static void main(String[] args) {
		NeuralNetwork nn = new NeuralNetwork();
		
		InputNeuron in1 = nn.createNewInput();
		InputNeuron in2 = nn.createNewInput();
		InputNeuron in3 = nn.createNewInput();
		InputNeuron in4 = nn.createNewInput();
		InputNeuron in5 = nn.createNewInput();
		
		OutputNeuron on1 = nn.createNewOutput();
		
		float[] shoulds = {5};
		float epsilon = 0.1f;
		
		nn.createFullMash(
				1,2,1,3,4
				);
		
		in1.setValue(1);
		in2.setValue(2);
		in3.setValue(3);
		in4.setValue(4);
		in5.setValue(5);
		
		for(int i = 0; i < 200; i++) {
			nn.deltaLearning(shoulds, epsilon);
//			epsilon *= 0.9;
		}
		
		
		System.out.println(on1.getValue());
	}

}
