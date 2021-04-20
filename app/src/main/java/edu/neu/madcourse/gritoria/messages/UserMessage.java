package edu.neu.madcourse.gritoria.messages;

import com.google.firebase.database.snapshot.Index;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//Code adopted from sendbird

public class UserMessage implements BaseMessage, Comparable<BaseMessage> {
    private String sender;
    private String message;
    private String createdAt;
    private String dateString;
    private String timeString;
    private Date actualDate;
    private int index;

    public UserMessage(String sender, String message, Date createdAt, int index) {
        this.sender = sender;
        this.message = message;
        DateFormat combined = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        this.createdAt = combined.format(createdAt);
        DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
        this.dateString = date.format(createdAt);
        DateFormat time = new SimpleDateFormat("hh:mm:ss a");
        this.timeString = time.format(createdAt);
        this.actualDate = createdAt;
        this.index = index;
    }

    public UserMessage(String sender, String message, String dateString, String timeString, String index) {
        this.sender = sender;
        this.message = message;
        this.dateString = dateString;
        this.timeString = timeString;
        this.index = Integer.parseInt(index);
        try {
            this.actualDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH).parse(dateString +" "+ timeString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Not valid date");
        }

    }

    @Override
    public String getSender() {
        return sender;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getDate() {
        return dateString;
    }

    @Override
    public String getTime() {
        return timeString;
    }

    @Override
    public Map<String, String> getMessageMap() {
        HashMap<String, String> messageMap = new HashMap<>();
        messageMap.put("sender", sender);
        messageMap.put("message", message);
        messageMap.put("date", dateString);
        messageMap.put("time", timeString);
        messageMap.put("index", String.valueOf(index));
        return messageMap;
    }

    @Override
    public Date getActualDate() {
        return actualDate;
    }

    @Override
    public int compareTo(BaseMessage o) {
        return this.actualDate.compareTo(o.getActualDate());
    }
}
