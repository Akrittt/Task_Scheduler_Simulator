package org.example.algorithms;

import org.example.model.GanttChart;
import org.example.model.ProcessEntity;
import org.example.model.SchedulerResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PriorityPreemptive {
    public SchedulerResult schedule(List<ProcessEntity> processes) {
        // Create a deep copy of processes to avoid modifying the original list
        List<ProcessEntity> originalProcesses = new ArrayList<>();
        for (ProcessEntity p : processes) {
            originalProcesses.add(new ProcessEntity(p.getId(), p.getArrivalTime(), p.getBurstTime(), p.getPriority()));
        }

        // Sort processes by arrival time
        originalProcesses.sort(Comparator.comparingInt(ProcessEntity::getArrivalTime));

        // Priority queue for ready processes (lower number = higher priority)
        PriorityQueue<ProcessEntity> readyQueue = new PriorityQueue<>(
            Comparator.comparingInt(ProcessEntity::getPriority)
                    .thenComparing(ProcessEntity::getArrivalTime)
                    .thenComparing(ProcessEntity::getId)
        );

        List<GanttChart> ganttChartData = new ArrayList<>();
        List<ProcessEntity> completedProcesses = new ArrayList<>();
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        
        int currentTime = 0;
        int processIndex = 0;
        ProcessEntity currentlyRunningProcess = null;
        int segmentStartTime = 0;

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
                if(!readyQueue.isEmpty() && readyQueue.peek().getPriority() < currentlyRunningProcess.getPriority()){
                    nextProcess = readyQueue.poll();
                    readyQueue.add(currentlyRunningProcess);
                }else {
                    nextProcess = currentlyRunningProcess;
                }

            }
            //update of chantt chart
            if (nextProcess != null) {
                // no process currently running or the process has changed or the vry first segment being recorded
                if (currentlyRunningProcess == null || nextProcess.getId() != currentlyRunningProcess.getId() || segmentStartTime == currentTime) {
                    // If there was a previous segment, close it
                    if (currentlyRunningProcess != null && segmentStartTime < currentTime) {
                        ganttChartData.add(new GanttChart(currentlyRunningProcess.getId(), segmentStartTime, currentTime));
                    } else if (currentlyRunningProcess == null && segmentStartTime < currentTime) {
                        // CPU was idle, and now a process starts
                        ganttChartData.add(new GanttChart(-1, segmentStartTime, currentTime)); // Record idle time
                    }
                    segmentStartTime = currentTime; // Start new segment
                }

                currentlyRunningProcess = nextProcess; // Update currently running process

                // Set start time for the process if it's running for the first time
                currentlyRunningProcess.setStartTime(currentTime);
                currentlyRunningProcess.decrementRemainingBurstTime();

                currentTime++; //for every time unit increasing the current time

                // If process completes after this time unit
                if (currentlyRunningProcess.getRemainingBurstTime() == 0) {
                    // Close the current Gantt chart segment
                    ganttChartData.add(new GanttChart(currentlyRunningProcess.getId(), segmentStartTime, currentTime));

                    // Calculate final metrics for the completed process
                    currentlyRunningProcess.setCompletionTime(currentTime);
                    int turnaroundTime = currentlyRunningProcess.getCompletionTime() - currentlyRunningProcess.getArrivalTime();
                    currentlyRunningProcess.setTurnaroundTime(turnaroundTime);
                    totalTurnaroundTime += turnaroundTime;

                    int waitingTime = turnaroundTime - currentlyRunningProcess.getBurstTime();
                    currentlyRunningProcess.setWaitingTime(waitingTime);
                    totalWaitingTime += waitingTime;

                    completedProcesses.add(currentlyRunningProcess);

                    currentlyRunningProcess = null; // CPU is now free
                    segmentStartTime = currentTime; // Reset segment start time for next segment
                }

            }
            else {
                // CPU is idle
                if (segmentStartTime < currentTime) {
                    // If the previous segment was a process, close it before starting idle
                    // This handles cases where CPU becomes idle after a process completes
                    // and there are no new processes ready immediately.
                    if (currentlyRunningProcess != null) {
                        ganttChartData.add(new GanttChart(currentlyRunningProcess.getId(), segmentStartTime, currentTime));
                    }
                }
                // Record idle time, or advance time if no processes have arrived yet
                int nextArrivalTime = Integer.MAX_VALUE;
                if (processIndex < originalProcesses.size()) {
                    nextArrivalTime = originalProcesses.get(processIndex).getArrivalTime();
                }

                if (nextArrivalTime == Integer.MAX_VALUE && readyQueue.isEmpty() && currentlyRunningProcess == null) {
                    // All processes processed and no more to arrive.
                    break;
                }

                if (currentTime < nextArrivalTime) {
                    // Only add idle segment if there's actual idle time
                    if (segmentStartTime < nextArrivalTime) {
                        ganttChartData.add(new GanttChart(-1, currentTime, nextArrivalTime));
                    }
                    currentTime = nextArrivalTime;
                    segmentStartTime = currentTime; // Reset segment start time after idle
                } else {
                    // This means currentTime is already >= nextArrivalTime,
                    // but readyQueue is empty and no process is running.
                    // This could happen if a process arrives at currentTime but has 0 burst time,
                    // or if there's a logic error, but for now, just advance time.
                    currentTime++;
                }
            }

        }

        // Sort the final completedProcesses list by ID for consistent output
        completedProcesses.sort(Comparator.comparing(ProcessEntity::getId));

        // Calculate Averages
        double averageWaitingTime = completedProcesses.isEmpty() ? 0 : totalWaitingTime / completedProcesses.size();
        double averageTurnaroundTime = completedProcesses.isEmpty() ? 0 : totalTurnaroundTime / completedProcesses.size();

        return new SchedulerResult(completedProcesses, ganttChartData, averageWaitingTime, averageTurnaroundTime);




    }
}
