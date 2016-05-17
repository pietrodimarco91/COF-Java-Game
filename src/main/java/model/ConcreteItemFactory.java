package model;

public class ConcreteItemFactory extends ItemFactory {

	@Override
	public ItemOnSale createPermitTileOnSale(Tile permitTile) {
		return new PermitTileOnSale(permitTile);
	}

	@Override
	public ItemOnSale createAssistantOnSale() {
		return new AssistantOnSale();
	}

	@Override
	public ItemOnSale createPoliticCardOnSale(PoliticCard politicCard) {
		return new PoliticCardOnSale(politicCard);
	}

}
