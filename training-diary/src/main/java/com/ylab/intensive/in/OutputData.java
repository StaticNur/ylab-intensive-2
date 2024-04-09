package com.ylab.intensive.in;

/**
 * The interface Output data.
 */
public interface OutputData {

    /**
     * Output.
     *
     * @param data the data
     */
    void output(Object data);

    void output(Object data1, Object data2);

    /**
     * Err output.
     *
     * @param data the data
     */
    void errOutput(Object data);
}
