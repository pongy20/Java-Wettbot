package de.wettbot.corvin;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Berechnung {
	
	SimpleDateFormat df = new SimpleDateFormat("YYYY");
	static File f = new File("src/SoccerDataAllWorldCups.csv");
	static ArrayList<Team> liste = new ArrayList<Team>();
	static ArrayList<Match> matchList = new ArrayList<Match>();
	public static void main(String[] args) {
		readData(f);
		Team t = liste.get(1);
		t.getRemisProb();
	}
	
	public static void readData(File f) {
		try {
			Scanner sc = new Scanner(f);
			sc.nextLine();
			int i = 0;
			while(sc.hasNextLine()) {
				i++;
				ArrayList<Team> listeT = new ArrayList<Team>();
				String line = sc.nextLine();
				String[] teile = line.split(";");
				if(teile.length > 7) {
					Team t = new Team(teile[5]);
					Team t2 = new Team(teile[6]);
					Date d = new Date();
					Calendar c = new GregorianCalendar();
					c.set(Calendar.YEAR, Integer.parseInt(teile[1]));
					d = c.getTime();
					Match m = new Match(t, t2, null, d);
					m.setHomeGoals(Integer.parseInt(teile[7]));
					m.setHomeGoals(Integer.parseInt(teile[8]));
					if(liste.size() == 0) {
						t.addGame(m); t2.addGame(m);
						liste.add(t); liste.add(t2);
					} else {
						int w = 0;
						for(Team team : liste) {
							w++;
							System.out.println("Teil: " + w);
							if(t.getName().equals(team.getName())) {
								t = team; t.addGame(m);
							} else {
								listeT.add(t);
							}
							if(t2.getName().equals(team.getName())) {
								t2 = team; t2.addGame(m);
							} else {
								listeT.add(t2);
							}
						}
					}
					System.out.println("Zeile: " + i + line);
					liste.addAll(listeT);
					matchList.add(m);
				}
				
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
