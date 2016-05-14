package model;

import java.util.ArrayList;

/**
 * Created by Pietro Di Marco on 14/05/16.
 * This class is used to create a permit Tile.
 */
public class PermitTile extends Tile {

    /**
     * Cities in which the tile permit the construction of emporiums.
     */
    private ArrayList<City> cities;
    /**
     * Indicate in which region the tile is located.
     */
    private Region region;

    /**
     * Constructor method that is used in the same time to fill the PermitTiles with bonuses.
     */
    public PermitTile(int bonusNumber) {
        bonus=PermitTileBonusType.random(bonusNumber);
    }



}