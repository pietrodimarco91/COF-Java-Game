package test;

import static org.junit.Assert.*;

import java.util.Queue;

import org.junit.Test;

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
		 testQueueCouncillors.add(new Councillor("BLACK"));
		 assertEquals(5,testQueueCouncillors.size());
		 testQueueCouncillors.remove();
		 assertEquals(4,testQueueCouncillors.size());	
	}

}
