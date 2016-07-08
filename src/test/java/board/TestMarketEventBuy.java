package board;

import static org.junit.Assert.*;

import org.junit.Test;

import controller.MarketEventBuy;

public class TestMarketEventBuy {

	@Test
	public void test() {
		int i=1;
		MarketEventBuy market=new MarketEventBuy(i);
		assertEquals(market.getItemId(),i);
		
	}

}
