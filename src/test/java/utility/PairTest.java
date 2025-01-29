
package utility;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import muyel.utility.Pair;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Tests the pair class
 */
public class PairTest {

    public Pair<String, Integer> pair1;
    public Pair<Boolean, Boolean> pair2; 

    // Constructs new pairs
    @BeforeEach
    public void constructPairs() {
        pair1 = new Pair<String,Integer>("0", 0);
        pair2 = new Pair<Boolean,Boolean>(false, false);
    }

    // Tests the constructor
    @Test
    public void constructorTest() {
        assertEquals("0", pair1.getFirst());
        assertEquals(Integer.valueOf(0), pair1.getSecond());

        assertFalse(pair2.getFirst() || pair2.getSecond());
    }

    // Modify values, then test
    @Test
    public void modifyValuesTest() {
        // Change the values
        pair1.setFirst("1");
        pair1.setSecond(1);
        pair2.setFirst(true);
        pair2.setSecond(true);

        // Check the values
        assertEquals("1", pair1.getFirst());
        assertEquals(Integer.valueOf(1), pair1.getSecond());
        assertTrue(pair2.getFirst() && pair2.getSecond());
    }
    
}
