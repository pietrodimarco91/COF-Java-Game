package model;

public abstract class ItemFactory {
	public abstract ItemOnSale createPermitTileOnSale(Tile permitTile);
	public abstract ItemOnSale createAssistantOnSale();
	public abstract ItemOnSale createPoliticCardOnSale(PoliticCard politicCard);
}
