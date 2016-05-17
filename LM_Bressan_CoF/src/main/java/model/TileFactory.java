package model;

import java.util.ArrayList;

/**
 * Created by Pietro Di Marco - edited by Riccardo - on 13/05/16.
 * AbstractFactory class used to create the different Tiles
 */
public abstract class TileFactory {

    public abstract Tile createRewardToken(int bonusNumber);

    public abstract Tile createPermitTile(ArrayList<City> cities, int bonusNumber);

    public abstract Tile createRegionBonusTile(int points);

    public abstract Tile createKingRewardTile(int points);

    public abstract Tile createColorBonusTile(int points,String color);

}
