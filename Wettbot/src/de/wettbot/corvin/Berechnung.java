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

	static ArrayList<Match> matchList = new ArrayList<Match>(), spieltag = new ArrayList<Match>();
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
//				flushDataFromHTMLtoCSV(url, f);
//			}
//		}
//		flushMatchDay();
//		readDataFromCSV(f, false);
//		System.out.println("Unentschieden insgesamt: " + getRemis());
//		System.out.println("Spiele insgesamt: " + getGames());
//		System.out.printf("Wahrscheinlichkeit für ein Unentschieden insgesamt: %.2f\n", getProbability(getRemis(), getGames()));
//		System.out.println();
//		System.out.println("Wahrschienlichkeit für ein Unentschieden zwischen den Mannschaften:");
//		String teamHomeName = "RB Leipzig";
//		String teamAwayName = "Dortmund";
//		System.out.println(teamHomeName + " und " + teamAwayName);
//		System.out.printf("%.2f%%", getRemisQuoteInPercent(teamHomeName, teamAwayName));
//		System.out.println();
//		System.out.printf("Die Quote für diese Wahrscheinlichkeit beträgt: %.2f", getRemisQuote(teamHomeName, teamAwayName));
		
		
		for(Match m : spieltag) {
			System.out.println(m.getHomeTeam().getName());
		}
		calculateMatchDay();
	}
	
	public static void readDataFromCSV(File f, boolean predictionData) {
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
				if(predictionData) {
					spieltag.add(match);
				} else {
					matchList.add(match);
				}
			}
			
			sc.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void printDataInCSV(File f, Match match) {
		try {
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			bw.append(df.format(match.getDate()).replace(";", ",") + ";");
			bw.append(match.getHomeTeam().getName().replace("&#039;", "önchen").replace(";", ",") + ";");
			bw.append(match.getAwayTeam().getName().replace("&#039;", "önchen").replace(";", ",") + ";");
			for(Map.Entry<Player, List<Integer>> map : match.getGoalgetter().entrySet()) {
				bw.append(map.getValue().get(0) + ";" + map.getKey().getTeam().getName().replace("&#039;", "önchen").replace(";", ",")  + ";" + map.getKey().getSurname().replace("&#039;", "önchen").replace(";", ",")  + ";" + map.getKey().getLastname().replace("&#039;", "önchen").replace(";", ",")  + ";");
				for(int i = 1; i < map.getValue().size(); i++) {
					bw.append(map.getValue().get(i) + ";" + map.getKey().getTeam().getName().replace("&#039;", "önchen").replace(";", ",")  + ";" + map.getKey().getSurname().replace("&#039;", "önchen").replace(";", ",")  + ";" + map.getKey().getLastname().replace("&#039;", "önchen").replace(";", ",")  + ";");
				}
			}
			bw.append("\n");
			bw.flush();
			if(exit) {
				fw.close();
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("FLUSH");
	}
	
	public static void flushDataFromHTMLtoCSV(URL url, File f) {
		
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
						}
						
						
						if(match != null) {
							if(matchAlt != match) {
								if(matchAlt != null) {
									printDataInCSV(f, matchAlt);
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
			printDataInCSV(f, matchAlt);
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

	public static void flushMatchDay() {
		File f = new File("src/BundesligaSpieltag.csv");
		try {
			PrintWriter pw = new PrintWriter(f);
			if(f.exists()) {
				readDataFromCSV(f, false);
				for(Match m : matchList) {
					System.out.println(m.getHomeTeam().getName());
					printDataInCSV(Berechnung.f, m);
				}
				
			}
			pw.print("");
			pw.close();
			for(int i = 1; i < 33; i++) {
				String s = "https://www.fussballdaten.de/bundesliga/2020/" + i + "/";
				flushDataFromHTMLtoCSV(new URL(s), f);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public static void calculateMatchDay() {
		readDataFromCSV(f, false);
		File f = new File("src/BundesligaSpieltag.csv");
		readDataFromCSV(f, true);
		int i = 1;
		for(Match m : spieltag) {
			System.out.println("Wahrscheinlichkeit für ein Unentschieden zwischen den Mannschaften:");
			String teamHomeName = m.getHomeTeam().getName();
			String teamAwayName = m.getAwayTeam().getName();
			System.out.println(teamHomeName + " und " + teamAwayName);
			System.out.printf("%.2f%%", getRemisQuoteInPercent(teamHomeName, teamAwayName));
			System.out.println();
			System.out.printf("Die Quote für diese Wahrscheinlichkeit beträgt: %.2f\n\n", getRemisQuote(teamHomeName, teamAwayName));
			i++;
		}
		System.out.println();
		System.out.println("Berechnet aus: " + i +" Daten");
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
		double probMatch = getRemisQuoteInPercent(teamHomeName, teamAwayName) / 100;
		return 1 / probMatch;
	}
	
	public static void getQuoteInPercent(String teamHomeName, String teamAwayName) {
		
	}
}
