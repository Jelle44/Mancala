package nl.sogyo.mancala.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nl.sogyo.mancala.domain.exceptions.gameEndedInDrawException;
import nl.sogyo.mancala.domain.exceptions.invalidBowlException;
import nl.sogyo.mancala.domain.exceptions.notYourTurnException;


public class TestBowl {
    @Test
    void testBowlEmptiesWhenPlayed() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        Bowl bowl = new Bowl();
        bowl.playBowl();
        assertEquals(0, bowl.getNumStones());
    }

    @Test
    void testBowlOpponentEmptiesWhenPlayed() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        Bowl bowl = new Bowl();
        Bowl bowl7 = (Bowl) bowl.getNextContainer(7);
        bowl.getOwner().switchTurn();
        bowl7.playBowl();
        assertEquals(0, bowl7.getNumStones());
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4})
    void testNeighboursOfPlayedBowlGetOneStone(int inputNumber) throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        Bowl bowl = new Bowl();
        bowl.playBowl();
        assertEquals(5, bowl.getNextContainer(inputNumber).getNumStones());
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4})
    void testNeighboursOfPlayedBowlOpponentGetOneStone(int inputNumber) throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        Bowl bowl = new Bowl();
        Bowl bowl7 = (Bowl) bowl.getNextContainer(7);
        bowl.getOwner().switchTurn();
        bowl7.playBowl();
        assertEquals(5, bowl7.getNextContainer(inputNumber).getNumStones());
    }

    @Test
    void test0StonesPassedDoesNotAddStonesToContainer() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        Bowl bowl = new Bowl();
        bowl.playBowl();
        assertEquals(4, bowl.getNextContainer(5).getNumStones());
    }

    @Test
    void testTurnEndsWhenAllStonesAreRedistributed() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        Bowl bowl = new Bowl();
        bowl.playBowl();
        assertFalse(bowl.getOwner().getTurn());
        assertTrue(bowl.getOwner().getOpponent().getTurn());
    }

    @Test
    void testTurnOpponentEndsWhenAllStonesAreRedistributed() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        Bowl bowl = new Bowl();
        Bowl bowl7 = (Bowl) bowl.getNextContainer(7);
        bowl.getOwner().switchTurn();
        bowl7.playBowl();
        assertFalse(bowl7.getOwner().getTurn());
        assertTrue(bowl7.getOwner().getOpponent().getTurn());
    }

    @Test
    void testEndInEmptyContainerSteals() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        int[] amountStonesForEachContainer = {4,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        bowl.playBowl();
        Bowl bowl5 = (Bowl) bowl.getNextContainer(4);
        assertEquals(0, bowl5.getNumStones());
    }

    @Test
    void testOppositeOfStealEmptiesWhenStealing() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        int[] amountStonesForEachContainer = {4,0,0,0,0,0,0,0,4,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        Bowl bowl8 = (Bowl) bowl.getNextContainer(8);
        bowl.playBowl();
        assertEquals(0, bowl8.getNumStones());
    }

    @Test
    void testStealingWorksWhenOppositeBowlIsEmpty() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        int[] amountStonesForEachContainer = {4,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        Bowl bowl8 = (Bowl) bowl.getNextContainer(8);
        bowl.playBowl();
        assertEquals(0, bowl8.getNumStones());
    }

    @Test
    void testStealingDoesNotWorkIfEndInBowlOpponent() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        int[] amountStonesForEachContainer = {4,0,0,4,0,0,0,0,0,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        Bowl bowl4 = (Bowl) bowl.getNextContainer(3);
        Container bowl7 = bowl.getNextContainer(7);
        bowl4.playBowl();
        assertEquals(1, bowl7.getNumStones());
    }

    @Test
    void testStealingWorksForOpponent() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        int[] amountStonesForEachContainer = {4,0,1,0,0,0,0,5,0,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        bowl.getOwner().switchTurn();
        Bowl bowl7 = (Bowl) bowl.getNextContainer(7);
        Bowl bowl12 = (Bowl) bowl.getNextContainer(12);
        Kalaha kalaha2 = (Kalaha) bowl.getNextContainer(13);
        bowl7.playBowl();
        assertEquals(0, bowl12.getNumStones());
        assertEquals(0, bowl.getNumStones());
        assertEquals(5, kalaha2.getNumStones());
    }

    @Test
    void testPlayedBowlContainsStoneWhen14OrMorePassed() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        int[] amountStonesForEachContainer = {14,0,1,0,0,0,0,1,0,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        bowl.playBowl();
        assertEquals(1, bowl.getNumStones());
    }

    @Test
    void testPlayedBowlStealsWhen13StonesPassed() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        int[] amountStonesForEachContainer = {13,0,1,0,0,0,0,1,0,0,0,0,1,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        Bowl bowl12 = (Bowl) bowl.getNextContainer(12);
        bowl.playBowl();
        assertEquals(0, bowl.getNumStones());
        assertEquals(0, bowl12.getNumStones());
    }

    @Test
    void testOpponentsBowlCanNotBePlayed(){
        int[] amountStonesForEachContainer = {4,0,0,0,0,0,0,0,4,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        Bowl bowl8 = (Bowl) bowl.getNextContainer(8);
        assertThrows(notYourTurnException.class, () -> bowl8.playBowl());
    }

    @Test
    void testEmptyBowlCanNotBePlayed(){
        int[] amountStonesForEachContainer = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        assertThrows(invalidBowlException.class, () -> bowl.playBowl());
    }

    @Test
    void testGameEndsWhenAllBowlsEmpty(){
        int[] amountStonesForEachContainer = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        boolean endGame = bowl.checkEndGame();
        assertTrue(endGame);
    }

    @Test
    void testGameDoesNotEndWhenTurnPlayerCanStillMove(){
        int[] amountStonesForEachContainer = {0,0,0,1,0,0,0,0,0,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        boolean endGame = bowl.checkEndGame();
        assertFalse(endGame);
    }

    @Test
    void testGameDoesNotEndWhenOpponentCanStillMoveAndHasTurn() throws notYourTurnException, invalidBowlException{
        int[] amountStonesForEachContainer = {0,0,0,0,0,0,0,1,0,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        bowl.getOwner().switchTurn();
        boolean endGame = bowl.checkEndGame();
        assertFalse(endGame);
    }

    @Test
    void testBowlsPassStonesWhenGameEnds() throws gameEndedInDrawException{
        int[] amountStonesForEachContainer = {1,0,0,1,0,0,0,0,0,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        Kalaha kalaha1 = (Kalaha) bowl.getNextContainer(6);
        bowl.passStonesForGameEnd();
        assertEquals(2, kalaha1.getNumStones());
    }

    @Test
    void testGameEmptiesAllBowlsWhenLastMoveSteals() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        int[] amountStonesForEachContainer = {1,0,0,0,1,0,0,4,0,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        Bowl bowl5 = (Bowl) bowl.getNextContainer(4);
        bowl5.playBowl();
        Bowl bowl7 = (Bowl) bowl.getNextContainer(7);
        boolean allBowlsEmpty = bowl7.emptyForEndGame();
        assertTrue(allBowlsEmpty);
    }
}
