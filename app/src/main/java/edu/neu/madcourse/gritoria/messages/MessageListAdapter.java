package edu.neu.madcourse.gritoria.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.gritoria.R;

public class MessageListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<BaseMessage> mMessageList;
    // Pseudo enum
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private String user;

    public MessageListAdapter(Context context, List<BaseMessage> messageList, String user) {
        this.mContext = context;
        this.mMessageList = messageList;
        this.user = user;
    }

    @Override
    public int getItemViewType(int position) {
        UserMessage message = (UserMessage) mMessageList.get(position);

        if (message.getSender().equals(user)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_chat_bubble, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.other_chat_bubble, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserMessage message = (UserMessage) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText, dateText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.OtherChatMessage);
            timeText = (TextView) itemView.findViewById(R.id.OtherChatTimeStamp);
            nameText = (TextView) itemView.findViewById(R.id.OtherName);
            dateText = (TextView) itemView.findViewById(R.id.otherDateTimeStamp);
        }

        void bind(UserMessage message) {
            messageText.setText(message.getMessage());
            dateText.setText(message.getDate());
            messageText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dateText.getVisibility() == View.VISIBLE) {
                        dateText.setVisibility(View.INVISIBLE);
                    } else {
                        dateText.setVisibility(View.VISIBLE);
                    }
                }
            });

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime());
            nameText.setText(message.getSender());
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, dateText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.MyChatMessage);
            timeText = (TextView) itemView.findViewById(R.id.MyChatTimeStamp);
            dateText = (TextView) itemView.findViewById(R.id.myChatDateStamp);
        }

        void bind(UserMessage message) {
            messageText.setText(message.getMessage());
            dateText.setText(message.getDate());
            messageText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dateText.getVisibility() == View.VISIBLE) {
                        dateText.setVisibility(View.INVISIBLE);
                    } else {
                        dateText.setVisibility(View.VISIBLE);
                    }

                }
            });
            timeText.setText(message.getTime());
        }
    }
}