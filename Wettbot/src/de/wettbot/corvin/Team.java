package de.wettbot.corvin;

public class Team {

	private String name;
	private int aktPunkte;

	public Team(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPoints(int points) {
		this.aktPunkte = points;
	}

	public int getPoints() {
		return this.aktPunkte;
	}

}