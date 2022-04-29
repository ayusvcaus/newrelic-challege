package com.newrelic.codingchallenge;

import java.util.Timer;
import java.util.TimerTask;

///Requirement 9
public class Reporter {
    
    private int[] count=new int[2];
    private Timer timer;
    private TimerTask task;
    
    public void start() {
        timer=new Timer();
        
        task=new TimerTask() {

            @Override
            public void run() {
                int [] copiedCount=LogManager.getInstance().getCount();
                int newUniqueCount=copiedCount[0]-count[0];
                count[0]=copiedCount[0];
                int newDuplicateCount=copiedCount[1]-count[1];
                count[1]=copiedCount[1];                
                System.out.println("Received "+newUniqueCount+" unique numbers, "+newDuplicateCount+" duplicates. Unique total: "+count[0]);           
            }
        };
        
        timer.schedule(task, 0, 10*1000);      
    }
    
    public void stop() {
        task.cancel();
        timer.cancel();
    }

}
