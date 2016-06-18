package model;

import controller.Player;
import exceptions.UnsufficientCoinsException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 */
public class Market {
	private ArrayList<ItemOnSale> itemsOnSale;

	private int itemIds;

	/**
	 * Default constructor: Market implements Singleton pattern
	 */
	public Market() {
		itemIds=0;
		itemsOnSale = new ArrayList<ItemOnSale>();
	}

	/**
	 * This method allows to insert new items on sale in the market
	 * 
	 * @param itemOnSale
	 *            the item on sale to be inserted in the market
	 */
	public void putItemOnSale(ItemOnSale itemOnSale) {
		itemOnSale.setId(itemIds);
		itemsOnSale.add(itemOnSale);
		itemIds++;
	}

	/**
	 * This method returns the full list of items on sale in the market
	 * 
	 * @return the list of items on sale
	 */
	public ArrayList<ItemOnSale> getItemsOnSale() {
		return itemsOnSale;
	}

	/**
	 * This item removes the specified item from the list of items on sale
	 * 
	 * @param item
	 *            the item to be removed
	 */
	public void removeItemFromMarket(ItemOnSale item) {
		boolean stop = false;
		Iterator<ItemOnSale> iterator = itemsOnSale.iterator();
		while (!stop && iterator.hasNext()) {
			if (iterator.next() == item) {
				itemsOnSale.remove(item);
				stop = true;
			}
		}
	}

	/**
	 * This method is invoked when a player wants to buy a specified item from
	 * the market
	 * 
	 * @param player
	 *            the player who wants to buy
	 * @param item
	 *            the item that will be purchased
	 * @throws UnsufficientCoinsException 
	 */
	public void buyItemOnSale(Player player, ItemOnSale item) throws UnsufficientCoinsException {
		Iterator<ItemOnSale> iterator = itemsOnSale.iterator();
		boolean stop = false;
		while (!stop && iterator.hasNext()) {
			if (iterator.next() == item) {
				if (item.hasEnoughCoins(player.getCoins())) {
					item.buyItem(player);
					CoinsManager.coinsTransaction(player, item.getSeller(), item.getPrice());
					removeItemFromMarket(item);
					stop = true;
				}
				else
					throw new UnsufficientCoinsException();
			}
		}
	}

}