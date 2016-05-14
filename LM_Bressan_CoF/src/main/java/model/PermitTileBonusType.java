package model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Pietro Di Marco on 14/05/16.
 * Enumeration of bonus.
 */
public enum PermitTileBonusType {
	ASSISTANT, VICTORYTRACK, POLITIC, NOBILITYTRACK, NEWMAINACTION, COINS;

	/**
	 *This method is used to fill the Tiles with bonuses, in particular this method return an array from the enum in a random way.
	 *The parameter bonusNumber indicate the size of this array.
	 */
	public static ArrayList random(int bonusNumber){
		ArrayList<String> bonuses=new ArrayList();
		for(int i=0;i<bonusNumber;i++){
			bonuses.add(String.valueOf(values()[new Random().nextInt(values().length)]));
		}

		return bonuses;
	}
}