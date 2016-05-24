package map;

import model.ConcreteTileFactory;
import model.Map;
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
        Map map=new Map(4,2,2,2,2);
        ConcreteTileFactory concreteTileFactory=new ConcreteTileFactory();
        Tile permitTile=concreteTileFactory.createPermitTile(map.getMap(),2);
        assertEquals(PermitTile.class,permitTile.getClass());
    }
}
