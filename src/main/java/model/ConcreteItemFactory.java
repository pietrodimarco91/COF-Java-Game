package model;

import controller.Player;

public class ConcreteItemFactory extends ItemFactory {

	@Override
	public ItemOnSale createPermitTileOnSale(Tile permitTile,Player player,int price) {
		return new PermitTileOnSale(permitTile,player,price);
	}

	@Override
	public ItemOnSale createAssistantOnSale(Player player,int price) {
		return new AssistantOnSale(player,price);
	}

	@Override
	public ItemOnSale createPoliticCardOnSale(PoliticCard politicCard,Player player,int price) {
		return new PoliticCardOnSale(politicCard,player,price);
	}

}
