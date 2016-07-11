package model;

import controller.Player;

public class AssistantOnSale extends ItemOnSale {

	public AssistantOnSale(Player seller, int price) {
		super(seller, price);
	}

	@Override
	public void buyItem(Player player) {
		player.addAssistant();
	}
	
	@Override
	public String toString() {
		String string=super.toString();
		string+="Kind of Item: Assistant\n";
		return string;
	}
}
