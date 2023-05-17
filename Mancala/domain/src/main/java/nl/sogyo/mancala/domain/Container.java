package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.gameEndedInDrawException;

public abstract class Container {
    private Player owner;
    private Container nextContainer;
    private int numStones;

    protected Player getOwner(){
        return this.owner;
    }

    protected void setOwner(Player player){
        this.owner = player;
    }

    protected int getNumStones(){
        return this.numStones;
    }

    protected void setNumStones(int stones) {
        this.numStones = stones;
    }

    protected Container getNextContainer(){
        return this.nextContainer;
    }

    protected Container getNextContainer(int steps){
        if(steps > 0){
            return nextContainer.getNextContainer(--steps);
        }
        return this;
    }

    protected void setNextContainer(Container container){
        this.nextContainer = container;
    }

    abstract void passRemainingStones(int remainingStones);
    abstract void checkEndTurn(int remainingStones);
    abstract Container getOppositeBowl(int counter);
    abstract Container getKalaha();
    abstract void passStonesForGameEnd() throws gameEndedInDrawException;
    abstract boolean emptyForEndGame();
}
