package de.wettbot.corvin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Berechnung {

	static ArrayList<Match> matchList = new ArrayList<Match>();
	
	public static void main(String[] args) throws MalformedURLException {
//		for(int j = 0; j < 19; j++) {
			for(int i = 1; i < 35; i++) {
				String s = "https://www.fussballdaten.de/bundesliga/2019/";
				s = s + i + "/";
				URL url = new URL(s);
				readDataFromHTML(url);
			}
//		}
		
//		File f = new File("src/SoccerDataAllWorldCups.csv");
		
//		readDataFromCSV(f);
		
		System.out.println("Unentschieden insgesamt: " + getRemis());
		System.out.println("Spiele insgesamt: " + getGames());
		System.out.printf("Wahrscheinlichkeit für ein Unentschieden insgesamt: %.2f\n", getProbability(getRemis(), getGames()));
//		System.out.println("Unentschieden der Mannschaft 'FRANCE': " + getRemis("France"));
//		System.out.println("Anzahl der Spiele der Mannschaft 'FRANCE': " + getGames("France"));
//		System.out.printf("Wahrscheinlichkeit fï¿½r ein Unentschieden bei 'FRANCE': %.2f\n", getProbability(getRemis("France"), getGames("France")));
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
					matchList.add(match);
//					System.out.println(matchList.get(i));
					
//					i++;
				}
				
			}
			
			sc.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void readDataFromHTML(URL url) {
		
		try {
			BufferedInputStream bis = new BufferedInputStream(url.openStream());
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.YYYY");
			Scanner sc = new Scanner(bis, "UTF-8");
			String line = null;
			Team home = null;
			Team away = null;
			Match match = null;
			Player player = null;
			Date date = null;
			int minute = 0;
			
			while(sc.hasNextLine()) {
				line = sc.nextLine();
				if(line.contains("<div class=\"spiele-details\"><div class=\"")) {
					String[] teile = line.split(">");
					for(int i = 0; i < teile.length; i++) {
//						Match herausfiltern
						if(teile[i].contains("title=\"Spieldetails: ")) {
							line = teile[i].substring(teile[i].indexOf(": ") + 2, teile[i].indexOf(" ("));
							String[] s = line.split(" gegen ");
							home = new Team(s[0]);
							away = new Team(s[1]);
							match = new Match(home, away, null, date);
							matchList.add(match);
						}
						
//						Datum des Spieltages herausfiltern
						if(teile[i].contains("datum-row")) {
							line = teile[i+1].substring(teile[i+1].indexOf(", ") + 2, teile[i+1].indexOf("<"));
							date = df.parse(line);
						}
						
						
//						Torschuetze herausfiltern
						if(teile[i].contains("<div class=\"text-right")) {
							if(teile[i+1].contains("/person/")) {
								line = teile[i+1].substring(teile[i+1].indexOf("/person/") + 8, teile[i+1].indexOf("/20"));
								String[] name = line.split("-");
								player = new Player(name[0], name[1], home);
								System.out.println("Name: " + name[0] + " " + name[1]);
							}
						} else if(teile[i].contains("<div class=\"text-left")) {
							if(teile[i+1].contains("/person/")) {
								line = teile[i+1].substring(teile[i+1].indexOf("/person/") + 8, teile[i+1].lastIndexOf("/20"));
								String[] name = line.split("-");
								player = new Player(name[0], name[1], away);
								System.out.println("Name: " + name[0] + " " + name[1]);
							}
							
//							Minute des Torerfolgs herausfiltern (innerhalb des Torschuetzenblocks)
							if(teile[i-2].contains("'</span")) {
								line = teile[i-2].substring(0, teile[i-2].indexOf("'"));
								minute = Integer.parseInt(line);
								match.addGoalgetter(player, minute);
								System.out.println("Tor in Minute: " + minute);
							}
						}
					}
				}
			}
			
			sc.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int getRemis() {
		int i = 0;
		for(Match m : matchList) {
			if (m == null) 
				continue;
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
