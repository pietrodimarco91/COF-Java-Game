package model;

import controller.Player;
import controller.PubSub;
import exceptions.ItemNotFoundException;
import exceptions.UnsufficientCoinsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 
 */
public class Market {
	
	private List<ItemOnSale> itemsOnSale;

	/**
	 * Default constructor:
	 */
	public Market() {
		itemsOnSale = Collections.synchronizedList(new ArrayList<ItemOnSale>());
	}

	/**
	 * This method allows to insert new items on sale in the market
	 * 
	 * @param itemOnSale
	 *            the item on sale to be inserted in the market
	 */
	public synchronized void putItemOnSale(ItemOnSale itemOnSale) {
		int id=itemsOnSale.size();
		itemOnSale.setId(id);
		itemsOnSale.add(itemOnSale);
	}

	/**
	 * This method returns the full list of items on sale in the market
	 * 
	 * @return the list of items on sale
	 */
	public List<ItemOnSale> getItemsOnSale() {
		return itemsOnSale;
	}

	/**
	 * This item removes the specified item from the list of items on sale
	 * 
	 * @param item
	 *            the item to be removed
	 */
	public synchronized void removeItemFromMarket(ItemOnSale item) {
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
	public synchronized void buyItemOnSale(Player player, int itemId) throws UnsufficientCoinsException, ItemNotFoundException {
		Iterator<ItemOnSale> iterator = itemsOnSale.iterator();
		boolean stop = false, found=false;
		while (!stop && iterator.hasNext()) {
			ItemOnSale item=iterator.next();
			if (item.getId() == itemId) {
				found=true;
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
		if(!found)
			throw new ItemNotFoundException();
	}
	
	public String toString() {
		String string="";
		string+="MARKET STATUS\n";
		for(ItemOnSale item : itemsOnSale) {
			string+=item.toString();
		}
		return string;
	}

}