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

		int numberOfPlayers = 4, bonusNumber = 2;
		Map map = new Map(numberOfPlayers, bonusNumber);
		System.out.println(map.toString());
		Council council = new RegionCouncil();
		PermitTileDeck permitTileDeck = new PermitTileDeck(20);
	}

}
