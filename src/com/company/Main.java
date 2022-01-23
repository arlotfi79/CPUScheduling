package com.company;

import com.company.Queue.Queue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static List<Process> readFile(String fileName) throws FileNotFoundException {
        File inputs = new File(fileName);
        Scanner scannner = new Scanner(inputs);

        List processes = new ArrayList<Process>();
        scannner.nextLine();
        while(scannner.hasNextLine()) {
            var row = scannner.nextLine().split(",");
            var process = new Process(Integer.valueOf(row[0]), Integer.valueOf(row[1]), Integer.valueOf(row[2]),
                    Integer.valueOf(row[3]), Integer.valueOf(row[4]), ProcessSituation.FirstTime);
            processes.add(process);
        }
        return processes;
    }


    public static void main(String[] args) throws FileNotFoundException {

        var processes =
                readFile("process_inputs.csv");

        Scanner scanner = new Scanner(System.in);
        System.out.println("1. FCFS  2. RR  3. SJF  4. MLFQ");
        System.out.print("Please Enter Your Choice: ");
        int choice = scanner.nextInt();

        // results
        Queue<Process> output;
        int totalTime = 0, idleTime = 0, totalWaitingTime = 0, totalResponseTime = 0, totalTurnAroundTime = 0;
        String algorithmName;

        switch (choice){
            case 1 -> {
                var fcfs = new FCFSAndSJF();
                output = fcfs.schedule(processes, Algorithms.FCFS, 0);
                totalTime = fcfs.getTotalTime();
                idleTime = fcfs.getIdleTime();
                algorithmName = "FirstComeFirstServed";
                break;
            }

            case 2 -> {
                var roundRobin = new RoundRobin();
                output = roundRobin.schedule(processes,5, Algorithms.RR,0);
                totalTime = roundRobin.getTotalTime();
                idleTime = roundRobin.getIdleTime();
                algorithmName = "RoundRobin";
                break;

            }

            case 3 -> {
                var sjf = new FCFSAndSJF();
                output = sjf.schedule(processes, Algorithms.SJF, 0);
                totalTime = sjf.getTotalTime();
                idleTime = sjf.getIdleTime();
                algorithmName = "ShortestJobFirst";
                break;
            }

            case 4 -> {
                var mlfq = new MultiLevelFeedbackQueue();
                output = mlfq.schedule(processes);
                totalTime = mlfq.getTotalTime();
                idleTime = mlfq.getIdleTime();
                algorithmName = "MultiLevelFeedbackQueue";
                break;

            }

            default -> throw new IllegalArgumentException("Illegal Choice. Please try again");
        }


        String processFormat = "| P%-9d | %-2d - %-2d\t | %-16d | %-13d | %-12d |%n";
        String avgFormat = "| Average %-3s  %-2s %-2s\t | %-16.2f | %-13.2f | %-12.2f |%n";
        System.out.format("\n+============+===========+==================+===============+==============+%n");
        System.out.format("| \t\t\t\t\t\t\t %-46s|%n", algorithmName);
        System.out.format("+============+===========+==================+===============+==============+%n");
        System.out.format("| Process ID | Start-End | Turn Around Time | Response Time | Waiting Time |%n");
        System.out.format("+------------+-----------+------------------+---------------+--------------+%n");
        float count = 0;
        while (!output.isEmpty()) {
            var process = output.dequeue();
            totalWaitingTime += process.getWaitingTime();
            totalResponseTime += process.getResponseTime();
            totalTurnAroundTime += process.getTurnAroundTime();

            System.out.format(processFormat, process.getProcessID(), process.getStartTime(), process.getTurnAroundTime(),
                    process.getTurnAroundTime(), process.getResponseTime(), process.getWaitingTime());
            count++;
        }
        System.out.format("+------------+-----------+------------------+---------------+--------------+%n");
        System.out.format(avgFormat, "", "", "", totalTurnAroundTime/count , totalResponseTime/count, totalWaitingTime/count);
        System.out.format("+------------------------+------------------+---------------+--------------+%n");


        System.out.format("\nTotal Time: %-4d", totalTime);
        System.out.format("\nIdle Time: %-4d", idleTime);
        System.out.format("\nCPU Utilization: %-4.2f", (float)(totalTime-idleTime)/totalTime);
        System.out.format("\nThroughput: %-4.2f", (float) (count*1000)/totalTime);
        System.out.println();


    }
}
