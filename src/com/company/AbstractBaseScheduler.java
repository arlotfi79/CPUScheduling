package com.company;

import com.company.Queue.Queue;

public abstract class AbstractBaseScheduler {
    protected int totalTime;
    protected int idleTime;

    protected Queue newQueue = new Queue<Process>(5);
    protected Queue readyQueue = new Queue<Process>(5);
    protected Queue runningQueue = new Queue<Process>(1);
    protected Queue ioQueue = new Queue<Process>(5);
    protected Queue finishedQueue = new Queue<Process>(5);

    protected boolean isFinished(int counter) {
        boolean result = readyQueue.isEmpty() && runningQueue.isEmpty() && ioQueue.isEmpty() && newQueue.isEmpty();
        if (result){
            // decrease by 1 because it's counting the last second too
            totalTime = --counter;
            idleTime--;
        }

        return result;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public Queue getReadyQueue() {
        return readyQueue;
    }
}
