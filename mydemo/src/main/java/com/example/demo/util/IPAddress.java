package com.example.demo.util;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class IPAddress {

    public static void main(String[] args) throws Exception {
        System.out.println(getIP());
    }

    public static String getIP() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        return "http://"+"172.26.217.104"+":8082";
    }

}
