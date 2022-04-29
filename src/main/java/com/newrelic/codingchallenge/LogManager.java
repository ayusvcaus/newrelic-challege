package com.newrelic.codingchallenge;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class LogManager {

    // Requirement 5
    private String logFileName="numbers.log";
   
    private int[] count;
    
    private LogManager() {
        System.out.println(">> Create a new log file or clean the old log file ...");
        try {
            new FileWriter(logFileName, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        count=new int[2];
    }
    
    public synchronized void log(List<String> list) throws Exception {
        
        BufferedReader br=new BufferedReader(new FileReader(logFileName));
        
        Set<String> set=new HashSet<>();
        String line="";
        while ((line=br.readLine())!=null) {
            set.add(line.trim());
        }
        StringBuffer buf=new StringBuffer();
        for (int i=0; i<list.size(); i++) {
            //Requirement 7
            String s=list.get(i);
            if (set.contains(s)) { // Duplicate numbers           
                count[1]++;
            } else {               // Unique numbers           
                count[0]++;
                set.add(s);
                buf.append(s+"\n");
            }
        }
        
        // Requirement 11
        br.close();
        
        //Append s into the log file
        BufferedWriter bw=new BufferedWriter(new FileWriter(logFileName, true));
        bw.write(buf.toString());

        // Requirement 11
        bw.close();
        
        //Less busy I/O
        Thread.sleep(5);
    }
    
    public synchronized int[] getCount() {
        //deepcopy holds the current data.
        int[] deepCopy=new int[2];
        deepCopy[0]=count[0];
        deepCopy[1]=count[1];
        return deepCopy;
    }
    
    //Create a singleton
    private static class LogManagerHelper {
        private static final LogManager INSTANCE=new LogManager();
    }
    
    public static LogManager getInstance() {
        return LogManagerHelper.INSTANCE;
    } 
}