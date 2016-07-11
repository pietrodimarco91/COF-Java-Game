package model;

/**
 * Created by Pietro Di Marco on 14/05/16.
 */
public class KingRewardTile extends Tile {
	/**
	 * Default constructor:
	 */
    public KingRewardTile(int points) {
        super(points);
    }
    
    @Override
    public String toString() {
    	String string="King Reward Tile\n";
    	string+="Points: "+super.getPoints()+"\n";
    	return string;
    }



}