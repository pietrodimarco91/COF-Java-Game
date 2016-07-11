package model;

import controller.Player;
import exceptions.UnsufficientCoinsException;
/**
 * 
 * @author Gabriele Bressan
 * 
 *
 */
public interface CoinsManager {
	public static void coinsTransaction(Player fromPlayer, Player toPlayer, int coins) throws UnsufficientCoinsException{
		
			fromPlayer.performPayment(coins);
			toPlayer.addCoins(coins);
		
		
	}
}
