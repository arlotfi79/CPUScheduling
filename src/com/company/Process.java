package com.company;

import com.company.Queue.Queue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Process {
    private int processID;
    private int arrivalTime;
    private int burstTime1;
    private int ioTime;
    private int burstTime2;

    private int waitingTime;
    private int responseTime;
    private int turnAroundTime;
    private int startTime;

    // helper attributes
    private ProcessSituation runningProcessSituation;
    private int remainingWorkTime; // SJF & RR
    private int currentTimeQuantum;
    private int finishTime;
    private int lastUsed;

    public Process(int processID, int arrivalTime, int burstTime1, int ioTime, int burstTime2, ProcessSituation runningProcessSituation) {
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.burstTime1 = burstTime1;
        this.ioTime = ioTime;
        this.burstTime2 = burstTime2;
        this.runningProcessSituation = runningProcessSituation;
    }

    public static void sortProcessByArrivalTime(List<Process> processes) {
        processes.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                return Integer.compare(p1.arrivalTime, p2.arrivalTime);
            }
        });
    }

    public static void sortProcessByRemainingTime(Queue<Process> readyQueue) {
        var processes = new ArrayList<Process>();

        while (!readyQueue.isEmpty())
            processes.add(readyQueue.dequeue());

        processes.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                return Integer.compare(p1.getRemainingWorkTime(), p2.getRemainingWorkTime());
            }
        });

        while (!processes.isEmpty())
            readyQueue.enqueue(processes.remove(0));

    }

    @Override
    public String toString() {
        return "Process{" +
                "processID=" + processID +
                ", responseTime=" + responseTime +
                ", turnAroundTime=" + turnAroundTime +
                ", waitingTime=" + waitingTime +
                '}';
    }

    public int getProcessID() {
        return processID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime1() {
        return burstTime1;
    }

    public int getIoTime() {
        return ioTime;
    }

    public int getBurstTime2() {
        return burstTime2;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public ProcessSituation getRunningProcessSituation() {
        return runningProcessSituation;
    }

    public void setRunningProcessSituation(ProcessSituation runningProcessSituation) {
        this.runningProcessSituation = runningProcessSituation;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(int lastUsed) {
        this.lastUsed = lastUsed;
    }

    public int getRemainingWorkTime() {
        return remainingWorkTime;
    }

    public void setRemainingWorkTime(int remainingWorkTime) {
        this.remainingWorkTime = remainingWorkTime;
    }

    public int getCurrentTimeQuantum() {
        return currentTimeQuantum;
    }

    public void setCurrentTimeQuantum(int currentTimeQuantumLeft) {
        this.currentTimeQuantum = currentTimeQuantumLeft;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
}
