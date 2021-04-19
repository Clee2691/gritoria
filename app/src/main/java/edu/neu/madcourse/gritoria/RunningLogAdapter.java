package edu.neu.madcourse.gritoria;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RunningLogAdapter extends RecyclerView.Adapter<RunningLogAdapter.RunningLogViewHolder> {
    private List<RunningLogItem> mRunLogList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onGoClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class RunningLogViewHolder extends RecyclerView.ViewHolder {

        public TextView runningDateValue;
        public TextView runningDistanceValue;
        public Button buttonInfo;
        private final Context context;

        public RunningLogViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            context = itemView.getContext();
            runningDateValue = itemView.findViewById(R.id.Run_Date);
            runningDistanceValue = itemView.findViewById(R.id.Run_Distance);
            buttonInfo = itemView.findViewById(R.id.Button_Info);

            buttonInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onGoClick(position);
                            Intent intent = new Intent(context, RunningInfoActivity.class);
                            intent.putExtra("date", runningDateValue.getText());
                            intent.putExtra("distance", runningDistanceValue.getText());
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }
    }

    public RunningLogAdapter(List<RunningLogItem> runningLogList) {
        mRunLogList = runningLogList;
    }

    @NonNull
    @Override
    public RunningLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.run_log_card, parent, false);
        RunningLogViewHolder holder = new RunningLogViewHolder(v, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RunningLogViewHolder holder, int position) {
        RunningLogItem item = mRunLogList.get(position);
        holder.runningDateValue.setText(item.getRunDate().toString());
        holder.runningDistanceValue.setText(item.getRunDistance());
    }

    @Override
    public int getItemCount() {
        return mRunLogList.size();
    }

}
