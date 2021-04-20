package edu.neu.madcourse.gritoria.messages;

import java.util.Date;
import java.util.Map;

public interface BaseMessage {

    String getSender();

    String getMessage();

    int getIndex();

    String getCreatedAt();

    String getDate();

    String getTime();

    Map<String, String> getMessageMap();

    Date getActualDate();
}
