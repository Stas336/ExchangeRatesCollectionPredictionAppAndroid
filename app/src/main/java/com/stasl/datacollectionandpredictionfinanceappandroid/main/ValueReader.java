package com.stasl.datacollectionandpredictionfinanceappandroid.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ValueReader
{
    public static String readValue() throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        return bufferedReader.readLine();
    }
}
