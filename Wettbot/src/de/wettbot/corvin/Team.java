package de.wettbot.corvin;

public class Team implements Comparable<Team>{

	private int points;
	private String name;
	private int number;

	public Team(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public int compareTo(Team o) {
		if(this.getPoints() > o.getPoints()) {
			return 1;
		} else {
			return -1;
		}
	}

}