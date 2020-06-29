package de.wettbot;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import MNIST.MNISTLearn.ProbabilityDigit;
import de.wettbot.corvin.*;
import de.wettbot.structure.*;

public class Wettbot {
	
	static File f = new File("src/SoccerDataBundesliga.csv");
	static ArrayList<Team> teams = new ArrayList<Team>();
	static ArrayList<String> namen = new ArrayList<String>();
	static ArrayList<Player> player = new ArrayList<Player>();
	
	static ArrayList<Match> matches = Berechnung.getMatchList();
	static InputNeuron[] inputNeurons = new InputNeuron[5];
	static OutputNeuron[] outputNeurons = new OutputNeuron[3];
	static Team[] tabelle = new Team[18];
	static NeuralNetwork nn = new NeuralNetwork();
	
	public static void main(String[] args) {
		Competition bundesliga = new Competition("Bundesliga", null);
		bundesliga.setSeason(2000);
		fillTeams();
		player.addAll(Berechnung.flushPlayerToCSV(teams));
		for(Player p : player) {
			System.out.println(p.getTeam().getName() + " " + p.getLastname() + " " + p.getSurname());
		}
		System.out.println(player.size());
//		useBot(bundesliga, 10);
	}
	
	public static void fillTeams() {
		Berechnung.readDataFromCSV(f, false);
		for(Match m : Berechnung.getMatchList()) {
			Team home = m.getHomeTeam();
			Team away = m.getAwayTeam();
//			System.out.println(home.getName() + " " + home.getPoints() + " : " + away.getPoints() + " "	 + away.getName());
			if(!namen.contains(home.getName())) {
				home.setNumber(teams.size() + 1);
				teams.add(home);
				namen.add(home.getName());
			}
			if(!namen.contains(away.getName())) {
				away.setNumber(teams.size() + 1);
				teams.add(away);
				namen.add(away.getName());
			}
		}
	}
	
	public static void useBot(Competition wettbewerb, int years) {
		fillTeams();
		int hiddenNeuronNumber = 3;
		Random rand = new Random();
		float[] weights = new float[inputNeurons.length * hiddenNeuronNumber + hiddenNeuronNumber * outputNeurons.length];
		float epsilon = 0.0005f;
		
		
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
		
		while(true) {
			int n = 0;
			int i = 0;
			int z = 0;
			test(years);
			while(n < years) {
//				System.out.println(wettbewerb.getSeason());
				for(int nu = 0; nu < tabelle.length; nu++) {
					tabelle[nu] = null;
				}
				while(z < (n+1) * 34) {
					int j = 0;
					while(i < (z+1) * 9){
						Match m = matches.get(i);
						boolean a = false, b = false;
						for(Team t : tabelle) {
							if(t == null) {
								
							} else if(t.getName().equals(m.getHomeTeam().getName())) {
								a = true;
							} else if(t.getName().equals(m.getAwayTeam().getName())) {
								b = true;
							}
						}
						if(!a) {
							tabelle[j] = m.getHomeTeam();
							j++;
						}
						if(!b) {
							tabelle[j] = m.getAwayTeam();
							j++;
						}
						if(m.getHomeGoals() == m.getAwayGoals()) {
							for(int te = 0; te < tabelle.length; te++) {
								if(tabelle[te] == null) {
									
								} else if(tabelle[te].getName().equals(m.getHomeTeam().getName())) {
									tabelle[te].setPoints(tabelle[te].getPoints() + 1);
								} else if(tabelle[te].getName().equals(m.getAwayTeam().getName())) {
									tabelle[te].setPoints(tabelle[te].getPoints() + 1);
								}
							}
						} else if(m.getHomeGoals() > m.getAwayGoals()) {
							for(int te = 0; te < tabelle.length; te++) {
								if(tabelle[te] == null) {
									
								} else if(tabelle[te].getName().equals(m.getHomeTeam().getName())) {
									tabelle[te].setPoints(tabelle[te].getPoints() + 3);
								}
							}
						} else {
							for(int te = 0; te < tabelle.length; te++) {
								if(tabelle[te] == null) {
									
								} else if(tabelle[te].getName().equals(m.getAwayTeam().getName())) {
									tabelle[te].setPoints(tabelle[te].getPoints() + 1);
								}
							}
						}
						
						for(int te = 0; te < teams.size(); te++) {
							for(int k = 0; k < tabelle.length; k++) {
								if(tabelle[k] ==  null) {
									
								} else if(tabelle[k].getName().equals(teams.get(te).getName())) {
									tabelle[k].setNumber(teams.get(te).getNumber());
								}
							}
						}
						
						int x = 0, y = 0, numX = 0, numY = 0;
						for(int k = 0; k < tabelle.length; k++) {
							if(tabelle[k] == null) {
								
							} else if(tabelle[k].getName().equals(m.getHomeTeam().getName())) {
								x = k + 1;
								numX = tabelle[k].getNumber();
							} else if(tabelle[k].getName().equals(m.getAwayTeam().getName())) {
								y = k + 1;
								numY = tabelle[k].getNumber();
							}
						}
						inputNeurons[0].setValue((float)((float)(z % 34) + 1) / 34f);
						inputNeurons[1].setValue((float)numX / (float)teams.size());
						inputNeurons[2].setValue((float)x / (float)tabelle.length);
						inputNeurons[3].setValue((float)numY /(float) teams.size());
						inputNeurons[4].setValue((float)y / (float)tabelle.length);
						for(InputNeuron in : inputNeurons) {
							System.out.println("InputWert: " + in.getValue());
						}
						System.out.println();
						float[] shoulds = new float[outputNeurons.length];
						if(m.getHomeGoals() == m.getAwayGoals()) {
							shoulds[0] = 1;
						} else if(m.getHomeGoals() > m.getAwayGoals()) {
							shoulds[1] = 1;
						} else {
							shoulds[2] = 1;
						}
						nn.backpropagation(shoulds, epsilon);
						
						for(int fl = 0; fl < outputNeurons.length; fl++) {
							System.out.println("Output[" + fl + "] " + outputNeurons[fl].getValue());
						}
						System.out.println();
						if(i == 50) {
							return;
						}
						i++;
						epsilon = epsilon * 0.9f;
					}
					z++;
				}
				n++;
				Arrays.sort(tabelle, Collections.reverseOrder());
//				System.out.println(outputNeurons[0].getValue());
//				System.out.println(outputNeurons[1].getValue());
//				System.out.println(outputNeurons[2].getValue());
//				System.out.println("______________________");
	//			System.out.println(wettbewerb.getSeason());
	//			for(Team t : tabelle) {
	//				System.out.println(t.getName());
	//			}
	//			System.out.println();
				wettbewerb.setSeason(2000 + n);
			}
		}
	}
		
	
	
