package edu.neu.madcourse.gritoria.rcViewPlayer;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import edu.neu.madcourse.gritoria.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RCViewHolder extends RecyclerView.ViewHolder {
    public TextView playerName;
    public TextView playerPower;
    public ImageView playerAvatar;
    public CheckBox isReadyCheck;

    public RCViewHolder(@NonNull View itemView) {
        super(itemView);
        this.playerName = itemView.findViewById(R.id.textViewPlayerName);
        this.playerPower = itemView.findViewById(R.id.textViewPowerVal);
        this.playerAvatar = itemView.findViewById(R.id.imageViewPlayerAvatar);
        this.isReadyCheck = itemView.findViewById(R.id.checkBoxIsReady);
    }

    public void setViewData(RCViewPlayer player) {
        playerName.setText(player.getPlayerName());
        playerPower.setText(String.format("%d",player.getAttackPower()));
        // TODO: Placeholder image! NEED TO REPLACE WITH ACTUAL PLAYER AVATAR
        Context context = playerAvatar.getContext();
        int avatarID = context.getResources().getIdentifier(player.getPlayerAvatar(),
                "drawable", context.getPackageName());
        playerAvatar.setImageResource(avatarID);
        isReadyCheck.setChecked(player.isReady());
    }
}
