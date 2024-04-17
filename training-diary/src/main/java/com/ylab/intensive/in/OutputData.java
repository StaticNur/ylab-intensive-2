package com.ylab.intensive.in;

/**
 * The OutputData interface provides methods for outputting data.
 */
public interface OutputData {

    /**
     * Outputs the provided data.
     *
     * @param data The data to be outputted
     */
    void output(Object data);

    /**
     * Outputs the provided data1 and data2.
     *
     * @param data1 The first data to be outputted
     * @param data2 The second data to be outputted
     */
    void output(Object data1, Object data2);

    /**
     * Outputs the error message.
     *
     * @param data The error message to be outputted
     */
    void errOutput(Object data);
}
