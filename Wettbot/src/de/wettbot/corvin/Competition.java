package de.wettbot.corvin;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import de.wettbot.league.Country;

public class Competition {
	
	private String name;
	private Country country;
	private ArrayList<Team> teams, tabelle;
	
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
	
	public void sortTabelle() {
		int z = 0;
		for(int i = 0; i < tabelle.size(); i++) {
			int x = 0;
			for(int k = 0; k < tabelle.size(); k++) {
				int y = tabelle.get(k).getPoints();
				if(x < y) {
					z = k;
					x = tabelle.get(k).getPoints();
				}
			}
			tabelle.add(i, tabelle.get(z));
		}
	}
}
