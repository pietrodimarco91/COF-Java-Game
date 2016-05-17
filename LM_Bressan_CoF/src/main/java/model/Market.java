package model;

import java.util.*;

import controller.Player;

/**
 * 
 */
public class Market {
	private ArrayList<ItemOnSale> itemsOnSale;
	private static Market instance;
	/**
	 * Default constructor: Market implements Singleton pattern
	 */
	private Market() {
		itemsOnSale=new ArrayList<ItemOnSale>();
	}
	
	public static Market getInstance() {
		return instance;
	}
	
	/**
	 * This method allows to insert new items on sale in the market
	 * @param itemOnSale the item on sale to be inserted in the market
	 */
	public void putItemOnSale(ItemOnSale itemOnSale) {
		itemsOnSale.add(itemOnSale);
	}
	
	/**
	 * This method returns the full list of items on sale in the market
	 * @return the list of items on sale
	 */
	public ArrayList<ItemOnSale> getItemsOnSale() {
		return itemsOnSale;
	}
	
	/**
	 * This item removes the specified item from the list of items on sale
	 * @param item the item to be removed
	 */
	public void removeItemFromMarket(ItemOnSale item) {
		boolean stop=false;
		Iterator<ItemOnSale> iterator = itemsOnSale.iterator();
		while(!stop&&iterator.hasNext()) {
			if(iterator.next()==item) {
				itemsOnSale.remove(item);
				stop=true;
			}
		}
	}
	
	/**
	 * This method is invoked when a player wants to buy a specified item from the market
	 * @param player the player who wants to buy
	 * @param item the item that will be purchased
	 */
	public void buyItemOnSale(Player player,ItemOnSale item) {
		Iterator<ItemOnSale> iterator = itemsOnSale.iterator();
		boolean stop=false;
		while(!stop&&iterator.hasNext()) {
			if(iterator.next()==item) {
				item.buyItem(player);
				CoinsManager.coinsTransaction(player, item.getSeller(), item.getPrice());
				stop=true;
			}
		}
	}

}