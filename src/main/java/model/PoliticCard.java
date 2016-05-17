package model;

import java.util.*;

/**
 * Created by Gabriele Bressan on 13/05/16.
 */
public class PoliticCard {
	/**
	 * Color of Politic Card
	 */
	private String color;
    /**
     * Default constructor that Set the colour of the Politic Card
     */
    public PoliticCard() {
    	this.color = CouncillorColors.getRandomColor();
    }
    
    
    /**
	 * @return String of card color
	 */
    public String getColorCard(){
    	return this.color;
    }


}