	public static float test(int years) {
		int correct = 0;
		int incorrect = 0;
		
		int n = years;
		int i = 0;
		int z = 0;
		while(n < 20) {
			for(int nu = 0; nu < tabelle.length; nu++) {
				tabelle[nu] = null;
			}
			
			while(z < (n+1) * 34) {
				
				int j = 0;
				while(i < (z+1) * 9){
					nn.reset();
					Match m = matches.get(i);
					boolean a = false, b = false;
					for(Team t : tabelle) {
						if(t == null) {
							
						} else if(t.getName().equals(m.getHomeTeam().getName())) {
							a = true;
						} else if(t.getName().equals(m.getAwayTeam().getName())) {
							b = true;
						}
					}
					if(!a) {
						tabelle[j] = m.getHomeTeam();
						j++;
					}
					if(!b) {
						tabelle[j] = m.getAwayTeam();
						j++;
					}
					if(m.getHomeGoals() == m.getAwayGoals()) {
						for(int te = 0; te < tabelle.length; te++) {
							if(tabelle[te] == null) {
								
							} else if(tabelle[te].getName().equals(m.getHomeTeam().getName())) {
								tabelle[te].setPoints(tabelle[te].getPoints() + 1);
							} else if(tabelle[te].getName().equals(m.getAwayTeam().getName())) {
								tabelle[te].setPoints(tabelle[te].getPoints() + 1);
							}
						}
					} else if(m.getHomeGoals() > m.getAwayGoals()) {
						for(int te = 0; te < tabelle.length; te++) {
							if(tabelle[te] == null) {
								
							} else if(tabelle[te].getName().equals(m.getHomeTeam().getName())) {
								tabelle[te].setPoints(tabelle[te].getPoints() + 3);
							}
						}
					} else {
						for(int te = 0; te < tabelle.length; te++) {
							if(tabelle[te] == null) {
								
							} else if(tabelle[te].getName().equals(m.getAwayTeam().getName())) {
								tabelle[te].setPoints(tabelle[te].getPoints() + 1);
							}
						}
					}
					
					for(int te = 0; te < teams.size(); te++) {
						for(int k = 0; k < tabelle.length; k++) {
							if(tabelle[k] ==  null) {
								
							} else if(tabelle[k].getName().equals(teams.get(te).getName())) {
								tabelle[k].setNumber(teams.get(te).getNumber());
							}
						}
					}
					
					int x = 0, y = 0, numX = 0, numY = 0;
					for(int k = 0; k < tabelle.length; k++) {
						if(tabelle[k] == null) {
							
						} else if(tabelle[k].getName().equals(m.getHomeTeam().getName())) {
							x = k;
							numX = tabelle[k].getNumber();
						} else if(tabelle[k].getName().equals(m.getAwayTeam().getName())) {
							y = k;
							numY = tabelle[k].getNumber();
						}
					}
					
					inputNeurons[0].setValue((float)((float)(z % 34) + 1) / 34f);
					inputNeurons[1].setValue((float)numX / (float)teams.size());
					inputNeurons[2].setValue((float)x / (float)tabelle.length);
					inputNeurons[3].setValue((float)numY /(float) teams.size());
					inputNeurons[4].setValue((float)y / (float)tabelle.length);
					
					ProbabilityDigit[] probs = new ProbabilityDigit[3];
					for(int k = 0; k < probs.length; k++) {
						probs[k] = new ProbabilityDigit(k, outputNeurons[k].getValue());
					}
					Arrays.sort(probs, Collections.reverseOrder());
					boolean wasCorrect = false;
					for(int k = 0; k < 1; k++) {
						if((m.getHomeGoals() == m.getAwayGoals() && probs[k].DIGIT == 0) || (m.getHomeGoals() > m.getAwayGoals() && probs[k].DIGIT == 1) || (m.getHomeGoals() < m.getAwayGoals() && probs[k].DIGIT == 0)) {
							wasCorrect = true;
//							System.out.println("true");
						}
					}
//					for(int k = 0; k < outputNeurons.length; k++) {
//						System.out.println(outputNeurons[k].getValue());
//						System.out.println(probs[k].DIGIT);
//					}
//					System.out.println();
					
					if(wasCorrect) {
						correct++;
					} else {
						incorrect++;
					}
					i++;
				}
				z++;
			}
			n++;
		}
		
		float percentage = (float) correct / (float) (correct + incorrect);
		System.out.println(percentage);
		return percentage;
	}
	
	public static class ProbabilityDigit implements Comparable<ProbabilityDigit> {
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
