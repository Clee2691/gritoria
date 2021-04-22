package edu.neu.madcourse.gritoria.rcViewTeamRank;


public class RViewTeamRank {
    private String teamName;
    private int numPlayersInTeam;
    private String icon;

    public RViewTeamRank (String teamName, int numPlayers, String teamIcon) {
        this.teamName = teamName;
        this.numPlayersInTeam = numPlayers;
        this.icon = teamIcon;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public int getNumPlayersInTeam() {
        return this.numPlayersInTeam;
    }

    public String getIcon() {
        return this.icon;
    }
}
