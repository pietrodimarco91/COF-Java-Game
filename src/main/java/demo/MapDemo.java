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
<<<<<<< HEAD
		int numberOfPlayers=8,bonusNumber=3;
		Map map = new Map(numberOfPlayers,bonusNumber);
		System.out.println(map.toString());
=======
>>>>>>> b65a57037676b31af1a4aebd999fcd3106910976

		int numberOfPlayers = 4, bonusNumber = 2;
		Map map = new Map(numberOfPlayers, bonusNumber);
		System.out.println(map.toString());
		Council council = new RegionCouncil();
		PermitTileDeck permitTileDeck = new PermitTileDeck(20);
	}

}
