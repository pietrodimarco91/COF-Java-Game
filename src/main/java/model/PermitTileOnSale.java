package model;

import controller.Player;

public class PermitTileOnSale extends ItemOnSale {

	Tile permitTile;
	
	public PermitTileOnSale(Tile permitTile,Player seller,int price) {
		super(seller,price);
		this.permitTile=permitTile;
	}
	@Override
	public void buyItem(Player player) {
		player.addUnusedPermitTiles(permitTile);
	}

}
