package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.gameEndedInDrawException;

public class Kalaha extends Container {
    
    Kalaha(Bowl lastBowl, Player player, int kalahaCounter){
        if(kalahaCounter == 2){
            kalahaCounter--;
            setOwner(player);
            player = player.getOpponent();
            int bowlCounter = 6;
            setNextContainer(new Bowl(lastBowl, bowlCounter, kalahaCounter, player));
        } else if(kalahaCounter == 1){
            setOwner(player);
            setNextContainer(lastBowl);
        }
    }

    Kalaha(Bowl lastBowl, Player player, int kalahaCounter, int[] amountStonesForEachContainer, int i){
        if(kalahaCounter == 2){
            i++;
            setNumStones(amountStonesForEachContainer[6]);
            kalahaCounter--;
            setOwner(player);
            player = player.getOpponent();
            int bowlCounter = 6;
            setNextContainer(new Bowl(lastBowl, bowlCounter, kalahaCounter, player, amountStonesForEachContainer, i));
        } else if(kalahaCounter == 1){
            i++;
            setNumStones(amountStonesForEachContainer[13]);
            setOwner(player);
            setNextContainer(lastBowl);
        }
    }

    @Override
    void passRemainingStones(int remainingStones) {
        boolean isMyTurn = this.getOwner().getTurn();
        if(isMyTurn && remainingStones > 0){
            remainingStones--;
            this.setNumStones(this.getNumStones()+1);
            checkEndTurn(remainingStones);
            this.getNextContainer().passRemainingStones(remainingStones);
        } else if (!isMyTurn){
            this.getNextContainer().passRemainingStones(remainingStones);
        }
    }

    @Override
    void checkEndTurn(int remainingStones) {
        if(remainingStones == 0){
            return;
        }
    }

    @Override
    Container getOppositeBowl(int counter) {
        return this.getNextContainer(counter);
    }

    @Override
    Container getKalaha() {
        return this;
    }

    @Override
    boolean emptyForEndGame() {
        return true;
    }

    @Override
    void passStonesForGameEnd() throws gameEndedInDrawException {
        endGame();
    }

    Kalaha endGame() throws gameEndedInDrawException {
        Kalaha kalahaOpponent = (Kalaha) this.getNextContainer().getKalaha();
        return kalahaOpponent.checkWinner(this.getNumStones());
    }

    private Kalaha checkWinner(int numStones) throws gameEndedInDrawException {
        Kalaha otherKalaha = (Kalaha) this.getNextContainer().getKalaha();
        if(this.getNumStones() > numStones){
            return this;
        } else if(this.getNumStones() < numStones){
            return otherKalaha;
        } else{
            throw new gameEndedInDrawException();
        }
    }
}
