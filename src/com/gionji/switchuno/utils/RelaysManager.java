package com.gionji.switchuno.utils;

import java.io.IOException;

/**
 * Created by Luigi on 14/07/2015.
 */
public class RelaysManager {

    /* execute a command
            Process process = Runtime.getRuntime().exec(commandLine);
     */

    final public static String RELAY1 = "7";
    final public static String RELAY2 = "85";
    final public static String RELAY3 = "41";

    final public static String ON  = "1";
    final public static String OFF = "0";

    public RelaysManager(){
        try {
            Process process = Runtime.getRuntime().exec("echo out > /sys/class/gpio7/direction");
            Process process1 = Runtime.getRuntime().exec("echo out > /sys/class/gpio85/direction");
            Process process2 = Runtime.getRuntime().exec("echo out > /sys/class/gpio41/direction");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchRelay(String relay, String status) {
        String commandLine = "echo " + status + " > /sys/class/gpio" + relay + "/value";
        try {
            Process process = Runtime.getRuntime().exec(commandLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
