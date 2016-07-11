package model;

import controller.Player;

public class PoliticCardOnSale extends ItemOnSale {
	private PoliticCard politicCard;

	public PoliticCardOnSale (PoliticCard politicCard,Player seller,int price) {
		super(seller,price);
		this.politicCard = politicCard;
	}

	@Override
	public void buyItem(Player player) {
		player.addCardOnHand(politicCard);
	}
	
	@Override
	public String toString() {
		String string=super.toString();
		string+="Kind of Item: Politic Card\nInfo:\n";
		string+=this.politicCard.getColorCard();
		return string;
	}

}
