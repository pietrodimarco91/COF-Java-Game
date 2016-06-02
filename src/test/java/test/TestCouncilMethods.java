package test;

import static org.junit.Assert.*;

import java.util.Queue;

import org.junit.Test;

import exceptions.CouncillorNotFoundException;
import model.Council;
import model.Councillor;
import model.CouncillorsPool;
import model.RegionCouncil;

public class TestCouncilMethods {

	@Test
	public void test() {
		CouncillorsPool testCouncillorPool=new CouncillorsPool();
		Council testCouncil=new RegionCouncil();
		Queue<Councillor>testQueueCouncillors= testCouncil.getCouncillors();
		assertEquals(4,testQueueCouncillors.size());
		
		 try {
			testCouncil.addCouncillor("BLACK");
		} catch (CouncillorNotFoundException e) {
			e.printStackTrace();
		}
		 testQueueCouncillors= testCouncil.getCouncillors();
		 
		 assertEquals(5,testQueueCouncillors.size());
		 testCouncil.removeCouncillor();
		 testQueueCouncillors= testCouncil.getCouncillors();
		 assertEquals(4,testQueueCouncillors.size());	
	}

}
