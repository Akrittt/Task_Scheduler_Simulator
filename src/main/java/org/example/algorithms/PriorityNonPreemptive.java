package org.example.algorithms;

import org.example.model.GanttChart;
import org.example.model.ProcessEntity;
import org.example.model.SchedulerResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PriorityNonPreemptive {

    public SchedulerResult schedule (List<ProcessEntity> processes){

        //copy of original list
        List<ProcessEntity> scheduledProcesses = new ArrayList<>();
        for(ProcessEntity process : processes ){
            scheduledProcesses.add(new ProcessEntity(
                    process.getId(),
                    process.getArrivalTime(),
                    process.getBurstTime(),
                    process.getPriority()));
        }

        scheduledProcesses.sort(Comparator.comparing(ProcessEntity::getArrivalTime)
                .thenComparing(ProcessEntity::getId)
        );

        int currentTime = 0 ;
        int processIndex = 0;
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        List<ProcessEntity> completedProcesses = new ArrayList<>();
        List<GanttChart> ganttChartData = new ArrayList<>();

        PriorityQueue<ProcessEntity> readyQueue = new PriorityQueue<>(Comparator.comparing(ProcessEntity::getPriority)
                .thenComparing(ProcessEntity::getArrivalTime)
                .thenComparing(ProcessEntity::getId)
        );

        while (!readyQueue.isEmpty() || processIndex < scheduledProcesses.size()) {
            // Add all processes that have arrived by currentTime
            while (processIndex < scheduledProcesses.size() && 
                   scheduledProcesses.get(processIndex).getArrivalTime() <= currentTime) {
                readyQueue.add(scheduledProcesses.get(processIndex));
                processIndex++;
            }
            
            // If no processes are ready but we still have processes to schedule
            if (readyQueue.isEmpty() && processIndex < scheduledProcesses.size()) {
                // Fast-forward time to the next process's arrival time
                currentTime = scheduledProcesses.get(processIndex).getArrivalTime();
                continue;
            }

            if(!readyQueue.isEmpty()){
                ProcessEntity currentProcess = readyQueue.poll();

                if(currentTime < currentProcess.getArrivalTime()){
                    ganttChartData.add(new GanttChart(-1,currentTime,currentProcess.getArrivalTime()));
                    currentTime = currentProcess.getArrivalTime();
                }

                int startTime = currentTime;
                currentProcess.setStartTime(startTime);

                int completionTime = startTime + currentProcess.getBurstTime();
                currentProcess.setCompletionTime(completionTime);

                int turnaroundTime = completionTime - currentProcess.getArrivalTime();
                currentProcess.setTurnaroundTime(turnaroundTime);
                totalTurnaroundTime += turnaroundTime;

                int waitingTime = turnaroundTime - currentProcess.getBurstTime();
                currentProcess.setWaitingTime(waitingTime);
                totalWaitingTime += waitingTime;

                // Update the current time for the next process
                currentTime = completionTime;

                // Add to Gantt chart and list of completed processes
                ganttChartData.add(new GanttChart(currentProcess.getId(), startTime, completionTime));
                completedProcesses.add(currentProcess);
            }else{
                //queue is empty
                if(processIndex < scheduledProcesses.size()){
                    int nextArrivalTime = scheduledProcesses.get(processIndex).getArrivalTime();
                    ganttChartData.add(new GanttChart(-1,currentTime,scheduledProcesses.get(processIndex).getArrivalTime()));
                    currentTime = nextArrivalTime;
                }else {
                    break; // empty queue and processIndex is greater than total processes
                }
            }


        }
        completedProcesses.sort(Comparator.comparing(ProcessEntity::getId));

        double averageWaitingTime = completedProcesses.isEmpty()? 0 : totalWaitingTime / scheduledProcesses.size();
        double averageTurnaroundTime = completedProcesses.isEmpty()? 0 : totalTurnaroundTime / scheduledProcesses.size();

        return new SchedulerResult(completedProcesses , ganttChartData  ,averageTurnaroundTime,averageWaitingTime);


    }
}
