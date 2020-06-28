package de.wettbot;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import de.wettbot.corvin.*;
import de.wettbot.structure.*;

public class Wettbot {
	
	static File f = new File("src/SoccerDataBundesliga.csv");
	ArrayList<Match> ms = new ArrayList<Match>();
	
	public static void main(String[] args) {
		Competition bundesliga = new Competition("Bundesliga", null);
		Berechnung.readDataFromCSV(f, false);
		bundesliga.setSeason(2000);
		
		useBot(bundesliga, 1);
		
//		System.out.println(bundesliga.getPlace("Hamburg"));
//		System.out.println(bundesliga.getTabelle().get(bundesliga.getPlace("Hamburg")).getPoints());
	}
	
	public static void useBot(Competition wettbewerb, int matchDays) {
		InputNeuron[] inputNeurons = new InputNeuron[5];
		OutputNeuron[] outputNeurons = new OutputNeuron[3];
		NeuralNetwork nn = new NeuralNetwork();
		int hiddenNeuronNumber = 3;
		Random rand = new Random();
		float[] weights = new float[inputNeurons.length * hiddenNeuronNumber + hiddenNeuronNumber * outputNeurons.length];
		float epsilon = 0.005f;
		float percentage = 0f;
		
		for(int i = 0; i < inputNeurons.length; i++) {
			inputNeurons[i] = nn.createNewInput();
		}
		for(int i = 0; i < outputNeurons.length; i++) {
			outputNeurons[i] = nn.createNewOutput();
		}
		nn.createHiddenNeurons(hiddenNeuronNumber);
		
		for(int i = 0; i < weights.length; i++) {
			weights[i] = rand.nextFloat();
		}
		nn.createFullMesh(weights);
		int n = 1;
		int num = 1;
		while(n < 21){
			percentage = test();
			ArrayList<Team> ts = new ArrayList<Team>();
			for(int i = 0; i < n * 34 * 9; i++) {
				Match m = Berechnung.getMatchList().get(i);
				m.distributePoints();
				Team t1 = m.getHomeTeam();
				Team t2 = m.getAwayTeam();
//				System.out.println(t1.getName());
//				System.out.println(t2.getName());
//				System.out.println();
				boolean b = false, c = false;
				for(int k = 0; k < ts.size(); k++) {
					if(t1.getName().equals(ts.get(k).getName())) {
						ts.get(k).setPoints(t1.getPoints() + ts.get(k).getPoints());
						b = true;
					} else if(t2.getName().equals(ts.get(k).getName())) {
						ts.get(k).setPoints(t2.getPoints() + ts.get(k).getPoints());
						c = true;
					}
				}
				if(!b) {
					t1.setNumber(num++);
					ts.add(t1);
				}
				if(!c) {
					t2.setNumber(num++);
					ts.add(t2);
				}
				Collections.sort(ts, Collections.reverseOrder());
				inputNeurons[0].setValue(i + 1);
				inputNeurons[1].setValue(t1.getNumber());
				inputNeurons[2].setValue(wettbewerb.getPlace(t1));
				inputNeurons[3].setValue(t2.getNumber());
				inputNeurons[4].setValue(wettbewerb.getPlace(t2));
				float[] shoulds = new float[outputNeurons.length];
				if(m.getHomeGoals() == m.getAwayGoals()) {
					shoulds[0] = 1;
				} else if(m.getHomeGoals() > m.getAwayGoals()) {
					shoulds[1] = 1;
				} else {
					shoulds[2] = 1;
				}
				nn.backpropagation(shoulds, epsilon);
			}
			wettbewerb.outTabelle(ts);
			wettbewerb.setSeason(wettbewerb.getSeason() + 1);
			n++;
		}
		
	}
	
	public static float test() {
		int correct = 0;
		int incorrect = 0;
		return 0f;
	}
	
}
