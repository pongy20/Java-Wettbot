package de.wettbot.corvin;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	public void fillTabelle() {
		for(int i = 0; i < 9; i++) {
			resetTabell();
			Team t1 = Berechnung.getMatchList().get(i).getHomeTeam();
			Team t2 = Berechnung.getMatchList().get(i).getAwayTeam();
			addTeam(t1);
			addTeam(t2);
			tabelle.add(t1);
			tabelle.add(t2);
		}
	}
	
	private void resetTabell() {
		this.tabelle.removeAll(tabelle);
	}

	public void sortTabelle() {
		for(int i = 0; i < tabelle.size(); i++) {
			int z = 0;
			int x = 0;
			int k = i;
			while(k < tabelle.size()) {
				int y = tabelle.get(k).getPoints();
				if(x < y) {
					z = k;
					x = tabelle.get(k).getPoints();
				}
				k++;
			}
			tabelle.add(i, tabelle.get(z));
		}
	}
}
