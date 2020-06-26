package de.wettbot;

import java.io.File;
import java.util.Collections;

import de.wettbot.corvin.*;

public class Wettbot {
	
	static File f = new File("src/SoccerDataBundesliga.csv");
	
	public static void main(String[] args) {
		Competition bundesliga = new Competition("Bundesliga", null);
		Berechnung.readDataFromCSV(f, false);
		bundesliga.setSeason(1999);
		bundesliga.fillTabelle();
		bundesliga.startMatchday(1);
//		bundesliga.startMatchday(2);
		Collections.sort(bundesliga.getTabelle(), Collections.reverseOrder());
		bundesliga.outTabelle();
	}
}
