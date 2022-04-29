package com.newrelic.codingchallenge;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.Random;

public class Utilities {
    
    public static void runServer(Application app, int port) throws Exception {
        Runnable t=new Runnable() {
            @Override
            public void run() {
                try {
                    app.work();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(t).start();
    }
    
    public static void send(String server, int port, String msg) throws Exception {
        Socket socket=new Socket(server, port);
        DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
        outStream.writeUTF(msg);
        outStream.flush();
        
        // Requirement 11
        outStream.close();
        outStream.close();
        socket.close();       
    }
    
    public static String[] numbers(char[] digits, int range, int duplicate, int longer, int shorter, boolean nonZeroStart) {
        
        Random rand=new Random();
        
        String[] nums=new String[range+duplicate+longer+shorter];
        int len=range;
        int i=0;
        
        //Unique numbers
        Set<String> set=new HashSet<>();
        while (set.size()<range) {
            String s="";
            for (int j=0; j<digits.length-1; j++) {
                char c=digits[rand.nextInt(10)];
                if (j==0 && c=='0' && nonZeroStart) {
                    c='1';
                }
                s+=c;
            }
            if (set.add(s)) {
                nums[i++]=s;
            }
        }
        
        len+=duplicate;
        //Generate duplicate numbers
        for (; i<len; i++) {
            nums[i]=nums[i-rand.nextInt(i)];
        }
        
        len+=longer;
        //Generate longer numbers
        for (; i<len; i++) {
            String s=nums[range-rand.nextInt(range)];
            nums[i]=s+s.substring(0, rand.nextInt(s.length()));
        }
        
        len+=shorter;
        //Generate shorter numbers
        for (; i<len; i++) {
            String s=nums[range-rand.nextInt(range)];
            s=s.substring(0, rand.nextInt(s.length()));
            if ("".equals(s)) {
                s="123";
            }
            nums[i]=s;
        }
        return nums;   
    }
    
    public static Set<String> readLog(String logFileName) throws Exception {
        Set<String> actual=new TreeSet<>();
        BufferedReader br=new BufferedReader(new FileReader(logFileName));
        String line="";
        while ((line=br.readLine())!=null) {
            actual.add(line);
        }
         // Requirement 11
        br.close();
        return actual;
    }
}
