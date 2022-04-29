package com.newrelic.codingchallenge;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;

import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

import org.junit.Test;
import junit.framework.TestCase;
import org.junit.Rule;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MainTest extends TestCase {
    
    private char[] digits;
    private int range;
    private int duplicate;
    private int longer;
    private int shorter;
    
    private String server;
    private int port;   
    
    private String logFileName;
    
    @Override
    public void setUp() {
  
        digits=new char[10];
        int i=0;
        for (char c='0'; c<='9'; c++) {
            digits[i++]=c;
        }
        range=100;
        duplicate=15;
        longer=7;
        shorter=8;
        
        server="127.0.0.1";
        port=4000;
        
        logFileName="numbers.log";
    }

    @Test
    public void testSocket() throws Exception {
        
        String[] nums=Utilities.numbers(digits, range, duplicate, longer, shorter, false);
        
        Application app=new Application(5, port);
        Utilities.runServer(app, port);
        
        while (!Application.started) {
            Thread.sleep(100);
        }   
        
        for (int i=0; i<nums.length; i++) {
            // Requirement 3
            Utilities.send(server, port, nums[i]+"\n");
        }
               
        //Server needs some time to write data 
        Thread.sleep(60*1000);
 
        
        Set<String> expected=new TreeSet<>();
        for (int i=0; i<nums.length; i++) {
            String s=nums[i];
            
           //Requirement 4, Notes 3
            while (s.startsWith("0")) {
                s=s.substring(1);
            }
            
            //Requirement 4, 6, 8
            if (s.length()==9 && s.matches("\\d+")) {
                expected.add(s);
            }
        }
        
        Set<String> actual=Utilities.readLog(logFileName);
        assertEquals(expected.toString(), actual.toString());    
        
        //Requirement 10
        assertTrue(Utilities.isAddressInUse(port));
        Utilities.send(server, port, "terminate\n");
        Thread.sleep(1000);
        assertFalse(Application.started); 
        
        if (app!=null) {
            app.stop();
        }
        
    }

    //@Test
    public void termination() throws Exception {
        //Use another port since the port as part of address could still not be released already in the following tests
        port=9000; 
        
        Application app=new Application(5, port);
        Utilities.runServer(app, port);
        
        while (!Application.started) {
            Thread.sleep(100);
        }
        assertTrue(Application.started);
        Utilities.send(server, port, "terminate\n");
        Thread.sleep(1000);
        assertFalse(Application.started);  
 
    }
}
