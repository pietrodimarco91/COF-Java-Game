package model;

import controller.Player;

public class PermitTileOnSale extends ItemOnSale {

	Tile permitTile;
	
	public PermitTileOnSale(Tile permitTile) {
		this.permitTile=permitTile;
	}
	@Override
	public void buyItem(Player player) {
		/*
		 * The basic idea is that the specified player is given the PermitTile associated to this object
		 */
	}

}
