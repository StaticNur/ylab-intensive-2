package com.ylab.intensive;

class AnsiColor {
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RESET = "\u001B[0m";

    public String green(String text) {
        return GREEN + text + RESET;
    }

    public String red(String text) {
        return RED + text + RESET;
    }

    public String yellow(String text) {
        return YELLOW + text + RESET;
    }
}
