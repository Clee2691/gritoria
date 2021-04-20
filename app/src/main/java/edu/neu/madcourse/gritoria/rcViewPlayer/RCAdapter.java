package edu.neu.madcourse.gritoria.rcViewPlayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.gritoria.R;


public class RCAdapter extends RecyclerView.Adapter<RCViewHolder> {

    private List<RCViewPlayer> playerList;

    public RCAdapter(List<RCViewPlayer> playList) {
        this.playerList = playList;
    }

    @NonNull
    @Override
    public RCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rc_player_card, parent, false);

        return new RCViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RCViewHolder holder, int position) {
        holder.setViewData(playerList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.playerList.size();
    }
}
