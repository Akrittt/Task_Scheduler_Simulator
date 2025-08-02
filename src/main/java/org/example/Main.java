package org.example;

import org.example.algorithms.*;
import org.example.model.ProcessEntity;
import org.example.model.SchedulerResult;
import org.example.utils.GanttChartPrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        List<ProcessEntity> processes =new ArrayList<>(Arrays.asList(
                new ProcessEntity(1,0,2),
                new ProcessEntity(2,1,4),
                new ProcessEntity(3,7,9),
                new ProcessEntity(4,8,3),
                new ProcessEntity(5,8,2)
        ));

        System.out.println("\n Initial process:");
        System.out.printf("%-5s %-12s %-10s\n", "ID" , "ARRIVAL TIME" , "BURST TIME");
        for(ProcessEntity process : processes){
            System.out.printf("%-5d %-12d %-10d \n", process.getId() , process.getArrivalTime() , process.getBurstTime());
        }

        //  FIRST COME FIRST SERVE
        FirstComeFirstServe fcfs = new FirstComeFirstServe();
        SchedulerResult resultfcfs = fcfs.schedule(processes);

        System.out.println("\n--- Scheduling Results for FCFS ---");
        System.out.printf("%-5s %-12s %-10s %-15s %-15s %-12s %-12s\n",
                "ID", "ARRIVAL", "BURST", "START TIME", "COMPLETION", "TURNAROUND", "WAITING");
        for (ProcessEntity p : resultfcfs.getScheduleProcesses()) {
            System.out.printf("%-5d %-12d %-10d %-15d %-15d %-12d %-12d\n",
                    p.getId(), p.getArrivalTime(), p.getBurstTime(),
                    p.getStartTime(), p.getCompletionTime(),
                    p.getTurnaroundTime(), p.getWaitingTime());
        }

        System.out.println("\nAverage Waiting Time: " + String.format("%.2f", resultfcfs.getAverageWaitingTime()));
        System.out.println("Average Turnaround Time: " + String.format("%.2f", resultfcfs.getAverageTurnaroundTime()));

        GanttChartPrinter ganttPrinter = new GanttChartPrinter();
        ganttPrinter.printGanttChart(resultfcfs.getGanttChartData());

        //SHORTEST JOB FIRST (NON-PREEMPTIVE)
        ShortestJobFirst sjfnp = new ShortestJobFirst();
        SchedulerResult resultsjfnp = sjfnp.schedule(processes);
        System.out.println("\n--- Scheduling Results for SJF (NON-PREEMPTIVE) ---");
        System.out.printf("%-5s %-12s %-10s %-15s %-15s %-12s %-12s\n",
                "ID", "Arrival", "Burst", "Start Time", "Completion", "Turnaround", "Waiting");
        for (ProcessEntity p : resultsjfnp.getScheduleProcesses()) {
            System.out.printf("%-5d %-12d %-10d %-15d %-15d %-12d %-12d\n",
                    p.getId(), p.getArrivalTime(), p.getBurstTime(),
                    p.getStartTime(), p.getCompletionTime(),
                    p.getTurnaroundTime(), p.getWaitingTime());
        }

        System.out.println("\nAverage Waiting Time: " + String.format("%.2f", resultsjfnp.getAverageWaitingTime()));
        System.out.println("Average Turnaround Time: " + String.format("%.2f", resultsjfnp.getAverageTurnaroundTime()));

        // Print Gantt Chart
        GanttChartPrinter ganttPrinter1 = new GanttChartPrinter();
        ganttPrinter1.printGanttChart(resultsjfnp.getGanttChartData());

        //SHORTEST JOB FIRST(PREEMPTIVE)
        ShortestRemainingJobFirst sjf = new ShortestRemainingJobFirst();
        SchedulerResult resultsjf = sjf.schedule(processes);
        System.out.println("\n--- Scheduling Results for SJF (PREEMPTIVE) OR SRJF ---");
        System.out.printf("%-5s %-12s %-10s %-15s %-15s %-12s %-12s\n",
                "ID", "Arrival", "Burst", "Start Time", "Completion", "Turnaround", "Waiting");
        for (ProcessEntity p : resultsjf.getScheduleProcesses()) {
            System.out.printf("%-5d %-12d %-10d %-15d %-15d %-12d %-12d\n",
                    p.getId(), p.getArrivalTime(), p.getBurstTime(),
                    p.getStartTime(), p.getCompletionTime(),
                    p.getTurnaroundTime(), p.getWaitingTime());
        }

        System.out.println("\nAverage Waiting Time: " + String.format("%.2f", resultsjf.getAverageWaitingTime()));
        System.out.println("Average Turnaround Time: " + String.format("%.2f", resultsjf.getAverageTurnaroundTime()));

        // Print Gantt Chart
        GanttChartPrinter ganttPrinter2 = new GanttChartPrinter();
        ganttPrinter2.printGanttChart(resultsjf.getGanttChartData());


        //PRIORITY SCHEDULING

        ArrayList<ProcessEntity> processesPriotrity = new ArrayList<>(Arrays.asList(
                new ProcessEntity(1, 0, 8, 3),
                new ProcessEntity(2, 1, 4, 1),
                new ProcessEntity(3, 2, 9, 4),
                new ProcessEntity(4, 3, 5, 2)
        ));

        //NON PREEMPTIVE
        System.out.println("\nInitial Processes:");
        System.out.printf("%-5s %-12s %-10s %-8s\n", "ID", "Arrival Time", "Burst Time", "Priority");
        for (ProcessEntity p : processesPriotrity) {
            System.out.printf("%-5d %-12d %-10d %-8d\n", p.getId(), p.getArrivalTime(), p.getBurstTime(), p.getPriority());
        }

        // 2. Instantiate Priority Scheduler
        PriorityNonPreemptive nonPreemptive = new PriorityNonPreemptive();

        // 3. Run Scheduling
        SchedulerResult result = nonPreemptive.schedule(processesPriotrity);

        // 4. Print Results
        System.out.println("\n--- Scheduling Results FOR PRIORITY(NON PREEMPTIVE) ---");
        System.out.printf("%-5s %-12s %-10s %-8s %-15s %-15s %-12s %-12s\n",
                "ID", "Arrival", "Burst", "Priority", "Start Time", "Completion", "Turnaround", "Waiting");
        for (ProcessEntity p : result.getScheduleProcesses()) {
            System.out.printf("%-5d %-12d %-10d %-8d %-15d %-15d %-12d %-12d\n",
                    p.getId(), p.getArrivalTime(), p.getBurstTime(), p.getPriority(),
                    p.getStartTime(), p.getCompletionTime(),
                    p.getTurnaroundTime(), p.getWaitingTime());
        }

        System.out.println("\nAverage Waiting Time: " + String.format("%.2f", result.getAverageWaitingTime()));
        System.out.println("Average Turnaround Time: " + String.format("%.2f", result.getAverageTurnaroundTime()));

        // Print Gantt Chart
        GanttChartPrinter ganttPrinter3 = new GanttChartPrinter();
        ganttPrinter3.printGanttChart(result.getGanttChartData());

        //PREEMPTIVE

        // Instantiate Priority Scheduler
        PriorityPreemptive Preemptive = new PriorityPreemptive();

        // Run Scheduling
        SchedulerResult result2 = Preemptive.schedule(processesPriotrity);

        // Print Results
        System.out.println("\n--- Scheduling Results FOR PRIORITY(PREEMPTIVE) ---");
        System.out.printf("%-5s %-12s %-10s %-8s %-15s %-15s %-12s %-12s\n",
                "ID", "Arrival", "Burst", "Priority", "Start Time", "Completion", "Turnaround", "Waiting");
        for (ProcessEntity p : result2.getScheduleProcesses()) {
            System.out.printf("%-5d %-12d %-10d %-8d %-15d %-15d %-12d %-12d\n",
                    p.getId(), p.getArrivalTime(), p.getBurstTime(), p.getPriority(),
                    p.getStartTime(), p.getCompletionTime(),
                    p.getTurnaroundTime(), p.getWaitingTime());
        }

        System.out.println("\nAverage Waiting Time: " + String.format("%.2f", result2.getAverageWaitingTime()));
        System.out.println("Average Turnaround Time: " + String.format("%.2f", result2.getAverageTurnaroundTime()));

        // Print Gantt Chart
        GanttChartPrinter ganttPrinter4 = new GanttChartPrinter();
        ganttPrinter4.printGanttChart(result2.getGanttChartData());

    }



}