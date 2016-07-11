package model;

import controller.Player;
import exceptions.UnsufficientCoinsException;

public interface CoinsManager {
	public static void coinsTransaction(Player fromPlayer, Player toPlayer, int coins) throws UnsufficientCoinsException{
		
			fromPlayer.performPayment(coins);
			toPlayer.addCoins(coins);
		
		
	}
}
