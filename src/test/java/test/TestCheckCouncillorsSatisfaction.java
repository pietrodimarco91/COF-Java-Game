package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import model.Council;
import model.Councillor;
import model.CouncillorsPool;
import model.PermitTileDeck;
import model.PoliticCard;
import model.Region;
import model.RegionCouncil;

public class TestCheckCouncillorsSatisfaction {

	@Test
	public void test() {
	
		CouncillorsPool testPool= new CouncillorsPool();
		Council testCouncil= new RegionCouncil();
		PoliticCard testPoliticCards;
		ArrayList<PoliticCard> tempCardsArrayList= new ArrayList<PoliticCard>();
		for(int i=0;i<5;i++){
			testPoliticCards=new PoliticCard();
			tempCardsArrayList.add(testPoliticCards);
			
		}
		
		int orange=0;
		int white=0;
		int pink=0;
		int purple=0;
		int black=0;
		int blue=0;
		int multicolor=0;
		
		String color;
		for(int i=0;i<5;i++){
		testPoliticCards= tempCardsArrayList.get(i);
		color=testPoliticCards.getColorCard();
		if(color=="BLACK")
			black++;
		if(color=="ORANGE")
			orange++;
		if(color=="WHITE")
			white++;
		if(color=="PINK")
			pink++;
		if(color=="PURPLE")
			purple++;
		if(color=="BLUE")
			blue++;
		if(color=="MULTICOLOR")
			multicolor++;
		
		}
		
		int councillorOrange=0;
		int councillorWhite=0;
		int councillorPink=0;
		int councillorPurple=0;
		int councillorBlack=0;
		int councillorMulticolor=0;
		int councillorBlue=0;
		int corrispondence=0;
		
		String councillorColor;
		Iterator<Councillor> iterationCouncillors = testCouncil.getCouncillors().iterator();
		Councillor councillor;
		while(iterationCouncillors.hasNext()){
			councillor = iterationCouncillors.next();
			councillorColor= councillor.getColor();
			if(councillorColor=="BLACK")
				councillorBlack++;
			if(councillorColor=="ORANGE")
				councillorOrange++;
			if(councillorColor=="WHITE")
				councillorWhite++;
			if(councillorColor=="PINK")
				councillorPink++;
			if(councillorColor=="PURPLE")
				councillorPurple++;
			if(councillorColor=="BLUE")
				councillorBlue++;
		}
		
			
			
			
			if(black==councillorBlack ||(black>=1 && councillorBlack>=1)){
				if(black>councillorBlack)
					corrispondence+=councillorBlack;
				else
					corrispondence+=black;
				
			}
				
			if(orange==councillorOrange ||(orange>=1 && councillorOrange>=1)){
				if(orange>councillorOrange)
					corrispondence+=councillorOrange;
				else
					corrispondence+=orange;
			}
				
			if(white==councillorWhite ||(white>=1 && councillorWhite>=1)){
				if(white>councillorWhite)
					corrispondence+=councillorWhite;
				else
					corrispondence+=white;
			}
				
			if(pink==councillorPink ||(pink>=1 && councillorPink>=1)){
				if(pink>councillorPink)
					corrispondence+=councillorPink;
				else
					corrispondence+=pink;
			}
				
			if(purple==councillorPurple ||(purple>=1 && councillorPurple>=1)){
				if(purple>councillorPurple)
				corrispondence+=councillorPurple;
			else
				corrispondence+=purple;
				
			}
			
			if(blue==councillorBlue ||(blue>=1 && councillorBlue>=1)){
				if(blue>councillorBlue)
					corrispondence+=councillorBlue;
				else
					corrispondence+=blue;
					
				}
			
			if(corrispondence<4){
				if((corrispondence+multicolor)<4)
					corrispondence+=multicolor;
				else
					corrispondence=4;
			}
			
			
			PermitTileDeck testDeck= new PermitTileDeck(10);
			Region testRegion= new Region("Example",testCouncil,testDeck);
			
			int testResultMethod=testRegion.checkCouncilSatisfaction(tempCardsArrayList);
			assertEquals(corrispondence,testResultMethod);
		
		}
			
		}
		
			
		



