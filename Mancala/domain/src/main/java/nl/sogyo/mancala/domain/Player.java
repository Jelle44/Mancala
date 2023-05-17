package nl.sogyo.mancala.domain;


public class Player {
    private Player opponent;
    private Boolean isMyTurn;

    Player() {
        setOpponent(new Player(this));
        setTurn(true);
    }

    Player(Player player) {
        setOpponent(player);
        setTurn(false);
    }

    protected Player getOpponent(){
        return this.opponent;
    }

    private void setOpponent(Player player) {
        this.opponent = player;
    }

    protected boolean getTurn(){
        return this.isMyTurn;
    }

    protected void setTurn(boolean turn) {
        this.isMyTurn = turn;
    }

    protected void switchTurn() {
        this.setTurn(!this.getTurn());
        this.getOpponent().setTurn(!this.getTurn());
    }
}
