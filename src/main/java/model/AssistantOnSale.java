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
}
