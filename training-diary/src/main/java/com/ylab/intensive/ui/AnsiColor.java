package com.ylab.intensive.ui;

public class AnsiColor {
    private static final String WELCOME = "\u001B[42;5;18m \u001B[1;30m ";
    private static final String GREY_BACKGROUND = "\u001B[48;5;8m";
    private static final String BLUE_BACKGROUND = "\u001B[48;5;12m \u001B[1;30m";
    private static final String YELLOW_BACKGROUND = "\u001B[43;5;18m\u001B[1;30m";
    private static final String GREEN_BACKGROUND = "\u001B[42;5;18m\u001B[1;30m";
    private static final String GREEN_TEXT = "\u001B[32m";
    private static final String RED_TEXT = "\u001B[31m";
    private static final String YELLOW_TEXT = "\u001B[33m";
    private static final String RESET = "\u001B[0m";

    public String welcome(String text) {
        return WELCOME + text + RESET;
    }

    public String greyBackground(String text) {
        return GREY_BACKGROUND + text + RESET;
    }

    public String blueBackground(String text) {
        return BLUE_BACKGROUND + text + RESET;
    }
    public String yellowBackground(String text) {
        return YELLOW_BACKGROUND + text + RESET;
    }
    public String greenBackground(String text) {
        return GREEN_BACKGROUND + text + RESET;
    }

    public String greenText(String text) {
        return GREEN_TEXT + text + RESET;
    }

    public String redText(String text) {
        return RED_TEXT + text + RESET;
    }

    public String yellowText(String text) {
        return YELLOW_TEXT + text + RESET;
    }
}
