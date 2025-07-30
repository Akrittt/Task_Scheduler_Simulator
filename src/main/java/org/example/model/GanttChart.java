package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GanttChart {
    private int processID;
    private int startTime;
    private int endTime;

    public GanttChart(int processID,int startTime,int endTime){
        this.processID = processID;
        this.startTime =startTime;
        this.endTime =endTime;
    }



}
