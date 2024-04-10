package com.ylab.intensive.in;

/**
 * The interface Output date.
 */
public interface OutputData {

    /**
     * Output.
     *
     * @param data the date
     */
    void output(Object data);

    void output(Object data1, Object data2);

    /**
     * Err output.
     *
     * @param data the date
     */
    void errOutput(Object data);
}
