package model;

import java.io.Serializable;

import controller.Player;

/**
 * 
 * @author Gabriele Bressan
 * Abstract class used for put one item on sale
 *
 */

public abstract class ItemOnSale implements Serializable {
	/**
	 * Variable used for serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This attribute represents the seller of this item
	 */
	private Player seller;

	private int id;

	/**
	 * This attribute represents the price (coins) for this item
	 */
	private int price;
	/**
	 * Default constructor:
	 */
	public ItemOnSale(Player seller, int price) {
		this.seller=seller;
		this.price=price;
	}
	
	/**
	 * This method returns this item on sale when someone buys it
	 * @return the item contained in this ItemOnSale object
	 */
	public abstract void buyItem(Player player);
	
	/**
	 * This methods allows to state whether a player could buy a specified item or not.
	 * @return true if coins are enough, false otherwise
	 */
	public boolean hasEnoughCoins(int coins) {
		return coins>=price;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Player getSeller() {
		return seller;
	}

	public int getPrice() {
		return price;
	}
	
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		String string="";
		string+="ITEM ON SALE\n";
		string+="ID: "+this.id+", ";
		string+="Seller: "+this.seller.getNickName()+"\n";
		string+="Price: "+this.getPrice()+"\n";
		return string;
	}


}
