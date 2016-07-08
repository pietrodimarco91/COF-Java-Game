package controller;

/**
 * Created by pietro on 12/06/16.
 */
public class MarketEventBuy implements MarketEvent {

    private Integer itemId;

    public MarketEventBuy(int itemId) {
        this.itemId=itemId;
    }

    public int getItemId() {
        return this.itemId;
    }
}
