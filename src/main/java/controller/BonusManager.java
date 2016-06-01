package controller;


import java.util.ArrayList;

import model.NobilityCell;
import model.Tile;

public abstract class BonusManager {
	/**
	 * Created by Gabriele Bressan on 31/05/16.
	 */
	
	/**
	 * Default constructor
	 */
	public BonusManager() {
	}
	
	/**
	 * 
	 * @param tile
	 * @return
	 */
	public static String takeBonusFromTile(Tile tile){
		String bonus="";
		return bonus;
	}
	/**
	 * 
	 * @param nobilityCell
	 * @return
	 */
	public static String takeBonusFromNobilityTrack(NobilityCell nobilityCell){
		String bonus="";
		return bonus;
	}
	
	public static void useBonus(ArrayList<String> bonus,Player player){
		for(String singleBonus: bonus){
			
			switch(singleBonus){
				case "ASSISTANT":
					break;
				case "VICTORYTRACK":
					break;
				case "POLITIC":
					break;
				case "COINS":
					break;
				case "NOBILITYTRACK":
					break;
				case "DRAWPERMITTILE":
					break;
				case "BONUSPERMITTILE":
					break;
				case "TWOEMPORIUMCITY":
					break;
				case "NEWMAINACTION":
					break;
					
			}
			
		}
	}
}
