package edu.neu.madcourse.gritoria.rcViewPlayer;

public class RCViewPlayer {
    private String playerName;
    private int attackPower;
    private boolean isReady;

    public RCViewPlayer(String playerName, int attackPower) {
        this.playerName = playerName;
        this.attackPower = attackPower;
        this.isReady = false;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getAttackPower() {
        return this.attackPower;
    }

    // Checks to see if player is ready to attack
    public boolean isReady() {
        return this.isReady;
    }
}
