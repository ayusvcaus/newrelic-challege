package com.newrelic.codingchallenge;

import java.net.Socket;
import java.io.DataInputStream;
import java.util.List;
import java.util.ArrayList;

public class ConnectedThread implements Runnable {
    
    private Socket skt;
    private int[] count;
    
    public ConnectedThread(Socket skt, int[] count) {
       this.skt=skt;
       this.count=count;
       this.count[0]++;  
    }

    @Override
    public void run() {
        DataInputStream in=null;
        try {
            in=new DataInputStream(skt.getInputStream());
            String message="";     
            
            //Requirement 6, 10
            while (Application.started && in.available()>0){
                message=in.readUTF();
                String[] arr=message.split("\\n");
                 //Requirement 10
                if ("terminate".equals(arr[0])) {    
                    Application.started=false;
                    break;
                }
                
                List<String> list=new ArrayList<>();
                for (int i=0; i<arr.length; i++) {
                    
                    //Requirement 4, Notes 3
                    while (arr[i].startsWith("0")) {
                        arr[i]=arr[i].substring(1);
                    }
                    //Requirement 4, 6
                    if (arr[i].length()==9 && arr[i].matches("\\d+")) {
                        list.add(arr[i]);
                    } else {
                        //Requirement 8
                        return;
                    }
                }
                LogManager.getInstance().log(list);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (in!=null) {
                try {
                    in.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            if (skt!=null) {
                try {
                    skt.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            if (count[0]>0) {
                count[0]--;
            }
        }
    }
}
