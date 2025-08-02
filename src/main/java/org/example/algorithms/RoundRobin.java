package org.example.algorithms;

import org.example.model.GanttChart;
import org.example.model.ProcessEntity;
import org.example.model.SchedulerResult;

import java.util.*;

public class RoundRobin {

    public SchedulerResult schedule(List<ProcessEntity> processes, int timeQuantum) {
        // Create a deep copy of processes to work with
        List<ProcessEntity> originalProcesses = new ArrayList<>();
        for (ProcessEntity process : processes) {
            originalProcesses.add(new ProcessEntity(process.getId(), process.getArrivalTime(), process.getBurstTime()));
        }

        // List to store processes once they are fully scheduled
        List<ProcessEntity> completedProcesses = new ArrayList<>();
        // List to store Gantt chart entries
        List<GanttChart> ganttChartData = new ArrayList<>();

        // Sort processes by arrival time to process them chronologically
        originalProcesses.sort(Comparator.comparing(ProcessEntity::getArrivalTime)
                .thenComparing(ProcessEntity::getId));

        // Queue to hold processes that have arrived and are ready to be executed
        Queue<ProcessEntity> readyQueue = new LinkedList<>();

        int currentTime = 0;
        int processIndex = 0; // Index for iterating through unarrived processes
        int completedCount = 0;

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        // Loop until all processes are completed
        while (completedCount < originalProcesses.size()) {
            // Add processes that have arrived by the current time to the ready queue
            while (processIndex < originalProcesses.size() && originalProcesses.get(processIndex).getArrivalTime() <= currentTime) {
                readyQueue.add(originalProcesses.get(processIndex));
                processIndex++;
            }

            if (!readyQueue.isEmpty()) {
                // Dequeue the process at the front of the ready queue
                ProcessEntity currentProcess = readyQueue.poll();

                // Set the start time if this is the first time the process is running
                currentProcess.setStartTime(currentTime);

                // Determine how long the process will run
                int runTime = Math.min(currentProcess.getRemainingBurstTime(), timeQuantum);

                // Add a new Gantt chart entry for this time slice
                ganttChartData.add(new GanttChart(currentProcess.getId(), currentTime, currentTime + runTime));

                // Decrement the remaining burst time
                currentProcess.decrementRemainingBurstTime(runTime);

                // Update the current time
                currentTime += runTime;

                // Add any new processes that arrived while the current process was running
                while (processIndex < originalProcesses.size() && originalProcesses.get(processIndex).getArrivalTime() <= currentTime) {
                    readyQueue.add(originalProcesses.get(processIndex));
                    processIndex++;
                }

                // Check if the process has finished
                if (currentProcess.getRemainingBurstTime() == 0) {
                    // Calculate and set final metrics
                    currentProcess.setCompletionTime(currentTime);
                    int turnaroundTime = currentProcess.getCompletionTime() - currentProcess.getArrivalTime();
                    currentProcess.setTurnaroundTime(turnaroundTime);
                    totalTurnaroundTime += turnaroundTime;

                    int waitingTime = turnaroundTime - currentProcess.getBurstTime();
                    currentProcess.setWaitingTime(waitingTime);
                    totalWaitingTime += waitingTime;

                    completedProcesses.add(currentProcess);
                    completedCount++;
                } else {
                    // If not finished, add the process back to the end of the ready queue
                    readyQueue.add(currentProcess);
                }
            } else {
                // If the ready queue is empty, but not all processes have arrived, the CPU is idle.
                // Advance time to the next process's arrival time.
                if (processIndex < originalProcesses.size()) {
                    int nextArrivalTime = originalProcesses.get(processIndex).getArrivalTime();
                    ganttChartData.add(new GanttChart(-1, currentTime, nextArrivalTime));
                    currentTime = nextArrivalTime;
                } else {
                    // No more processes to arrive and ready queue is empty. Break the loop.
                    break;
                }
            }
        }

        // Sort the final completed processes by ID for consistent output
        completedProcesses.sort(Comparator.comparing(ProcessEntity::getId));

        // Calculate averages
        double averageWaitingTime = completedProcesses.isEmpty() ? 0 : totalWaitingTime / completedProcesses.size();
        double averageTurnaroundTime = completedProcesses.isEmpty() ? 0 : totalTurnaroundTime / completedProcesses.size();

        return new SchedulerResult(completedProcesses, ganttChartData, averageWaitingTime, averageTurnaroundTime);
    }
}