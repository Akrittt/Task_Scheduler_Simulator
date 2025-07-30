package org.example.algorithms;

import org.example.model.GanttChart;
import org.example.model.ProcessEntity;
import org.example.model.SchedulerResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FirstComeFirstServe {
    public SchedulerResult schedule(List<ProcessEntity> processes){

        //Made a duplicate list of processes
        List<ProcessEntity> scheduledProcesses = new ArrayList<>();
        for(ProcessEntity p : processes){
            scheduledProcesses.add(new ProcessEntity(p.getId(), p.getArrivalTime(), p.getBurstTime()));
        }

        //Sort on the basis of arrival time and is arrival time is same , sort on the basis of id
        scheduledProcesses.sort(Comparator
                .comparing(ProcessEntity::getArrivalTime)
                .thenComparing(ProcessEntity::getId)
        );

        List<GanttChart> ganttChartData = new ArrayList<>();
        int currentTime = 0;
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0 ;

        //calculation
        for(ProcessEntity process : scheduledProcesses){
            //start time
            int startTime = Math.max(currentTime , process.getArrivalTime());
            process.setStartTime(startTime);
            //completion time
            int completionTime = startTime + process.getBurstTime();
            process.setCompletionTime(completionTime);
            //turnaround time
            int turnaroundTime = completionTime - process.getArrivalTime();
            process.setTurnaroundTime(turnaroundTime);
            totalTurnaroundTime += turnaroundTime;
            //waiting time
            int waitingTime = turnaroundTime- process.getBurstTime();
            process.setWaitingTime(waitingTime);
            totalWaitingTime += waitingTime;

            currentTime = completionTime;
            ganttChartData.add(new GanttChart(process.getId(), startTime , completionTime));

        }

        double averageWaitingtime = scheduledProcesses.isEmpty() ? 0 : totalWaitingTime / scheduledProcesses.size();
        double averageTurnaroundTime = scheduledProcesses.isEmpty() ? 0 : totalTurnaroundTime / scheduledProcesses.size();
        return new SchedulerResult(scheduledProcesses , ganttChartData , averageTurnaroundTime ,averageWaitingtime );


    }
}
