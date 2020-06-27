package de.wettbot.corvin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import de.wettbot.league.Country;

public class Competition {
	
	private String name;
	private int season , matchDay;
	private Country country;
	private ArrayList<Team> teams, tabelle;
	
	public Competition(String name, Country country) {
		this.name = name;
		this.country = country;
		this.season = 0;
		this.setMatchDayNumber(0);
		this.teams = new ArrayList<Team>();
		this.tabelle = new ArrayList<Team>();
	}
	
	public String getName() {
		return name;
	}
	public Country getCountry() {
		return country;
	}
	public void addTeam(Team team) {
		boolean bool = false;
		for(Team t : teams) {
			if(t.getName().equals(team.getName())) {
				bool = true;
			}
		}
		if(!bool) {
			this.teams.add(team);
		}
		
	}
	public int[] getPoints() {
		int[] i = new int[tabelle.size()];
		for(int n = 0; n < i.length; n++) {
			i[n] = teams.get(n).getPoints();
		}
		return i;
	}
	
	public int getPoints(Team t) {
		return t.getPoints();
	}
	
	public void fillTabelle() {
		resetTabelle();
		setSeason(getSeason() + 1);
		for(int i = 0; i < 9 * 34; i++) {
			Team t1 = Berechnung.getMatchList().get(i).getHomeTeam();
			Team t2 = Berechnung.getMatchList().get(i).getAwayTeam();
			addTeam(t1);
			addTeam(t2);
		}
		for(Team t : teams) {
			tabelle.add(t);
		}
	}
	
	private void resetTabelle() {
			this.tabelle.removeAll(tabelle);
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public int getMatchDayNumber() {
		return matchDay;
	}

	public void setMatchDayNumber(int matchDay) {
		this.matchDay = matchDay;
	}

	public ArrayList<Team> getTeams() {
		return teams;
	}

	public ArrayList<Team> getTabelle() {
		return tabelle;
	}
	
	public void startMatchday(int matchDayNumber) {
		int i = 0;
		ArrayList<Match> matches = Berechnung.getMatchList();
		ArrayList<Team> teams = new ArrayList<Team>();
		for(Match m : matches) {
			if(i > (9 * matchDayNumber) - 1) {
				break;
			}
			teams.add(m.getHomeTeam());
			teams.add(m.getAwayTeam());
			m.distributePoints();
			System.out.println(m.getHomeTeam().getName() + " : " + m.getHomeGoals() + " | " + m.getAwayTeam().getName() + " : " + m.getAwayGoals());
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
		temp.removeAll(temp);
	}
	
	public void outTabelle() {
		Collections.sort(this.getTabelle(), Collections.reverseOrder());
		String format = "%20s\t|\t%d\t| %5d\n";
		System.out.printf("%20s\t| %6s %d\t|%8s\n", "1. Bundesliga", "Jahr", this.getSeason(), "Platz");
		System.out.printf("%53s\n", "-----------------------------------------------");
		for(int i = 0; i < this.getTabelle().size(); i++) {
			System.out.printf(format, this.getTabelle().get(i).getName(), this.getTabelle().get(i).getPoints(), i + 1);
		}
	}
}
