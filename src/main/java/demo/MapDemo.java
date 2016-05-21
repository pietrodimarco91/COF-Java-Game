package demo;

import java.util.ArrayList;

import model.City;
import model.Council;
import model.Map;
import model.PermitTileDeck;
import model.Region;
import model.RegionCouncil;

public class MapDemo {

	public static void main(String[] args) {
		int numberOfPlayers=8,bonusNumber=3;
		Map map = new Map(numberOfPlayers,bonusNumber);
		System.out.println(map.toString());
	}

}
