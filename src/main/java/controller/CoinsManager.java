package controller;

/**
 * 
 */
public abstract class CoinsManager {

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