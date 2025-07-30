package org.example.algorithms;

import org.example.model.GanttChart;
import org.example.model.ProcessEntity;
import org.example.model.SchedulerResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class ShortestJobFirst {
    public SchedulerResult schedule (List<ProcessEntity> processes){
        List<ProcessEntity> originalProcesses = new ArrayList<>();
        for(ProcessEntity p : processes){
            originalProcesses.add(new ProcessEntity(p.getId(), p.getArrivalTime(), p.getBurstTime()));
        }

        List<ProcessEntity> ScheduledProcesses =new ArrayList<>();

        List<GanttChart> ganttChartData = new ArrayList<>();
        int currentTime = 0;
        int completeProcessCount = 0 ;
        int processIndex = 0 ;

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        originalProcesses.sort(Comparator.comparing(ProcessEntity::getArrivalTime));

        PriorityQueue<ProcessEntity> readyQueue = new PriorityQueue<>(
                Comparator.comparing(ProcessEntity::getBurstTime)
                        .thenComparing(ProcessEntity::getArrivalTime)
                        .thenComparing(ProcessEntity::getId)

        );

        while(completeProcessCount < originalProcesses.size()){
            while(processIndex < originalProcesses.size() && originalProcesses.get(processIndex).getArrivalTime() <= currentTime){
                readyQueue.add(originalProcesses.get(processIndex));
                processIndex++;
            }
            if(readyQueue.isEmpty()){
                if(processIndex< originalProcesses.size()){
                    int idealStartTime = currentTime ;
                    currentTime = originalProcesses.get(processIndex).getArrivalTime();

                    if(currentTime > idealStartTime){
                        ganttChartData.add(new GanttChart(-1 , idealStartTime , currentTime));
                    }
                }else {
                    break;
                }
            }else {
                ProcessEntity currentProcess = readyQueue.poll();

                currentProcess.setStartTime(currentTime);

                int completionTime = currentTime + currentProcess.getBurstTime();
                currentProcess.setCompletionTime(completionTime);

                int turnaroundTime = completionTime - currentProcess.getArrivalTime();
                currentProcess.setTurnaroundTime(turnaroundTime);
                totalTurnaroundTime += turnaroundTime;

                int waitingTime = turnaroundTime - currentProcess.getBurstTime();
                currentProcess.setWaitingTime(waitingTime);
                totalWaitingTime += waitingTime;

                currentTime = completionTime;

                ganttChartData.add(new GanttChart(currentProcess.getId() , currentProcess.getStartTime() , currentProcess.getCompletionTime()));

                ScheduledProcesses.add(currentProcess);
                completeProcessCount++;

            }

        }

        ScheduledProcesses.sort(Comparator.comparing(ProcessEntity::getId));
        double averageWaitingTime = ScheduledProcesses.isEmpty() ? 0 : totalWaitingTime / ScheduledProcesses.size();
        double averageTurnaroundTime = ScheduledProcesses.isEmpty() ? 0 : totalTurnaroundTime / ScheduledProcesses.size();

        return new SchedulerResult(ScheduledProcesses , ganttChartData , averageWaitingTime , averageTurnaroundTime);

    }
}
