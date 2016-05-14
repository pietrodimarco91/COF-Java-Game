package model;

/**
 * Created by Pietro Di Marco on 13/05/16.
 * RegionBonusTiles that are assigned to a player thar build an emporiums on every city of that region.
 */
public class RegionBonusTile extends Tile {

    private int points;

    /**
     * Set the RegionBonusTile's points.
     */
    public RegionBonusTile(int points) {
        this.points=points;
    }


}