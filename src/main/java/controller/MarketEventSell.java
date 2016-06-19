package controller;

/**
 * Created by pietro on 12/06/16.
 */
public class MarketEventSell implements MarketEvent {

    private String header;

    private Integer permitTileId;

    private String politicCardColor;
    
    private int price;

    public MarketEventSell(int permitTileId, int price) {
        header="PERMITTILE";
        this.permitTileId=permitTileId;
        this.price=price;
    }

    public MarketEventSell(String color, int price) {
        header="POLITICCARD";
        this.politicCardColor=color;
        this.price=price;
    }

    public MarketEventSell(int price) {
        header="ASSISTANT";
        this.price=price;
    }

    public String getHeader() {
        return this.header;
    }

    public int getPermitTileId() {
        return this.permitTileId;
    }

    public String getPoliticCardColor(){
        return this.politicCardColor;
    }
    
    public int getPrice() {
    	return this.price;
    }
}
