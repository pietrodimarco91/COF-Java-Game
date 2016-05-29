package model;

import controller.Player;

public abstract class CoinsManager {
	public static void coinsTransaction(Player fromPlayer, Player toPlayer, int coins) {
		fromPlayer.removeCoins(coins);
		toPlayer.addCoins(coins);
	}
}
