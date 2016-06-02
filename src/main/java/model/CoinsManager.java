package model;

import controller.Player;

public interface CoinsManager {
	public static void coinsTransaction(Player fromPlayer, Player toPlayer, int coins) {
		fromPlayer.removeCoins(coins);
		toPlayer.addCoins(coins);
	}
}
