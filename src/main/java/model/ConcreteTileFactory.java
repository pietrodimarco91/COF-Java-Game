package model;

import java.util.ArrayList;

/**
 * Created by Pietro Di Marco on 13/05/16.
 * ConcreteFactory class used to invoke the different Tile's constructors
 * with the parameter BonusNumber that specify how many bonus have to be insert in one tile.
 */
public class ConcreteTileFactory extends TileFactory {


    @Override
    public Tile createPermitTile(ArrayList<City> cities,int bonusNumber) {
        return new PermitTile(cities,bonusNumber);
    }

    @Override
    public Tile createRewardToken(int bonusNumber) {
        return new RewardToken(bonusNumber);
    }

    @Override
    public Tile createRegionBonusTile(int points) {return new RegionBonusTile(points);}

    @Override
    public Tile createKingRewardTile(int points) {return new KingRewardTile(points);}

    @Override
    public Tile createColorBonusTile(int points,String color) {return new ColorBonusTile(points,color);}
}
