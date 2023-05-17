package nl.sogyo.mancala.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


public class TestContainer {
    @Test
    void testBowl1isbowl(){
        Bowl bowl1 = new Bowl();
        assertTrue(bowl1 instanceof Bowl);
    }

    @Test
    void testPlayer2Exists(){
        Bowl bowl1 = new Bowl();
        Player owner = bowl1.getOwner();
        assertNotNull(owner.getOpponent());
    }

    @Test
    void testPlayer2HasOpponent(){
        Bowl bowl1 = new Bowl();
        Player player1 = bowl1.getOwner();
        Player player2 = player1.getOpponent();
        assertNotNull(player2.getOpponent());
    }

    @Test
    void testPlayers1And2Differ(){
        Bowl bowl1 = new Bowl();
        Player player1 = bowl1.getOwner();
        Player player2 = player1.getOpponent();
        assertNotSame(player1, player2);
    }

    @Test
    void testPlayer1HasTurn(){
        Bowl bowl1 = new Bowl();
        Player player1 = bowl1.getOwner();
        assertTrue(player1.getTurn());
    }

    @Test
    void testPlayer2DoesNotHaveTurn(){
        Bowl bowl1 = new Bowl();
        Player player1 = bowl1.getOwner();
        Player player2 = player1.getOpponent();
        assertFalse(player2.getTurn());
    }

    @Test  
    void testBowl1And2HaveSameOwner(){
        Bowl bowl1 = new Bowl();
        Container bowl2 = bowl1.getNextContainer();
        assertSame(bowl1.getOwner(), bowl2.getOwner());
    }

    @Test
    void testGetNextContainerWithStepsWorks(){
        Bowl bowl1 = new Bowl();
        Container bowl2 = bowl1.getNextContainer(1);
        assertSame(bowl1.getNextContainer(), bowl2);
    }

    @Test
    void testGetNextContainerWith14StepsIsSame(){
        Bowl bowl1 = new Bowl();
        assertSame(bowl1, bowl1.getNextContainer(14));
    }

    @Test
    void testBowl1And6HaveSameOwner(){
        Bowl bowl1 = new Bowl();
        Container bowl6 = bowl1.getNextContainer(5);
        assertSame(bowl6.getOwner(), bowl1.getOwner());
    }

    @Test
    void testBowl6HasKalahaAsNextContainer(){
        Bowl bowl1 = new Bowl();
        Container bowl6 = bowl1.getNextContainer(5);
        assertTrue(bowl6.getNextContainer() instanceof Kalaha);
    }

    @Test  
    void testBowl7AndBowl1HaveDifferentOwner(){
        Bowl bowl1 = new Bowl();
        Container kalaha1 = bowl1.getNextContainer(7);
        Container bowl7 = kalaha1.getNextContainer();
        assertNotSame(bowl7.getOwner(), bowl1.getOwner());
    }

    @Test
    void testKalaha2IsKalaha(){
        Bowl bowl1 = new Bowl();
        Container kalaha1 = bowl1.getNextContainer(6);
        Container kalaha2 = kalaha1.getNextContainer(7);
        assertTrue(kalaha2 instanceof Kalaha);
    }

    @Test
    void testKalaha1And2HaveDifferentOwners(){
        Bowl bowl1 = new Bowl();
        Container kalaha1 = bowl1.getNextContainer(6);
        Container kalaha2 = kalaha1.getNextContainer(7);
        assertNotSame(kalaha1.getOwner(), kalaha2.getOwner());
    }

    @Test
    void testKalaha2HasBowl1AsNextContainer(){
        Bowl bowl1 = new Bowl();
        Container kalaha1 = bowl1.getNextContainer(6);
        Container kalaha2 = kalaha1.getNextContainer(7);
        assertEquals(kalaha2.getNextContainer(), bowl1);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4,5,7,8,9,10,11,12})
    void testBowlsContain4Stones(int inputNumber){
        assertEquals(4, new Bowl().getNextContainer(inputNumber).getNumStones());
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 13})
    void testKalahasContain0Stones(int inputNumber){
        assertEquals(4, new Kalaha(new Bowl(), new Bowl().getOwner(), 2).getNextContainer(inputNumber).getNumStones());
    }
}
