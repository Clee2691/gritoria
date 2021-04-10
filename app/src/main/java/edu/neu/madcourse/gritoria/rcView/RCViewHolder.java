package edu.neu.madcourse.gritoria.rcView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import edu.neu.madcourse.gritoria.R;

import androidx.recyclerview.widget.RecyclerView;

public class RCViewHolder extends RecyclerView.ViewHolder {
    public TextView teamName;
    public ImageView teamIcon;
    public TextView numPlayersInTeam;

    public RCViewHolder(View v) {
        super(v);
        this.teamName = v.findViewById(R.id.textViewTeamName);
        this.teamIcon = v.findViewById(R.id.imageViewTeamIcon);
        this.numPlayersInTeam = v.findViewById(R.id.textViewNumMemVal);
    }

    public void setViewData(RViewTeamRank team) {
        teamIcon.setImageResource(R.drawable.adventurer);
        teamName.setText(team.getTeamName());
        numPlayersInTeam.setText(String.format("%d",team.getNumPlayersInTeam()));
    }

}
