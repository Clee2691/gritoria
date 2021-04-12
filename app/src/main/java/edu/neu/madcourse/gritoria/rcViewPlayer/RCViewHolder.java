package edu.neu.madcourse.gritoria.rcViewPlayer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import edu.neu.madcourse.gritoria.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RCViewHolder extends RecyclerView.ViewHolder {
    public TextView playerName;
    public TextView playerPower;
    public ImageView playerAvatar;

    public RCViewHolder(@NonNull View itemView) {
        super(itemView);
        this.playerName = itemView.findViewById(R.id.textViewPlayerName);
        this.playerPower = itemView.findViewById(R.id.textViewPowerVal);
        this.playerAvatar = itemView.findViewById(R.id.imageViewPlayerAvatar);
    }

    public void setViewData(RCViewPlayer player) {
        playerName.setText(player.getPlayerName());
        playerPower.setText(String.format("%d",player.getAttackPower()));
        // TODO: Placeholder image! NEED TO REPLACE WITH ACTUAL PLAYER AVATAR
        playerAvatar.setImageResource(R.drawable.clairvoyant);
    }
}
