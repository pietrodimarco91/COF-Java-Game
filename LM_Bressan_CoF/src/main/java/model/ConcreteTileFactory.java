package model;

/**
 * Created by Pietro Di Marco on 13/05/16.
 * ConcreteFactory class used to invoke the different Tile's constructors
 * with the parameter BonusNumber that specify how many bonus have to be insert in one tile.
 */
public class ConcreteTileFactory extends TileFactory {

    @Override
    public Tile createRewardToken(int BonusNumber) {
        return new RewardToken(BonusNumber);
    }

    @Override
    public Tile createRegionBonusTile(int BonusNumber) {
        return new RegionBonusTile(BonusNumber);
    }

    @Override
    public Tile createPermitTile(int BonusNumber) {
        return new PermitTile(BonusNumber);
    }

    @Override
    public Tile createKingRewardTile(int BonusNumber) {
        return new KingRewardTile(BonusNumber);
    }

    @Override
    public Tile createColorBonusTile(int BonusNumber) {
        return new ColorBonusTile(BonusNumber);
    }

}
