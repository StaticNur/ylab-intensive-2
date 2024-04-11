package com.ylab.intensive.in;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Implementation of the InputData interface for reading data from the console.
 */
public class ConsoleInputData implements InputData {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public String input() {
        String readData;
        try {
            readData = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return readData;
    }

    @Override
    public void closeInput() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
