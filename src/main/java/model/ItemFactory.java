package model;

import controller.Player;

public abstract class ItemFactory {
	public abstract ItemOnSale createPermitTileOnSale(Tile permitTile, Player player, int price);

	public abstract ItemOnSale createAssistantOnSale(Player player, int price);

	public abstract ItemOnSale createPoliticCardOnSale(PoliticCard politicCard, Player player, int price);
}
