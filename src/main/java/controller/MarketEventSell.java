package controller;

/**
 * Created by pietro on 12/06/16. This class implements the sell action during
 * market time
 */
public class MarketEventSell implements MarketEvent {
	/**
	 * String used for distinguish the correct item
	 */
	private String header;
	/**
	 * Permit tile id
	 */
	private Integer permitTileId;
	/**
	 * String used to recognize the politic card color
	 */
	private String politicCardColor;

	/**
	 * int used to set the price of item sell
	 */
	private int price;

	/**
	 * Method used to sell Permit tile
	 * 
	 * @param permitTileId
	 *            used to recognize the correct permit tile
	 * @param price
	 *            used to set the price of the item sell
	 */
	public MarketEventSell(int permitTileId, int price) {
		header = "PERMITTILE";
		this.permitTileId = permitTileId;
		this.price = price;
	}

	/**
	 * Method used to sell Politic card
	 * 
	 * @param color
	 *            used to recognize the correct politic card color
	 * @param price
	 *            used to set the price of the politic card
	 */
	public MarketEventSell(String color, int price) {
		header = "POLITICCARD";
		this.politicCardColor = color;
		this.price = price;
	}

	/**
	 * Method used to sell Assistant
	 * 
	 * @param price
	 *            used to set the price of the assistant
	 */
	public MarketEventSell(int price) {
		header = "ASSISTANT";
		this.price = price;
	}

	public String getHeader() {
		return this.header;
	}

	public int getPermitTileId() {
		return this.permitTileId;
	}

	public String getPoliticCardColor() {
		return this.politicCardColor;
	}

	public int getPrice() {
		return this.price;
	}
}
