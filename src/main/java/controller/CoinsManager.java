package controller;

import java.util.*;

/**
 * 
 */
public class CoinsManager {

    /**
     * Default constructor
     */
    public CoinsManager() {
    }
   
    /**
     * This method decide what is the correct payment that one player have to do,
     * in according to the number of councillor satisfied in a specific region.
     * 
     * @param is the integer number of councillor satisfied.
     * @return the integer payment in according to the number of councillor satisfied.
     */
    public int paymentForPermitTile(int numberOfCouncillorSatisfied){
    	int payment=0;
    	switch(numberOfCouncillorSatisfied){
    	case 1:
    		payment=10;
    		break;
    	case 2:
    		payment=7;
    		break;
    	case 3:
    		payment=4;
    		break;
    	case 4:
    		payment=0;
    		break;
    	}
    	return payment;
    	
    }
}