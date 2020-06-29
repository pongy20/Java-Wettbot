package de.wettbot.corvin;

import java.util.ArrayList;
import java.util.Collections;

import de.wettbot.league.Country;

public class Competition {
	
	private String name;
	private int season;
	private Country country;
	private ArrayList<Team> teams, tabelle;
	
	public Competition(String name, Country country) {
		this.name = name;
		this.country = country;
		this.season = 0;
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
		for(int i = 0; i < teams.size(); i++) {
			if(teams.get(i).getName().equals(team.getName())) {
				bool = true;
				teams.get(i).setPoints(team.getPoints() + teams.get(i).getPoints());
			}
		}
		if(!bool) {
			team.setNumber(teams.size());
			this.teams.add(team);
			this.tabelle.add(team);
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
	
	public void resetTabelle() {
			this.tabelle.removeAll(tabelle);
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public ArrayList<Team> getTeams() {
		return teams;
	}

	public ArrayList<Team> getTabelle() {
		return tabelle;
	}
	
	public void outTabelle(ArrayList<Team> t) {
		Collections.sort(t, Collections.reverseOrder());
		String format = "%20s\t|\t%d\t| %5d\n";
		System.out.printf("%20s\t| %6s %d\t|%8s\n", "1. Bundesliga", "Jahr", this.getSeason(), "Platz");
		System.out.printf("%53s\n", "-----------------------------------------------");
		for(int i = 0; i < t.size(); i++) {
			System.out.printf(format, t.get(i).getName(), t.get(i).getPoints(), i + 1);
		}
	}
	
	public int getPlace(String teamname) {
		int i = 0;
		while(i < getTabelle().size()) {
			if(tabelle.get(i).getName().equals(teamname)) {
				break;
			}
			i++;
		}
		return i + 1;
	}
	
	public int getPlace(Team team) {
		return getPlace(team.getName());
	}
}
