package demo;

import model.Map;

public class MapDemo {

	public static void main(String[] args) {
		int numberOfPlayers=8,bonusNumber=3;
		Map map = new Map(numberOfPlayers,bonusNumber);
		System.out.println(map.toString());

	}

}
