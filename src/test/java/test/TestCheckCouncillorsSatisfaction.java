package test;

import model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

public class TestCheckCouncillorsSatisfaction {

	@Test
	public void test() {
		CouncillorsPool testPool= new CouncillorsPool();
		Council testCouncil= new RegionCouncil();
		PoliticCard testPoliticCards;
		ArrayList<PoliticCard> tempCardsArrayList= new ArrayList<PoliticCard>();
		for(int i=0;i<6;i++){
			testPoliticCards=new PoliticCard();
			tempCardsArrayList.add(testPoliticCards);
			
		}
		
		int orange=0;
		int white=0;
		int pink=0;
		int purple=0;
		int black=0;
		int multicolor=0;
		
		
		for(int i=0;i<6;i++){
		testPoliticCards= tempCardsArrayList.get(i);
		String color=testPoliticCards.getColorCard();
		if(color=="BLACK")
			black++;
		if(color=="ORANGE")
			orange++;
		if(color=="WHITE")
			white++;
		if(color=="PINK")
			pink++;
		if(color=="PURPLE")
			pink++;
		if(color=="MULTICOLOR")
			multicolor++;
		
		int councillorOrange=0;
		int councillorWhite=0;
		int councillorPink=0;
		int councillorPurple=0;
		int councillorBlack=0;
		int councillorMulticolor=0;
		int cprrispondence=0;
		
		Iterator<Councillor> iterationCouncillors = testCouncil.getCouncillors().iterator();
		Councillor councillor;
		while(iterationCouncillors.hasNext()){
			councillor = iterationCouncillors.next();
			if(color=="BLACK")
				black++;
			if(color=="ORANGE")
				orange++;
			if(color=="WHITE")
				white++;
			if(color=="PINK")
				pink++;
			if(color=="PURPLE")
				pink++;
			if(color=="MULTICOLOR")
				multicolor++;
			
		}
			
		}
		
			
		
	}

}
