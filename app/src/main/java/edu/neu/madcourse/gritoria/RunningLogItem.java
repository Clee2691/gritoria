package edu.neu.madcourse.gritoria;

import java.sql.Date;

public class RunningLogItem {
    private final String runDistance;
    private final Date runDate;

    public RunningLogItem(String distance, Date date) {
        runDistance = distance;
        runDate = date;
    }

    public String getRunDistance() {
        return runDistance;
    }

    public Date getRunDate() {
        return runDate;
    }
}
