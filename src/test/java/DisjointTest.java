import com.goeuro.busroute.datatstructures.DisjointSet;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by hhmx3422 on 12/12/16.
 */
public class DisjointTest {
    @Test
    public void testingIsDisjointSetsConnected()
    {
        DisjointSet disjointSet=new DisjointSet();
        disjointSet.union(1,2);
        disjointSet.union(2,3);
        disjointSet.union(3,6);
        disjointSet.union(1,4);
        disjointSet.union(4,5);
        disjointSet.union(5,7);
        disjointSet.union(10,9);
        disjointSet.union(9,8);
        assertTrue(disjointSet.isConnected(1,5));
        assertTrue(disjointSet.isConnected(3,1));
        assertTrue(disjointSet.isConnected(6,7));
        assertFalse(disjointSet.isConnected(10,1));
        assertFalse(disjointSet.isConnected(9,5));
        assertFalse(disjointSet.isConnected(8,6));
    }
}
