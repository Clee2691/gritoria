package edu.neu.madcourse.gritoria;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserMessage implements BaseMessage{
    private String sender;
    private String message;
    private Date createdAt;

    public UserMessage(String sender, String message, Date createdAt) {
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
    }


    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getCreatedAt() {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(createdAt);
    }
}
