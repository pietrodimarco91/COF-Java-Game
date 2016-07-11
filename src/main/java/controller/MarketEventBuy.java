package controller;

/**
 * Created by pietro on 12/06/16.
 * This class implements the buy action during market time
 */
public class MarketEventBuy implements MarketEvent {
/**
 * id of the item on Market
 */
    private Integer itemId;
/**
 * Default constructor
 */
    public MarketEventBuy(int itemId) {
        this.itemId=itemId;
    }

    public int getItemId() {
        return this.itemId;
    }
}
