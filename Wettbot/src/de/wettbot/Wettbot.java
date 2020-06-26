package de.wettbot;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import de.wettbot.corvin.*;

public class Wettbot {
	
	static File f = new File("src/SoccerDataBundesliga.csv");
	
	public static void main(String[] args) {
		Competition bundesliga = new Competition("Bundesliga", null);
		Berechnung.readDataFromCSV(f, false);
		bundesliga.setSeason(1999);
		bundesliga.fillTabelle();
//		for(int i = 1; i < 35; i++) {
//			bundesliga.startMatchday(i);
//		}
//		bundesliga.startMatchday(1);
//		bundesliga.startMatchday(2);
//		
//		
		int i = 0;
		ArrayList<Match> matches = Berechnung.getMatchList();
		ArrayList<Team> teams = new ArrayList<Team>();
		for(Match m : matches) {
			if(i > 26) {
				break;
			}
			teams.add(m.getHomeTeam());
			teams.add(m.getAwayTeam());
			m.distributePoints();
			System.out.println(m.getHomeTeam().getName() + " : " + m.getHomeTeam().getPoints() + " | " + m.getAwayTeam().getName() + " : " + m.getAwayTeam().getPoints());
			if((i + 1) % 9 == 0) {
				System.out.println("---------------------------------------------");
			}
			i++;
		}
		ArrayList<Team> temp = new ArrayList<>();
		boolean bool = false;
		for(int k = 0; k < teams.size(); k++) {
			for(int j = 0; j < temp.size(); j++) {
				if(teams.get(k).getName().equals((temp.get(j).getName()))) {
					bool = true;
					temp.get(j).setPoints(temp.get(j).getPoints() + teams.get(k).getPoints());
				}
			}
			if(!bool) {
				temp.add(teams.get(k));
			}
		}
		teams.removeAll(teams);
		teams.addAll(temp);
		Collections.sort(bundesliga.getTabelle(), Collections.reverseOrder());
		bundesliga.outTabelle();
	}
}
