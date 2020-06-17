package de.wettbot.corvin;

import java.util.ArrayList;

public class Team {

	private String name;
	private int gameCount;
//	5 Jahre MatchHistory erforderlich
	private ArrayList<Match> matchHistory;


	public Team(String name) {
		this.setName(name);
		this.matchHistory = new ArrayList<Match>();
	}
	
	
	public void getRemisProb() {
		double game = getGameCount();
		double remis = getRemisCount();
		double p = (remis / game);
		System.out.println(p);
	}
	
	public int getRemisCount() {
		int count = 0;
		for(Match m : matchHistory) {
			if(m.getHomeGoals() == m.getAwayGoals()) {
				count++;
			}
		}
		
		return count;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getGameCount() {
		return gameCount;
	}
	
	public void addGame(Match match) {
		for(Match m : matchHistory) {
			if(m != match) {
				matchHistory.add(match);
				this.gameCount++;
			}
		}
	}
}