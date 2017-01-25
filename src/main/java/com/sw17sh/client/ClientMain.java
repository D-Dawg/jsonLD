package com.sw17sh.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by denni on 1/25/2017.
 */
public class ClientMain {



    public static void main(String[] args){
        ClientImpl client = new ClientImpl();
        client.run();
    }


}
