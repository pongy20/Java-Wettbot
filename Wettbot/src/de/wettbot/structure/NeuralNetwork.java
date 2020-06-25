package de.wettbot.structure;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {

	private List<InputNeuron> inputNeurons = new ArrayList<>();
	private List<OutputNeuron> hiddenNeurons = new ArrayList<>();
	private List<OutputNeuron> outputNeurons = new ArrayList<>();
	
	public InputNeuron createNewInput() {
		InputNeuron in = new InputNeuron();
		inputNeurons.add(in);
		return in;
	}
	
	public OutputNeuron createNewOutput() {
		OutputNeuron on = new OutputNeuron();
		outputNeurons.add(on);
		return on;
	}
	
	public void createFullMesh() {
		if(hiddenNeurons.size() == 0) {
			for(OutputNeuron on : outputNeurons) {
				for(InputNeuron in : inputNeurons) {
					on.addConnection(new Connection(in, 0));
				}
			}
		} else {
			for(OutputNeuron on : outputNeurons) {
				for(OutputNeuron hidden : hiddenNeurons) {
					on.addConnection(new Connection(hidden, 0));
					for(InputNeuron in : inputNeurons) {
						hidden.addConnection(new Connection(in, 0));
					}
				}
			}
		}
	}

	public void createFullMesh(float... weights) {
		if(hiddenNeurons.size() == 0) {
			if(weights.length != inputNeurons.size() * outputNeurons.size()) {
				throw new RuntimeException();
			}
			
			int index = 0;
			
			for(OutputNeuron on : outputNeurons) {
				for(InputNeuron in : inputNeurons) {
					on.addConnection(new Connection(in, weights[index++]));
				}
			}
		} else {
			if(weights.length != inputNeurons.size() * hiddenNeurons.size() + hiddenNeurons.size() * outputNeurons.size()) {
				System.out.println("Anzahl der Gewichte: " + (inputNeurons.size() * hiddenNeurons.size() + hiddenNeurons.size() * outputNeurons.size()));
				throw new RuntimeException();
			}
			
			int index = 0;
			
			for(OutputNeuron hidden : hiddenNeurons) {
				for(InputNeuron in : inputNeurons) {
					hidden.addConnection(new Connection(in, weights[index++]));
				}
			}
			for(OutputNeuron on : outputNeurons) {
				for(OutputNeuron hidden : hiddenNeurons) {
					on.addConnection(new Connection(hidden, weights[index++]));
				}
			}
		}
	}

	public void createHiddenNeurons(int amount) {
		for(int i = 0; i < amount; i++) {
			hiddenNeurons.add(new OutputNeuron());
		}
	}
	
	public void deltaLearning(float[] shoulds, float epsilon) {
		if(shoulds.length != outputNeurons.size()) {
			throw new IllegalArgumentException();
		}
		
		if(hiddenNeurons.size() != 0) {
			throw new IllegalStateException();
		}
		
		for (int i = 0; i < shoulds.length; i++) {
			float smallDelta = shoulds[i] - outputNeurons.get(i).getValue();
			outputNeurons.get(i).deltaLearning(epsilon, smallDelta);
		}
	}
}
