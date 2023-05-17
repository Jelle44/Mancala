package nl.sogyo.mancala.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import nl.sogyo.mancala.domain.exceptions.gameEndedInDrawException;
import nl.sogyo.mancala.domain.exceptions.invalidBowlException;
import nl.sogyo.mancala.domain.exceptions.notYourTurnException;

public class TestKalaha {
    @Test
    void testSamePlayerGetsTurnWhenLastStoneIsDroppedInHisKalaha() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        Bowl bowl = new Bowl();
        Bowl bowl3 = (Bowl) bowl.getNextContainer(2);
        bowl3.playBowl();
        assertTrue(bowl3.getOwner().getTurn());
        assertFalse(bowl3.getOwner().getOpponent().getTurn());
    }

    @Test
    void testOpponentGetsTurnWhenLastStoneIsDroppedInHisKalaha() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        Bowl bowl = new Bowl();
        Bowl bowl9 = (Bowl) bowl.getNextContainer(9);
        bowl.getOwner().switchTurn();
        bowl9.playBowl();
        assertTrue(bowl9.getOwner().getTurn());
        assertFalse(bowl9.getOwner().getOpponent().getTurn());
    }

    @Test
    void testKalahaReceivesStones() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        Bowl bowl = new Bowl();
        Bowl bowl3 = (Bowl) bowl.getNextContainer(2);
        bowl3.playBowl();
        assertEquals(1, bowl.getNextContainer(6).getNumStones());
    }

    @Test
    void testKalahaOtherPlayerReceivesStones() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        Bowl bowl = new Bowl();
        Bowl bowl9 = (Bowl) bowl.getNextContainer(9);
        bowl.getOwner().switchTurn();
        bowl9.playBowl();
        assertEquals(1, bowl.getNextContainer(13).getNumStones());
    }

    @Test
    void testKalahaPassesCorrectAmountOfStones() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        Bowl bowl = new Bowl();
        Bowl bowl6 = (Bowl) bowl.getNextContainer(5);
        bowl6.playBowl();
        Bowl bowl7 = (Bowl) bowl.getNextContainer(7);
        assertEquals(5, bowl7.getNumStones());
    }

    @Test
    void testKalahaOtherPlayerPassesCorrectAmountOfStones() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        Bowl bowl = new Bowl();
        Bowl bowl12 = (Bowl) bowl.getNextContainer(12);
        bowl.getOwner().switchTurn();
        bowl12.playBowl();
        assertEquals(5, bowl.getNumStones());
    }

    @Test
    void testOpponentKalahaDoesNotReceiveStones() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        int[] amountStonesForEachContainer = {4,0,0,0,0,0,0,0,0,0,0,0,8,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        bowl.getOwner().switchTurn();
        Bowl bowl12 = (Bowl) bowl.getNextContainer(12);
        bowl12.playBowl();
        Kalaha kalaha1 = (Kalaha) bowl.getNextContainer(6);
        assertEquals(0, kalaha1.getNumStones());
    }
    
    @Test
    void testKalahaReceivesStonesWhenStolen() throws notYourTurnException, invalidBowlException, gameEndedInDrawException{
        int[] amountStonesForEachContainer = {4,0,0,0,0,0,0,0,4,0,0,1,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        Kalaha kalaha1 = (Kalaha) bowl.getNextContainer(6);
        bowl.playBowl();
        assertTrue(kalaha1 instanceof Kalaha);
        assertEquals(5, kalaha1.getNumStones());
    }

    @Test
    void testKalahaCanEndGame(){
        int[] amountStonesForEachContainer = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        Kalaha kalaha1 = (Kalaha) bowl.getNextContainer(6);
        assertTrue(kalaha1.emptyForEndGame());
    }

    @Test
    void testKalahaOpponentCanEndGame(){
        int[] amountStonesForEachContainer = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        bowl.getOwner().switchTurn();
        Kalaha kalaha2 = (Kalaha) bowl.getNextContainer(13);
        assertTrue(kalaha2.emptyForEndGame());
    }

    @Test
    void testGameHasWinnerWhenEnding() throws gameEndedInDrawException{
        int[] amountStonesForEachContainer = {0,0,0,0,0,0,1,2,0,0,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        bowl.checkEndGame();
        Kalaha kalaha1 = (Kalaha) bowl.getKalaha();
        Kalaha winner = kalaha1.endGame();
        assertSame(kalaha1.getOwner(), winner.getOwner());
    }

    @Test
    void testOpponentCanWinWhenNotHisKalahaEndsGame() throws gameEndedInDrawException{
        int[] amountStonesForEachContainer = {0,0,0,0,0,0,1,2,0,8,0,0,0,0};
        int i = 0;
        Bowl bowl = new Bowl(amountStonesForEachContainer, i);
        Bowl bowl7 = (Bowl) bowl.getNextContainer(7);
        bowl.checkEndGame();
        bowl.passStonesForGameEnd();
        bowl7.passStonesForGameEnd();
        Kalaha kalaha1 = (Kalaha) bowl.getKalaha();
        Kalaha kalaha2 = (Kalaha) kalaha1.getNextContainer().getKalaha();
        Kalaha winner = kalaha1.endGame();
        assertSame(kalaha2.getOwner(), winner.getOwner());
    }
}
