package edu.neu.madcourse.gritoria.rcViewPlayer;

public class RCViewPlayer {
    private String playerName;
    private int attackPower;
    private boolean isReady;
    private String playerWorld;
    private String playerAvatar;

    public RCViewPlayer(String playerName, int attackPower, boolean ready, String playerWorld,
                        String playerAvatar) {
        this.playerAvatar = playerAvatar;
        this.playerName = playerName;
        this.attackPower = attackPower;
        this.isReady = ready;
        this.playerWorld = playerWorld;
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

    public String getPlayerWorld() {
        return this.playerWorld;
    }
    public String getPlayerAvatar() {
        return this.playerAvatar;
    }
}
