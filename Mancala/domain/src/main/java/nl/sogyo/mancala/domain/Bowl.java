package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.gameEndedInDrawException;
import nl.sogyo.mancala.domain.exceptions.invalidBowlException;
import nl.sogyo.mancala.domain.exceptions.notYourTurnException;

public class Bowl extends Container {

    Bowl() {
        setOwner(new Player());
        setNumStones(4);
        int bowlCounter = 5;
        int kalahaCounter = 2;
        setNextContainer(new Bowl(this, bowlCounter, kalahaCounter, this.getOwner()));
    }

    Bowl(Bowl lastBowl, int bowlCounter, int kalahaCounter, Player player) {
        if(bowlCounter == 1){
            setNumStones(4);
            setOwner(player);
            setNextContainer(new Kalaha(lastBowl, player, kalahaCounter));
        } else if (bowlCounter > 1){
            setNumStones(4);
            bowlCounter--;
            setOwner(player);
            setNextContainer(new Bowl(lastBowl, bowlCounter, kalahaCounter, player));
        }
    }

    Bowl(int[] amountStonesForEachContainer, int i){
        setOwner(new Player());
        setNumStones(amountStonesForEachContainer[0]);
        int bowlCounter = 5;
        int kalahaCounter = 2;
        setNextContainer(new Bowl(this, bowlCounter, kalahaCounter, this.getOwner(), amountStonesForEachContainer, i));
    }

    Bowl(Bowl lastBowl, int bowlCounter, int kalahaCounter, Player player, int[] amountStonesForEachContainer, int i) {
        if(bowlCounter == 1){
            i++;
            setNumStones(amountStonesForEachContainer[i]);
            setOwner(player);
            setNextContainer(new Kalaha(lastBowl, player, kalahaCounter, amountStonesForEachContainer, i));
        } else if (bowlCounter > 1){
            i++;
            setNumStones(amountStonesForEachContainer[i]);
            bowlCounter--;
            setOwner(player);
            setNextContainer(new Bowl(lastBowl, bowlCounter, kalahaCounter, player, amountStonesForEachContainer, i));
        }
    }

    public void playBowl() throws notYourTurnException, invalidBowlException, gameEndedInDrawException {
        boolean isMyTurn = this.getOwner().getTurn();
        if(isMyTurn && this.getNumStones() > 0){
            int remainingStones = this.getNumStones();
            this.setNumStones(0);
            this.getNextContainer().passRemainingStones(remainingStones);
            if(checkEndGame()){
                Bowl firstBowlOpponent = (Bowl) getKalahaOpponent().getNextContainer();
                firstBowlOpponent.passStonesForGameEnd();
            }
        } else if (!isMyTurn){
            throw new notYourTurnException();
        } else{
            throw new invalidBowlException();
        }
    }

    @Override
    void passRemainingStones(int remainingStones){
        if(remainingStones > 0){
            this.setNumStones(this.getNumStones() + 1);
            remainingStones --;
            checkEndTurn(remainingStones);
            this.getNextContainer().passRemainingStones(remainingStones);
        }
    }

    @Override
    void checkEndTurn(int remainingStones) {
        if(remainingStones == 0 && allowedToSteal()){
            steal();
        }

        if(remainingStones == 0){
            this.getOwner().switchTurn();
        }
    }

    private boolean allowedToSteal(){
        boolean isMyTurn = this.getOwner().getTurn();
        if(isMyTurn && this.getNumStones() == 1){
            return true;
        }
        return false;
    }

    private void steal() {
        this.passStonesToKalaha();
        int counterToOpposite = 0;
        Bowl myOppositeBowl = (Bowl) this.getOppositeBowl(counterToOpposite);
        Container kalahaOpponent = myOppositeBowl.getKalahaOpponent();
        kalahaOpponent.setNumStones(kalahaOpponent.getNumStones() + myOppositeBowl.getNumStones());
        myOppositeBowl.setNumStones(0);
    }

    private void passStonesToKalaha() {
        Kalaha myKalaha = (Kalaha) getKalaha();
        myKalaha.setNumStones(myKalaha.getNumStones() + this.getNumStones());
        this.setNumStones(0);
    }

    @Override
    Container getKalaha() {
        return this.getNextContainer().getKalaha();
    }

    @Override
    Container getOppositeBowl(int counter) {
        return this.getNextContainer().getOppositeBowl(++counter);
    }

    private Container getKalahaOpponent() {
        Container firstBowlTurnPlayer = getKalaha().getNextContainer();
        Container kalahaOpponent = firstBowlTurnPlayer.getKalaha();
        return kalahaOpponent;
    }

    boolean checkEndGame() {
        boolean isMyTurn = this.getOwner().getTurn();
        if(isMyTurn){
            Bowl myFirstBowl = (Bowl) getKalaha().getNextContainer().getKalaha().getNextContainer();
            return myFirstBowl.emptyForEndGame();
        } else{
            Bowl opponentFirstBowl = (Bowl) this.getKalaha().getNextContainer();
            return opponentFirstBowl.emptyForEndGame();
        }
    }

    @Override
    void passStonesForGameEnd() throws gameEndedInDrawException {
        this.passStonesToKalaha();
        this.getNextContainer().passStonesForGameEnd();
    }

    @Override
    boolean emptyForEndGame() {
        if(this.getNumStones() == 0){
            return this.getNextContainer().emptyForEndGame();
        }
        return false;
    }
}
