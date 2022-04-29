package com.newrelic.codingchallenge;

public class SocketBulkInvalidDataTester {
    public static void main(String[] args) throws Exception {
        char[] digits=new char[10];
        int i=0;
        for (char c='0'; c<='9'; c++) {
            digits[i++]=c;
        }
        
        int range=100;
        int duplicate=15;
        int longer=7;
        int shorter=8;
        
        //Tests containing invalid data in each quests from clients, there should be ZERO data shown on server console
        String[] nums=Utilities.numbers(digits, range, duplicate, longer, shorter, false);
        
        String server="127.0.0.1";
        int port=4000;
        
        /*
         * Array nums holds data: 1170 bytes = nums.length(130) * average length of string element (9)
         * 1170 * 32 * 57 > 2M required by Notes 4
         * The unique numbers are saved into log file only.
         */
        
        for (int l=0; l<57; l++) {
            StringBuffer buf=new StringBuffer();
            for (int k=0; k<32; k++) {
                for (int j=0; j<nums.length; j++) {
                    // Requirement 3
                    buf.append(nums[j]+"\n");
                }
            }
            Utilities.send(server, port, buf.toString());
        }       
    }
}
