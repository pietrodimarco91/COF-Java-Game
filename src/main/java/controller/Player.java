package controller;

import java.util.*;

import model.City;
import model.CouncillorColors;
import model.PermitTile;
import model.PermitTileDeck;
import model.PoliticCard;
import model.Tile;

/**
 * 
 */
public class Player {

	/**
	 * 
	 */
	private String userName;

	/**
	 * 
	 */
	private String password;

	/**
	 * 
	 */
	private int matchesWon;

	/**
	 * 
	 */
	private int rageQuits;
	
	/**
	*
	*/
	private String ipAddress;
	
	/**
	 *
	 */
	private int port;
	
	/**
	 *
	 */
	private int coins;
	
	/**
	 *
	 */
	private int assistants;
	
	/**
	 *
	 */
	private int turnNumber;
	
	/**
	 *
	 */
	private ArrayList<PoliticCard> politicCards;
	
	/**
	 *
	 */
	private int victoryPoints;
	/**
	 *
	 */
	private ArrayList<PermitTile> usedPermitTiles;
	/**
	 *
	 */
	private ArrayList<PermitTile> unusedPermitTiles;
	/**
	 *
	 */
	private int emporiums;
	
	/**
	 *
	 */
	private ArrayList<City> controlledCities;
	
	/**
	 * 
	 */
	private Connector playerConnector;// To add UML scheme

	/**
	 * Default constructor
	 */
	public Player(Connector playerConnector) {
		this.playerConnector = playerConnector;
	}

	/**
	 * @return
	 */
	public String getUserName() {
		// TODO implement here
		return "";
	}

	/**
	 * @return
	 */
	public String getPassword() {
		// TODO implement here
		return "";
	}

	/**
	 * @return
	 */
	public int getMatchesWon() {
		// TODO implement here
		return 0;
	}

	/**
	 * @return
	 */
	public int getRageQuits() {
		// TODO implement here
		return 0;
	}

	/**
	 * @return
	 */
	public Connector getConnector() { // Da aggiunfere UML
		// TODO implement here
		return this.playerConnector;
	}
	
	public ArrayList<PoliticCard> cardsToCouncilSatisfaction (){
		int numberOfCardsUsed=0;
		String colorCard;
		boolean flagStopChoose=false;
		ArrayList<PoliticCard> choseCards= new ArrayList<PoliticCard>();
		
		Scanner input=new Scanner(System.in);
		while(numberOfCardsUsed<4 && flagStopChoose==false){
			System.out.println("Write the color card that yoy would to use:");
			colorCard=input.nextLine();
			if(colorCard!="stop"){
			do{
				System.out.println("You have entered an incorret color Card!");
				System.out.println("Write the color card that yoy would to use:");
				colorCard=input.nextLine();
			}while(!checkExistingColor(colorCard));
			
			do{
				System.out.println("You don't have this card!");
				System.out.println("Write the color card that yoy would to use:");
			}while(!checkIfYouHaveThisCard(colorCard));
			PoliticCard politicCard=new PoliticCard(colorCard);
			choseCards.add(politicCard);
			numberOfCardsUsed++;
			}
			else if(colorCard=="stop" && choseCards.size()==0)
				System.out.println("ERROR: You have to enter at least one card!!");
			else 
			flagStopChoose=true;
		}
		return choseCards;
		
	}
			
	public boolean checkExistingColor(String colorCard){
		ArrayList<String> allColorsCards;
		allColorsCards=CouncillorColors.getPoliticCardsColors();
		if(allColorsCards.contains(colorCard))
			return true;
		else 
			return false;
	}
	
	public boolean checkIfYouHaveThisCard(String colorCard){
		for(int i=0;i<this.politicCards.size();i++){
			if(this.politicCards.get(i).getColorCard()==colorCard)
			return true;
		}
		return false;
	}
	
	public void removeCardsFromHand(ArrayList<PoliticCard> cardsChose){
		for(int i=0;i<cardsChose.size();i++)
			if(this.politicCards.contains(cardsChose.get(i)))
				this.politicCards.remove(i);
	}
	
	
	public boolean applyPayment(int payment){
		if((this.coins-payment)>=0){
		this.coins-=payment;
		return true;
		}
		else
		return false;
	}
	
	public void setUnusedPermitTiles (Tile permitTile){
		this.unusedPermitTiles.add((PermitTile) permitTile);
	}

}