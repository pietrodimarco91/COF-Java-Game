package model;

/**
 * Created by Pietro Di Marco on 13/05/16.
 */
public class ColorBonusTile extends Tile {

    private String color;

    public ColorBonusTile(int points,String color) {
    	super(points);
        this.color=color;
    }
    
    public String getColor() {
    	return this.color;
    }
    
    @Override
    public String toString() {
    	String string="ColorBonusTile:\n";
    	string+="Color: "+color+"\n";
    	string+="Points: "+super.getPoints()+"\n";
    	return string;
    }
}