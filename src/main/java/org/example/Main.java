package org.example;

import org.example.algorithms.*;
import org.example.model.ProcessEntity;
import org.example.model.SchedulerResult;
import org.example.utils.GanttChartPrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // --- Main Menu Loop ---
        while(true){
            System.out.println("\n--- CPU Scheduling Simulator ---");
            System.out.println("1. First-Come, First-Served (FCFS)");
            System.out.println("2. Shortest Job First ");
            System.out.println("3. Shortest Remaining Job First ");
            System.out.println("4. Preemptive Priority");
            System.out.println("5. Non Preemptive Priority");
            System.out.println("6. Round Robin");
            System.out.println("7. Exit");
            System.out.print("Select an algorithm: ");

            try{
                int choice = sc.nextInt();
                if (choice < 1 || choice > 7) {
                    System.out.println("Invalid choice. Please enter a number from 1 to 5.");
                    continue;
                }

                if(choice == 7){
                    System.out.println("Exiting scheduler. Goodbye!!!");
                    break;
                }

                sc.nextLine();
                System.out.print("Enter the number of processes: ");
                int numProcesses = sc.nextInt();
                List<ProcessEntity> processes = new ArrayList<>();
                for (int i = 0; i < numProcesses; i++) {
                    System.out.println("\nEnter details for Process " + (i + 1) + ":");
                    System.out.print("  Arrival Time: ");
                    int arrivalTime = sc.nextInt();
                    System.out.print("  Burst Time: ");
                    int burstTime = sc.nextInt();

                    int priority = 0;
                    if (choice == 4) {
                        System.out.print("  Priority (lower number = higher priority): ");
                        priority = sc.nextInt();
                    }
                    processes.add(new ProcessEntity(i + 1, arrivalTime, burstTime, priority));

                }
                // --- Run Selected Algorithm ---
                SchedulerResult result = null;
                switch (choice){
                    case 1:
                        FirstComeFirstServe fcfsScheduler = new FirstComeFirstServe();
                        result = fcfsScheduler.schedule(processes);
                        System.out.println("\n--- First Come First Serve Scheduling Results ---");
                        break;
                    case 2:
                        ShortestJobFirst sjfScheduler = new ShortestJobFirst();
                        result = sjfScheduler.schedule(processes);
                        System.out.println("\n--- Shortest Job First Scheduling Results ---");
                        break;
                    case 3:
                        ShortestRemainingJobFirst srjfScheduler = new ShortestRemainingJobFirst();
                        result = srjfScheduler.schedule(processes);
                        System.out.println("\n--- Shortest Remaining Job First Scheduling Results ---");
                        break;
                    case 4:
                        PriorityPreemptive priorityScheduler = new PriorityPreemptive();
                        result = priorityScheduler.schedule(processes);
                        System.out.println("\n--- Priority Preemptive Scheduling Results ---");
                        break;
                    case 5:
                        PriorityNonPreemptive pnpScheduler = new PriorityNonPreemptive();
                        result = pnpScheduler.schedule(processes);
                        System.out.println("\n--- Priority Non Preemptive Scheduling Results ---");
                        break;
                    case 6:
                        System.out.print("Enter the time quantum: ");
                        int timeQuantum = sc.nextInt();
                        RoundRobin rrScheduler = new RoundRobin();
                        result = rrScheduler.schedule(processes , timeQuantum);
                        System.out.println("\n--- Round Robin Scheduling Results ---");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }
                // --- Print Results ---
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

                GanttChartPrinter ganttPrinter = new GanttChartPrinter();
                ganttPrinter.printGanttChart(result.getGanttChartData());
            }catch (java.util.InputMismatchException e){
                System.out.println("Invalid input. Please enter integer values only. ");
                sc.nextLine();
            }
        }
        sc.close();


    }



}