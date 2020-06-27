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
		int matchDayNumber = 34;
//		bundesliga.calculateTabelle(matchDayNumber);
		bundesliga.startMatchday(matchDayNumber);
		bundesliga.outTabelle();
		
//		for(Team t : bundesliga.getTabelle()) {
//			System.out.println(t.getName() + " : " + t.getPoints());
//		}
		
	}
}
