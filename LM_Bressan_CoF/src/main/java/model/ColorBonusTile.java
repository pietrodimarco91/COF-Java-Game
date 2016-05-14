package model;

/**
 * Created by Pietro Di Marco on 13/05/16.
 */
public class ColorBonusTile extends Tile {

    private int points;
    private String color;

    public ColorBonusTile(int points,String color) {
        this.color=color;
        this.points=points;
    }



}