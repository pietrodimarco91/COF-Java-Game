package model;

import java.util.*;

/**
 * 
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
	 * @return string of card color
	 */
    public String getColorCard(){
    	return this.color;
    }


}