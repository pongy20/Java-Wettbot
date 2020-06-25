package de.wettbot;

import java.util.ArrayList;

public class Wettbot {
	public static void main(String[] args) {
		ArrayList<Integer> ints = new ArrayList<Integer>();
		
		int a = 1;
		int b = 2;
		int c = 3;
		
		ints.add(a);
		ints.add(b);
//		ints.add(c);
		ints.add(0, c);
//		ints.remove(3);
		
		for(int i : ints) {
			System.out.println(i);
		}
	}
}
