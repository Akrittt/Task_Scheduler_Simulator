package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SchedulerResult {
    private List<ProcessEntity> scheduleProcesses;
    private List<GanttChart> ganttChartData;
    private double averageWaitingTime;
    private double averageTurnaroundTime;

    public SchedulerResult(List<ProcessEntity> scheduleProcesses, List<GanttChart> ganttChartData, double averageTurnaroundTime , double averageWaitingTime){
        this.scheduleProcesses = scheduleProcesses;
        this.ganttChartData=ganttChartData;
        this.averageTurnaroundTime = averageTurnaroundTime;
        this.averageWaitingTime =averageWaitingTime;
    }
}
