package com.company;

import com.company.Queue.Queue;

import java.util.List;

public class RoundRobin extends AbstractBaseScheduler{

    private Queue transferToNextQueue = new Queue<Process>(5);

    public Queue getTransferToNextQueue() {
        return transferToNextQueue;
    }

    private void addingNewProcessesToReadyQueue(int counter) {
        while (true) {
            if (!newQueue.isEmpty() && ((Process) newQueue.first()).getArrivalTime() == counter) {
                var process = (Process) newQueue.dequeue();
                process.setRemainingWorkTime(process.getBurstTime1()-1);
                readyQueue.enqueue(process);
            } else
                break;
        }
    }

    private void stopProcessOrEnterIo(int counter, Algorithms algorithm, int timeQuantum) {
        if (!runningQueue.isEmpty()) {

            if (((Process) runningQueue.first()).getRemainingWorkTime() == 0){
                var process = (Process) runningQueue.dequeue();

                switch (process.getRunningProcessSituation()) {
                    case CPU1 -> {
                        process.setRunningProcessSituation(ProcessSituation.IO);
                        process.setFinishTime(counter + process.getIoTime());
                        process.setLastUsed(counter + process.getIoTime());
                        ioQueue.enqueue(process);
                        break;
                    }
                    case CPU2 -> {
                        process.setRunningProcessSituation(ProcessSituation.FINISHED);
                        process.setEndTime(counter);
                        finishedQueue.enqueue(process);
                        break;
                    }
                    default -> throw new IllegalArgumentException("process is not in not running in burst mode");
                }

            }

            else {
                if (((Process) runningQueue.first()).getCurrentTimeQuantum() == timeQuantum) {
                    var process = (Process) runningQueue.dequeue();
                    process.setRemainingWorkTime(process.getRemainingWorkTime() - 1);
                    process.setLastUsed(counter);
                    process.setCurrentTimeQuantum(1);

                    if (algorithm.equals(Algorithms.RR))
                        readyQueue.enqueue(process);
                    else if (algorithm.equals(Algorithms.MLFQ))
                        transferToNextQueue.enqueue(process);
                } else{
                    var process = (Process) runningQueue.first();
                    process.setCurrentTimeQuantum(process.getCurrentTimeQuantum()+1);
                    process.setRemainingWorkTime(process.getRemainingWorkTime() - 1);
                }
            }
        }
    }

    private void checkIfIoIsFinished(int counter) {
        while (true){
            if (!ioQueue.isEmpty() && ((Process) ioQueue.first()).getFinishTime() == counter){
                var process = (Process) ioQueue.dequeue();

                if (process.getRunningProcessSituation().equals(ProcessSituation.IO)){
                    process.setRemainingWorkTime(process.getBurstTime2()-1);
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
                    process.setRunningProcessSituation(ProcessSituation.CPU1);
                    process.setStartTime(counter);
                    process.setRemainingWorkTime(process.getBurstTime1()-1);
                    process.setResponseTime(counter - process.getArrivalTime());
                    process.setWaitingTime(counter - process.getArrivalTime());
                    process.setCurrentTimeQuantum(1); // because we count from 0 in our main loop
                    runningQueue.enqueue(process);
                    break;
                }

                case IO -> {
                    process.setRunningProcessSituation(ProcessSituation.CPU2);
                    process.setRemainingWorkTime(process.getBurstTime2()-1);
                    process.setWaitingTime(process.getWaitingTime() + counter - process.getLastUsed());
                    process.setCurrentTimeQuantum(1); // because we count from 0 in our main loop
                    runningQueue.enqueue(process);
                    break;
                }

                case CPU1, CPU2 -> {
                    process.setWaitingTime(process.getWaitingTime() + counter - process.getLastUsed());
                    process.setCurrentTimeQuantum(1); // because we count from 0 in our main loop
                    runningQueue.enqueue(process);
                    break;
                }

                default -> throw new IllegalArgumentException("process not in the right situation to run");
            }
        }
    }

    public Queue<Process> schedule(List<Process> processes, int timeQuantum, Algorithms algorithm, int counter) {
        Process.sortProcessByArrivalTime(processes);

        for (Process process : processes) {
            newQueue.enqueue(process);
        }


        while (true) {
            // 1. adding to ready queue
            addingNewProcessesToReadyQueue(counter);

            // 2. stopping process or entering IO
            stopProcessOrEnterIo(counter, algorithm, timeQuantum);

            // 3. checking if we need to put it on ready queue
            checkIfIoIsFinished(counter);

            // 4. checking to run which CPU burst time
            checkWhichCpuBurstTimeToRun(counter);

            // 5. check if we have a running process or not
            if (runningQueue.isEmpty())
                idleTime++;

            counter++;

            // check if all processes are finished
            if (isFinished(counter))
                break;
        }

        return finishedQueue;
    }
}
