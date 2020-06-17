package de.wettbot.corvin;

import java.util.Date;

import de.wettbot.team.PlayerPosition;

public class Player implements Comparable<Player> {
	private String surname, lastname;
	private Team team;
	private boolean isInjured;
	private int marketValue;
	private PlayerPosition position;
	private Date birthdate;
	private Date contractEnd;
	
	
	
	public Player(String surname, String lastname, Team team) {
		this.surname = surname;
		this.lastname = lastname;
		this.team = team;
	}
	


	public Team getTeam() {
		return team;
	}



	public void setTeam(Team team) {
		this.team = team;
	}



	public boolean isInjured() {
		return isInjured;
	}



	public void setInjured(boolean isInjured) {
		this.isInjured = isInjured;
	}



	public int getMarketValue() {
		return marketValue;
	}



	public void setMarketValue(int marketValue) {
		this.marketValue = marketValue;
	}



	public PlayerPosition getPosition() {
		return position;
	}


	public void setPosition(PlayerPosition position) {
		this.position = position;
	}

	public Date getContractEnd() {
		return contractEnd;
	}

	public void setContractEnd(Date contractEnd) {
		this.contractEnd = contractEnd;
	}


	public String getSurname() {
		return surname;
	}

	public String getLastname() {
		return lastname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public int getWeight() {
		return 0;
	}

	@Override
	public int compareTo(Player o) {
		return this.getLastname().compareTo(o.getLastname());
	}
	
}
