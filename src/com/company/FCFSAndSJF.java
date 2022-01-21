package com.company;

import com.company.Queue.Queue;

import java.util.List;

public class FCFSAndSJF {
    private int totalTime;
    private int idleTime;

    public int getTotalTime() {
        return totalTime;
    }

    public int getIdleTime() {
        return idleTime;
    }

    private Queue newQueue = new Queue<Process>(5);
    private Queue readyQueue = new Queue<Process>(5);
    private Queue runningQueue = new Queue<Process>(1);
    private Queue ioQueue = new Queue<Process>(5);
    private Queue finishedQueue = new Queue<Process>(5);

    private void addingNewProcessesToReadyQueue(int counter) {
        while(true){
            if (!newQueue.isEmpty() && ((Process) newQueue.first()).getArrivalTime() <= counter) {
                var process = (Process) newQueue.dequeue();
                process.setRemainingWorkTime(process.getBurstTime1()); // for SJF
                readyQueue.enqueue(process);
            }
            else
                break;
        }
    }

    private void stopProcessOrEnterIo(int counter) {
        if (!runningQueue.isEmpty()) {
            if (((Process) runningQueue.first()).getFinishTime() == counter) {
                var process = (Process) runningQueue.dequeue();

                switch (process.getRunningProcessSituation()) {
                    case CPU1 -> {
                        ioQueue.enqueue(process);
                        process.setRunningProcessSituation(ProcessSituation.IO);
                        process.setFinishTime(counter + process.getIoTime());
                        process.setLastUsed(counter + process.getIoTime());
                    }
                    case CPU2 -> {
                        finishedQueue.enqueue(process);
                        process.setRunningProcessSituation(ProcessSituation.FINISHED);
                        process.setTurnAroundTime(counter);
                    }
                    default -> throw new IllegalArgumentException("process is not in not running in burst mode");
                }
            }
        }
    }

    private void checkIfIoIsFinished(int counter) {
        while (true){
            if (!ioQueue.isEmpty() && ((Process) ioQueue.first()).getFinishTime() == counter){
                var process = (Process) ioQueue.dequeue();

                if (process.getRunningProcessSituation().equals(ProcessSituation.IO)){
                    process.setRemainingWorkTime(process.getBurstTime2()); // for SJF
                    readyQueue.enqueue(process);
                }
            }
            else
                break;
        }
    }

    private void checkWhichCpuBurstTimeToRun(int counter) {
        if (runningQueue.isEmpty() && !readyQueue.isEmpty()) {
            var process = (Process) readyQueue.dequeue();

            switch (process.getRunningProcessSituation()) {
                case FirstTime -> {
                    runningQueue.enqueue(process);
                    process.setRunningProcessSituation(ProcessSituation.CPU1);
                    process.setFinishTime(counter + process.getBurstTime1());
                    process.setResponseTime(counter - process.getArrivalTime());
                    process.setWaitingTime(counter - process.getArrivalTime());
                }

                case IO -> {
                    runningQueue.enqueue(process);
                    process.setRunningProcessSituation(ProcessSituation.CPU2);
                    process.setFinishTime(counter + process.getBurstTime2());
                    process.setWaitingTime(process.getWaitingTime() + counter - process.getLastUsed());
                }

                default -> throw new IllegalArgumentException("process not in a situation to be in CPU burst 1 or 2");
            }
        }
    }

    private boolean isFinished() {
        return readyQueue.isEmpty() && runningQueue.isEmpty() && ioQueue.isEmpty() && newQueue.isEmpty();
    }


    public Queue<Process> schedule(List<Process> processes, Algorithms algorithm) {
        Process.sortProcessByArrivalTime(processes);

        for (Process process : processes) {
            newQueue.enqueue(process);
        }

        int counter = 0; // works as the current time of the program
        while (true) {

            // 1. adding to ready queue
            addingNewProcessesToReadyQueue(counter);

            // 2. stopping process or entering IO
            stopProcessOrEnterIo(counter);

            // 3. checking if we need to put it on ready queue
            checkIfIoIsFinished(counter);

            // 4. checking to run which CPU burst time
            // if SJF -> we should order the queue based on minimum remaining time for each process
            if (readyQueue.size() > 1 && algorithm.equals(Algorithms.SJF))
                Process.sortProcessByRemainingTime(readyQueue);
            checkWhichCpuBurstTimeToRun(counter);

            // 5. check if we have a running process or not
            if (runningQueue.isEmpty())
                idleTime++;

            counter++;

            if (isFinished()) {
                // decrease by 1 because it's counting the last second too
                totalTime = --counter;
                idleTime--;
                break;
            }

        }

        return finishedQueue;
    }
}
