package model;

import java.util.*;

import controller.Player;
import exceptions.UnsufficientCoinsException;

/**
 * 
 */
public class Market {
	private static ArrayList<ItemOnSale> itemsOnSale;

	/**
	 * Default constructor: Market implements Singleton pattern
	 */
	public Market() {
		itemsOnSale = new ArrayList<ItemOnSale>();
	}

	/**
	 * This method allows to insert new items on sale in the market
	 * 
	 * @param itemOnSale
	 *            the item on sale to be inserted in the market
	 */
	public static void putItemOnSale(ItemOnSale itemOnSale) {
		itemsOnSale.add(itemOnSale);
	}

	/**
	 * This method returns the full list of items on sale in the market
	 * 
	 * @return the list of items on sale
	 */
	public static ArrayList<ItemOnSale> getItemsOnSale() {
		return itemsOnSale;
	}

	/**
	 * This item removes the specified item from the list of items on sale
	 * 
	 * @param item
	 *            the item to be removed
	 */
	public static void removeItemFromMarket(ItemOnSale item) {
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
	public static void buyItemOnSale(Player player, ItemOnSale item) throws UnsufficientCoinsException {
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