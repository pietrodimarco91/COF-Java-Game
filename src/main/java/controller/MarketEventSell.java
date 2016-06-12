package controller;

/**
 * Created by pietro on 12/06/16.
 */
public class MarketEventSell implements MarketEvent {

    private String header;

    private Integer permitTileId;

    private String politicCardColor;

    public MarketEventSell(int permitTileId) {
        header="PERMITTILE";
        this.permitTileId=permitTileId;
    }

    public MarketEventSell(String color) {
        header="POLITICCARD";
        this.politicCardColor=color;
    }

    public MarketEventSell() {
        header="ASSISTANT";
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
}
