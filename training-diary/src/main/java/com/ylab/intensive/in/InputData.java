package com.ylab.intensive.in;

/**
 * The interface Input date.
 */
public interface InputData {

    /**
     * Reads input from the console.
     *
     * @return The input string read from the console
     */
    Object input();

    /**
     * Closes the input stream.
     */
    void closeInput();
}
