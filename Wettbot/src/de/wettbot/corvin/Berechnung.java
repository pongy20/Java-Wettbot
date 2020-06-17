package de.wettbot.corvin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Berechnung {

	static ArrayList<Match> matchList = new ArrayList<Match>();
	
	public static void main(String[] args) {
		File f = new File("src/SoccerDataAllWorldCups.csv");
		readDataFromCSV(f);
		System.out.println("Unentschieden insgesamt: " + getRemis());
		System.out.println("Spiele insgesamt: " + getGames());
		System.out.printf("Wahrscheinlichkeit für ein Unentschieden insgesamt: %.2f\n", getProbability(getRemis(), getGames()));
		System.out.println("Unentschieden der Mannschaft 'FRANCE': " + getRemis("France"));
		System.out.println("Anzahl der Spiele der Mannschaft 'FRANCE': " + getGames("France"));
		System.out.printf("Wahrscheinlichkeit für ein Unentschieden bei 'FRANCE': %.2f\n", getProbability(getRemis("France"), getGames("France")));
	}
	
	public static void readDataFromCSV(File f) {
		try {
			Scanner sc = new Scanner(f);
			String line = null;
			int tor1 = 0;
			int tor2 = 0;
//			int i = 0;
			while(sc.hasNextLine()) {
				line = sc.nextLine();
//				System.out.println(line);
				
				String[] teile = line.split(";");
//				System.out.println(Arrays.toString(teile));
				
				if(teile.length > 7) {
					tor1 = Integer.parseInt(teile[7]);
					tor2 = Integer.parseInt(teile[8]);
//					System.out.println(tor1 + " : " + tor2);
					
					Team team1 = new Team(teile[5]);
					Team team2 = new Team(teile[6]);
//					System.out.println(team1.getName() + " : " + team2.getName());
					
					Match match = new Match(team1, team2, null, null);
					match.setHomeGoals(tor1);
					match.setAwayGoals(tor2);
					matchList.add(match);
//					System.out.println(matchList.get(i));
					
//					i++;
				}
				
				sc.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static int getRemis() {
		int i = 0;
		for(Match m : matchList) {
			if(m.getHomeGoals() == m.getAwayGoals()) {
				i++;
			}
		}
		return i;
	}
	
	public static int getGames() {
		return matchList.size();
	}
	
	public static int getRemis(String teamName) {
		int i = 0;
		for(Match m : matchList) {
			if(teamName.equals(m.getHomeTeam().getName()) || teamName.equals(m.getAwayTeam().getName())) {
				if(m.getHomeGoals() == m.getAwayGoals()) {
					i++;
				}
			}
		}
		return i;
	}
	
	public static int getGames(String teamName) {
		int i = 0;
		for(Match m : matchList) {
			if(teamName.equals(m.getHomeTeam().getName()) || teamName.equals(m.getAwayTeam().getName())) {
				i++;
			}
		}
		return i;
	}
	
	public static double getProbability(int n, int N) {
		double kleinN = n;
		double grossN = N;
		return (kleinN / grossN) * 100;
	}
	
	
}
