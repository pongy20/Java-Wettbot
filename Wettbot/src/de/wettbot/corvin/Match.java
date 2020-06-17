package de.wettbot.corvin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import de.wettbot.league.Competition;
import de.wettbot.location.Stadium;
import de.wettbot.match.Weather;
import de.wettbot.team.Card;

public class Match {

	private Team homeTeam, awayTeam;
	private List<Player> homeLineup, awayLineup, homeBank, awayBank;
	private Stadium stadium;
	private Weather weather;
	private Date date;
	
	private int homeGoals;
	private int awayGoals;
	private Competition liga;
	

	/**
	 * @param Player Torschuetze, List<Integer> Minuten
	 */
	private	SortedMap<Player, List<Integer>> goalgetter;
	private List<Integer> goalMinutes; 
	private	SortedMap<Player, Card> playerCards;
	
	public Match(Team homeTeam, Team awayTeam, Stadium stadium, Date date) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.stadium = stadium;
		this.date = date;
		goalgetter = new TreeMap<Player, List<Integer>>();
		goalMinutes = new ArrayList<Integer>();
		playerCards = new TreeMap<Player, Card>();
		this.homeGoals = 0; this.awayGoals = 0;
	}

	public void addGoalgetter(Player player, int minute) {
		goalMinutes.add(minute);
		goalgetter.put(player, goalMinutes);	
	}
	public void addCard(Player player, Card c) {
		if (playerCards.containsKey(player))
			playerCards.remove(player);
		playerCards.put(player, c);
	}
	
	public SortedMap<Player, Card> getPlayerCards() {
		return playerCards;
	}
	
	public int getHomeGoals() {
		return homeGoals;
	}
	
	public void setHomeGoals(int homeGoals) {
		this.homeGoals = homeGoals;
	}

	public int getAwayGoals() {
		return awayGoals;
	}

	public void setAwayGoals(int awayGoals) {
		this.awayGoals = awayGoals;
	}
	
	public List<Player> getHomeBank() {
		return homeBank;
	}
	public void setHomeBank(List<Player> homeBank) {
		this.homeBank = homeBank;
	}

	public SortedMap<Player, List<Integer>> getGoalgetter() {
		return goalgetter;
	}
	public List<Player> getHomeLineup() {
		return homeLineup;
	}

	public void setHomeLineup(List<Player> homeLineup) {
		this.homeLineup = homeLineup;
	}

	public List<Player> getAwayLineup() {
		return awayLineup;
	}

	public void setAwayLineup(List<Player> awayLineup) {
		this.awayLineup = awayLineup;
	}

	public List<Player> getAwayBank() {
		return awayBank;
	}

	public void setAwayBank(List<Player> awayBank) {
		this.awayBank = awayBank;
	}

	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}

	public Stadium getStadium() {
		return stadium;
	}

	public void setStadium(Stadium stadium) {
		this.stadium = stadium;
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Team getHomeTeam() {
		return homeTeam;
	}

	public Team getAwayTeam() {
		return awayTeam;
	}

	public Competition getCompetition() {
		return liga;
	}

	public void setCompetition(Competition liga) {
		this.liga = liga;
	}

	public List<Integer> getGoalMinutes() {
		return goalMinutes;
	}

	public void setGoalMinutes(List<Integer> goalMinutes) {
		this.goalMinutes = goalMinutes;
	}
	
}
