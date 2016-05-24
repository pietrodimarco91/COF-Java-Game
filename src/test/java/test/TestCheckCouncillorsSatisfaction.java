package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import model.Council;
import model.CouncillorsPool;
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
		for(int i=0;i<6;i++){
			testPoliticCards=new PoliticCard();
			tempCardsArrayList.add(testPoliticCards);
			
		}
			
		
	}

}
