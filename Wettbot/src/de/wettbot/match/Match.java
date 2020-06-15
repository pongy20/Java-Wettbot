package de.wettbot.match;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import de.wettbot.location.Stadium;
import de.wettbot.team.Card;
import de.wettbot.team.Player;
import de.wettbot.team.Team;

public abstract class Match {

	private Team homeTeam, awayTeam;
	private List<Player> homeLineup, awayLineup, homeBank, awayBank;
	private Stadium stadium;
	private Weather weather;
	private Date date;
	/**
	 * @param Player Torschuetze, List<Integer> Minuten
	 */
	private	SortedMap<Player, List<Integer>> goalgetter;
	
	private	SortedMap<Player, Card> playerCards;
	
	public Match(Team homeTeam, Team awayTeam, Stadium stadium, Date date) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.stadium = stadium;
		this.date = date;
		goalgetter = new TreeMap<Player, List<Integer>>();
		playerCards = new TreeMap<Player, Card>();
	}

	public void addGoalgetter(Player player, int minute) {
		if (goalgetter.containsKey(player))
			goalgetter.get(player).add(minute);
		else {
			List<Integer> liste = new ArrayList<Integer>();
			liste.add(minute);
			goalgetter.put(player, liste);
		}
			
	}
	public void addCard(Player player, Card c) {
		if (playerCards.containsKey(player))
			playerCards.remove(player);
		playerCards.put(player, c);
	}
	
	public SortedMap<Player, Card> getPlayerCards() {
		return playerCards;
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
	
}
