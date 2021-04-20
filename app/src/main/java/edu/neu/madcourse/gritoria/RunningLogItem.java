package edu.neu.madcourse.gritoria;

import java.sql.Date;

public class RunningLogItem {
    private final String runDistance;
    private final Date runDate;
    private final String stepsTaken;

    public RunningLogItem(String distance, Date date, String steps) {
        runDistance = distance;
        runDate = date;
        stepsTaken = steps;
    }

    public String getRunDistance() {
        return runDistance;
    }

    public Date getRunDate() {
        return runDate;
    }

    public String getSteps() {return stepsTaken;}
}
