package de.wettbot.corvin;

import java.util.ArrayList;

import de.wettbot.league.Country;

public class Competition {
	
	private String name;
	private Country country;
	private ArrayList<Team> teams;
	
	public Competition(String name, Country country) {
		this.name = name;
		this.country = country;
	}
	
	public String getName() {
		return name;
	}
	public Country getCountry() {
		return country;
	}
	public void addTeam(Team team) {
		this.teams.add(team);
	}
	public int[] getPoints() {
		int[] i = new int[18];
		for(int n = 0; n < teams.size(); n++) {
			i[n] = teams.get(n).getPoints();
		}
		return i;
	}
}
