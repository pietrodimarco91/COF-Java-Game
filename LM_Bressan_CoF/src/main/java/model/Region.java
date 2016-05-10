
import java.util.*;

/**
 * 
 */
public class Region {

    /**
     * Default constructor
     */
    public Region() {
    }

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Council council;

    /**
     * 
     */
    private ArrayList<City> cities;

    /**
     * 
     */
    private PermitTileDeck deck;

    /**
     * 
     */
    private RegionBonusTile regionBonus;











    /**
     * @param councillor 
     * @return
     */
    public void electCouncillor(Councillor councillor) {
        // TODO implement here
        return null;
    }

    /**
     * @param politicCards 
     * @return
     */
    public boolean checkCouncilSatisfaction(ArrayList<PoliticCard> politicCards) {
        // TODO implement here
        return false;
    }

    /**
     * @param owner 
     * @return
     */
    public boolean isEligibleForRegionBonus(Player owner) {
        // TODO implement here
        return false;
    }

    /**
     * @param owner 
     * @return
     */
    public RegionBonusTile winRegionBonus(Player owner) {
        // TODO implement here
        return null;
    }

}