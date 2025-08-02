package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessEntity {

    private int id;
    private int arrivalTime;
    private int burstTime;
    private int completionTime;
    private int turnaroundTime;
    private int waitingTime;
    private int startTime;
    private int remainingBurstTime;
    private int priority;

    public ProcessEntity(int id, int arrivalTime, int burstTime){
        this.id = id;
        this.arrivalTime = arrivalTime ;
        this.burstTime = burstTime;
        this.completionTime = -1 ;
        this.turnaroundTime = -1 ;
        this.waitingTime = -1 ;
        this.startTime = -1 ;
        this.remainingBurstTime = burstTime ;
    }

    public ProcessEntity(int id, int arrivalTime, int burstTime, int priority){
        this.id = id;
        this.arrivalTime = arrivalTime ;
        this.burstTime = burstTime;
        this.priority = priority;
        this.completionTime = -1 ;
        this.turnaroundTime = -1 ;
        this.waitingTime = -1 ;
        this.startTime = -1 ;
        this.remainingBurstTime = burstTime ;
    }

    public void decrementRemainingBurstTime() {
        if (this.remainingBurstTime> 0) {
            this.remainingBurstTime--;
        }
    }



}
