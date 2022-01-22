package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

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

//        System.out.println("New-> " + newQueue.toString());
//        System.out.println("Ready-> " + readyQueue.toString());
//        System.out.println("IO-> " + ioQueue.toString());
//        System.out.println("Running-> " + runningQueue.toString());
//        System.out.println("Finished-> " + finishedQueue.toString());

        var processes =
                readFile("process_inputs.csv");

        Scanner scanner = new Scanner(System.in);
        System.out.println("1. FCFS  2. RR  3. SJF  4. MLFQ");
        System.out.print("Please Enter Your Choice: ");
        int choice = scanner.nextInt();

        switch (choice){
            case 1 -> {
                var fcfs = new FCFSAndSJF();
                var result = fcfs.schedule(processes, Algorithms.FCFS);
                System.out.println("\n=========== FCFS ===========");
                System.out.println(result.dequeue().toString());
                System.out.println(result.dequeue().toString());
                System.out.println(result.dequeue().toString());
                System.out.println(result.dequeue().toString());
                System.out.println(result.dequeue().toString());
                System.out.println("TotalTime: " + fcfs.getTotalTime());
                System.out.println("IdleTime: " + fcfs.getIdleTime());
                break;
            }

            case 2 -> {
                var roundRobin = new RoundRobin();
                var result = roundRobin.schedule(processes, 5);
                System.out.println("\n=========== RR ===========");
                System.out.println(result.dequeue().toString());
                System.out.println(result.dequeue().toString());
                System.out.println(result.dequeue().toString());
                System.out.println(result.dequeue().toString());
                System.out.println(result.dequeue().toString());
                System.out.println("TotalTime: " + roundRobin.getTotalTime());
                System.out.println("IdleTime: " + roundRobin.getIdleTime());
                break;

            }

            case 3 -> {
                var sjf = new FCFSAndSJF();
                var result2 = sjf.schedule(processes, Algorithms.SJF);
                System.out.println("\n=========== SJF ===========");
                System.out.println(result2.dequeue().toString());
                System.out.println(result2.dequeue().toString());
                System.out.println(result2.dequeue().toString());
                System.out.println(result2.dequeue().toString());
                System.out.println(result2.dequeue().toString());
                System.out.println("TotalTime: " + sjf.getTotalTime());
                System.out.println("IdleTime: " + sjf.getIdleTime());
                break;
            }

            case 4 -> {

            }

            default -> throw new IllegalArgumentException("Illegal Choice. Please try again");
        }


    }
}
