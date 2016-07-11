package model;

/**
 * Created by Pietro Di Marco on 14/05/16.
 */
public class RewardToken extends Tile {

    /**
     * Constructor method that is used in the same time to fill the Reward Token with bonuses.
     */
    public RewardToken(int bonusNumber) {
        super(RewardTokenType.random(bonusNumber));
    }


}