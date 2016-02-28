package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StringGetter {
    static String getStringFromUser() throws IOException {
        return new BufferedReader(new InputStreamReader(System.in)).readLine();
    }
}
