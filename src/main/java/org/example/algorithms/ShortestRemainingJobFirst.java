package org.example.algorithms;

import org.example.model.GanttChart;
import org.example.model.ProcessEntity;
import org.example.model.SchedulerResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class ShortestRemainingJobFirst {
    public SchedulerResult schedule(List<ProcessEntity> processes) {

        List<ProcessEntity> originalProcesses = new ArrayList<>();
        for(ProcessEntity process : processes){
            originalProcesses.add(new ProcessEntity(process.getId(), process.getArrivalTime(), process.getBurstTime()));
        }

        List<ProcessEntity> completedProcesses = new ArrayList<>();
        List<GanttChart> ganttChartData = new ArrayList<>();
        int currentTime = 0 ;
        int processIndex = 0 ;
        ProcessEntity currentlyRunningProcess = null ;
        int segmentStartTime = 0;
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        originalProcesses.sort(Comparator.comparing(ProcessEntity::getArrivalTime));

        PriorityQueue<ProcessEntity> readyQueue = new PriorityQueue<>(
                Comparator.comparing(ProcessEntity::getRemainingBurstTime)
                        .thenComparing(ProcessEntity::getArrivalTime)
                        .thenComparing(ProcessEntity::getId)
        );
        while (completedProcesses.size() < originalProcesses.size() || !readyQueue.isEmpty() || currentlyRunningProcess != null){
            while(processIndex < originalProcesses.size() && originalProcesses.get(processIndex).getArrivalTime() <= currentTime){
                readyQueue.add(originalProcesses.get(processIndex));
                processIndex++;
            }
            ProcessEntity nextProcess = null;
             if(currentlyRunningProcess != null && currentlyRunningProcess.getRemainingBurstTime()==0){
                 currentlyRunningProcess = null ;
             }

             if(currentlyRunningProcess == null){
                 //CPU is idle
                 if(!readyQueue.isEmpty()){
                     nextProcess = readyQueue.poll();
                 }
             }else {
                 //CPU is currently running
                 if(!readyQueue.isEmpty() && readyQueue.peek().getRemainingBurstTime() < currentlyRunningProcess.getRemainingBurstTime()){
                     nextProcess = readyQueue.poll();
                     readyQueue.add(currentlyRunningProcess);
                 }else {
                     nextProcess = currentlyRunningProcess;
                 }

             }

        }


    }
}
