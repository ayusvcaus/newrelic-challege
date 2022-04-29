package com.newrelic.codingchallenge;

import java.net.ServerSocket;
import java.net.Socket;

public class Application {
    
    private int maxConnections;
    private Reporter rep;
    public static boolean started;

    private int port;
    
    public Application(int maxConnections, int port) {
        this.maxConnections=maxConnections;
        this.port=port;
        rep=new Reporter();
    }
    
    public void stop() {
        //Requirement 10
        started=false;
        rep.stop();
    }
    
    public void work() throws Exception {
        rep.start();
        ServerSocket server=null;
        try {
            
            //Requirement 2
            server=new ServerSocket(port);
            
            int[] count= {0};
            System.out.println(">> Server Started ....");
            started=true;
            
            //Requirement 9           
            while (started) {
                Socket skt=server.accept();
                Thread t=new Thread(new ConnectedThread(skt, count));
                t.start();
  
                //Requirement 1
                while (count[0]>=maxConnections) {
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            started=false;
            if (server!=null) {
                // Requirement 11
                server.close();
            }
        }
        System.out.println(">> Server ended ....");
    }

    public static void main(String[] args) {
        
        //Requirement 1
        Application app=new Application(5, 4000);
        try {
            app.work();
            Thread.sleep(60*1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        app.stop();
    }
}
