package model;

import controller.Player;

public class PoliticCardOnSale extends ItemOnSale {
	private PoliticCard politicCard;

	public PoliticCardOnSale (PoliticCard politicCard) {
		this.politicCard = politicCard;
	}

	@Override
	public void buyItem(Player player) {
		/*
		 * The basic idea is that the specified player earns the politic card
		 * associated to this Item
		 */

	}

}
