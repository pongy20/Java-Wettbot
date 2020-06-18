package de.wettbot.corvin;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Berechnung {

	static ArrayList<Match> matchList = new ArrayList<Match>();
	static File f = new File("src/SoccerDataBundesliga.csv");
	static boolean exit = false;
	
	public static void main(String[] args) throws MalformedURLException, FileNotFoundException {
//		PrintWriter pw = new PrintWriter(f);
//		pw.print("");
//		for(int j = 2000; j < 2020; j++) {
//			for(int i = 1; i < 35; i++) {
//				String s = "https://www.fussballdaten.de/bundesliga/";
//				s = s + j + "/" + i + "/";
//				URL url = new URL(s);
//				readDataFromHTML(url);
//			}
//		}
		readDataFromCSV(f);
		
		System.out.println("Unentschieden insgesamt: " + getRemis());
		System.out.println("Spiele insgesamt: " + getGames());
		System.out.printf("Wahrscheinlichkeit für ein Unentschieden insgesamt: %.2f\n", getProbability(getRemis(), getGames()));
		System.out.println();
		System.out.println("Wahrschienlichkeit für ein Unentschieden zwischen den Mannschaften:");
		String teamHomeName = matchList.get(0).getHomeTeam().getName();
		String teamAwayName = matchList.get(0).getAwayTeam().getName();
		System.out.println(teamHomeName + " und " + teamAwayName);
		System.out.printf("%.2f%%", getRemisQuoteInPercent(teamHomeName, teamAwayName));
		System.out.println();
		System.out.printf("Die Quote für diese Wahrscheinlichkeit beträgt: %.2f", getRemisQuote(teamHomeName, teamAwayName));
//		System.out.println("Unentschieden der Mannschaft 'FRANCE': " + getRemis("France"));
//		System.out.println("Anzahl der Spiele der Mannschaft 'FRANCE': " + getGames("France"));
//		System.out.printf("Wahrscheinlichkeit fï¿½r ein Unentschieden bei 'FRANCE': %.2f\n", getProbability(getRemis("France"), getGames("France")));
	}
	
	public static void readDataFromCSV(File f) {
		try {
			Scanner sc = new Scanner(f);
			String line = null;
			while(sc.hasNextLine()) {
				line = sc.nextLine();				
				String[] teile = line.split(";");
				
				Date date = new Date();
				SimpleDateFormat df = new SimpleDateFormat("dd.MM.YYYY");
				date = df.parse(teile[0]);
				Team homeTeam = new Team(teile[1]);
				Team awayTeam = new Team(teile[2]);
				Match match = new Match(homeTeam, awayTeam, null, date);
				
				if(teile.length > 6) {
					for(int i = 3; i < teile.length - 3; i++) {
						int minute = Integer.parseInt(teile[i]);
						i++;
						if(homeTeam.getName().equals(teile[i])) {
							i++;
							Player player = new Player(teile[i], teile[i+1], homeTeam);
							match.addGoalgetter(player, minute);
						} else if(awayTeam.getName().equals(teile[i])) {
							i++;
							Player player = new Player(teile[i], teile[i+1], awayTeam);
							match.addGoalgetter(player, minute);
						}
						i++;
						
						
					}
				}
				matchList.add(match);
			}
			
			sc.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void printDataInCSV(Match match) {
		try {
			FileWriter fw = new FileWriter(f, true);
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			fw.append(df.format(match.getDate()).replace(";", ",") + ";");
			fw.append(match.getHomeTeam().getName().replace("&#039;", "önchen").replace(";", ",") + ";");
			fw.append(match.getAwayTeam().getName().replace("&#039;", "önchen").replace(";", ",") + ";");
			for(Map.Entry<Player, List<Integer>> map : match.getGoalgetter().entrySet()) {
				fw.append(map.getValue().get(0) + ";" + map.getKey().getTeam().getName().replace("&#039;", "önchen").replace(";", ",")  + ";" + map.getKey().getSurname().replace("&#039;", "önchen").replace(";", ",")  + ";" + map.getKey().getLastname().replace("&#039;", "önchen").replace(";", ",")  + ";");
				for(int i = 1; i < map.getValue().size(); i++) {
					fw.append(map.getValue().get(i) + ";" + map.getKey().getTeam().getName().replace("&#039;", "önchen").replace(";", ",")  + ";" + map.getKey().getSurname().replace("&#039;", "önchen").replace(";", ",")  + ";" + map.getKey().getLastname().replace("&#039;", "önchen").replace(";", ",")  + ";");
				}
			}
			fw.append("\n");
			fw.flush();
			if(exit) {
				fw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("FLUSH");
	}
	
	public static void readDataFromHTML(URL url) {
		
		try {
			BufferedInputStream bis = new BufferedInputStream(url.openStream());
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			Scanner sc = new Scanner(bis, "UTF-8");
			String line = null;
			Team home = null;
			Team away = null;
			Match match = null, matchAlt = null;
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
						
						
						if(match != null) {
							if(matchAlt != match) {
								if(matchAlt != null) {
									printDataInCSV(matchAlt);
								}
								matchAlt = match;
							} 
								
						}
						
						
//						Datum des Spieltages herausfiltern
						if(teile[i].contains("datum-row")) {
							line = teile[i+1].substring(teile[i+1].indexOf(", ") + 2, teile[i+1].indexOf("<"));
							System.out.println(line);
							date = df.parse(line);
//							System.out.println("Date: " + date);
						}
						
						
//						Torschuetze herausfiltern
						if(teile[i].contains("<div class=\"text-right")) {
							if(teile[i+1].contains("/person/")) {
								line = teile[i+1].substring(teile[i+1].indexOf("/person/") + 8, teile[i+1].indexOf("/20"));
								String[] name = line.split("-");
								if(name.length >= 2) {
									player = new Player(name[0], name[1], home);
									System.out.println("Name: " + name[0] + " " + name[1]);
								} else {
									player = new Player("", name[0], home);
									System.out.println("Name: " + name[0]);
									
								}
								
							}
						} else if(teile[i].contains("<div class=\"text-left")) {
							if(teile[i+1].contains("/person/")) {
								line = teile[i+1].substring(teile[i+1].indexOf("/person/") + 8, teile[i+1].indexOf("/20"));
								String[] name = line.split("-");
								if(name.length >= 2) {
									player = new Player(name[0], name[1], away); 
									System.out.println("Name: " + name[0] + " " + name[1]);
								} else {
									player = new Player("", name[0], away);
									System.out.println("Name: " + name[0]);
									
								}
								
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
			exit = true;
			printDataInCSV(matchAlt);
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
		return (kleinN / grossN);
	}
	
	public static double getRemisQuoteInPercent(String teamHomeName, String teamAwayName) {
		double homeProb = getProbability(getRemis(teamHomeName), getGames(teamHomeName));
		double awayProb = getProbability(getRemis(teamAwayName), getGames(teamAwayName));
		double quote = (homeProb + awayProb) / 2;
		return quote * 100;
	}
	
	public static double getRemisQuote(String teamHomeName, String teamAwayName) {
		double probGesamt = getProbability(getRemis(), getGames());
		double probMatch = getRemisQuoteInPercent(teamHomeName, teamAwayName) / 100;
		if(probGesamt > probMatch) {
			return ((probGesamt - probMatch) + 0.025) * 100;
		} else {
			return ((probMatch - probGesamt) + 0.025) * 100;
		}
	}
	
}
