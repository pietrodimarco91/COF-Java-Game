package model;

/**
 * Created by Pietro Di Marco on 13/05/16.
 * AbstractFactory class used to create the different Tiles
 */
public abstract class TileFactory {

    public abstract Tile createRewardToken(int BonusNumber);

    public abstract Tile createRegionBonusTile(int BonusNumber);

    public abstract Tile createPermitTile(int BonusNumber);

    public abstract Tile createKingRewardTile(int BonusNumber);

    public abstract Tile createColorBonusTile(int BonusNumber);

}
