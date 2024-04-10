package com.ylab.intensive.ui;

public class ConsoleText {
    public static final String WELCOME = "\u001B[42;5;18m \u001B[1;30m Здравствуйте, добро пожаловать в консольное приложения! Следуйте указаниям из документации. \u001B[0m";
    public static final String AUTHORIZED_USER = "\u001B[48;5;12m \u001B[1;30mЯ %s \n\u001B[0m";
    public static final String MENU_NOT_AUTH =
            "\u001B[48;5;8m 1 - регистрация пользователя\n" +
            " 2 - авторизация пользователя\n"+
            " 12 - завершить приложения \u001B[0m";
    public static final String MENU_AUTH =
            "\u001B[48;5;8m 3 - расширить перечень типов тренировок\n" +
            " 4 - добавить новую тренировку\n" +
            " 5 - добавить дополнительную информацию о тренировке\n" +
            " 6 - посмотреть свои предыдущие тренировки\n" +
            " 7 - редактировать тренировку\n" +
            " 8 - удалить тренировку\n" +
            " 9 - изменить права пользователя\n" +
            " 10 - получить статистику по тренировкам\n" +
            " 11 - просмотр Аудит действий\n" +
            " 12 - выход (разлогиниться)\u001B[0m";
    public static final String UNKNOWN_COMMAND = "Неизвестная команда, попробуйте еще раз.";
    public static final String REGISTRATION_INFO = "Неизвестная команда, попробуйте еще раз.";
}
