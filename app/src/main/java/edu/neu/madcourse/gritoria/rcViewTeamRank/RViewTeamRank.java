package edu.neu.madcourse.gritoria.rcViewTeamRank;


public class RViewTeamRank {
    private String teamName;
    private int numPlayersInTeam;

    public RViewTeamRank (String teamName, int numPlayers) {
        this.teamName = teamName;
        this.numPlayersInTeam = numPlayers;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public int getNumPlayersInTeam() {
        return this.numPlayersInTeam;
    }
}
