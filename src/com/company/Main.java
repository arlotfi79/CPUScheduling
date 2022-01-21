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


        var fcfs = new FirstComeFirstServe();
        var xx = fcfs.schedule(processes);
        System.out.println(xx.dequeue().toString());
        System.out.println(xx.dequeue().toString());
        System.out.println(xx.dequeue().toString());
        System.out.println(xx.dequeue().toString());
        System.out.println(xx.dequeue().toString());
        System.out.println(fcfs.getTotalTime());
        System.out.println(fcfs.getIdleTime());

    }
}
