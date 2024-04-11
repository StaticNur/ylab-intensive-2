package com.ylab.intensive.in;

/**
 * The type Console output date.
 */
public class ConsoleOutputData implements OutputData {
    @Override
    public void output(Object data) {
        System.out.println(data.toString());
    }

    public void output(Object data1, Object data2) {
        System.out.printf(data1.toString(), data2.toString());
    }

    @Override
    public void errOutput(Object data) {
        System.err.println(data.toString());
    }
}
