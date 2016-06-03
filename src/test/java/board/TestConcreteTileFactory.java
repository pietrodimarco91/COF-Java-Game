package board;

import model.ConcreteTileFactory;
import model.Board;
import model.PermitTile;
import model.Tile;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by pietro on 24/05/16.
 */
public class TestConcreteTileFactory {
    @Test
    public void Test(){
        Board map=new Board(4,2,2,2,2);
        ConcreteTileFactory concreteTileFactory=new ConcreteTileFactory();
        Tile permitTile=concreteTileFactory.createPermitTile(1,map.getMap(),2);
        assertEquals(PermitTile.class,permitTile.getClass());
    }
}
