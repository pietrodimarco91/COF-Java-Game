package controller;

import java.util.*;

/**
 * 
 */
public abstract class CoinsManager {

    /**
     * Default constructor
     */
    public CoinsManager() {
    }
   
    /**
     * This method decides what is the correct payment that one player has to do,
     * according to the number of councillors satisfied in a specific region.
     * 
     * @param is the integer number of the satisfied councillors.
     * @return the integer payment according to the number of councillors satisfied.
     */
    public static int paymentForPermitTile(int numberOfCouncillorSatisfied){
    	int coins=0;
    	switch(numberOfCouncillorSatisfied){
    	case 1:
    		coins=10;
    		break;
    	case 2:
    		coins=7;
    		break;
    	case 3:
    		coins=4;
    		break;
    	case 4:
    		coins=0;
    		break;
    	}
    	return coins;
    	
    }
}