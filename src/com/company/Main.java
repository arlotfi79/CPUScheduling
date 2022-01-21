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

        var processes =
                readFile("/home/arlotfi79/Programming/IntellijProjects/OperatingSystems_Fall2021/process_inputs.csv");

        Scanner scanner = new Scanner(System.in);
        System.out.println("1. FCFS  2. RR  3. SJF  4. MLFQ");
        System.out.print("Please Enter Your Choice: ");
        int choice = scanner.nextInt();

        switch (choice){
            case 1 -> {
                var fcfs = new FCFSAndSJF();
                var result = fcfs.schedule(processes, Algorithms.FCFS);
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

            }

            case 3 -> {
                var sjf = new FCFSAndSJF();
                var result2 = sjf.schedule(processes, Algorithms.SJF);
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
