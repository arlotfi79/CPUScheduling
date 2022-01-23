package com.company;

import com.company.Queue.Queue;

import java.util.ArrayList;
import java.util.List;

public class MultiLevelFeedbackQueue extends AbstractBaseScheduler{

    private Queue finalResult = new Queue<Process>(5);

    public Queue<Process> schedule(List<Process> processes) {

        // free ArrayList for passing
        var temp = new ArrayList<Process>();

        // put in first queue
        var firstQueue = new RoundRobin();
        var q1Result = firstQueue.schedule(processes, 8, Algorithms.MLFQ, 0);

        // put in second queue
        var secondQueue = new RoundRobin();
        while (!firstQueue.getTransferToNextQueue().isEmpty())
            secondQueue.getReadyQueue().enqueue(firstQueue.getTransferToNextQueue().dequeue());

        var q2Result = secondQueue.schedule(temp, 16, Algorithms.MLFQ, firstQueue.getTotalTime());

        // put in last queue
        var lastQueue = new FCFSAndSJF();
        while (!secondQueue.getTransferToNextQueue().isEmpty())
            lastQueue.getReadyQueue().enqueue(secondQueue.getTransferToNextQueue().dequeue());

        var q3Result = lastQueue.schedule(temp, Algorithms.FCFS, secondQueue.getTotalTime());

        // returning the final result
        while (!q1Result.isEmpty())
            finalResult.enqueue(q1Result.dequeue());

        while (!q2Result.isEmpty())
            finalResult.enqueue(q2Result.dequeue());

        while (!q3Result.isEmpty())
            finalResult.enqueue(q3Result.dequeue());

        totalTime = lastQueue.getTotalTime();
        idleTime = firstQueue.getIdleTime() + secondQueue.idleTime + lastQueue.idleTime;

        return finalResult;
    }
}
